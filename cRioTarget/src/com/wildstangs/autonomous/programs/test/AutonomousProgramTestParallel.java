/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs.test;

import com.wildstangs.autonomous.AutonomousProgram;
import com.wildstangs.autonomous.steps.AutonomousParallelStepGroup;
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
public class AutonomousProgramTestParallel extends AutonomousProgram {

    public void defineSteps() {
        AutonomousParallelStepGroup parallelGroup = new AutonomousParallelStepGroup("Test parallel step group");
            parallelGroup.addStep(new AutonomousStepDriveManual(AutonomousStepDriveManual.KEEP_PREVIOUS_STATE, 1.0));
            parallelGroup.addStep(new AutonomousStepDelay(250));
            parallelGroup.addStep(new AutonomousStepDriveManual(1.0, AutonomousStepDriveManual.KEEP_PREVIOUS_STATE));
        addStep(parallelGroup);
        addStep(new AutonomousStepDriveManual(0.0, 0.0));

    }

    public String toString() {
        return "Test Parallel Groups";
    }
}
