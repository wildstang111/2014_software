/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.AutonomousProgram;
import com.wildstangs.autonomous.steps.arms.AutonomousStepSetArmPresets;
import com.wildstangs.autonomous.steps.catapult.AutonomousStepArmCatapult;
import com.wildstangs.autonomous.steps.catapult.AutonomousStepFireCatapult;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepSetShifter;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepStartDriveUsingMotionProfile;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepStopDriveUsingMotionProfile;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepWaitForDriveMotionProfile;
import com.wildstangs.autonomous.steps.vision.AutonomousStepDelayForHotGoal;
import com.wildstangs.autonomous.steps.vision.AutonomousStepSetCameraLedState;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.subsystems.BallHandler;
import com.wildstangs.subsystems.HotGoalDetector;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 *
 * @author Joey
 */
public class AutonomousProgramDriveAndShootForHotGoal extends AutonomousProgram
{
    protected final DoubleConfigFileParameter DISTANCE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "DistanceToDrive", 175.0);
    protected final IntegerConfigFileParameter NO_HOT_GOAL_DELAY_TIME = new IntegerConfigFileParameter(this.getClass().getName(), "NoHotGoalDelayTimeMs", 4500);
    
    protected void defineSteps()
    {
        addStep(new AutonomousStepSetCameraLedState(true));
        addStep(new AutonomousStepDelayForHotGoal(NO_HOT_GOAL_DELAY_TIME.getValue(), HotGoalDetector.HotGoalSideEnum.EITHER));
        addStep(new AutonomousStepSetShifter(DoubleSolenoid.Value.kReverse));
        addStep(new AutonomousStepArmCatapult());
        addStep(new AutonomousStepStartDriveUsingMotionProfile(DISTANCE_CONFIG.getValue(), 1.0));
        addStep(new AutonomousStepWaitForDriveMotionProfile());
        addStep(new AutonomousStepStopDriveUsingMotionProfile());
        addStep(new AutonomousStepSetArmPresets(BallHandler.CATAPULT_TENSION_PRESET_BACK));
        addStep(new AutonomousStepFireCatapult());
        addStep(new AutonomousStepSetCameraLedState(false));
    }

    public String toString()
    {
        return "Drive and Shoot for the Hot Goal";
    }
}
