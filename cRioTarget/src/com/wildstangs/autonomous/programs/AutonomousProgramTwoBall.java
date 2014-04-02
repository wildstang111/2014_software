/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.AutonomousProgram;
import com.wildstangs.autonomous.parameters.IAutonomousChangeOnLockIn;
import com.wildstangs.autonomous.steps.arms.AutonomousStepSetArmPresets;
import com.wildstangs.autonomous.steps.arms.AutonomousStepSetArmRoller;
import com.wildstangs.autonomous.steps.catapult.AutonomousStepArmCatapult;
import com.wildstangs.autonomous.steps.catapult.AutonomousStepFireCatapult;
import com.wildstangs.autonomous.steps.catapult.AutonomousStepWaitForCatapultDown;
import com.wildstangs.autonomous.steps.control.AutonomousStepDelay;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepStartDriveUsingMotionProfile;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepStopDriveUsingMotionProfile;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepWaitForDriveMotionProfile;
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
public class AutonomousProgramTwoBall extends AutonomousProgram implements IAutonomousChangeOnLockIn
{
    protected final DoubleConfigFileParameter DISTANCE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "DistanceToDrive", 175.0);
    protected final IntegerConfigFileParameter BALL_SETTLE_DELAY_CONFIG = new IntegerConfigFileParameter(this.getClass().getName(), "BallSettleDelayMS", 700);
    protected final IntegerConfigFileParameter SMALL_INTAKE_TIME_CONFIG = new IntegerConfigFileParameter(this.getClass().getName(), "SmallIntakeTimeMS", 100);
    protected final IntegerConfigFileParameter SMALL_OUTPUT_TIME_CONFIG = new IntegerConfigFileParameter(this.getClass().getName(), "SmallOutputTimeMS", 50);
    
    protected void defineSteps()
    {
        addStep(new AutonomousStepSetArmPresets(new ArmPreset(ArmPreset.IGNORE_VALUE, 100, "AutonomousProgramTwoBall.CaptureBall")));
        addStep(new AutonomousStepArmCatapult());
        addStep(new AutonomousStepSetArmRoller(false, ArmRollerEnum.INTAKE));
        addStep(new AutonomousStepDelay(SMALL_INTAKE_TIME_CONFIG.getValue()));
        addStep(new AutonomousStepSetArmRoller(false, ArmRollerEnum.OFF));
        addStep(new AutonomousStepStartDriveUsingMotionProfile(DISTANCE_CONFIG.getValue(), 1.0));
        addStep(new AutonomousStepWaitForDriveMotionProfile());
        addStep(new AutonomousStepStopDriveUsingMotionProfile());
        addStep(new AutonomousStepSetArmRoller(false, ArmRollerEnum.OUTPUT));
        addStep(new AutonomousStepDelay(SMALL_INTAKE_TIME_CONFIG.getValue()));
        addStep(new AutonomousStepSetArmRoller(false, ArmRollerEnum.OFF));
        addStep(new AutonomousStepDelay(BALL_SETTLE_DELAY_CONFIG.getValue()));
        addStep(new AutonomousStepFireCatapult());
        addStep(new AutonomousStepWaitForCatapultDown());
        addStep(new AutonomousStepSetArmRoller(false, ArmRollerEnum.INTAKE));
        addStep(new AutonomousStepSetArmPresets(BallHandler.DEFAULT_POSITION));
        addStep(new AutonomousStepSetArmRoller(false, ArmRollerEnum.OFF));
        addStep(new AutonomousStepSetArmPresets(BallHandler.CATAPULT_TENSION_PRESET_BOTH));
        addStep(new AutonomousStepArmCatapult());
        addStep(new AutonomousStepFireCatapult());
    }

    public String toString()
    {
        return "Two Ball, Intake From Back";
    }

    public void onLockIn()
    {
        //This program assumes that the back arm starts in the up position so we need to calibrate it accordingly
        ((BallHandler) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.BALL_HANDLER_INDEX)).setBackArmCalibrateState(false);
    }

    public void onDisengage()
    {
        //If we aren't going to use this program we go back to assuming that the back arm starts down
        ((BallHandler) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.BALL_HANDLER_INDEX)).setBackArmCalibrateState(true);
    }
}
