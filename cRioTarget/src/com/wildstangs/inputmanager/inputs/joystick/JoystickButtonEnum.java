package com.wildstangs.inputmanager.inputs.joystick;

import com.wildstangs.inputmanager.base.IInputEnum;

/**
 *
 * @author Joey
 */
public class JoystickButtonEnum implements IInputEnum {

    protected static JoystickButtonEnum[] driverButtons;
    protected static JoystickButtonEnum[] manipulatorButtons;

    protected boolean isDriver;
    protected int index;
    protected String name;

    public JoystickButtonEnum(boolean isDriver, int index, String desc) {
        this.isDriver = isDriver;
        this.index = index;
        this.name = desc;
    }

    //Set up all of the driver buttons
    public static final JoystickButtonEnum DRIVER_BUTTON_1 = new JoystickButtonEnum(true, 0, "D_BUTTON1");
    public static final JoystickButtonEnum DRIVER_BUTTON_2 = new JoystickButtonEnum(true, 1, "D_BUTTON2");
    public static final JoystickButtonEnum DRIVER_BUTTON_3 = new JoystickButtonEnum(true, 2, "D_BUTTON3");
    public static final JoystickButtonEnum DRIVER_BUTTON_4 = new JoystickButtonEnum(true, 3, "D_BUTTON4");
    public static final JoystickButtonEnum DRIVER_BUTTON_5 = new JoystickButtonEnum(true, 4, "D_BUTTON5");
    public static final JoystickButtonEnum DRIVER_BUTTON_6 = new JoystickButtonEnum(true, 5, "D_BUTTON6");
    public static final JoystickButtonEnum DRIVER_BUTTON_7 = new JoystickButtonEnum(true, 6, "D_BUTTON7");
    public static final JoystickButtonEnum DRIVER_BUTTON_8 = new JoystickButtonEnum(true, 7, "D_BUTTON8");
    public static final JoystickButtonEnum DRIVER_BUTTON_9 = new JoystickButtonEnum(true, 8, "D_BUTTON9");
    public static final JoystickButtonEnum DRIVER_BUTTON_10 = new JoystickButtonEnum(true, 9, "D_BUTTON10");
    public static final JoystickButtonEnum DRIVER_BUTTON_11 = new JoystickButtonEnum(true, 10, "D_BUTTON11");
    public static final JoystickButtonEnum DRIVER_BUTTON_12 = new JoystickButtonEnum(true, 11, "D_BUTTON12");

    //Set up all of the manipulator buttons
    public static final JoystickButtonEnum MANIPULATOR_BUTTON_1 = new JoystickButtonEnum(false, 0, "M_BUTTON1");
    public static final JoystickButtonEnum MANIPULATOR_BUTTON_2 = new JoystickButtonEnum(false, 1, "M_BUTTON2");
    public static final JoystickButtonEnum MANIPULATOR_BUTTON_3 = new JoystickButtonEnum(false, 2, "M_BUTTON3");
    public static final JoystickButtonEnum MANIPULATOR_BUTTON_4 = new JoystickButtonEnum(false, 3, "M_BUTTON4");
    public static final JoystickButtonEnum MANIPULATOR_BUTTON_5 = new JoystickButtonEnum(false, 4, "M_BUTTON5");
    public static final JoystickButtonEnum MANIPULATOR_BUTTON_6 = new JoystickButtonEnum(false, 5, "M_BUTTON6");
    public static final JoystickButtonEnum MANIPULATOR_BUTTON_7 = new JoystickButtonEnum(false, 6, "M_BUTTON7");
    public static final JoystickButtonEnum MANIPULATOR_BUTTON_8 = new JoystickButtonEnum(false, 7, "M_BUTTON8");
    public static final JoystickButtonEnum MANIPULATOR_BUTTON_9 = new JoystickButtonEnum(false, 8, "M_BUTTON9");
    public static final JoystickButtonEnum MANIPULATOR_BUTTON_10 = new JoystickButtonEnum(false, 9, "M_BUTTON10");
    public static final JoystickButtonEnum MANIPULATOR_BUTTON_11 = new JoystickButtonEnum(false, 10, "M_BUTTON11");
    public static final JoystickButtonEnum MANIPULATOR_BUTTON_12 = new JoystickButtonEnum(false, 11, "M_BUTTON12");

    public static JoystickButtonEnum getEnumFromIndex(boolean driver, int index) {
        if (index >= 0 && index < (driver ? driverButtons.length : manipulatorButtons.length)) {
            return (driver ? driverButtons[index] : manipulatorButtons[index]);
        }

        return null;
    }

    public String toString() {
        return name;
    }

    public boolean isDriver() {
        return isDriver;
    }

    public int toValue() {
        return index;
    }

    //Add all of the buttons into an array for easy access
    static {
        driverButtons = new JoystickButtonEnum[12];
        manipulatorButtons = new JoystickButtonEnum[12];

        driverButtons[0] = DRIVER_BUTTON_1;
        driverButtons[1] = DRIVER_BUTTON_2;
        driverButtons[2] = DRIVER_BUTTON_3;
        driverButtons[3] = DRIVER_BUTTON_4;
        driverButtons[4] = DRIVER_BUTTON_5;
        driverButtons[5] = DRIVER_BUTTON_6;
        driverButtons[6] = DRIVER_BUTTON_7;
        driverButtons[7] = DRIVER_BUTTON_8;
        driverButtons[8] = DRIVER_BUTTON_9;
        driverButtons[9] = DRIVER_BUTTON_10;
        driverButtons[10] = DRIVER_BUTTON_11;
        driverButtons[11] = DRIVER_BUTTON_12;

        manipulatorButtons[0] = MANIPULATOR_BUTTON_1;
        manipulatorButtons[1] = MANIPULATOR_BUTTON_2;
        manipulatorButtons[2] = MANIPULATOR_BUTTON_3;
        manipulatorButtons[3] = MANIPULATOR_BUTTON_4;
        manipulatorButtons[4] = MANIPULATOR_BUTTON_5;
        manipulatorButtons[5] = MANIPULATOR_BUTTON_6;
        manipulatorButtons[6] = MANIPULATOR_BUTTON_7;
        manipulatorButtons[7] = MANIPULATOR_BUTTON_8;
        manipulatorButtons[8] = MANIPULATOR_BUTTON_9;
        manipulatorButtons[9] = MANIPULATOR_BUTTON_10;
        manipulatorButtons[10] = MANIPULATOR_BUTTON_11;
        manipulatorButtons[11] = MANIPULATOR_BUTTON_12;
    }
}
