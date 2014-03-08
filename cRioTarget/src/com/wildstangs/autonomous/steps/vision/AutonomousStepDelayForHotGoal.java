/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.vision;

import com.wildstangs.autonomous.steps.AutonomousStep;
import com.wildstangs.logger.Logger;
import com.wildstangs.subsystems.HotGoalDetector;
import com.wildstangs.subsystems.HotGoalDetector.HotGoalSideEnum;
import com.wildstangs.subsystems.base.SubsystemContainer;
import com.wildstangs.timer.WsTimer;

/**
 *
 * @author Joey
 */
public class AutonomousStepDelayForHotGoal extends AutonomousStep
{
    protected int delayInMs;
    protected HotGoalSideEnum sideToLookFor;
    protected WsTimer timer;
    
    public AutonomousStepDelayForHotGoal(int delayInMs)
    {
        this(delayInMs, HotGoalSideEnum.EITHER);
    }
    
    public AutonomousStepDelayForHotGoal(int delayInMs, HotGoalSideEnum sideToLookFor)
    {
        this.delayInMs = delayInMs;
        this.sideToLookFor = sideToLookFor;
    }
    
    public void initialize()
    {
        if(((HotGoalDetector) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.HOT_GOAL_DETECTOR_INDEX)).checkForHotGoal())
        {
            if(sideToLookFor == HotGoalSideEnum.EITHER || sideToLookFor == HotGoalSideEnum.NONE)
            {
                finished = true;
            }
            else if(((HotGoalDetector) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.HOT_GOAL_DETECTOR_INDEX)).getLastReportSide() == sideToLookFor)
            {
                finished = true;
            }
        }
        
        if(!finished)
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
        return "Delay " + this.delayInMs + "ms for Hot Goal on " + sideToLookFor.toString() + " Side";
    }
    
}
