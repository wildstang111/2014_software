/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.simulation.sensorsimulation;

import com.wildstangs.outputmanager.base.OutputManager;
import com.wildstangs.simulation.analoginput.AnalogInputContainer;
import com.wildstangs.simulation.sensorsimulation.base.ISensorSimulation;

/**
 *
 * @author Joey
 */
public class ArmPot implements ISensorSimulation
{
    protected static final double FULL_POWER_VOLTAGE_INCREMENT = 0.036;
    
    protected static final int FRONT_POT_CHANNEL = 2;
    protected static final int BACK_POT_CHANNEL = 3;
    
    protected boolean front;

    public ArmPot(boolean front)
    {
        this.front = front;
    }
    
    @Override
    public void init()
    {
    }

    @Override
    public void update()
    {
        double victorSpeed = ((Double) OutputManager.getInstance().getOutput(front ? OutputManager.FRONT_ARM_VICTOR_INDEX : OutputManager.BACK_ARM_VICTOR_INDEX).get()).doubleValue();
        
        double increment = (FULL_POWER_VOLTAGE_INCREMENT * victorSpeed);
        
        AnalogInputContainer.getInstance().getInput(front ? FRONT_POT_CHANNEL : BACK_POT_CHANNEL).setVoltage(AnalogInputContainer.getInstance().getInput(front ? FRONT_POT_CHANNEL : BACK_POT_CHANNEL).getVoltage() + increment);
    }
}
