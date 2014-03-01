/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subjects.debouncer;

import com.wildstangs.subjects.base.Subject;

/**
 *
 * @author Joey
 */
public class Debouncer extends Subject
{
    protected final int CYCLES_TO_DEBOUNCED;
    
    protected int cyclesOnCurrentValue = 0;
    protected Object currentDebouncedValue, lastValue;
    public Debouncer(int cyclesToUpdate, Object defaultDebouncedValue)
    {
        if(cyclesToUpdate <= 0) CYCLES_TO_DEBOUNCED = 0;
        else CYCLES_TO_DEBOUNCED = cyclesToUpdate;
        
        this.currentDebouncedValue = defaultDebouncedValue;
        this.lastValue = defaultDebouncedValue;
    }
    
    public void update(Object currentValue)
    {
        //The second check is just to reset the cuclesOnCurrentValue
        if(currentDebouncedValue.equals(lastValue) && lastValue.equals(currentValue))
        {
            return;
        }
        
        if(!lastValue.equals(currentValue))
        {
            cyclesOnCurrentValue = 0;
            lastValue = currentValue;
        }
        else
        {
            cyclesOnCurrentValue++;
        }
        
        if(cyclesOnCurrentValue >= CYCLES_TO_DEBOUNCED)
        {
            currentDebouncedValue = currentValue;
            this.notifyObservers();
        }
    }
    
    public Object getDebouncedValue()
    {
        return currentDebouncedValue;
    }

    public Object getValueAsObject()
    {
        return this.currentDebouncedValue;
    }
}
