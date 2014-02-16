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
public class AutoMovementPassBackToFront extends AutoMovement{
    protected ArmPreset startPositionForBackPasser = new ArmPreset(90, 20, "AutoMovementPassBackArm.StartPosition");
    protected ArmPreset endPositionForBackPasser = new ArmPreset(90, -20, "AutoMovementPassBackArm.EndPosition");
    public void abort() {
    }

    protected void defineSteps() {
    addStep(new AutonomousStepSetArmPresets(startPositionForBackPasser));
    addStep(new AutonomousStepSetArmPresets(endPositionForBackPasser));
    }

    public String toString() {
        return "Passing Back To Front";
    }
}
