/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.AutonomousProgram;
import com.wildstangs.autonomous.parameters.IAutonomousChangeOnLockIn;
import com.wildstangs.autonomous.steps.AutonomousParallelStepGroup;
import com.wildstangs.autonomous.steps.AutonomousSerialStepGroup;
import com.wildstangs.autonomous.steps.arms.AutonomousStepSetArmPresets;
import com.wildstangs.autonomous.steps.arms.AutonomousStepSetArmRoller;
import com.wildstangs.autonomous.steps.catapult.AutonomousStepArmCatapult;
import com.wildstangs.autonomous.steps.catapult.AutonomousStepFireCatapult;
import com.wildstangs.autonomous.steps.catapult.AutonomousStepWaitForCatapultDown;
import com.wildstangs.autonomous.steps.control.AutonomousStepDelay;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepStartDriveUsingMotionProfile;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepStopDriveUsingMotionProfile;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepWaitForDriveMotionProfile;
import com.wildstangs.autonomous.steps.wings.AutonomousStepSetWingsState;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.subsystems.BallHandler;
import com.wildstangs.subsystems.arm.ArmPreset;
import com.wildstangs.subsystems.arm.ArmRollerEnum;
import com.wildstangs.subsystems.base.SubsystemContainer;

/**
 *
 * @author Joey
 */
public class AutonomousProgramTwoBallFront extends AutonomousProgram implements IAutonomousChangeOnLockIn
{
    protected final DoubleConfigFileParameter DISTANCE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "DistanceToDrive", 175.0);
    protected final IntegerConfigFileParameter BALL_SETTLE_DELAY_CONFIG = new IntegerConfigFileParameter(this.getClass().getName(), "BallSettleDelayMS", 700);
    protected final IntegerConfigFileParameter INITIAL_INTAKE_DELAY_CONFIG = new IntegerConfigFileParameter(this.getClass().getName(), "InitialIntakeDelayMS", 100);
    protected final IntegerConfigFileParameter INTAKE_DELAY_CONFIG = new IntegerConfigFileParameter(this.getClass().getName(), "IntakeDelayMS", 100);
    protected final IntegerConfigFileParameter TENSION_DELAY_CONFIG = new IntegerConfigFileParameter(this.getClass().getName(), "TensionDelayMS", 100);
    
    protected void defineSteps()
    {
        AutonomousParallelStepGroup tensionAndLowerArm = new AutonomousParallelStepGroup("TensionAndLowerArm");
        tensionAndLowerArm.addStep(new AutonomousStepArmCatapult());
        tensionAndLowerArm.addStep(new AutonomousStepSetArmRoller(true, ArmRollerEnum.INTAKE));
        tensionAndLowerArm.addStep(new AutonomousStepSetArmPresets(new ArmPreset(120, ArmPreset.IGNORE_VALUE, "AutonomousProgramTwoBallFront.CaptureBallInitial")));
        addStep(tensionAndLowerArm);
//        addStep(new AutonomousStepDelay(INITIAL_INTAKE_DELAY_CONFIG.getValue()));
        AutonomousParallelStepGroup driveAndAccumulate = new AutonomousParallelStepGroup("Drive and Accumulate");
        AutonomousSerialStepGroup accumulate = new AutonomousSerialStepGroup("Accumulate");
        AutonomousSerialStepGroup drive = new AutonomousSerialStepGroup("Drive");
        
        accumulate.addStep(new AutonomousStepSetArmPresets(new ArmPreset(120, ArmPreset.IGNORE_VALUE, "AutonomousProgramTwoBallFront.CaptureBallFinal")));
        accumulate.addStep(new AutonomousStepSetArmRoller(true, ArmRollerEnum.OFF));
        
        drive.addStep(new AutonomousStepStartDriveUsingMotionProfile(DISTANCE_CONFIG.getValue(), 1.0));
        drive.addStep(new AutonomousStepWaitForDriveMotionProfile());
        drive.addStep(new AutonomousStepStopDriveUsingMotionProfile());
        drive.addStep(new AutonomousStepSetWingsState(true));
        
        driveAndAccumulate.addStep(drive);
        driveAndAccumulate.addStep(accumulate);
//        addStep(new AutonomousStepSetArmPresets(new ArmPreset(100, ArmPreset.IGNORE_VALUE, "AutonomousProgramTwoBallFront.ReleaseBall")));
        addStep(driveAndAccumulate);
        addStep(new AutonomousStepDelay(BALL_SETTLE_DELAY_CONFIG.getValue()));
        addStep(new AutonomousStepFireCatapult());
//        addStep(new AutonomousStepDelay(INTAKE_DELAY_CONFIG.getValue()));
        addStep(new AutonomousStepWaitForCatapultDown());
        
        AutonomousParallelStepGroup intakeAndBringFrontArmIn = new AutonomousParallelStepGroup("Intake and Bring Front Arm In");
        AutonomousParallelStepGroup stopRollerAndArmsToShootPreset = new AutonomousParallelStepGroup("Stop Roller and Arms To Shoot Preset");
        
        intakeAndBringFrontArmIn.addStep(new AutonomousStepSetArmRoller(true, ArmRollerEnum.INTAKE));
        intakeAndBringFrontArmIn.addStep(new AutonomousStepSetArmPresets(new ArmPreset(180, ArmPreset.IGNORE_VALUE, "AutonomousProgramTwoBallFront.FrontArmIn")));
        
        stopRollerAndArmsToShootPreset.addStep(new AutonomousStepSetArmRoller(true, ArmRollerEnum.OFF));
        stopRollerAndArmsToShootPreset.addStep(new AutonomousStepSetArmPresets(BallHandler.CATAPULT_TENSION_PRESET_BOTH));
        stopRollerAndArmsToShootPreset.addStep(new AutonomousStepArmCatapult());
        
        addStep(intakeAndBringFrontArmIn);
        addStep(stopRollerAndArmsToShootPreset);
        
        addStep(new AutonomousStepDelay(TENSION_DELAY_CONFIG.getValue()));
        addStep(new AutonomousStepFireCatapult());
//        addStep(new AutonomousStepSetWingsState(false));
    }

    public String toString()
    {
        return "Two Ball, Intake From Front";
    }

    public void onLockIn()
    {
        //This program assumes that the front arm starts in the up position so we need to calibrate it accordingly
        ((BallHandler) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.BALL_HANDLER_INDEX)).setFrontArmCalibrateState(false);
    }

    public void onDisengage()
    {
        //If we aren't going to use this program we go back to assuming that the front arm starts down
        ((BallHandler) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.BALL_HANDLER_INDEX)).setFrontArmCalibrateState(true);
    }
}
