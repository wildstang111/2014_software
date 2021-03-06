/*
 * To change thias template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.pid.inputs;

import com.wildstangs.inputmanager.base.IInputEnum;
import com.wildstangs.inputmanager.base.InputManager;
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
        double currentVoltage = ((Double) InputManager.getInstance().getSensorInput(potIndex).get()).doubleValue();
        
        //Pot voltages are used to calculate 180 degree rotation
        return (180 * ((currentVoltage - lowerVoltage) / (upperVoltage - lowerVoltage)));
    }
    
    public void setVoltageValues(double upperVoltageValue, double lowerVoltageValue)
    {
        this.upperVoltage = upperVoltageValue;
        this.lowerVoltage = lowerVoltageValue;
    }
}
