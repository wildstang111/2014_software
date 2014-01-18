/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs.test;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.WsAutonomousSerialStepGroup;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepDriveManual;

/**
 *
 * @author Nathan
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class WsAutonomousProgramTestSerial extends WsAutonomousProgram {

    public void defineSteps() {
        System.out.println("Define steps called");
        WsAutonomousSerialStepGroup parallelGroup = new WsAutonomousSerialStepGroup("Test serial step group.");
            parallelGroup.addStep(new WsAutonomousStepDriveManual(WsAutonomousStepDriveManual.KEEP_PREVIOUS_STATE, 1.0));
            parallelGroup.addStep(new WsAutonomousStepDelay(250));
            parallelGroup.addStep(new WsAutonomousStepDriveManual(1.0, WsAutonomousStepDriveManual.KEEP_PREVIOUS_STATE));
        addStep(parallelGroup);
        addStep(new WsAutonomousStepDriveManual(0.0, 0.0));

    }

    public String toString() {
        return "Test Serial Groups";
    }
}
