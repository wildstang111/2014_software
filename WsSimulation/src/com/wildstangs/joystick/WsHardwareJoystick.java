/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.joystick;

import com.wildstangs.input.Controller;
import com.wildstangs.input.Controllers;
import com.wildstangs.inputmanager.inputs.joystick.IHardwareJoystick;
import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author chadschmidt
 */
public class WsHardwareJoystick implements IHardwareJoystick {

    int controllerIndex;

    boolean[] buttonStates;

    double rightJoystickY;

    double rightJoystickX;

    double leftJoystickY;

    double leftJoystickX;

    // Up is -1
    int dPadUpDown;

    // Right is -1
    int dPadLeftRight;

    private final byte[] m_axes_mapping;
    private final byte[] m_buttons_mapping;

    public WsHardwareJoystick() {
        m_axes_mapping = new byte[6];
        m_buttons_mapping = new byte[2];

        m_axes_mapping[AxisType.kX.value] = kDefaultXAxis;
        m_axes_mapping[AxisType.kY.value] = kDefaultYAxis;
        m_axes_mapping[AxisType.kZ.value] = kDefaultZAxis;
        m_axes_mapping[AxisType.kTwist.value] = kDefaultTwistAxis;
        m_axes_mapping[AxisType.kThrottle.value] = kDefaultThrottleAxis;

        m_buttons_mapping[ButtonType.kTrigger.value] = kDefaultTriggerButton;
        m_buttons_mapping[ButtonType.kTop.value] = kDefaultTopButton;
    }

    public boolean initializeJoystick() {
        try {
            Controllers.create();
            for (int i = 0; i < Controllers.getControllerCount(); i++) {
                if ((Controllers.getController(i).getName().toUpperCase().contains("Logitech".toUpperCase())
                        || Controllers.getController(i).getName().toUpperCase().contains("Gamepad".toUpperCase()))
                        && !Controllers.isControllerUsed(i)) {
                    Controllers.markControllerUsed(i);
                    controllerIndex = i;
                    buttonStates = new boolean[Controllers.getController(i).getButtonCount()];
                    return true;
                }
            }
        } catch (Exception ex) {
            System.out.println("Controller creation failed!");
        }
        return false;

    }

    public void pullData() {
        Controller controller = Controllers.getController(controllerIndex);
        controller.poll();
        for (int i = 0; i < buttonStates.length; i++) {
            buttonStates[i] = controller.isButtonPressed(i);
        }
        rightJoystickY = controller.getRZAxisValue();
        rightJoystickX = controller.getZAxisValue();
        leftJoystickY = controller.getYAxisValue();
        leftJoystickX = controller.getXAxisValue();

        dPadLeftRight = (int) controller.getPovX();
        dPadUpDown = (int) controller.getPovY();
    }

    static final byte kDefaultXAxis = 1;
    static final byte kDefaultYAxis = 2;
    static final byte kDefaultZAxis = 3;
    static final byte kDefaultTwistAxis = 3;
    static final byte kDefaultThrottleAxis = 4;
    static final int kDefaultTriggerButton = 1;
    static final int kDefaultTopButton = 2;

    /**
     * Represents an analog axis on a joystick.
     */
    public static class AxisType {

        /**
         * The integer value representing this enumeration
         */
        public final int value;
        static final int kX_val = 0;
        static final int kY_val = 1;
        static final int kZ_val = 2;
        static final int kTwist_val = 3;
        static final int kThrottle_val = 4;
        static final int kNumAxis_val = 5;
        /**
         * axis: x-axis
         */
        public static final AxisType kX = new AxisType(kX_val);
        /**
         * axis: y-axis
         */
        public static final AxisType kY = new AxisType(kY_val);
        /**
         * axis: z-axis
         */
        public static final AxisType kZ = new AxisType(kZ_val);
        /**
         * axis: twist
         */
        public static final AxisType kTwist = new AxisType(kTwist_val);
        /**
         * axis: throttle
         */
        public static final AxisType kThrottle = new AxisType(kThrottle_val);
        /**
         * axis: number of axis
         */
        public static final AxisType kNumAxis = new AxisType(kNumAxis_val);

        private AxisType(int value) {
            this.value = value;
        }
    }

    /**
     * Represents a digital button on the JoyStick
     */
    public static class ButtonType {

        /**
         * The integer value representing this enumeration
         */
        public final int value;
        static final int kTrigger_val = 0;
        static final int kTop_val = 1;
        static final int kNumButton_val = 2;
        /**
         * button: trigger
         */
        public static final ButtonType kTrigger = new ButtonType((kTrigger_val));
        /**
         * button: top button
         */
        public static final ButtonType kTop = new ButtonType(kTop_val);
        /**
         * button: num button types
         */
        public static final ButtonType kNumButton = new ButtonType(kNumButton_val);

        private ButtonType(int value) {
            this.value = value;
        }
    }

