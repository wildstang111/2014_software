package com.wildstangs.autonomous.steps;

import edu.wpi.first.wpilibj.networktables2.util.List;

/**
 *
 * @author coder65535
 */
public class AutonomousSerialStepGroup extends AutonomousStep {
    //Serial groups execute all contained steps sequentially

    final List steps = new List();
    int currentStep = 0;
    boolean initialized = false;
    String name = "";
    private boolean finishedPreviousStep;

    public AutonomousSerialStepGroup() {
        name = "";
    }

    public AutonomousSerialStepGroup(String name) {
        this.name = name;
    }

    public void initialize() {
        finishedPreviousStep = false;
        currentStep = 0;
        if (!steps.isEmpty()) {
            ((AutonomousStep) steps.get(currentStep)).initialize();
            System.out.println("Starting step " + ((AutonomousStep) steps.get(currentStep)).toString());
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
                ((AutonomousStep) steps.get(currentStep)).initialize();
                System.out.println("Starting step " + ((AutonomousStep) steps.get(currentStep)).toString());
            }
        }
        AutonomousStep step = (AutonomousStep) steps.get(currentStep);
        step.update();
        if (step.isFinished()) {
            finishedPreviousStep = true;
        }
    }

    public void addStep(AutonomousStep step) {
        if (!initialized) {
            steps.add(step);
        }
    }

    public String toString() {
        return "Serial step group: " + name;
    }

    public AutonomousStep getCurrentStep() {
        return (AutonomousStep) steps.get(currentStep);
    }

    public AutonomousStep getNextStep() {
        if (currentStep + 1 < steps.size()) {
            return (AutonomousStep) steps.get(currentStep + 1);
        } else {
            return null;
        }
    }

    public void finishGroup() {
        finished = true;
    }
}
