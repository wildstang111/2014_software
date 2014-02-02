/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.AutonomousProgram;
import com.wildstangs.autonomous.steps.control.AutonomousStepStopAutonomous;

/**
 *
 * @author coder65535
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class AutonomousProgramSleeper extends AutonomousProgram {

    public void defineSteps() {
        addStep(new AutonomousStepStopAutonomous());
    }

    public String toString() {
        return "Sleeper";
    }
}
