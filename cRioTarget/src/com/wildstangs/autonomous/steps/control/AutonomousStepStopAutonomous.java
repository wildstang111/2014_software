/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.control;

import com.wildstangs.autonomous.steps.AutonomousStep;

/**
 *
 * @author coder65535
 */
public class AutonomousStepStopAutonomous extends AutonomousStep {

    public AutonomousStepStopAutonomous() {
        // Do nothing. This step does nothing, and never finishes, effectively halting autonomous operations.
        // Note: If included in a parallel step group, it only halts operations after all other steps in the group finish.
    }

    public void initialize() {
        // Do nothing.
    }

    public void update() {
        // Do nothing.
    }

    public String toString() {
        return "Stop auto-op";
    }
}
