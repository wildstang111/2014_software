/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.arms.WsAutonomousStepSetArmPresets;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.subsystems.arm.ArmPreset;

/**
 *
 * @author Joey
 */
public class WsAutonomousProgramYMCA extends WsAutonomousProgram
{
    protected ArmPreset yPreset = new ArmPreset(180, 180, "yPreset");
    protected ArmPreset mPreset = new ArmPreset(0, 0, "mPreset");
    protected ArmPreset cPreset = new ArmPreset(150, 270, "cPreset");
    protected ArmPreset aPreset = new ArmPreset(200, 200, "aPreset");
    protected void defineSteps()
    {
        addStep(new WsAutonomousStepSetArmPresets(yPreset));
        addStep(new WsAutonomousStepDelay(500));
        addStep(new WsAutonomousStepSetArmPresets(mPreset));
        addStep(new WsAutonomousStepDelay(500));
        addStep(new WsAutonomousStepSetArmPresets(cPreset));
        addStep(new WsAutonomousStepDelay(500));
        addStep(new WsAutonomousStepSetArmPresets(aPreset));
    }

    public String toString()
    {
        return "YMCA";
    }
    
}
