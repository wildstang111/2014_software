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
public class AutonomousStepStopDriveUsingMotionProfile extends AutonomousStep {

    public AutonomousStepStopDriveUsingMotionProfile() {
    }

    public void initialize() {
    }

    public void update() {
        ((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.WS_DRIVE_BASE_INDEX)).stopStraightMoveWithMotionProfile();
        finished = true;
    }

    public String toString() {
        return "Stop the drive using motion profile" ; 
    }
}
