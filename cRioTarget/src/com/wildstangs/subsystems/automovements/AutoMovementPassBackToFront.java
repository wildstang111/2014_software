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
public class AutoMovementPassBackToFront extends AutoMovement{
    protected IntegerConfigFileParameter delayTime = new IntegerConfigFileParameter(this.getClass().getName(), "DelayTime", 500);
    protected ArmPreset startPositionForBackPasser = new ArmPreset(90, 20, "AutoMovementPassBackToFront.StartPosition");
    protected ArmPreset endPositionForBackPasser = new ArmPreset(90, -20, "AutoMovementPassBackToFront.EndPosition");
    public void abort() {
    }

    protected void defineSteps() {
        addStep(new AutonomousStepSetArmPresets(startPositionForBackPasser));
        addStep(new AutonomousStepDelay(delayTime.getValue()));
        addStep(new AutonomousStepSetArmPresets(endPositionForBackPasser));
    }

    public String toString() {
        return "Passing Back To Front";
    }
}
