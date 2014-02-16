/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems.automovements;

import com.wildstangs.autonomous.steps.arms.AutonomousStepSetArmPresets;
import com.wildstangs.subsystems.arm.ArmPreset;
import com.wildstangs.subsystems.base.AutoMovement;

/**
 *
 * @author Alex
 */
public class AutoMovementPassFrontToBack extends AutoMovement{
    protected ArmPreset startPositionForFrontPasser = new ArmPreset(20, 90, "AutoMovementPassFrontArm.StartPosition");
    protected ArmPreset endPositionForFrontPasser = new ArmPreset(-20, 90, "AutoMovementPassFrontArm.EndPosition");
    public void abort() {
    }

    protected void defineSteps() {
    addStep(new AutonomousStepSetArmPresets(startPositionForFrontPasser));
    addStep(new AutonomousStepSetArmPresets(endPositionForFrontPasser));
    }

    public String toString() {
        return "Passing Front To Back";
    }

    
}
