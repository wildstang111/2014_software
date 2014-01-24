/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.drivebase;

import com.wildstangs.autonomous.steps.WsAutonomousStep;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.inputmanager.inputs.joystick.WsJoystickAxisEnum;

/**
 *
 * @author coder65535
 */
public class WsAutonomousStepDriveManual extends WsAutonomousStep {

    private double throttle, heading;
    public static final double KEEP_PREVIOUS_STATE = 2.0;

    public WsAutonomousStepDriveManual(double throttle, double heading) {
        this.throttle = throttle;
        this.heading = heading;
    }

    public void initialize() {
        finished = true;
        if (throttle != KEEP_PREVIOUS_STATE) {
            WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK_INDEX).set(WsJoystickAxisEnum.DRIVER_THROTTLE, new Double(Math.max(Math.min(throttle, 1.0), -1.0)));
        }
        if (heading != KEEP_PREVIOUS_STATE) {
            WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK_INDEX).set(WsJoystickAxisEnum.DRIVER_HEADING, new Double(Math.max(Math.min(heading, 1.0), -1.0)));
        }
    }

    public void update() {
    }

    public String toString() {
        return "Set throttle to " + throttle + " and heading to " + heading;
    }
}
