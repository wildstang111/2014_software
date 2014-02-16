package com.wildstangs.inputmanager.inputs.joystick.driver;

import com.wildstangs.inputmanager.base.IInput;
import com.wildstangs.inputmanager.base.IInputEnum;
import com.wildstangs.inputmanager.inputs.joystick.IHardwareJoystick;
import com.wildstangs.inputmanager.inputs.joystick.JoystickAxisEnum;
import com.wildstangs.inputmanager.inputs.joystick.JoystickButtonEnum;
import com.wildstangs.inputmanager.inputs.joystick.JoystickDPadButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.types.DataElement;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Nathan
 */
public class DriverJoystick implements IInput {

    protected final static int numberOfAxes = 6;
    protected DoubleSubject[] axes;
    protected final static int numberOfButtons = 12;
    protected BooleanSubject[] buttons;
    protected final static int numberOfDPadButtons = 4;
    protected BooleanSubject[] dPadButtons;
    protected final static int neededCyclesForChange = 8;
    protected DataElement[] dPadCyclesInState;
    protected Joystick driverJoystick = null;

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
        
        dPadButtons = new BooleanSubject[numberOfDPadButtons];
        dPadCyclesInState = new DataElement[numberOfDPadButtons];
        for(int i = 0; i < dPadButtons.length; i++)
        {
            dPadButtons[i] = new BooleanSubject(JoystickDPadButtonEnum.getEnumFromIndex(true, i));
            dPadCyclesInState[i] = new DataElement(dPadButtons[i].getValueAsObject(), new Integer(0));
        }
    }

    public Subject getSubject(ISubjectEnum subjectEnum) {
        if (subjectEnum instanceof JoystickAxisEnum && ((JoystickAxisEnum) subjectEnum).isDriver() == true) {
            return axes[((JoystickAxisEnum) subjectEnum).toValue()];
        } else if (subjectEnum instanceof JoystickButtonEnum && ((JoystickButtonEnum) subjectEnum).isDriver() == true) {
            return buttons[((JoystickButtonEnum) subjectEnum).toValue()];
        }
        else if(subjectEnum instanceof JoystickDPadButtonEnum && ((JoystickDPadButtonEnum) subjectEnum).isDriver())
        {
            return dPadButtons[((JoystickDPadButtonEnum) subjectEnum).toValue()];
        } 
        else {
            System.out.println("Subject not supported or incorrect.");
            return null;
        }
    }

    public void set(IInputEnum key, Object value) {
        if (key instanceof JoystickAxisEnum && ((JoystickAxisEnum) key).isDriver() == true) {
            axes[((JoystickAxisEnum) key).toValue()].setValue(value);
        } else if (key instanceof JoystickButtonEnum && ((JoystickButtonEnum) key).isDriver() == true) {
            buttons[((JoystickButtonEnum) key).toValue()].setValue(value);
        }
        else if(key instanceof JoystickDPadButtonEnum && ((JoystickDPadButtonEnum) key).isDriver())
        {
             dPadButtons[((JoystickDPadButtonEnum) key).toValue()].setValue(value);
        }
        else {
            System.out.println("key not supported or incorrect.");
        }
    }

    public Object get(IInputEnum key) {
        if (key instanceof JoystickAxisEnum && ((JoystickAxisEnum) key).isDriver() == true) {
            return axes[((JoystickAxisEnum) key).toValue()].getValueAsObject();
        } else if (key instanceof JoystickButtonEnum && ((JoystickButtonEnum) key).isDriver() == true) {
            return buttons[((JoystickButtonEnum) key).toValue()].getValueAsObject();
        }
        else if(key instanceof JoystickDPadButtonEnum && ((JoystickDPadButtonEnum) key).isDriver())
        {
            return dPadButtons[((JoystickDPadButtonEnum) key).toValue()].getValueAsObject();
        }
        else {
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
        for(int i = 0; i < dPadButtons.length; i++)
        {
            dPadButtons[i].updateValue();
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
        
        //DPad Button Logic
        if(axes[JoystickAxisEnum.DPAD_X].getValue() > 0)
        {
            if(dPadButtons[JoystickDPadButtonEnum.DPAD_LEFT].getValue())
            {
                Boolean key = (Boolean) dPadCyclesInState[JoystickDPadButtonEnum.DPAD_LEFT].getKey();
                if(key.booleanValue())
                {
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_LEFT].setKey(new Boolean(false));
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_LEFT].setValue(new Integer(0));
                }
                else
                {
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_LEFT].setValue(new Integer(((Integer)dPadCyclesInState[JoystickDPadButtonEnum.DPAD_LEFT].getValue()).intValue() + 1));
                }
            }
            
            if(!dPadButtons[JoystickDPadButtonEnum.DPAD_RIGHT].getValue())
            {
                Boolean key = (Boolean) dPadCyclesInState[JoystickDPadButtonEnum.DPAD_RIGHT].getKey();
                if(!key.booleanValue())
                {
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_RIGHT].setKey(new Boolean(true));
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_RIGHT].setValue(new Integer(0));
                }
                else
                {
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_RIGHT].setValue(new Integer(((Integer)dPadCyclesInState[JoystickDPadButtonEnum.DPAD_RIGHT].getValue()).intValue() + 1));
                }
            }
        }
        else if(axes[JoystickAxisEnum.DPAD_X].getValue() < 0)
        {
            if(!dPadButtons[JoystickDPadButtonEnum.DPAD_LEFT].getValue())
            {
                Boolean key = (Boolean) dPadCyclesInState[JoystickDPadButtonEnum.DPAD_LEFT].getKey();
                if(!key.booleanValue())
                {
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_LEFT].setKey(new Boolean(true));
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_LEFT].setValue(new Integer(0));
                }
                else
                {
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_LEFT].setValue(new Integer(((Integer)dPadCyclesInState[JoystickDPadButtonEnum.DPAD_LEFT].getValue()).intValue() + 1));
                }
            }
            
            if(dPadButtons[JoystickDPadButtonEnum.DPAD_RIGHT].getValue())
            {
                Boolean key = (Boolean) dPadCyclesInState[JoystickDPadButtonEnum.DPAD_RIGHT].getKey();
                if(key.booleanValue())
                {
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_RIGHT].setKey(new Boolean(false));
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_RIGHT].setValue(new Integer(0));
                }
                else
                {
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_RIGHT].setValue(new Integer(((Integer)dPadCyclesInState[JoystickDPadButtonEnum.DPAD_RIGHT].getValue()).intValue() + 1));
                }
            }
        }
        else
        {
            Boolean key = (Boolean) dPadCyclesInState[JoystickDPadButtonEnum.DPAD_LEFT].getKey();
            if(key.booleanValue())
            {
                dPadCyclesInState[JoystickDPadButtonEnum.DPAD_LEFT].setKey(new Boolean(false));
                dPadCyclesInState[JoystickDPadButtonEnum.DPAD_LEFT].setValue(new Integer(0));
            }
            else
            {
                dPadCyclesInState[JoystickDPadButtonEnum.DPAD_LEFT].setValue(new Integer(((Integer)dPadCyclesInState[JoystickDPadButtonEnum.DPAD_LEFT].getValue()).intValue() + 1));
            }
            
            key = (Boolean) dPadCyclesInState[JoystickDPadButtonEnum.DPAD_RIGHT].getKey();
            if(key.booleanValue())
            {
                dPadCyclesInState[JoystickDPadButtonEnum.DPAD_RIGHT].setKey(new Boolean(false));
                dPadCyclesInState[JoystickDPadButtonEnum.DPAD_RIGHT].setValue(new Integer(0));
            }
            else
            {
                dPadCyclesInState[JoystickDPadButtonEnum.DPAD_RIGHT].setValue(new Integer(((Integer)dPadCyclesInState[JoystickDPadButtonEnum.DPAD_RIGHT].getValue()).intValue() + 1));
            }
        }

        if(axes[JoystickAxisEnum.DPAD_Y].getValue() > 0)
        {
            if(dPadButtons[JoystickDPadButtonEnum.DPAD_DOWN].getValue())
            {
                Boolean key = (Boolean) dPadCyclesInState[JoystickDPadButtonEnum.DPAD_DOWN].getKey();
                if(key.booleanValue())
                {
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_DOWN].setKey(new Boolean(false));
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_DOWN].setValue(new Integer(0));
                }
                else
                {
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_DOWN].setValue(new Integer(((Integer)dPadCyclesInState[JoystickDPadButtonEnum.DPAD_DOWN].getValue()).intValue() + 1));
                }
            }
            
            if(!dPadButtons[JoystickDPadButtonEnum.DPAD_UP].getValue())
            {
                Boolean key = (Boolean) dPadCyclesInState[JoystickDPadButtonEnum.DPAD_UP].getKey();
                if(!key.booleanValue())
                {
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_UP].setKey(new Boolean(true));
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_UP].setValue(new Integer(0));
                }
                else
                {
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_UP].setValue(new Integer(((Integer)dPadCyclesInState[JoystickDPadButtonEnum.DPAD_UP].getValue()).intValue() + 1));
                }
            }
        }
        else if(axes[JoystickAxisEnum.DPAD_Y].getValue() < 0)
        {
            if(!dPadButtons[JoystickDPadButtonEnum.DPAD_DOWN].getValue())
            {
                Boolean key = (Boolean) dPadCyclesInState[JoystickDPadButtonEnum.DPAD_DOWN].getKey();
                if(!key.booleanValue())
                {
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_DOWN].setKey(new Boolean(true));
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_DOWN].setValue(new Integer(0));
                }
                else
                {
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_DOWN].setValue(new Integer(((Integer)dPadCyclesInState[JoystickDPadButtonEnum.DPAD_DOWN].getValue()).intValue() + 1));
                }
            }
            
            if(dPadButtons[JoystickDPadButtonEnum.DPAD_UP].getValue())
            {
                Boolean key = (Boolean) dPadCyclesInState[JoystickDPadButtonEnum.DPAD_UP].getKey();
                if(key.booleanValue())
                {
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_UP].setKey(new Boolean(false));
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_UP].setValue(new Integer(0));
                }
                else
                {
                    dPadCyclesInState[JoystickDPadButtonEnum.DPAD_UP].setValue(new Integer(((Integer)dPadCyclesInState[JoystickDPadButtonEnum.DPAD_UP].getValue()).intValue() + 1));
                }
            }
        }
        else
        {
            Boolean key = (Boolean) dPadCyclesInState[JoystickDPadButtonEnum.DPAD_DOWN].getKey();
            if(key.booleanValue())
            {
                dPadCyclesInState[JoystickDPadButtonEnum.DPAD_DOWN].setKey(new Boolean(false));
                dPadCyclesInState[JoystickDPadButtonEnum.DPAD_DOWN].setValue(new Integer(0));
            }
            else
            {
                dPadCyclesInState[JoystickDPadButtonEnum.DPAD_DOWN].setValue(new Integer(((Integer)dPadCyclesInState[JoystickDPadButtonEnum.DPAD_DOWN].getValue()).intValue() + 1));
            }
            
            key = (Boolean) dPadCyclesInState[JoystickDPadButtonEnum.DPAD_UP].getKey();
            if(key.booleanValue())
            {
                dPadCyclesInState[JoystickDPadButtonEnum.DPAD_UP].setKey(new Boolean(false));
                dPadCyclesInState[JoystickDPadButtonEnum.DPAD_UP].setValue(new Integer(0));
            }
            else
            {
                dPadCyclesInState[JoystickDPadButtonEnum.DPAD_UP].setValue(new Integer(((Integer)dPadCyclesInState[JoystickDPadButtonEnum.DPAD_UP].getValue()).intValue() + 1));
            }
        }
        
        if (((Integer) dPadCyclesInState[JoystickDPadButtonEnum.DPAD_LEFT].getValue()).intValue() >= neededCyclesForChange)
        {
            dPadButtons[JoystickDPadButtonEnum.DPAD_LEFT].setValue(dPadCyclesInState[JoystickDPadButtonEnum.DPAD_LEFT].getKey());
        }
        if (((Integer) dPadCyclesInState[JoystickDPadButtonEnum.DPAD_RIGHT].getValue()).intValue() >= neededCyclesForChange)
        {
            dPadButtons[JoystickDPadButtonEnum.DPAD_RIGHT].setValue(dPadCyclesInState[JoystickDPadButtonEnum.DPAD_RIGHT].getKey());
        }
        if (((Integer) dPadCyclesInState[JoystickDPadButtonEnum.DPAD_UP].getValue()).intValue() >= neededCyclesForChange)
        {
            dPadButtons[JoystickDPadButtonEnum.DPAD_UP].setValue(dPadCyclesInState[JoystickDPadButtonEnum.DPAD_UP].getKey());
        }
        if (((Integer) dPadCyclesInState[JoystickDPadButtonEnum.DPAD_DOWN].getValue()).intValue() >= neededCyclesForChange)
        {
            dPadButtons[JoystickDPadButtonEnum.DPAD_DOWN].setValue(dPadCyclesInState[JoystickDPadButtonEnum.DPAD_DOWN].getKey());
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
