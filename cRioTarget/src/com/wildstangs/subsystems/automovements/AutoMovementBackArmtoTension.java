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
public class AutoMovementBackArmtoTension extends AutoMovement{
    protected ArmPreset backArmTensionPreset = new ArmPreset(0, 20, "Back arm tension preset");
    public void abort() {
    //nothing needed here. PID handles it.
    }

    protected void defineSteps() {
        addStep(new AutonomousStepSetArmPresets(backArmTensionPreset));
    }

    public String toString() {
        return "Back Arm is Tension.";
    }
    
}
