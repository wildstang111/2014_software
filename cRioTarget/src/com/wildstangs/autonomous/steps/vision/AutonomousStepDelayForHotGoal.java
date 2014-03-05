/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.vision;

import com.wildstangs.autonomous.steps.AutonomousStep;
import com.wildstangs.subsystems.HotGoalDetector;
import com.wildstangs.subsystems.base.SubsystemContainer;
import com.wildstangs.timer.WsTimer;

/**
 *
 * @author Joey
 */
public class AutonomousStepDelayForHotGoal extends AutonomousStep
{
    protected int delayInMs;
    protected WsTimer timer;
    public AutonomousStepDelayForHotGoal(int delayInMs)
    {
        this.delayInMs = delayInMs;
    }
    
    public void initialize()
    {
        if(((HotGoalDetector) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.HOT_GOAL_DETECTOR_INDEX)).checkForHotGoal())
        {
            finished = true;
        }
        else
        {
            timer = new WsTimer();
            timer.start();
        }
    }

    public void update()
    {
        if(finished)
        {
            return;
        }
        
        if(timer.hasPeriodPassed(delayInMs / 1000.0))
        {
            finished = true;
        }
    }

    public String toString()
    {
        return "Delay for Hot Goal";
    }
    
}
