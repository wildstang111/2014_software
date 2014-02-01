/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.arms;

import com.wildstangs.autonomous.steps.WsAutonomousStep;
import com.wildstangs.subsystems.BallHandler;
import com.wildstangs.subsystems.arm.ArmPreset;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Joey
 */
public class WsAutonomousStepSetArmPresets extends WsAutonomousStep
{
    protected ArmPreset preset;

    public WsAutonomousStepSetArmPresets(ArmPreset preset)
    {
        this.preset = preset;
    }
    
    public void initialize()
    {
        ((BallHandler) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.BALL_HANDLER_INDEX)).setArmPreset(preset);
    }

    public void update()
    {
        if(((BallHandler) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.BALL_HANDLER_INDEX)).areArmsUsingPidControl())
        {
            finished = true;
        }
    }

    public String toString()
    {
        return "Moving Arms to preset: " + preset.toString();
    }
    
}