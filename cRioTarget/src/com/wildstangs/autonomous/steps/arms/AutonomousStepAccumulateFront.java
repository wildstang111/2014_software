/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous.steps.arms;

import com.wildstangs.autonomous.steps.AutonomousStep;
import com.wildstangs.subsystems.BallHandler;
import com.wildstangs.subsystems.arm.ArmRollerEnum;
import com.wildstangs.subsystems.base.SubsystemContainer;

/**
 *
 * @author Nathan
 */
public class AutonomousStepAccumulateFront extends AutonomousStep{

    public void initialize() {
        ((BallHandler) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.BALL_HANDLER_INDEX)).setFrontArmAccumulator(ArmRollerEnum.OUTPUT);
        finished = true;
    }

    public void update() {
        
    }

    public String toString() {
        return "Accumulate with the front roller";
    }
    
}
