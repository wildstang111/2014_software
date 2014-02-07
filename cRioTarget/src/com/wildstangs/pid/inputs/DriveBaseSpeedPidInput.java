package com.wildstangs.pid.inputs;

import com.wildstangs.pid.inputs.base.IPidInput;
import com.wildstangs.subsystems.DriveBase;
import com.wildstangs.subsystems.base.SubsystemContainer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Nathan
 */
public class DriveBaseSpeedPidInput implements IPidInput {

    public DriveBaseSpeedPidInput() {
        //Nothing to do here
    }

    public double pidRead() {
        double /*left_encoder_value,*/ right_encoder_value, final_encoder_value;
        //left_encoder_value = ((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.DRIVE_BASE)).getLeftDistance();
        double currentVelocity = ((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.DRIVE_BASE_INDEX)).getVelocity();
        return currentVelocity;
    }
}
