/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.arms;

import com.wildstangs.autonomous.steps.AutonomousStep;
import com.wildstangs.subsystems.BallHandler;
import com.wildstangs.subsystems.arm.ArmPreset;
import com.wildstangs.subsystems.base.SubsystemContainer;

/**
 *
 * @author Joey
 */
public class AutonomousStepSetArmPresets extends AutonomousStep {

    protected ArmPreset preset;

    public AutonomousStepSetArmPresets(ArmPreset preset) {
        this.preset = preset;
    }

    public void initialize() {
        ((BallHandler) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.BALL_HANDLER_INDEX)).setArmPreset(preset);
    }

    public void update() {
        if (!((BallHandler) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.BALL_HANDLER_INDEX)).areArmsUsingPidControl()) {
            finished = true;
        }
    }

    public String toString() {
        return "Moving Arms to preset: " + preset.toString();
    }

}
