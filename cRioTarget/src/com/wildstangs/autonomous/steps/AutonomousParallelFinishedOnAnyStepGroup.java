/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps;

import edu.wpi.first.wpilibj.networktables2.util.List;

/**
 *
 * @author Joey
 */
public class AutonomousParallelFinishedOnAnyStepGroup extends AutonomousStep {

    private String name;
    private boolean initialized = false;
    private final List steps = new List();

    public AutonomousParallelFinishedOnAnyStepGroup() {
        name = "";
    }

    public AutonomousParallelFinishedOnAnyStepGroup(String name) {
        this.name = name;
    }

    public void initialize() {
        initialized = true;
        for (int i = 0; i < steps.size(); i++) {
            ((AutonomousStep) steps.get(i)).initialize();
        }
    }

    public void update() {
        for (int i = 0; i < steps.size(); i++) {
            AutonomousStep step = (AutonomousStep) steps.get(i);
            step.update();
            if (step.isFinished()) {
                steps.clear();
                break;
            }
        }
        if (steps.isEmpty()) {
            finished = true;
        }
    }

    public void addStep(AutonomousStep step) {
        if (!initialized) {
            steps.add(step);
        }
    }

    public String toString() {
        return "Parallel finished on any step group: " + name;
    }
}
