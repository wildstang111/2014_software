package com.wildstangs.inputmanager.inputs.joystick.manipulator;

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
public class ManipulatorJoystick implements IInput {

    final static int numberOfAxes = 6;
    DoubleSubject[] axes;
    final static int numberOfButtons = 12;
    BooleanSubject[] buttons;
    protected final static int numberOfDPadButtons = 4;
    protected BooleanSubject[] dPadButtons;
    protected final static int neededCyclesForChange = 8;
    protected DataElement[] dPadCyclesInState;
    Joystick manipulatorJoystick = null;

    public ManipulatorJoystick() {
        manipulatorJoystick = (Joystick) new Joystick(2);

        axes = new DoubleSubject[numberOfAxes];
        for (int i = 0; i < axes.length; i++) {
            if (JoystickAxisEnum.getEnumFromIndex(false, i) != null) {
                axes[i] = new DoubleSubject(JoystickAxisEnum.getEnumFromIndex(false, i));
            } else {
                axes[i] = new DoubleSubject("ManipulatorSubject" + 1);
            }
        }

        buttons = new BooleanSubject[numberOfButtons];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new BooleanSubject(JoystickButtonEnum.getEnumFromIndex(false, i));
        }
        
        dPadButtons = new BooleanSubject[numberOfDPadButtons];
        dPadCyclesInState = new DataElement[numberOfDPadButtons];
        for(int i = 0; i < dPadButtons.length; i++)
        {
            dPadButtons[i] = new BooleanSubject(JoystickDPadButtonEnum.getEnumFromIndex(false, i));
            dPadCyclesInState[i] = new DataElement(dPadButtons[i].getValueAsObject(), new Integer(0));
        }
    }

    public Subject getSubject(ISubjectEnum subjectEnum) {
        if (subjectEnum instanceof JoystickAxisEnum && ((JoystickAxisEnum) subjectEnum).isDriver() == false) {
            return axes[((JoystickAxisEnum) subjectEnum).toValue()];
        } else if (subjectEnum instanceof JoystickButtonEnum && ((JoystickButtonEnum) subjectEnum).isDriver() == false) {
            return buttons[((JoystickButtonEnum) subjectEnum).toValue()];
        }
        else if(subjectEnum instanceof JoystickDPadButtonEnum && !((JoystickDPadButtonEnum) subjectEnum).isDriver())
        {
            return dPadButtons[((JoystickDPadButtonEnum) subjectEnum).toValue()];
        } 
        else {
            System.out.println("Subject not supported or incorrect.");
            return null;
        }
    }

    public void set(IInputEnum key, Object value) {
        if (key instanceof JoystickAxisEnum && ((JoystickAxisEnum) key).isDriver() == false) {
            axes[((JoystickAxisEnum) key).toValue() - 1].setValue(value);
        } else if (key instanceof JoystickButtonEnum && ((JoystickButtonEnum) key).isDriver() == false) {
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
        if (key instanceof JoystickAxisEnum) {
            return axes[((JoystickAxisEnum) key).toValue() - 1].getValueAsObject();
        } else if (key instanceof JoystickButtonEnum && ((JoystickButtonEnum) key).isDriver() == false) {
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
        
        SmartDashboard.putBoolean("DPad Left", dPadButtons[JoystickDPadButtonEnum.DPAD_LEFT].getValue());
        SmartDashboard.putBoolean("DPad Right", dPadButtons[JoystickDPadButtonEnum.DPAD_RIGHT].getValue());
        SmartDashboard.putBoolean("DPad Up", dPadButtons[JoystickDPadButtonEnum.DPAD_UP].getValue());
        SmartDashboard.putBoolean("DPad Down", dPadButtons[JoystickDPadButtonEnum.DPAD_DOWN].getValue());
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
