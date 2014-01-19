/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.inputmanager.inputs.joystick;

import com.wildstangs.inputmanager.base.IInputEnum;

/**
 *
 * @author Joey
 */
public class WsJoystickAxisEnum implements IInputEnum {

    protected boolean isDriver;
    protected int index;
    protected String name;

    protected WsJoystickAxisEnum(boolean isDriver, int index, String desc) {
        this.isDriver = isDriver;
        this.index = index;
        this.name = desc;
    }

    // Axis Enums
    public static final int LEFT_JOYSTICK_Y = 1;
    public static final int LEFT_JOYSTICK_X = 2;
    public static final int RIGHT_JOYSTICK_Y = 3;
    public static final int RIGHT_JOYSTICK_X = 4;
    public static final int DPAD_Y = 5;
    public static final int DPAD_X = 6;
    
    // Driver Enums
    public static final WsJoystickAxisEnum DRIVER_THROTTLE = new WsJoystickAxisEnum(true, 0, "DRIVER_THROTTLE");
    public static final WsJoystickAxisEnum DRIVER_HEADING = new WsJoystickAxisEnum(true, 1, "DRIVER_HEADING");
    public static final WsJoystickAxisEnum DRIVER_DPAD_Y = new WsJoystickAxisEnum(true, 2, "DRIVER_DPAD_Y");
    
    // Manipulator Enums
    public static final WsJoystickAxisEnum MANIPULATOR_ENTER_FLYWHEEL_ADJUSTMENT = new WsJoystickAxisEnum(false, 0, "MANIPULATOR_ENTER_FLYWHEEL_ADJUSTMENT");
    public static final WsJoystickAxisEnum MANIPULATOR_EXIT_FLYWHEEL_ADJUSTMENT = new WsJoystickAxisEnum(false, 1, "MANIPULATOR_EXIT_FLYWHEEL_ADJUSTMENT");
    public static final WsJoystickAxisEnum MANIPULATOR_DPAD_Y = new WsJoystickAxisEnum(false, 2, "MANIPULATOR_D_PAD_UP_DOWN");
    public static final WsJoystickAxisEnum MANIPULATOR_DPAD_X = new WsJoystickAxisEnum(false, 3, "MANIPULATOR_D_PAD_LEFT_RIGHT");
    
    // Driver Enums

    public String toString() {
        return name;
    }

    public int toValue() {
        return index;
    }

}
