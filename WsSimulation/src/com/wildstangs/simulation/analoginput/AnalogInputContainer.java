/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.simulation.analoginput;

/**
 *
 * @author Joey
 */
public class AnalogInputContainer
{
    protected static AnalogInputContainer instance = null;
    
    public static AnalogInputContainer getInstance()
    {
        if(instance == null)
        {
            instance = new AnalogInputContainer();
        }
        
        return instance;
    }
    
    protected AnalogInputSimulation[] inputs;
    
    private AnalogInputContainer() 
    {
        inputs = new AnalogInputSimulation[16];
        for(int i = 0; i < 16; i++)
        {
            inputs[i] = new AnalogInputSimulation(i + 1);
        }
    }
    
    public AnalogInputSimulation getInput(int channel)
    {
        if(channel < 1) 
        {
            channel = 1;
        }
        else if(channel > 16)
        {
            channel = 16;
        }
        
        return inputs[channel - 1];
    }
}
