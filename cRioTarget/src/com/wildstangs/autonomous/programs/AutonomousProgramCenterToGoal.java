/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.AutonomousProgram;
import com.wildstangs.autonomous.steps.arms.AutonomousStepSetArmPresets;
import com.wildstangs.autonomous.steps.catapult.AutonomousStepArmCatapult;
import com.wildstangs.autonomous.steps.catapult.AutonomousStepFireCatapult;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepQuickTurn;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepStartDriveUsingMotionProfile;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepStopDriveUsingMotionProfile;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepWaitForDriveMotionProfile;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.subsystems.BallHandler;

/**
 *
 * @author Joey
 */
public class AutonomousProgramCenterToGoal extends AutonomousProgram
{
    protected final DoubleConfigFileParameter TURN_ANGLE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "StartingTurnAngle", 45.0);
    protected final DoubleConfigFileParameter DRIVE_DISTANCE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "DriveDistance", 175);
    
    protected void defineSteps()
    {
        addStep(new AutonomousStepArmCatapult());
        addStep(new AutonomousStepQuickTurn(TURN_ANGLE_CONFIG.getValue()));
        addStep(new AutonomousStepStartDriveUsingMotionProfile(DRIVE_DISTANCE_CONFIG.getValue(), 1.0));
        addStep(new AutonomousStepWaitForDriveMotionProfile());
        addStep(new AutonomousStepStopDriveUsingMotionProfile());
        addStep(new AutonomousStepSetArmPresets(BallHandler.CATAPULT_TENSION_PRESET_BACK));
        addStep(new AutonomousStepFireCatapult());
    }

    public String toString()
    {
        return "Turn and Shoot for Goal from Center";
    }
}