    /**
     * Get the X value of the joystick. This depends on the mapping of the
     * joystick connected to the current port.
     *
     * @return The X value of the joystick.
     */
    public double getX() {
        return leftJoystickX;
    }

    /**
     * Get the Y value of the joystick. This depends on the mapping of the
     * joystick connected to the current port.
     *
     * @return The Y value of the joystick.
     */
    public double getY() {
        return leftJoystickY;
    }

    /**
     * Get the Z value of the joystick. This depends on the mapping of the
     * joystick connected to the current port.
     *
     * @return The Z value of the joystick.
     */
    public double getZ() {
        return rightJoystickX;
    }

    /**
     * Get the twist value of the current joystick. This depends on the mapping
     * of the joystick connected to the current port.
     *
     * @return The Twist value of the joystick.
     */
    public double getTwist() {
        return dPadLeftRight;
    }

    /**
     * Get the throttle value of the current joystick. This depends on the
     * mapping of the joystick connected to the current port.
     *
     * @return The Throttle value of the joystick.
     */
    public double getThrottle() {
        return dPadUpDown;
    }

    /**
     * Get the value of the axis.
     *
     * @param axis The axis to read [1-6].
     * @return The value of the axis.
     */
    public double getRawAxis(final int axis) {
        return 0;
    }

    /**
     * For the current joystick, return the axis determined by the argument.
     *
     * This is for cases where the joystick axis is returned programatically,
     * otherwise one of the previous functions would be preferable (for example
     * getX()).
     *
     * @param axis The axis to read.
     * @return The value of the axis.
     */
    public double getAxis(final AxisType axis) {
        switch (axis.value) {
            case AxisType.kX_val:
                return getX();
            case AxisType.kY_val:
                return getY();
            case AxisType.kZ_val:
                return getZ();
            case AxisType.kTwist_val:
                return getTwist();
            case AxisType.kThrottle_val:
                return getThrottle();
            default:
                return 0.0;
        }
    }

    /**
     * Read the state of the trigger on the joystick.
     *
     * Look up which button has been assigned to the trigger and read its state.
     *
     * @return The state of the trigger.
     */
    public boolean getTrigger() {
        return getRawButton(m_buttons_mapping[ButtonType.kTrigger.value]);
    }

    /**
     * Read the state of the top button on the joystick.
     *
     * Look up which button has been assigned to the top and read its state.
     *
     * @return The state of the top button.
     */
    public boolean getTop() {
        return getRawButton(m_buttons_mapping[ButtonType.kTop.value]);
    }

    /**
     * This is not supported for the Joystick. This method is only here to
     * complete the GenericHID interface.
     *
     * @return The state of the bumper (always false)
     */
    public boolean getBumper() {
        return false;
    }

    /**
     * Get the button value for buttons 1 through 12.
     *
     * The buttons are returned in a single 16 bit value with one bit
     * representing the state of each button. The appropriate button is returned
     * as a boolean value.
     *
     * @param button The button number to be read.
     * @return The state of the button.
     */
    public boolean getRawButton(final int button) {
        return buttonStates[button - 1];
    }

    /**
     * Get buttons based on an enumerated type.
     *
     * The button type will be looked up in the list of buttons and then read.
     *
     * @param button The type of button to read.
     * @return The state of the button.
     */
    public boolean getButton(ButtonType button) {
        switch (button.value) {
            case ButtonType.kTrigger_val:
                return getTrigger();
            case ButtonType.kTop_val:
                return getTop();
            default:
                return false;
        }
    }

    /**
     * Get the magnitude of the direction vector formed by the joystick's
     * current position relative to its origin
     *
     * @return The magnitude of the direction vector
     */
    public double getMagnitude() {
        return Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2));
    }

    /**
     * Get the direction of the vector formed by the joystick and its origin in
     * radians
     *
     * @return The direction of the vector in radians
     */
    public double getDirectionRadians() {
        return Math.atan2(getX(), -getY());
    }

    /**
     * Get the direction of the vector formed by the joystick and its origin in
     * degrees
     *
     * uses acos(-1) to represent Pi due to absence of readily accessable Pi
     * constant in C++
     *
     * @return The direction of the vector in degrees
     */
    public double getDirectionDegrees() {
        return Math.toDegrees(getDirectionRadians());
    }

    /**
     * Get the channel currently associated with the specified axis.
     *
     * @param axis The axis to look up the channel for.
     * @return The channel fr the axis.
     */
    public int getAxisChannel(Joystick.AxisType axis) {
        return m_axes_mapping[axis.value];
    }

    /**
     * Set the channel associated with a specified axis.
     *
     * @param axis The axis to set the channel for.
     * @param channel The channel to set the axis to.
     */
    public void setAxisChannel(Joystick.AxisType axis, int channel) {
        m_axes_mapping[axis.value] = (byte) channel;
    }

}
