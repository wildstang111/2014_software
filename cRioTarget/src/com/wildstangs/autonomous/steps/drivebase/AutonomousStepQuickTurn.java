/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.drivebase;

import com.wildstangs.autonomous.steps.AutonomousStep;
import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.inputmanager.inputs.joystick.JoystickAxisEnum;
import com.wildstangs.subsystems.DriveBase;
import com.wildstangs.subsystems.base.SubsystemContainer;

/**
 *
 * @author Joey
 */
public class AutonomousStepQuickTurn extends AutonomousStep {

    private double value, angle;
    private boolean shouldFinish = false;

    public AutonomousStepQuickTurn(double relativeAngle) {
        this.value = relativeAngle;
    }

    public void initialize() {

        angle = ((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.WS_DRIVE_BASE_INDEX)).getGyroAngle() + value;
        ((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.WS_DRIVE_BASE_INDEX)).setThrottleValue(0);
        ((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.WS_DRIVE_BASE_INDEX)).overrideHeadingValue(value < 0 ? 0.6 : -0.6);

        InputManager.getInstance().getOiInput(InputManager.DRIVER_JOYSTICK_INDEX).set(JoystickAxisEnum.DRIVER_THROTTLE, new Double(0.0));
        InputManager.getInstance().getOiInput(InputManager.DRIVER_JOYSTICK_INDEX).set(JoystickAxisEnum.DRIVER_HEADING, new Double(value < 0 ? 0.6 : -0.6));

    }

    public void update() {
        if (shouldFinish) {
            finished = true;
            ((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.WS_DRIVE_BASE_INDEX)).overrideHeadingValue(0.0);
            return;
        }
        double gyroAngle = ((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.WS_DRIVE_BASE_INDEX)).getGyroAngle();
        if (value < 0) {
            if (angle > gyroAngle) {
                ((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.WS_DRIVE_BASE_INDEX)).overrideHeadingValue(0.0);
                InputManager.getInstance().getOiInput(InputManager.DRIVER_JOYSTICK_INDEX).set(JoystickAxisEnum.DRIVER_HEADING, new Double(0.0));
                shouldFinish = true;
            }
        } else {
            if (angle < gyroAngle) {
                ((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.WS_DRIVE_BASE_INDEX)).overrideHeadingValue(0.0);
                InputManager.getInstance().getOiInput(InputManager.DRIVER_JOYSTICK_INDEX).set(JoystickAxisEnum.DRIVER_HEADING, new Double(0.0));
                shouldFinish = true;
            }
        }
    }

    public String toString() {
        return "Turning using quickturn with a relative angle";
    }
}
