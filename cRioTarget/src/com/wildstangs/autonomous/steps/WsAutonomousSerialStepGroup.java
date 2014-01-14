package com.wildstangs.autonomous.steps;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.logger.Logger;
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
    private boolean finishedPreviousStep, lastStepError;

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
            ((WsAutonomousStep) steps.get(0)).initialize();
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
            }
        }
        WsAutonomousStep step = (WsAutonomousStep) steps.get(currentStep);
        step.update();
        if (step.isFinished()) {
            finishedPreviousStep = true;
            if (!step.isPassed()) {
                failedStep(step, currentStep);
                lastStepError = true;
            } else {
                finishedPreviousStep = true;
                lastStepError = false;
            }
        }
    }

    protected final void failedStep(WsAutonomousStep step, int i) {
        if (step.isFatal()) {
            finished = true;
            fatal = true;
        }
        handleError(step, i);
    }

    protected void handleError(WsAutonomousStep step, int i) //Separate method for easy overrides.
    {
        pass = false;
        Logger.getLogger().error("Substep " + i + "(" + step.toString() + ") of serial autonomous step group.", "Auto Step", step.errorInfo);
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

    public void setNextStep(WsAutonomousStep newStep) {
        if (steps.get(currentStep) instanceof WsAutonomousSerialStepGroup) {
            if ((((WsAutonomousSerialStepGroup) steps.get(currentStep))).getNextStep() != null) {
                ((WsAutonomousSerialStepGroup) steps.get(currentStep)).setNextStep(newStep);
            } else {
                steps.set(currentStep + 1, newStep);
            }
        } else if (currentStep + 1 < steps.size()) {
            steps.set(currentStep + 1, newStep);
        }
    }

    public boolean lastStepHadError() {
        if (steps.get(currentStep) instanceof WsAutonomousSerialStepGroup) {
            return ((WsAutonomousSerialStepGroup) steps.get(currentStep)).lastStepHadError();
        }
        return lastStepError;
    }

    public void finishGroup() {
        finished = true;
    }
}
