/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems.automovements;

import com.wildstangs.autonomous.steps.arms.AutonomousStepAccumulateFront;
import com.wildstangs.autonomous.steps.arms.AutonomousStepSetArmPresets;
import com.wildstangs.autonomous.steps.arms.AutonomousStepStopAccumulateFront;
import com.wildstangs.autonomous.steps.control.AutonomousStepDelay;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.subsystems.arm.ArmPreset;
import com.wildstangs.subsystems.base.AutoMovement;

/**
 *
 * @author Alex
 */
public class AutoMovementAccumulateFrontArm extends AutoMovement{
    protected IntegerConfigFileParameter delayTime = new IntegerConfigFileParameter(this.getClass().getName(), "AccumulateDelayTime", 2000);
    protected ArmPreset startPositionFrontAccumulate = new ArmPreset(90,0, "AutoMovementAccumulateFrontArm.StartPosition");
    protected ArmPreset endPositionFrontAccumulate = new ArmPreset(0,0, "AutoMovementAccumulateFrontArm.EndPosition");
    
    public void abort() {
    //not sure what to put here.
    }

    protected void defineSteps() {
        addStep(new AutonomousStepSetArmPresets(startPositionFrontAccumulate));
        addStep(new AutonomousStepAccumulateFront());
        addStep(new AutonomousStepDelay(delayTime.getValue()));
        addStep(new AutonomousStepSetArmPresets(endPositionFrontAccumulate));
        addStep(new AutonomousStepStopAccumulateFront());
    }

    public String toString() {
        return "Front Arm Accumulating";
    }
    
}
