package com.wildstangs.inputmanager.inputs.driverstation;

import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.inputmanager.base.IInput;
import com.wildstangs.inputmanager.base.IInputEnum;
import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import edu.wpi.first.wpilibj.DriverStation;

/**
 *
 * @author Nathan
 */
public class WsDSAnalogInput implements IInput {

    DoubleSubject analogValue;
    int channel;

    //By giving the input number in the constructor we can make this generic for all analog inputs
    public WsDSAnalogInput(int channel) {
        this.analogValue = new DoubleSubject("AnalogInput" + channel);
        this.channel = channel;

        analogValue.setValue(0.0);
    }

    public void set(IInputEnum key, Object value) {
        double d = 0.0;
        if (value instanceof Boolean) {
            boolean b = ((Boolean) value).booleanValue();
            d = 0;
            if (b) {
                d = 1;
            }
        } else {
            d = ((Double) value).doubleValue();
        }
        analogValue.setValue(d);

    }

    public Subject getSubject(ISubjectEnum subjectEnum) {
        return analogValue;
    }

    public Object get(IInputEnum key) {
        return analogValue.getValueAsObject();
    }

    public void update() {
        analogValue.updateValue();
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
    
    public void pullData() {
        
        analogValue.setValue(DriverStation.getInstance().getAnalogIn(channel));
    }

    public void notifyConfigChange() {
        //Nothing to update here, since the config value only affect the
        //start state.
    }
}
