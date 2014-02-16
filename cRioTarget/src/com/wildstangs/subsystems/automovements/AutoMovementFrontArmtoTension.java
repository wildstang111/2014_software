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
public class AutoMovementFrontArmtoTension extends AutoMovement {
    protected ArmPreset frontArmTensionPreset = new ArmPreset(20, 0, "Front arm tension preset");
    public void abort() {
    // nothing needed here. PID handles movements.
    }

    protected void defineSteps() {
        addStep(new AutonomousStepSetArmPresets(frontArmTensionPreset));
    }

    public String toString() {
        return "Front Arm is Tension";
    }
}
