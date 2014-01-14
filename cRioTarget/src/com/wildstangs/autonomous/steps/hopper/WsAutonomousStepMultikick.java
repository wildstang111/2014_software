/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.hopper;

import com.wildstangs.autonomous.steps.WsAutonomousSerialStepGroup;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepWaitForShooter;

/**
 *
 * @author coder65535
 */
public class WsAutonomousStepMultikick extends WsAutonomousSerialStepGroup {

    private int numFrisbees;

    public WsAutonomousStepMultikick(int numFrisbees) {
        this.numFrisbees = numFrisbees;
        defineSteps();
    }

    public void defineSteps() {
        for (int i = 0; i < getNumSteps(numFrisbees); i++) {
            if (i % 2 == 0) {
                addStep(new WsAutonomousStepKick());
            } else {
                addStep(new WsAutonomousStepWaitForShooter());
            }
        }
    }

    public String toString() {
        return "Kick " + numFrisbees + " frisbees";
    }
    
    private int getNumSteps(int frisbees) {
        return frisbees + (frisbees - 1);
    }
}
