/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.pid.outputs;

import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.outputmanager.base.OutputManager;
import com.wildstangs.pid.outputs.base.IPidOutput;

/**
 *
 * @author Joey
 */
public class ArmVictorPidOutput implements IPidOutput
{
    protected int victorIndex;
    public ArmVictorPidOutput(int victorIndex)
    {
        this.victorIndex = victorIndex;
    }
    
    public void pidWrite(double output)
    {
        OutputManager.getInstance().getOutput(victorIndex).set(new Double(output));
    }
    
}
