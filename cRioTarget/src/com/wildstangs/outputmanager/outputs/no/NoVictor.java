package com.wildstangs.outputmanager.outputs.no;

import com.wildstangs.outputmanager.outputs.*;
import com.wildstangs.outputmanager.base.IOutput;
import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author Nathan
 */
public class NoVictor implements IOutput {

    DoubleSubject motorSpeed;

    //By giving the victor1 number in the constructor we can make this generic for all digital victor1s
    public NoVictor(String name, int channel) {
        this.motorSpeed = new DoubleSubject(name);

        motorSpeed.setValue(0.0);
    }

    public void set(IOutputEnum key, Object value) {
        motorSpeed.setValue(((Double) value).doubleValue());

    }

    public Subject getSubject(ISubjectEnum subjectEnum) {
        return motorSpeed;
    }

    public Object get(IOutputEnum key) {
        return motorSpeed.getValueAsObject();
    }

    public void update() {
        motorSpeed.updateValue();
    }

    public void notifyConfigChange() {
        //Nothing to update here, since the config value only affect the
        //start state.
    }
}
