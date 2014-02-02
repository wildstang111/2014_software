/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.simulation.accumulator;

import com.wildstangs.simulation.digitalinputs.DigitalInputContainer;
import com.wildstangs.outputmanager.base.OutputManager;

/**
 *
 * @author chadschmidt
 */
public class AccumulatorLimitSwitch {

    private static int UP_LIMIT_SWITCH_CHANNEL = 8;
    public AccumulatorLimitSwitch() {
    }
    
    public void update(){
        
        //Get the solenoid value 
        Boolean solState = ((Boolean)OutputManager.getInstance().getOutput(OutputManager.UNKNOWN_INDEX).get(null)); 
        
        //Forward is up
        if (false== solState){
            //Set the limit switches based on that value
            DigitalInputContainer.getInstance().inputs[UP_LIMIT_SWITCH_CHANNEL].set(true);
            
        } else { 
            //Set the limit switches based on that value
            DigitalInputContainer.getInstance().inputs[UP_LIMIT_SWITCH_CHANNEL].set(false);
            
        }
        
    }
}
