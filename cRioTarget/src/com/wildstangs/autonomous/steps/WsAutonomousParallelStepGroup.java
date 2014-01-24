/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps;

import edu.wpi.first.wpilibj.networktables2.util.List;

/**
 *
 * @author coder65535
 */
public class WsAutonomousParallelStepGroup extends WsAutonomousStep {
    //Parallel groups execute all contained steps in the same frame. Be careful!
    //Note: a finished step is immediately removed from the list. update() is not called on any step that finishes.

    final List steps = new List();
    boolean initialized = false;
    String name = "";

    public WsAutonomousParallelStepGroup() {
        name = "";
    }

    public WsAutonomousParallelStepGroup(String name) {
        this.name = name;
    }

    public void initialize() {
        initialized = true;
        for (int i = 0; i < steps.size(); i++) {
            ((WsAutonomousStep) steps.get(i)).initialize();
        }
    }

    public void update() {
        for (int i = 0; i < steps.size(); i++) {
            WsAutonomousStep step = (WsAutonomousStep) steps.get(i);
            step.update();
            if (step.isFinished()) {
                steps.remove(step);
            }
        }
        if (steps.isEmpty()) {
            finished = true;
        }
    }

    public void addStep(WsAutonomousStep step) {
        if (!initialized) {
            steps.add(step);
        }
    }

    public String toString() {
        return "Parallel step group: " + name;
    }
}
