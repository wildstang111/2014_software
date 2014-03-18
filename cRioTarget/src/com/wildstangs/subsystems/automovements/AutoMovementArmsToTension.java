/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems.automovements;

import com.wildstangs.autonomous.steps.arms.AutonomousStepSetArmPresets;
import com.wildstangs.subsystems.BallHandler;
import com.wildstangs.subsystems.arm.ArmPreset;
import com.wildstangs.subsystems.base.AutoMovement;

/**
 *
 * @author Alex
 */
public class AutoMovementArmsToTension extends AutoMovement{
    public void abort() {
    //nothing needed here. PID handles it.
    }

    protected void defineSteps() {
        addStep(new AutonomousStepSetArmPresets(BallHandler.CATAPULT_TENSION_PRESET_BACK));
    }

    public String toString() {
        return "Arms to Tension";
    }
    
}
