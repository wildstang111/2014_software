/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.AutonomousProgram;
import com.wildstangs.autonomous.steps.AutonomousParallelStepGroup;
import com.wildstangs.autonomous.steps.AutonomousSerialStepGroup;
import com.wildstangs.autonomous.steps.arms.AutonomousStepSetArmPresets;
import com.wildstangs.autonomous.steps.catapult.AutonomousStepArmCatapult;
import com.wildstangs.autonomous.steps.catapult.AutonomousStepFireCatapult;
import com.wildstangs.autonomous.steps.control.AutonomousStepDelay;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepSetShifter;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepStartDriveUsingMotionProfile;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepStopDriveUsingMotionProfile;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepWaitForDriveMotionProfile;
import com.wildstangs.autonomous.steps.vision.AutonomousStepDelayForHotGoal;
import com.wildstangs.autonomous.steps.vision.AutonomousStepSetCameraLedState;
import com.wildstangs.autonomous.steps.wings.AutonomousStepSetWingsState;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.subsystems.BallHandler;
import com.wildstangs.subsystems.HotGoalDetector;

/**
 *
 * @author Joey
 */
public class AutonomousProgramDriveAndShootForHotGoal extends AutonomousProgram
{
    protected final DoubleConfigFileParameter DISTANCE_CONFIG = new DoubleConfigFileParameter(AutonomousProgramDriveAndShootForHotGoal.class.getName(), "DistanceToDrive", 175.0);
    protected final IntegerConfigFileParameter NO_HOT_GOAL_DELAY_TIME = new IntegerConfigFileParameter(AutonomousProgramDriveAndShootForHotGoal.class.getName(), "NoHotGoalDelayTimeMs", 4500);
    protected final IntegerConfigFileParameter LED_DELAY_TIME = new IntegerConfigFileParameter(AutonomousProgramDriveAndShootForHotGoal.class.getName(), "LEDDelayTimeMs", 70);
    protected final IntegerConfigFileParameter BALL_SETTLE_DELAY_TIME = new IntegerConfigFileParameter(this.getClass().getName(), "BallSettleDelayTimeMS", 250);
    
    protected void defineSteps()
    {
        addStep(new AutonomousStepSetCameraLedState(true));
        addStep(new AutonomousStepDelay(LED_DELAY_TIME.getValue()));
        addStep(new AutonomousStepDelayForHotGoal(NO_HOT_GOAL_DELAY_TIME.getValue(), HotGoalDetector.HotGoalSideEnum.EITHER));
        addStep(new AutonomousStepSetCameraLedState(false));
        AutonomousParallelStepGroup tensionAndDrive = new AutonomousParallelStepGroup("Tension And Drive");
        
        AutonomousSerialStepGroup visionSteps = new AutonomousSerialStepGroup("Vision Steps");
        
//        tensionAndDrive.addStep(visionSteps);
        tensionAndDrive.addStep(new AutonomousStepArmCatapult());
        tensionAndDrive.addStep(new AutonomousStepSetArmPresets(BallHandler.CATAPULT_TENSION_PRESET_BACK));
        
        AutonomousSerialStepGroup drive = new AutonomousSerialStepGroup("Drive");
        drive.addStep(new AutonomousStepStartDriveUsingMotionProfile(DISTANCE_CONFIG.getValue(), 1.0));
        drive.addStep(new AutonomousStepWaitForDriveMotionProfile());
        drive.addStep(new AutonomousStepStopDriveUsingMotionProfile());
        
        tensionAndDrive.addStep(drive);
        
        addStep(tensionAndDrive);
//        addStep(new AutonomousStepSetWingsState(true));
        addStep(new AutonomousStepDelay(BALL_SETTLE_DELAY_TIME.getValue()));
        addStep(new AutonomousStepFireCatapult());
    }

    public String toString()
    {
        return "Drive and Shoot for the Hot Goal";
    }
}
