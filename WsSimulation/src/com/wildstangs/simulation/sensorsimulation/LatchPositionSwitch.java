/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.simulation.sensorsimulation;

import com.wildstangs.outputmanager.base.OutputManager;
import com.wildstangs.simulation.digitalinputs.DigitalInputContainer;
import com.wildstangs.simulation.sensorsimulation.base.ISensorSimulation;

/**
 *
 * @author Jason
 */
public class LatchPositionSwitch implements ISensorSimulation {

    @Override
    public void init() {
    }
     private static int latchPositionSwitch = 8;
    
    public void update(){
        
        //Get the solenoid value 
        Boolean solState = ((Boolean)OutputManager.getInstance().getOutput(OutputManager.LATCH_SOLENOID_INDEX).get(null)); 
        
        //Forward is up
        if (false== solState){
            //Set the limit switches based on that value
            DigitalInputContainer.getInstance().inputs[latchPositionSwitch].set(false);
            
        } else { 
            //Set the limit switches based on that value
            DigitalInputContainer.getInstance().inputs[latchPositionSwitch].set(true);
            
        }
    }   
}
