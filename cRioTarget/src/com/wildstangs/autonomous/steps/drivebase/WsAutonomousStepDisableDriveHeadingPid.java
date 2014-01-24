/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.drivebase;

import com.wildstangs.autonomous.steps.WsAutonomousStep;
import com.wildstangs.logger.Logger;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Nathan
 */
public class WsAutonomousStepDisableDriveHeadingPid extends WsAutonomousStep {

    public WsAutonomousStepDisableDriveHeadingPid() {
    }

    public void initialize() {
        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE_INDEX)).disableHeadingPidControl();
        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE_INDEX)).resetLeftEncoder();
        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE_INDEX)).resetRightEncoder();
        Logger.getLogger().info(this.toString(), "initialize", "Drive Heading pid is disabled");
        finished = true;
    }

    public void update() {
    }

    public String toString() {
        return "Disable the drive heading PID";
    }
}
