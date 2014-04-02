/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.ears;

import com.wildstangs.autonomous.steps.AutonomousStep;
import com.wildstangs.subsystems.Ears;
import com.wildstangs.subsystems.base.SubsystemContainer;

/**
 *
 * @author Jason
 */
public class AutonomousStepSetEarState extends AutonomousStep  {

    public void initialize() {
        
//      ((Ears)SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.EAR_INDEX)).setEarState(earPreset);
              finished = true;
    }

    public void update() {
        
        
    }

    public String toString () {
  
        return "Setting Ear State";
      
    }
    public AutonomousStepSetEarState (boolean earPreset){     
        this.earPreset = earPreset;   
        
    }
    
    boolean earPreset;
    
}
    
