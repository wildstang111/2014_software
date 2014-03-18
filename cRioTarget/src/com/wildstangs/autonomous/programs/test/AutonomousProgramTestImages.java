/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs.test;

import com.wildstangs.autonomous.AutonomousProgram;
import com.wildstangs.autonomous.steps.AutonomousStep;
import com.wildstangs.autonomous.steps.control.AutonomousStepDelay;
import com.wildstangs.autonomous.steps.vision.AutonomousStepSetCameraLedState;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.subsystems.HotGoalDetector;
import com.wildstangs.subsystems.base.SubsystemContainer;
import com.wildstangs.timer.WsTimer;

/**
 *
 * @author Joey
 */
public class AutonomousProgramTestImages extends AutonomousProgram
{
    DoubleConfigFileParameter timeDelay = new DoubleConfigFileParameter(this.getClass().getName(), "TimeDelay", 200.0);
    protected void defineSteps()
    {
        addStep(new AutonomousStepSetCameraLedState(true));
        addStep(new AutonomousStepDelay((int) timeDelay.getValue()));
        addStep(new AutonomousStepContinuouslyGetImage(1));
        addStep(new AutonomousStepSetCameraLedState(false));
    }

    public String toString()
    {
        return "Test autonomous steps check images";
    }
    public static class AutonomousStepContinuouslyGetImage extends AutonomousStep
    {
        protected int numTimes;
        protected int current = 1;
        protected WsTimer timer;
        public AutonomousStepContinuouslyGetImage(int numTimes)
        {
            this.numTimes = numTimes;
        }

        public void initialize()
        {
            ((HotGoalDetector) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.HOT_GOAL_DETECTOR_INDEX)).getImages("-1");
            numTimes--;
            current++;
            timer = new WsTimer();
            timer.reset();
            timer.start();
        }

        public void update()
        {
            if(numTimes > 0)
            {
                if(timer.hasPeriodPassed(.050))
                {
                    ((HotGoalDetector) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.HOT_GOAL_DETECTOR_INDEX)).getImages("-" + current);
                    numTimes--;
                    current++;
                }
            }
            else
            {
                finished = true;
            }
        }

        public String toString()
        {
            return "continuously checking image";
        }
    }
}