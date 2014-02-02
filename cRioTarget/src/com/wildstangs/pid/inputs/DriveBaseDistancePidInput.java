package com.wildstangs.pid.inputs;

import com.wildstangs.pid.inputs.base.IPidInput;
import com.wildstangs.subsystems.DriveBase;
import com.wildstangs.subsystems.base.SubsystemContainer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Nathan
 */
public class DriveBaseDistancePidInput implements IPidInput {

    public DriveBaseDistancePidInput() {
        //Nothing to do here
    }

    public double pidRead() {
        double /*left_encoder_value,*/ right_encoder_value, final_encoder_value;
        //left_encoder_value = ((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.WS_DRIVE_BASE)).getLeftDistance();
        right_encoder_value = ((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.WS_DRIVE_BASE_INDEX)).getRightDistance();
        final_encoder_value = (/*left_encoder_value + */right_encoder_value)/* / 2*/;
        SmartDashboard.putNumber("Distance: ", final_encoder_value);
        return final_encoder_value;
    }
}
