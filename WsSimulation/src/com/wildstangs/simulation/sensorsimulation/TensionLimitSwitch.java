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
public class TensionLimitSwitch implements ISensorSimulation {

    @Override
    public void init() {
    }
     private static int tensionLimitSwitch = 6;
    
    public void update(){
        
        //Get the solenoid value 
        Boolean solState = ((Boolean)OutputManager.getInstance().getOutput(OutputManager.CATAPAULT_SOLENOID_INDEX).get(null)); 
        
        //Forward is up
        if (false== solState){
            //Set the limit switches based on that value
            DigitalInputContainer.getInstance().inputs[tensionLimitSwitch].set(true);
            
        } else { 
            //Set the limit switches based on that value
            DigitalInputContainer.getInstance().inputs[tensionLimitSwitch].set(false);
            
        }
    }   
}
