package com.wildstangs.inputmanager.inputs.joystick.driver;

import com.wildstangs.inputmanager.base.IInput;
import com.wildstangs.inputmanager.base.IInputEnum;
import com.wildstangs.inputmanager.inputs.joystick.IHardwareJoystick;
import com.wildstangs.inputmanager.inputs.joystick.JoystickAxisEnum;
import com.wildstangs.inputmanager.inputs.joystick.JoystickButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author Nathan
 */
public class DriverJoystick implements IInput {

    final static int numberOfAxes = 6;
    DoubleSubject[] axes;
    final static int numberOfButtons = 12;
    BooleanSubject[] buttons;
    Joystick driverJoystick = null;

    public DriverJoystick() {
        driverJoystick = (Joystick) new Joystick(1);

        axes = new DoubleSubject[numberOfAxes];
        for (int i = 0; i < axes.length; i++) {
            if (JoystickAxisEnum.getEnumFromIndex(true, i) != null) {
                axes[i] = new DoubleSubject(JoystickAxisEnum.getEnumFromIndex(true, i));
            } else {
                axes[i] = new DoubleSubject("DriverSubject" + i);
            }
        }

        buttons = new BooleanSubject[numberOfButtons];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new BooleanSubject(JoystickButtonEnum.getEnumFromIndex(true, i));
        }
    }

    public Subject getSubject(ISubjectEnum subjectEnum) {
        if (subjectEnum instanceof JoystickAxisEnum && ((JoystickAxisEnum) subjectEnum).isDriver() == true) {
            return axes[((JoystickAxisEnum) subjectEnum).toValue() - 1];
        } else if (subjectEnum instanceof JoystickButtonEnum && ((JoystickButtonEnum) subjectEnum).isDriver() == true) {
            return buttons[((JoystickButtonEnum) subjectEnum).toValue()];
        } else {
            System.out.println("Subject not supported or incorrect.");
            return null;
        }
    }

    public void set(IInputEnum key, Object value) {
        if (key instanceof JoystickAxisEnum && ((JoystickAxisEnum) key).isDriver() == true) {
            axes[((JoystickAxisEnum) key).toValue() - 1].setValue(value);
        } else if (key instanceof JoystickButtonEnum && ((JoystickButtonEnum) key).isDriver() == true) {
            buttons[((JoystickButtonEnum) key).toValue()].setValue(value);
        } else {
            System.out.println("key not supported or incorrect.");
        }
    }

    public Object get(IInputEnum key) {
        if (key instanceof JoystickAxisEnum && ((JoystickAxisEnum) key).isDriver() == true) {
            return axes[((JoystickAxisEnum) key).toValue() - 1].getValueAsObject();
        } else if (key instanceof JoystickButtonEnum && ((JoystickButtonEnum) key).isDriver() == true) {
            return buttons[((JoystickButtonEnum) key).toValue()].getValueAsObject();
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
        if (driverJoystick instanceof IHardwareJoystick) {
            ((IHardwareJoystick) driverJoystick).pullData();
        }
        for (int i = 0; i < axes.length; i++) {
            // Invert the vertical axes so that full up is 1
            if (i % 2 != 0) {
                axes[i].setValue(driverJoystick.getRawAxis(i + 1) * -1);
            } else {
                axes[i].setValue(driverJoystick.getRawAxis(i + 1));
            }
        }
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setValue(driverJoystick.getRawButton(i + 1));
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
