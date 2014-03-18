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
public class AutonomousParallelStepGroup extends AutonomousStep {
    //Parallel groups execute all contained steps in the same frame. Be careful!
    //Note: a finished step is immediately removed from the list. update() is not called on any step that finishes.

    final List steps = new List();
    boolean initialized = false;
    String name = "";

    public AutonomousParallelStepGroup() {
        name = "";
    }

    public AutonomousParallelStepGroup(String name) {
        this.name = name;
    }

    public void initialize() {
        initialized = true;
        for (int i = 0; i < steps.size(); i++) {
            ((AutonomousStep) steps.get(i)).initialize();
        }
    }

    public void update() {
        List toRemove = new List();
        for (int i = 0; i < steps.size(); i++) {
            AutonomousStep step = (AutonomousStep) steps.get(i);
            step.update();
            if (step.isFinished()) {
                toRemove.add(step);
            }
        }
        
        for(int i = 0; i < toRemove.size(); i++)
        {
            steps.remove(toRemove.get(i));
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
        return "Parallel step group: " + name;
    }
}
