/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.simulation.analoginput;

/**
 *
 * @author Joey
 */
public class AnalogInputSimulation
{
    protected static final double MAX_VOLTAGE = 5.2;
    protected static final double MIN_VOLTAGE = 0.0;
    
    protected int mChannel;
    protected double analogVoltage = 0.0;
    
    public AnalogInputSimulation(int channel)
    {
        this.mChannel = channel;
    }
    
    public double getVoltage()
    {
        return analogVoltage;
    }
    
    public void setVoltage(double voltage)
    {
        if(voltage > MAX_VOLTAGE) 
        {
            voltage = MAX_VOLTAGE;
        }
        else if(voltage < MIN_VOLTAGE)
        {
            voltage = MIN_VOLTAGE;
        }
        
        this.analogVoltage = voltage;
    }
}
