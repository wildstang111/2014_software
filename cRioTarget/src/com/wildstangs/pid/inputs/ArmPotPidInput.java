/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.pid.inputs;

import com.wildstangs.inputmanager.base.IInputEnum;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.pid.inputs.base.IPidInput;

/**
 *
 * @author Joey
 */
public class ArmPotPidInput implements IPidInput
{
    protected int potIndex;
    protected double upperVoltage, lowerVoltage;
    public ArmPotPidInput(int potIndex, double upperVoltage, double lowerVoltage)
    {
        this.potIndex = potIndex;
        this.upperVoltage = upperVoltage;
        this.lowerVoltage = lowerVoltage;
    }
    
    public double pidRead()
    {
        double currentVoltage = ((Double) WsInputManager.getInstance().getSensorInput(potIndex).get((IInputEnum) null)).doubleValue();
        return 360.0 * ((currentVoltage - lowerVoltage) / (upperVoltage - lowerVoltage));
    }
    
    public void setVoltageValues(double upperVoltageValue, double lowerVoltageValue)
    {
        this.upperVoltage = upperVoltageValue;
        this.lowerVoltage = lowerVoltageValue;
    }
    
}
