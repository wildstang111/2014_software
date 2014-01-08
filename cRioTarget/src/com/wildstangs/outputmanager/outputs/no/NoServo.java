/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.outputmanager.outputs.no;

import com.wildstangs.outputmanager.base.IServo;
import com.wildstangs.outputmanager.outputs.*;
import com.wildstangs.outputmanager.base.IOutput;
import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;

/**
 *
 * @author Rick a.k.a. Batman
 */
public class NoServo implements IServo {

    DoubleSubject position;
    private boolean angleSet;

    public NoServo(String name, int channel) {
        this.position = new DoubleSubject(name);
        angleSet = false;
    }

    public void set(IOutputEnum key, Object value) {
        position.setValue(((Double) value).doubleValue());
        angleSet = false;
    }

    public void setAngle(IOutputEnum key, Object value) {
        position.setValue(((Double) value).doubleValue());
        angleSet = true;
    }

    public Object get(IOutputEnum key) {
        return this.position.getValueAsObject();
    }

    public Subject getSubject(ISubjectEnum subjectEnum) {
        return this.position;
    }

    public void update() {
        this.position.updateValue();
    }

    public void notifyConfigChange() {
    }
}
