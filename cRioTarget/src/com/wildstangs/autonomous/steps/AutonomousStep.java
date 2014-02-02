/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps;

/**
 *
 * @author coder65535
 */
public abstract class AutonomousStep {

    protected boolean finished;

    public AutonomousStep() {
        //initialize variables
        finished = false; //A step can't finish before it starts.
    }

    public abstract void initialize(); //This method is called once, when the step is first run. Use this method to set up anything that is necessary for the step.

    public abstract void update(); //This method is called on the active step, once per call to RobotTemplate.autonomousPeriodic().
    //Steps will continue to have this method called until they set finished to true.
    //Note: this method is first called right after initialize(), with no delay in between.

    public boolean isFinished() {
        return finished;
    }

    public abstract String toString(); //Please use future tense (NOT present tense!) when naming steps.
}
