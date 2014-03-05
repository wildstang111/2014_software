/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.AutonomousProgram;
import com.wildstangs.autonomous.steps.arms.AutonomousStepSetArmPresets;
import com.wildstangs.autonomous.steps.catapult.AutonomousStepArmCatapult;
import com.wildstangs.autonomous.steps.catapult.AutonomousStepFireCatapult;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepStartDriveUsingMotionProfile;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepWaitForDriveMotionProfile;
import com.wildstangs.autonomous.steps.vision.AutonomousStepDelayForHotGoal;
import com.wildstangs.subsystems.BallHandler;

/**
 *
 * @author Joey
 */
public class AutonomousProgramDriveAndShootForHotGoal extends AutonomousProgram
{
    protected void defineSteps()
    {
        addStep(new AutonomousStepDelayForHotGoal(4500));
        addStep(new AutonomousStepStartDriveUsingMotionProfile(60.0, 0.75));
        addStep(new AutonomousStepWaitForDriveMotionProfile());
        addStep(new AutonomousStepSetArmPresets(BallHandler.CATAPULT_TENSION_PRESET));
        addStep(new AutonomousStepArmCatapult());
        addStep(new AutonomousStepFireCatapult());
    }

    public String toString()
    {
        return "Drive and Shoot for the Hot Goal";
    }
}
