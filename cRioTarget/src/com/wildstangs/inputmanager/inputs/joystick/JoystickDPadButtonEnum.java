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
public class JoystickDPadButtonEnum implements IInputEnum
{

    protected static JoystickDPadButtonEnum[] driverDPadButtons;
    protected static JoystickDPadButtonEnum[] manipulatorDPadButtons;
    protected static final int DRIVER_DPAD_BUTTON_COUNT = 4;
    protected static final int MANIPULATOR_DPAD_BUTTON_COUNT = 4;
    protected boolean isDriver;
    protected int index;
    protected String name;

    protected JoystickDPadButtonEnum(boolean isDriver, int index, String name)
    {
        this.isDriver = isDriver;
        this.index = index;
        this.name = name;
    }
    public static final int DPAD_LEFT = 0;
    public static final int DPAD_RIGHT = 1;
    public static final int DPAD_UP = 2;
    public static final int DPAD_DOWN = 3;
    
    public static final JoystickDPadButtonEnum DRIVER_D_PAD_BUTTON_LEFT = new JoystickDPadButtonEnum(true, DPAD_LEFT, "DRIVER_D_PAD_LEFT");
    public static final JoystickDPadButtonEnum DRIVER_D_PAD_BUTTON_RIGHT = new JoystickDPadButtonEnum(true, DPAD_RIGHT, "DRIVER_D_PAD_RIGHT");
    public static final JoystickDPadButtonEnum DRIVER_D_PAD_BUTTON_UP = new JoystickDPadButtonEnum(true, DPAD_UP, "DRIVER_D_PAD_UP");
    public static final JoystickDPadButtonEnum DRIVER_D_PAD_BUTTON_DOWN = new JoystickDPadButtonEnum(true, DPAD_DOWN, "DRIVER_D_PAD_DOWN");
    
    public static final JoystickDPadButtonEnum MANIPULATOR_D_PAD_BUTTON_LEFT = new JoystickDPadButtonEnum(false, DPAD_LEFT, "MANIPULATOR_D_PAD_LEFT");
    public static final JoystickDPadButtonEnum MANIPULATOR_D_PAD_BUTTON_RIGHT = new JoystickDPadButtonEnum(false, DPAD_RIGHT, "MANIPULATOR_D_PAD_RIGHT");
    public static final JoystickDPadButtonEnum MANIPULATOR_D_PAD_BUTTON_UP = new JoystickDPadButtonEnum(false, DPAD_UP, "MANIPULATOR_D_PAD_UP");
    public static final JoystickDPadButtonEnum MANIPULATOR_D_PAD_BUTTON_DOWN = new JoystickDPadButtonEnum(false, DPAD_DOWN, "MANIPULATOR_D_PAD_DOWN");

    public static JoystickDPadButtonEnum getEnumFromIndex(boolean driver, int index)
    {
        if (index >= 0 && index < (driver ? driverDPadButtons.length : manipulatorDPadButtons.length))
        {
            return (driver ? driverDPadButtons[index] : manipulatorDPadButtons[index]);
        }
        return null;
    }

    public String toString()
    {
        return name;
    }

    public int toValue()
    {
        return index;
    }

    public boolean isDriver()
    {
        return isDriver;
    }

    static
    {
        driverDPadButtons = new JoystickDPadButtonEnum[DRIVER_DPAD_BUTTON_COUNT];
        manipulatorDPadButtons = new JoystickDPadButtonEnum[MANIPULATOR_DPAD_BUTTON_COUNT];

        driverDPadButtons[DPAD_LEFT] = DRIVER_D_PAD_BUTTON_LEFT;
        driverDPadButtons[DPAD_RIGHT] = DRIVER_D_PAD_BUTTON_RIGHT;
        driverDPadButtons[DPAD_UP] = DRIVER_D_PAD_BUTTON_UP;
        driverDPadButtons[DPAD_DOWN] = DRIVER_D_PAD_BUTTON_DOWN;

        manipulatorDPadButtons[DPAD_LEFT] = MANIPULATOR_D_PAD_BUTTON_LEFT;
        manipulatorDPadButtons[DPAD_RIGHT] = MANIPULATOR_D_PAD_BUTTON_RIGHT;
        manipulatorDPadButtons[DPAD_UP] = MANIPULATOR_D_PAD_BUTTON_UP;
        manipulatorDPadButtons[DPAD_DOWN] = MANIPULATOR_D_PAD_BUTTON_DOWN;
    }
}
