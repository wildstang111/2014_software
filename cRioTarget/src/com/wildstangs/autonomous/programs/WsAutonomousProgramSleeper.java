/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepStopAutonomous;

/**
 *
 * @author coder65535
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class WsAutonomousProgramSleeper extends WsAutonomousProgram {

    public void defineSteps() {
        addStep(new WsAutonomousStepStopAutonomous());
    }

    public String toString() {
        return "Sleeper";
    }
}
