/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.outputmanager.outputs.no;

import com.wildstangs.outputmanager.base.IOutput;
import com.wildstangs.outputmanager.base.IOutputEnum;

/**
 *
 * @author Joey
 */
public class NoOutput implements IOutput
{
    protected Object object = new Object();
    public void set(IOutputEnum key, Object value)
    {
    }

    public Object get(IOutputEnum key)
    {
        return object;
    }

    public void update()
    {
    }

    public void notifyConfigChange()
    {
    }
    
}
