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

    protected static WsJoystickAxisEnum[] driverAxes;
    protected static WsJoystickAxisEnum[] manipulatorAxes;
    
    public static final int DRIVER_AXES_COUNT = 3;
    public static final int MANIPULATOR_AXES_COUNT = 4;

    protected boolean isDriver;
    protected int index;
    protected String name;

    protected WsJoystickAxisEnum(boolean isDriver, int index, String desc) {
        this.isDriver = isDriver;
        this.index = index;
        this.name = desc;
    }

    // Axis Enums
    public static final int LEFT_JOYSTICK_Y = 2;
    public static final int LEFT_JOYSTICK_X = 1;
    public static final int RIGHT_JOYSTICK_Y = 4;
    public static final int RIGHT_JOYSTICK_X = 3;
    public static final int DPAD_Y = 6;
    public static final int DPAD_X = 5;

    // Driver Enums
    public static final WsJoystickAxisEnum DRIVER_THROTTLE = new WsJoystickAxisEnum(true, LEFT_JOYSTICK_Y, "DRIVER_THROTTLE");
    public static final WsJoystickAxisEnum DRIVER_HEADING = new WsJoystickAxisEnum(true, RIGHT_JOYSTICK_X, "DRIVER_HEADING");
    public static final WsJoystickAxisEnum DRIVER_DPAD_Y = new WsJoystickAxisEnum(true, DPAD_Y, "DRIVER_DPAD_Y");

    // Manipulator Enums
    public static final WsJoystickAxisEnum MANIPULATOR_ENTER_FLYWHEEL_ADJUSTMENT = new WsJoystickAxisEnum(false, LEFT_JOYSTICK_Y, "MANIPULATOR_ENTER_FLYWHEEL_ADJUSTMENT");
    public static final WsJoystickAxisEnum MANIPULATOR_EXIT_FLYWHEEL_ADJUSTMENT = new WsJoystickAxisEnum(false, RIGHT_JOYSTICK_Y, "MANIPULATOR_EXIT_FLYWHEEL_ADJUSTMENT");
    public static final WsJoystickAxisEnum MANIPULATOR_DPAD_Y = new WsJoystickAxisEnum(false, DPAD_Y, "MANIPULATOR_DPAD_Y");
    public static final WsJoystickAxisEnum MANIPULATOR_DPAD_X = new WsJoystickAxisEnum(false, DPAD_X, "MANIPULATOR_DPAD_X");

    public static WsJoystickAxisEnum getEnumFromIndex(boolean driver, int index) {
        if (index >= 0 && index < (driver ? driverAxes.length : manipulatorAxes.length)) {
            return (driver ? driverAxes[index] : manipulatorAxes[index]);
        }
        return null;
    }

    public String toString() {
        return name;
    }

    public int toValue() {
        return index;
    }
    
    public boolean isDriver() {
        return isDriver;
    }
    
    static {
        driverAxes = new WsJoystickAxisEnum[DRIVER_AXES_COUNT];
        manipulatorAxes = new WsJoystickAxisEnum[MANIPULATOR_AXES_COUNT];
        
        driverAxes[0] = DRIVER_THROTTLE;
        driverAxes[1] = DRIVER_HEADING;
        driverAxes[2] = DRIVER_DPAD_Y;
        
        manipulatorAxes[0] = MANIPULATOR_ENTER_FLYWHEEL_ADJUSTMENT;
        manipulatorAxes[1] = MANIPULATOR_EXIT_FLYWHEEL_ADJUSTMENT;
        manipulatorAxes[2] = MANIPULATOR_DPAD_Y;
        manipulatorAxes[3] = MANIPULATOR_DPAD_X;
    }

}
