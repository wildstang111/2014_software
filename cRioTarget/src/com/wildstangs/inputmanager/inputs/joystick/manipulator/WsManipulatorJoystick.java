package com.wildstangs.inputmanager.inputs.joystick.manipulator;

import com.wildstangs.inputmanager.base.IInput;
import com.wildstangs.inputmanager.base.IInputEnum;
import com.wildstangs.inputmanager.inputs.joystick.IHardwareJoystick;
import com.wildstangs.inputmanager.inputs.joystick.WsJoystickAxisEnum;
import com.wildstangs.inputmanager.inputs.joystick.WsJoystickButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Nathan
 */
public class WsManipulatorJoystick implements IInput {

    DoubleSubject enterFlywheelAdjustment;
    DoubleSubject exitFlywheelAdjustment;
    DoubleSubject dPadUpDown;
    DoubleSubject dPadLeftRight;
    final static int numberOfButtons = 12;
    BooleanSubject[] buttons;
    Joystick manipulatorJoystick = null;

    public Subject getSubject(ISubjectEnum subjectEnum) {
        if (subjectEnum == WsJoystickAxisEnum.MANIPULATOR_ENTER_FLYWHEEL_ADJUSTMENT) {
            return enterFlywheelAdjustment;
        } else if (subjectEnum == WsJoystickAxisEnum.MANIPULATOR_EXIT_FLYWHEEL_ADJUSTMENT) {
            return exitFlywheelAdjustment;
        } else if (subjectEnum == WsJoystickAxisEnum.MANIPULATOR_DPAD_Y) {
            return dPadUpDown;
        } else if (subjectEnum == WsJoystickAxisEnum.MANIPULATOR_DPAD_X) {
            return dPadLeftRight;
        } else if (subjectEnum instanceof WsJoystickButtonEnum && ((WsJoystickButtonEnum) subjectEnum).isDriver() == false) {
            return buttons[((WsJoystickButtonEnum) subjectEnum).toValue()];
        } else {
            System.out.println("Subject not supported or incorrect.");
            return null;
        }
    }

    public WsManipulatorJoystick() {
        enterFlywheelAdjustment = new DoubleSubject(WsJoystickAxisEnum.MANIPULATOR_ENTER_FLYWHEEL_ADJUSTMENT);
        exitFlywheelAdjustment = new DoubleSubject(WsJoystickAxisEnum.MANIPULATOR_EXIT_FLYWHEEL_ADJUSTMENT);
        dPadUpDown = new DoubleSubject(WsJoystickAxisEnum.MANIPULATOR_DPAD_Y);
        dPadLeftRight = new DoubleSubject(WsJoystickAxisEnum.MANIPULATOR_DPAD_X);
        manipulatorJoystick = (Joystick) new Joystick(2);
        manipulatorJoystick.setAxisChannel(Joystick.AxisType.kX, 1);
        manipulatorJoystick.setAxisChannel(Joystick.AxisType.kY, 2);
        manipulatorJoystick.setAxisChannel(Joystick.AxisType.kZ, 4);
//        manipulatorJoystick.setAxisChannel(Joystick.AxisType.k, 4);
        manipulatorJoystick.setAxisChannel(Joystick.AxisType.kTwist, 5);
        manipulatorJoystick.setAxisChannel(Joystick.AxisType.kThrottle, 6);

        buttons = new BooleanSubject[numberOfButtons];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new BooleanSubject(WsJoystickButtonEnum.getEnumFromIndex(false, i));
        }
    }

    public void set(IInputEnum key, Object value) {
        if (key == WsJoystickAxisEnum.MANIPULATOR_ENTER_FLYWHEEL_ADJUSTMENT) {
            enterFlywheelAdjustment.setValue(value);
        } else if (key == WsJoystickAxisEnum.MANIPULATOR_EXIT_FLYWHEEL_ADJUSTMENT) {
            exitFlywheelAdjustment.setValue(value);
        } else if (key == WsJoystickAxisEnum.MANIPULATOR_DPAD_Y) {
            dPadUpDown.setValue(value);
        } else if (key == WsJoystickAxisEnum.MANIPULATOR_DPAD_X) {
            dPadLeftRight.setValue(value);
        } else if (key instanceof WsJoystickButtonEnum && ((WsJoystickButtonEnum) key).isDriver() == false) {
            buttons[((WsJoystickButtonEnum) key).toValue()].setValue(value);
        } else {
            System.out.println("key not supported or incorrect.");
        }
    }

    public Object get(IInputEnum key) {
        if (key == WsJoystickAxisEnum.MANIPULATOR_ENTER_FLYWHEEL_ADJUSTMENT) {
            return enterFlywheelAdjustment.getValueAsObject();
        } else if (key == WsJoystickAxisEnum.MANIPULATOR_EXIT_FLYWHEEL_ADJUSTMENT) {
            return exitFlywheelAdjustment.getValueAsObject();
        } else if (key == WsJoystickAxisEnum.MANIPULATOR_DPAD_Y) {
            return dPadUpDown.getValueAsObject();
        } else if (key == WsJoystickAxisEnum.MANIPULATOR_DPAD_X) {
            return dPadLeftRight.getValueAsObject();
        } else if (key instanceof WsJoystickButtonEnum && ((WsJoystickButtonEnum) key).isDriver() == false) {
            return buttons[((WsJoystickButtonEnum) key).toValue()].getValueAsObject();
        } else {
            return new Double(-100);
        }
    }

    public void update() {
        enterFlywheelAdjustment.updateValue();
        exitFlywheelAdjustment.updateValue();
        dPadUpDown.updateValue();
        dPadLeftRight.updateValue();
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].updateValue();
        }
    }

    public void pullData() {
        if (manipulatorJoystick instanceof IHardwareJoystick) {
            ((IHardwareJoystick) manipulatorJoystick).pullData();
        }
        enterFlywheelAdjustment.setValue(manipulatorJoystick.getRawAxis(WsJoystickAxisEnum.LEFT_JOYSTICK_Y) * -1);
        exitFlywheelAdjustment.setValue(manipulatorJoystick.getRawAxis(WsJoystickAxisEnum.RIGHT_JOYSTICK_Y) * -1);
        //Get data from the D-pad
        //We invert the values so up & left are 1, down & right are -1
        dPadUpDown.setValue(manipulatorJoystick.getRawAxis(WsJoystickAxisEnum.DPAD_Y) * -1);
        dPadLeftRight.setValue(manipulatorJoystick.getRawAxis(WsJoystickAxisEnum.DPAD_X) * -1);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setValue(manipulatorJoystick.getRawButton(i + 1));
        }
        SmartDashboard.putNumber("Rax Axis 1", manipulatorJoystick.getRawAxis(1));
        SmartDashboard.putNumber("Rax Axis 2", manipulatorJoystick.getRawAxis(2));
        SmartDashboard.putNumber("Rax Axis 3", manipulatorJoystick.getRawAxis(3));
        SmartDashboard.putNumber("Rax Axis 4", manipulatorJoystick.getRawAxis(4));
        SmartDashboard.putNumber("Rax Axis 5", manipulatorJoystick.getRawAxis(5));
        SmartDashboard.putNumber("Rax Axis 6", manipulatorJoystick.getRawAxis(6));
    }

    public void notifyConfigChange() {
    }
}
