/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.AutonomousProgram;
import com.wildstangs.autonomous.steps.arms.AutonomousStepSetArmPresets;
import com.wildstangs.autonomous.steps.control.AutonomousStepDelay;
import com.wildstangs.subsystems.arm.ArmPreset;

/**
 *
 * @author Joey
 */
public class AutonomousProgramYMCA extends AutonomousProgram
{
    protected ArmPreset yPreset = new ArmPreset(180, 180, "yPreset");
    protected ArmPreset mPreset = new ArmPreset(0, 0, "mPreset");
    protected ArmPreset cPreset = new ArmPreset(150, 270, "cPreset");
    protected ArmPreset aPreset = new ArmPreset(200, 200, "aPreset");
    protected void defineSteps()
    {
        addStep(new AutonomousStepSetArmPresets(yPreset));
        addStep(new AutonomousStepDelay(500));
        addStep(new AutonomousStepSetArmPresets(mPreset));
        addStep(new AutonomousStepDelay(500));
        addStep(new AutonomousStepSetArmPresets(cPreset));
        addStep(new AutonomousStepDelay(500));
        addStep(new AutonomousStepSetArmPresets(aPreset));
    }

    public String toString()
    {
        return "YMCA";
    }
    
}
