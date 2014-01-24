/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs.test;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepDriveManual;

/**
 *
 * @author coder65535
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class WsAutonomousProgramForwardsTest extends WsAutonomousProgram {

    public void defineSteps() {
        addStep(new WsAutonomousStepDriveManual(1.0, 0.0));
        addStep(new WsAutonomousStepDelay(500));
        addStep(new WsAutonomousStepDriveManual(0.0, 0.0));
    }

    public String toString() {
        return "Test by driving forwards for 10 seconds";
    }
}
