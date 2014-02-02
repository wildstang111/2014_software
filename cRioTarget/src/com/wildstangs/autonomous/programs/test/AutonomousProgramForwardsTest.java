/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs.test;

import com.wildstangs.autonomous.AutonomousProgram;
import com.wildstangs.autonomous.steps.control.AutonomousStepDelay;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepDriveManual;

/**
 *
 * @author coder65535
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class AutonomousProgramForwardsTest extends AutonomousProgram {

    public void defineSteps() {
        addStep(new AutonomousStepDriveManual(1.0, 0.0));
        addStep(new AutonomousStepDelay(500));
        addStep(new AutonomousStepDriveManual(0.0, 0.0));
    }

    public String toString() {
        return "Test by driving forwards for 10 seconds";
    }
}
