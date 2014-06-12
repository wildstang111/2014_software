/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.wings;

import com.wildstangs.autonomous.steps.AutonomousStep;
import com.wildstangs.subsystems.Wings;
import com.wildstangs.subsystems.base.SubsystemContainer;

/**
 *
 * @author Joey
 */
public class AutonomousStepSetWingsState extends AutonomousStep
{
    protected boolean open;
    public AutonomousStepSetWingsState(boolean open)
    {
        this.open = open;
    }
    
    public void initialize()
    {
        ((Wings) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.WINGS_INDEX)).setWingState(open);
        finished = true;
    }

    public void update()
    {
    }

    public String toString()
    {
        return "Set wings " + (open ? "Open" : "Closed");
    }
    
}
