/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems.automovements;

import com.wildstangs.autonomous.steps.arms.AutonomousStepSetArmPresets;
import com.wildstangs.autonomous.steps.control.AutonomousStepDelay;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.subsystems.arm.ArmPreset;
import com.wildstangs.subsystems.base.AutoMovement;

/**
 *
 * @author Alex
 */
public class AutoMovementPassFrontToBack extends AutoMovement{
    protected IntegerConfigFileParameter delayTime = new IntegerConfigFileParameter(this.getClass().getName(), "DelayTime", 500);
    protected ArmPreset startPositionForFrontPasser = new ArmPreset(20, 90, "AutoMovementPassFrontToBack.StartPosition");
    protected ArmPreset endPositionForFrontPasser = new ArmPreset(-20, 90, "AutoMovementPassFrontToBack.EndPosition");
    
    public void abort() {
    }

    protected void defineSteps() {
        addStep(new AutonomousStepSetArmPresets(startPositionForFrontPasser));
        addStep(new AutonomousStepDelay(delayTime.getValue()));
        addStep(new AutonomousStepSetArmPresets(endPositionForFrontPasser));
    }

    public String toString() {
        return "Passing Front To Back";
    }

    
}
