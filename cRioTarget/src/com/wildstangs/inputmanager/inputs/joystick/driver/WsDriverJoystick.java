/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.inputmanager.inputs.joystick.driver;

import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.inputmanager.base.IInput;
import com.wildstangs.inputmanager.base.IInputEnum;
import com.wildstangs.inputmanager.inputs.joystick.IHardwareJoystick;
import com.wildstangs.inputmanager.inputs.joystick.IJoystick;
import com.wildstangs.inputmanager.inputs.joystick.WsJoystickAxisEnum;
import com.wildstangs.inputmanager.inputs.joystick.WsJoystickButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author Nathan
 */
public class WsDriverJoystick implements IInput {

    DoubleSubject throttle;
    DoubleSubject heading;
    private DoubleSubject dPadUpDown;
    final static int numberOfButtons = 12;
    BooleanSubject[] buttons;
    Joystick driverJoystick = null;

    public Subject getSubject(ISubjectEnum subjectEnum) {
        if (subjectEnum == WsJoystickAxisEnum.DRIVER_THROTTLE) {
            return throttle;
        } else if (subjectEnum == WsJoystickAxisEnum.DRIVER_HEADING) {
            return heading;
        } else if (subjectEnum == WsJoystickAxisEnum.DRIVER_D_PAD_UP_DOWN) {
            return dPadUpDown;
        } else if (subjectEnum instanceof WsJoystickButtonEnum && ((WsJoystickButtonEnum) subjectEnum).isDriver() == true) {
            return buttons[((WsJoystickButtonEnum) subjectEnum).toValue()];
        } else {
            System.out.println("Subject not supported or incorrect.");
            return null;
        }
    }

    public WsDriverJoystick() {
        throttle = new DoubleSubject("Throttle");
        heading = new DoubleSubject("Heading");
        dPadUpDown = new DoubleSubject(WsJoystickAxisEnum.DRIVER_D_PAD_UP_DOWN);
        driverJoystick = (Joystick) new Joystick(1);
        buttons = new BooleanSubject[numberOfButtons];
        driverJoystick.setAxisChannel(Joystick.AxisType.kThrottle, 6);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new BooleanSubject(WsJoystickButtonEnum.getEnumFromIndex(true, i));
        }

    }

    public void set(IInputEnum key, Object value) {
        if (key == WsJoystickAxisEnum.DRIVER_THROTTLE) {
            throttle.setValue(value);
            // this serves no purpose but an example
        } else if (key == WsJoystickAxisEnum.DRIVER_HEADING) {
            heading.setValue(value);
        } else if (key == WsJoystickAxisEnum.DRIVER_D_PAD_UP_DOWN) {
            dPadUpDown.setValue(value);
        } else if (key instanceof WsJoystickButtonEnum && ((WsJoystickButtonEnum) key).isDriver() == true) {
            buttons[((WsJoystickButtonEnum) key).toValue()].setValue(value);
        } else {
            System.out.println("key not supported or incorrect.");
        }

    }

    public Object get(IInputEnum key) {
        if (key == WsJoystickAxisEnum.DRIVER_THROTTLE) {
            return throttle.getValueAsObject();
        } else if (key == WsJoystickAxisEnum.DRIVER_HEADING) {
            return heading.getValueAsObject();
        } else if (key == WsJoystickAxisEnum.DRIVER_D_PAD_UP_DOWN) {
            return dPadUpDown.getValueAsObject();
        } else if (key instanceof WsJoystickButtonEnum && ((WsJoystickButtonEnum) key).isDriver() == true) {
            return buttons[((WsJoystickButtonEnum) key).toValue()].getValueAsObject();
        } else {
            return new Double(-100);
        }
    }

    public void update() {
        throttle.updateValue();
        heading.updateValue();
        dPadUpDown.updateValue();
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].updateValue();
        }
    }

    public void pullData() {
        if (driverJoystick instanceof IHardwareJoystick) {
            ((IHardwareJoystick) driverJoystick).pullData();
        }
        throttle.setValue(driverJoystick.getY() * -1);
        heading.setValue(driverJoystick.getZ() * -1);
        dPadUpDown.setValue(driverJoystick.getThrottle() * -1);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setValue(driverJoystick.getRawButton(i + 1));
        }

    }

    public void notifyConfigChange() {
    }
}
