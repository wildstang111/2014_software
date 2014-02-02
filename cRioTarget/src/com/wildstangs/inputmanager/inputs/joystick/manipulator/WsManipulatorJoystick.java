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

/**
 *
 * @author Nathan
 */
public class WsManipulatorJoystick implements IInput {

    final static int numberOfAxes = 6;
    DoubleSubject[] axes;
    final static int numberOfButtons = 12;
    BooleanSubject[] buttons;
    Joystick manipulatorJoystick = null;

    public WsManipulatorJoystick() {
        manipulatorJoystick = (Joystick) new Joystick(2);

        axes = new DoubleSubject[numberOfAxes];
        for (int i = 0; i < axes.length; i++) {
            if (WsJoystickAxisEnum.getEnumFromIndex(true, i) != null) {
                axes[i] = new DoubleSubject(WsJoystickAxisEnum.getEnumFromIndex(true, i));
            } else {
                axes[i] = new DoubleSubject("DriverSubject" + 1);
            }
        }

        buttons = new BooleanSubject[numberOfButtons];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new BooleanSubject(WsJoystickButtonEnum.getEnumFromIndex(false, i));
        }
    }

    public Subject getSubject(ISubjectEnum subjectEnum) {
        if (subjectEnum instanceof WsJoystickAxisEnum && ((WsJoystickAxisEnum) subjectEnum).isDriver() == false) {
            return axes[((WsJoystickAxisEnum) subjectEnum).toValue() - 1];
        } else if (subjectEnum instanceof WsJoystickButtonEnum && ((WsJoystickButtonEnum) subjectEnum).isDriver() == false) {
            return buttons[((WsJoystickButtonEnum) subjectEnum).toValue()];
        } else {
            System.out.println("Subject not supported or incorrect.");
            return null;
        }
    }

    public void set(IInputEnum key, Object value) {
        if (key instanceof WsJoystickAxisEnum && ((WsJoystickAxisEnum) key).isDriver() == false) {
            axes[((WsJoystickAxisEnum) key).toValue() - 1].setValue(value);
        } else if (key instanceof WsJoystickButtonEnum && ((WsJoystickButtonEnum) key).isDriver() == false) {
            buttons[((WsJoystickButtonEnum) key).toValue()].setValue(value);
        } else {
            System.out.println("key not supported or incorrect.");
        }
    }

    public Object get(IInputEnum key) {
        if (key instanceof WsJoystickAxisEnum) {
            return axes[((WsJoystickAxisEnum) key).toValue() - 1].getValueAsObject();
        } else if (key instanceof WsJoystickButtonEnum && ((WsJoystickButtonEnum) key).isDriver() == false) {
            return buttons[((WsJoystickButtonEnum) key).toValue()].getValueAsObject();
        } else {
            return new Double(-100);
        }
    }

    public void update() {
        for (int i = 0; i < axes.length; i++) {
            axes[i].updateValue();
        }
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].updateValue();
        }
    }

    public void pullData() {
        if (manipulatorJoystick instanceof IHardwareJoystick) {
            ((IHardwareJoystick) manipulatorJoystick).pullData();
        }
        for (int i = 0; i < axes.length; i++) {
            // Invert the vertical axes so that full up is 1
            if (i % 2 != 0) {
                axes[i].setValue(manipulatorJoystick.getRawAxis(i + 1) * -1);
            } else {
                axes[i].setValue(manipulatorJoystick.getRawAxis(i + 1));
            }
        }
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setValue(manipulatorJoystick.getRawButton(i + 1));
        }
    }
    
    public void set(Object value)
    {
        this.set((IInputEnum) null, value);
    }
    
    public Subject getSubject()
    {
        return this.getSubject((ISubjectEnum) null);
    }

    public Object get()
    {
        return this.get((IInputEnum) null);
    }

    public void notifyConfigChange() {
    }
}
