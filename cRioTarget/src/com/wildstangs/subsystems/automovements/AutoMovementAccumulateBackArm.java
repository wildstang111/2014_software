/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems.automovements;

import com.wildstangs.autonomous.steps.arms.AutonomousStepAccumulateBack;
import com.wildstangs.autonomous.steps.arms.AutonomousStepSetArmPresets;
import com.wildstangs.autonomous.steps.arms.AutonomousStepStopAccumulateBack;
import com.wildstangs.autonomous.steps.control.AutonomousStepDelay;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.subsystems.arm.ArmPreset;
import com.wildstangs.subsystems.base.AutoMovement;

/**
 *
 * @author Alex
 */
public class AutoMovementAccumulateBackArm extends AutoMovement{
    protected IntegerConfigFileParameter delayTime = new IntegerConfigFileParameter(this.getClass().getName(), "AccumulateDelayTime", 2000);
    protected ArmPreset startPositionBackArmAccumulate = new ArmPreset(0,90, "AutoMovementAccumulateBackArm.StartPosition");
    protected ArmPreset endPositionBackArmAccumulate = new ArmPreset(0,0, "AutoMovementAccumulateBackArm.EndPosition");
    
    public void abort() {
    //not sure what to add here.
    }

    protected void defineSteps() {
        addStep(new AutonomousStepSetArmPresets(startPositionBackArmAccumulate));
        addStep(new AutonomousStepAccumulateBack());
        addStep(new AutonomousStepDelay(delayTime.getValue()));
        addStep(new AutonomousStepSetArmPresets(endPositionBackArmAccumulate));
        addStep(new AutonomousStepStopAccumulateBack());
    }

    public String toString() {
        return "Back Arm Accumulating";
    }
    
}
