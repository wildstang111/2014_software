/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.vision;

import com.wildstangs.autonomous.steps.AutonomousStep;
import com.wildstangs.subsystems.HotGoalDetector;
import com.wildstangs.subsystems.base.SubsystemContainer;

/**
 *
 * @author Joey
 */
public class AutonomousStepSetCameraLedState extends AutonomousStep 
{
    protected boolean state;
    public AutonomousStepSetCameraLedState(boolean on)
    {
        this.state = on;
    }
    
    public void initialize()
    {
        ((HotGoalDetector) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.HOT_GOAL_DETECTOR_INDEX)).setLedState(state);
        finished = true;
    }

    public void update()
    {
    }

    public String toString()
    {
        return "Turning Camera LEDs " + (state ? "On" : "Off");
    }
}
