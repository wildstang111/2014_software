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
public class JoystickAxisEnum implements IInputEnum {

    protected static JoystickAxisEnum[] driverAxes;
    protected static JoystickAxisEnum[] manipulatorAxes;
    
    public static final int DRIVER_AXES_COUNT = 3;
    public static final int MANIPULATOR_AXES_COUNT = 4;

    protected boolean isDriver;
    protected int index;
    protected String name;

    protected JoystickAxisEnum(boolean isDriver, int index, String desc) {
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
    public static final JoystickAxisEnum DRIVER_THROTTLE = new JoystickAxisEnum(true, LEFT_JOYSTICK_Y, "DRIVER_THROTTLE");
    public static final JoystickAxisEnum DRIVER_HEADING = new JoystickAxisEnum(true, RIGHT_JOYSTICK_X, "DRIVER_HEADING");
    public static final JoystickAxisEnum DRIVER_DPAD_Y = new JoystickAxisEnum(true, DPAD_Y, "DRIVER_DPAD_Y");

    // Manipulator Enums
    public static final JoystickAxisEnum MANIPULATOR_FRONT_ARM_CONTROL = new JoystickAxisEnum(false, LEFT_JOYSTICK_Y, "MANIPULATOR_ENTER_FLYWHEEL_ADJUSTMENT");
    public static final JoystickAxisEnum MANIPULATOR_BACK_ARM_CONTROL = new JoystickAxisEnum(false, RIGHT_JOYSTICK_Y, "MANIPULATOR_EXIT_FLYWHEEL_ADJUSTMENT");
    public static final JoystickAxisEnum MANIPULATOR_DPAD_Y = new JoystickAxisEnum(false, DPAD_Y, "MANIPULATOR_DPAD_Y");
    public static final JoystickAxisEnum MANIPULATOR_DPAD_X = new JoystickAxisEnum(false, DPAD_X, "MANIPULATOR_DPAD_X");

    public static JoystickAxisEnum getEnumFromIndex(boolean driver, int index) {
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
        driverAxes = new JoystickAxisEnum[DRIVER_AXES_COUNT];
        manipulatorAxes = new JoystickAxisEnum[MANIPULATOR_AXES_COUNT];
        
        driverAxes[0] = DRIVER_THROTTLE;
        driverAxes[1] = DRIVER_HEADING;
        driverAxes[2] = DRIVER_DPAD_Y;
        
        manipulatorAxes[0] = MANIPULATOR_FRONT_ARM_CONTROL;
        manipulatorAxes[1] = MANIPULATOR_BACK_ARM_CONTROL;
        manipulatorAxes[2] = MANIPULATOR_DPAD_Y;
        manipulatorAxes[3] = MANIPULATOR_DPAD_X;
    }

}
