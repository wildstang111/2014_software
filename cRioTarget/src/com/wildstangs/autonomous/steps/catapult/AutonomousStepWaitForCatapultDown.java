/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.catapult;

import com.wildstangs.autonomous.steps.AutonomousStep;
import com.wildstangs.subsystems.Catapult;
import com.wildstangs.subsystems.base.SubsystemContainer;

/**
 *
 * @author Joey
 */
public class AutonomousStepWaitForCatapultDown extends AutonomousStep
{

    public void initialize()
    {
    }

    public void update()
    {
        if(((Catapult) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.CATAPULT_INDEX)).isCatapultDown())
        {
            finished = true;
        }
    }

    public String toString()
    {
        return "Waiting for Catapult down";
    }
}
