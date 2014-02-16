/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.pid.outputs;

import com.wildstangs.outputmanager.base.OutputManager;
import com.wildstangs.pid.outputs.base.IPidOutput;
import com.wildstangs.subsystems.arm.Arm;

/**
 *
 * @author Joey
 */
public class ArmVictorPidOutput implements IPidOutput
{
    protected Arm arm;
    public ArmVictorPidOutput(Arm arm)
    {
        this.arm = arm;
    }
    
    public void pidWrite(double output)
    {
        arm.setVictor(output);
    }
    
}
