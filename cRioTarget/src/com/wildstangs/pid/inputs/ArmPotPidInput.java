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
    protected double lowAngle, highAngle;
    public ArmPotPidInput(int potIndex, double upperVoltage, double lowerVoltage, double lowAngle, double highAngle)
    {
        this.potIndex = potIndex;
        this.upperVoltage = upperVoltage;
        this.lowerVoltage = lowerVoltage;
        this.lowAngle = lowAngle;
        this.highAngle = highAngle;
    }
    
    public double pidRead()
    {
        double currentVoltage = ((Double) InputManager.getInstance().getSensorInput(potIndex).get()).doubleValue();
        return ((highAngle - lowAngle) * ((currentVoltage - lowerVoltage) / (upperVoltage - lowerVoltage))) + lowAngle;
    }
    
    public void setVoltageValues(double upperVoltageValue, double lowerVoltageValue)
    {
        this.upperVoltage = upperVoltageValue;
        this.lowerVoltage = lowerVoltageValue;
    }
    
    public void setAngleBounds(double lowAngle, double highAngle)
    {
        this.lowAngle = lowAngle;
        this.highAngle = highAngle;
    }
}
