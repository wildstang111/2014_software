package com.wildstangs.inputmanager.inputs.driverstation;

import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.inputmanager.base.IInput;
import com.wildstangs.inputmanager.base.IInputEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import edu.wpi.first.wpilibj.DriverStation;

/**
 *
 * @author Nathan
 */
public class WsDSDigitalInput implements IInput {

    BooleanSubject digitalValue;
    int channel;

    //By giving the input number in the constructor we can make this generic for all digital inputs
    public WsDSDigitalInput(int channel) {
        this.digitalValue = new BooleanSubject("DigitalInput" + channel);
        this.channel = channel;

        digitalValue.setValue(false);
    }

    public void set(IInputEnum key, Object value) {
        digitalValue.setValue(((Boolean) value).booleanValue());

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
    
    public Subject getSubject(ISubjectEnum subjectEnum) {
        return digitalValue;
    }

    public Object get(IInputEnum key) {
        return digitalValue.getValueAsObject();
    }
    
    public void update() {
        digitalValue.updateValue();
    }

    public void pullData() {
        digitalValue.setValue(DriverStation.getInstance().getDigitalIn(channel));
    }

    public void notifyConfigChange() {
        //Nothing to update here, since the config value only affect the
        //start state.
    }
}