/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.drivebase;

import com.wildstangs.autonomous.steps.AutonomousStep;
import com.wildstangs.subsystems.DriveBase;
import com.wildstangs.subsystems.base.SubsystemContainer;

/**
 *
 * @author Nathan
 */
public class AutonomousStepStartDriveUsingMotionProfile extends AutonomousStep {

    double distance;
    double goal_velocity;

    public AutonomousStepStartDriveUsingMotionProfile(double distance , double goal_velocity) {
        this.distance = distance;
        this.goal_velocity = goal_velocity;
    }

    public void initialize() {
        ((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.DRIVE_BASE_INDEX)).resetLeftEncoder();
        ((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.DRIVE_BASE_INDEX)).resetRightEncoder();
        ((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.DRIVE_BASE_INDEX)).startStraightMoveWithMotionProfile(distance , goal_velocity);
        finished = true;
    }

    public void update() {
    }

    public String toString() {
        return "Start the drive using motion profile for " + distance + " inches and reach going " + goal_velocity + " inches/second ";
    }
}
