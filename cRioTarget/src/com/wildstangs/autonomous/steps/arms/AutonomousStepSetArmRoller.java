/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.arms;

import com.wildstangs.autonomous.steps.AutonomousStep;
import com.wildstangs.subsystems.BallHandler;
import com.wildstangs.subsystems.arm.ArmRollerEnum;
import com.wildstangs.subsystems.base.SubsystemContainer;

/**
 *
 * @author Joey
 */
public class AutonomousStepSetArmRoller extends AutonomousStep
{
    protected boolean front;
    protected ArmRollerEnum rollerState;
    public AutonomousStepSetArmRoller(boolean front, ArmRollerEnum rollerState)
    {
        this.front = front;
        this.rollerState = rollerState;
    }
    
    public void initialize()
    {
        if(front)
        {
            ((BallHandler) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.BALL_HANDLER_INDEX)).setFrontArmAccumulator(rollerState);
        }
        else
        {
            ((BallHandler) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.BALL_HANDLER_INDEX)).setBackArmAccumulator(rollerState);
        }
        
        finished = true;
    }

    public void update()
    {
    }

    public String toString()
    {
        return "Setting " + (front ? "Front" : "Back") + " Arm Roller to " + rollerState;
    }
    
}
