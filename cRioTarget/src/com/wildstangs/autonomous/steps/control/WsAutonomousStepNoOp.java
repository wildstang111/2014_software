/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.control;

import com.wildstangs.autonomous.WsAutonomousStep;

/**
 *
 * @author coder65535
 */
public class WsAutonomousStepNoOp extends WsAutonomousStep {
    //Note: This step silently passes on the error status of the previous step.
    //(That is, if the previous step had an error, this step doesn't log an error, but WsAutonomousManager.getInstance().getRunningProgram().lastStepHadError() will remain true.

    public WsAutonomousStepNoOp() {
    }

    public void initialize() {
        finished = true; //This step does nothing, and finishes immediately.
    }

    public void update() {
    }

    public String toString() {
        return "No-Op";
    }
}
