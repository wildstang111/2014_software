/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.drivebase;

import com.wildstangs.autonomous.steps.AutonomousStep;
import com.wildstangs.pid.controller.base.PidStateType;
import com.wildstangs.subsystems.DriveBase;
import com.wildstangs.subsystems.base.SubsystemContainer;

/**
 *
 * @author Nathan
 */
public class AutonomousStepWaitForDriveMotionProfile extends AutonomousStep {

    public AutonomousStepWaitForDriveMotionProfile() {
    }

    public void initialize() {
    }

    public void update() {
        double distanceRemaining = ((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.DRIVE_BASE_INDEX)).getDistanceRemaining();
        double velocity = ((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.DRIVE_BASE_INDEX)).getVelocity();
        if ((distanceRemaining < 0.01) &&
            (distanceRemaining > -0.01))  {
            finished = true;
        } 
        if ((distanceRemaining < 12.0) &&
            (distanceRemaining > -12.0) &&
                (velocity < 0.10) &&
                (velocity > -0.10))  {
            finished = true; 
    
        }
    }

    public String toString() {
        return "Wait for the motion profile to finish moving to target";
    }
}
