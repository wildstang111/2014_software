package com.wildstangs.autonomous.steps;

import edu.wpi.first.wpilibj.networktables2.util.List;

/**
 *
 * @author coder65535
 */
public class WsAutonomousSerialStepGroup extends WsAutonomousStep {
    //Serial groups execute all contained steps sequentially

    final List steps = new List();
    int currentStep = 0;
    boolean initialized = false;
    String name = "";
    private boolean finishedPreviousStep;

    public WsAutonomousSerialStepGroup() {
        name = "";
    }

    public WsAutonomousSerialStepGroup(String name) {
        this.name = name;
    }

    public void initialize() {
        finishedPreviousStep = false;
        currentStep = 0;
        if (!steps.isEmpty()) {
            ((WsAutonomousStep) steps.get(currentStep)).initialize();
            System.out.println("Starting step " + ((WsAutonomousStep) steps.get(currentStep)).toString());
        }
        initialized = true;
    }

    public void update() {
        if (finished) {
            return;
        }
        if (finishedPreviousStep) {
            finishedPreviousStep = false;
            currentStep++;
            if (currentStep >= steps.size()) {
                //We have reached the end of our list of steps, we're finished
                finished = true;
                return;
            } else {
                ((WsAutonomousStep) steps.get(currentStep)).initialize();
                System.out.println("Starting step " + ((WsAutonomousStep) steps.get(currentStep)).toString());
            }
        }
        WsAutonomousStep step = (WsAutonomousStep) steps.get(currentStep);
        step.update();
        if (step.isFinished()) {
            finishedPreviousStep = true;
        }
    }

    public void addStep(WsAutonomousStep step) {
        if (!initialized) {
            steps.add(step);
        }
    }

    public String toString() {
        return "Serial step group: " + name;
    }

    public WsAutonomousStep getCurrentStep() {
        return (WsAutonomousStep) steps.get(currentStep);
    }

    public WsAutonomousStep getNextStep() {
        if (currentStep + 1 < steps.size()) {
            return (WsAutonomousStep) steps.get(currentStep + 1);
        } else {
            return null;
        }
    }

    public void finishGroup() {
        finished = true;
    }
}
