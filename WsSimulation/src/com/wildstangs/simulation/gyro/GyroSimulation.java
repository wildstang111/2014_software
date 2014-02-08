package com.wildstangs.simulation.gyro;

import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.outputmanager.base.OutputManager;
import com.wildstangs.subsystems.DriveBase;
import com.wildstangs.subsystems.base.SubsystemContainer;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GyroSimulation {
    
    private double left_drive_speed = 0.0;
    private double right_drive_speed = 0.0;
    
    public GyroSimulation() {
        
        left_drive_speed = 0.0;
        right_drive_speed = 0.0;
    }
    
    public void update() {
        left_drive_speed = ((Double) OutputManager.getInstance().getOutput(OutputManager.LEFT_DRIVE_SPEED_INDEX).get((IOutputEnum) null));
        right_drive_speed = ((Double) OutputManager.getInstance().getOutput(OutputManager.RIGHT_DRIVE_SPEED_INDEX).get((IOutputEnum) null));
        
        Gyro gyro = ((DriveBase) (SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.DRIVE_BASE_INDEX))).getGyro();
        double angle = gyro.getAngle();
        //Handle brakes
        if ((left_drive_speed > 0.1) && (right_drive_speed < 0.1)) {
            angle--;
        } else if ((left_drive_speed < 0.1) && (right_drive_speed > 0.1)) {
            angle++;
        }
        gyro.setAngle(angle);
        SmartDashboard.putNumber("Gyro Angle", angle);
    }
}
