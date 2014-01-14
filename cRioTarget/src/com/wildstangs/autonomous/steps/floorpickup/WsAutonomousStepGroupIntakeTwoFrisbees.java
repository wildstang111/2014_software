/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.floorpickup;

import com.wildstangs.autonomous.steps.WsAutonomousSerialStepGroup;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;

/**
 *
 * @author Nathan
 */
public class WsAutonomousStepGroupIntakeTwoFrisbees extends WsAutonomousSerialStepGroup {

    int delay;

    public WsAutonomousStepGroupIntakeTwoFrisbees(int delay) {
        this.delay = delay;
        this.defineSteps();
    }

    public void defineSteps() {
        addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
        addStep(new WsAutonomousStepDelay(delay));
        addStep(new WsAutonomousStepOverrideFunnelatorButtonOn());
        addStep(new WsAutonomousStepDelay(delay));
        addStep(new WsAutonomousStepOverrideFunnelatorButtonOff());
        addStep(new WsAutonomousStepIntakeMotorStop());

    }

    public String toString() {
        return "Taking in two frisbees";
    }
}
