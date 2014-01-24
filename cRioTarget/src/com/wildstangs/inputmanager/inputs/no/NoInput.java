/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.inputmanager.inputs.no;

import com.wildstangs.inputmanager.base.IInput;
import com.wildstangs.inputmanager.base.IInputEnum;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;

/**
 *
 * @author Joey
 */
public class NoInput implements IInput
{
    protected Subject subject = new Subject()
    {
        protected Object object = new Object();
        public void attach(IObserver observer)
        {
        }

        public Object getValueAsObject()
        {
            return object;
        }
        
    };
    public void set(IInputEnum key, Object value)
    {
    }

    public Object get(IInputEnum key)
    {
        return subject.getValueAsObject();
    }

    public void update()
    {
    }

    public void pullData()
    {
    }

    public void notifyConfigChange()
    {
    }

    public Subject getSubject(ISubjectEnum subjectEnum)
    {
        return subject;
    }
}
