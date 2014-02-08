/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.pid.outputs;

import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.outputmanager.base.OutputManager;
import com.wildstangs.pid.outputs.base.IPidOutput;
import com.wildstangs.subsystems.DriveBase;
import com.wildstangs.subsystems.base.SubsystemContainer;

/**
 *
 * @author Nathan
 */
public class DriveBaseSpeedPidOutput implements IPidOutput {

    public DriveBaseSpeedPidOutput() {
        //Nothing to do here
    }

    public void pidWrite(double output) {
        ((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.DRIVE_BASE_INDEX)).setPidSpeedValue(output);
    }
}
