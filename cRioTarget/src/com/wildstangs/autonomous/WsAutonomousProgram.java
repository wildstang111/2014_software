package com.wildstangs.autonomous;

import com.wildstangs.autonomous.steps.WsAutonomousSerialStepGroup;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepStopAutonomous;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.logger.Logger;
import edu.wpi.first.wpilibj.networktables2.util.List;

/**
 *
 * @author Nathan
 */
public abstract class WsAutonomousProgram implements IStepContainer {

    protected final List programSteps = new List();
    protected int currentStep;
    protected boolean finishedStep, finished, lastStepError;

    protected abstract void defineSteps(); //Use this method to set the steps for this program. Programs execute the steps in the array programSteps serially.
    //Remember to clear everything before all of your steps are finished, because once they are, it immediately drops into Sleeper.

    public void initialize() {
        defineSteps();
        setStopPosition();
        currentStep = 0;
        finishedStep = false;
        finished = false;
        lastStepError = false;
        ((WsAutonomousStep) programSteps.get(0)).initialize();
        Logger.getLogger().debug("Auton", "Step Starting", programSteps.get(0).toString());
    }

    public void cleanup() {
        for (int i = 0; i < programSteps.size(); i++) {
            programSteps.remove(i);
        }
    }

    public void update() {
        if (finished) {
            return;
        }
        if (finishedStep) {
            finishedStep = false;
            currentStep++;
            if (currentStep >= programSteps.size()) {
                finished = true;
                return;
            } else {
                Logger.getLogger().debug("Auton", "Step Start", programSteps.get(currentStep).toString());
                ((WsAutonomousStep) programSteps.get(currentStep)).initialize();
            }
        }
        WsAutonomousStep step = (WsAutonomousStep) programSteps.get(currentStep); //Prevent errors caused by mistyping.
        step.update();
        if (step.isFinished()) {
            Logger.getLogger().debug("Auton", "Step Finished", step);
            finishedStep = true;
            if (!step.isPassed()) {
                lastStepError = true;
                failedStep(step);
            } else {
                lastStepError = false;
            }
        }
    }

    public WsAutonomousStep getCurrentStep() {
        return (WsAutonomousStep) programSteps.get(currentStep);
    }

    public WsAutonomousStep getNextStep() {
        if (currentStep + 1 < programSteps.size()) {
            return (WsAutonomousStep) programSteps.get(currentStep + 1);
        } else {
            return null;
        }
    }

    public void setNextStep(WsAutonomousStep newStep) {
        if (programSteps.get(currentStep) instanceof WsAutonomousSerialStepGroup) {
            if (((WsAutonomousSerialStepGroup) programSteps.get(currentStep)).getNextStep() != null) {
                ((WsAutonomousSerialStepGroup) programSteps.get(currentStep)).setNextStep(newStep);
            } else {
                programSteps.set(currentStep + 1, newStep);
            }
        } else if (currentStep + 1 < programSteps.size()) {
            programSteps.set(currentStep + 1, newStep);
        }
    }

    public void setStep(WsAutonomousStep newStep, int stepNumber) {
        if (currentStep != stepNumber && stepNumber >= 0 && stepNumber < programSteps.size()) {
            programSteps.set(stepNumber, newStep);
        }
    }

    protected void setStopPosition() {
        IntegerConfigFileParameter ForceStopAtStep = new IntegerConfigFileParameter(this.getClass().getName(), "ForceStopAtStep", 0);
        if (ForceStopAtStep.getValue() != 0) {
            int forceStop = ForceStopAtStep.getValue();
            if ((forceStop <= programSteps.size()) && (forceStop > 0)) {
                programSteps.set(forceStop, new WsAutonomousStepStopAutonomous());
                Logger.getLogger().always("Auton", "Force Stop", "Program is forced to stop at Step " + forceStop);
            } else {
                Logger.getLogger().error("Auton", "Force Stop", "Force stop value is outside of bounds. (0 to " + (programSteps.size() - 1));
            }
        }
    }

    protected final void failedStep(WsAutonomousStep step) {
        if (step.isFatal()) {
            finished = true;
            fatalError(step);
        } else {
            handleError(step);
        }
    }

    protected void fatalError(WsAutonomousStep step) //Separate method for easy overrides.
    {
        handleError(step);
    }

    protected void handleError(WsAutonomousStep step) {
        if (step.errorInfo.trim().length() != 0) {
            return;
        }
        Logger.getLogger().error("Autonomous step " + currentStep + " (" + step.toString() + ")", "Auto Step", step.errorInfo);
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean lastStepHadError() {
        if ((WsAutonomousStep) programSteps.get(currentStep) instanceof WsAutonomousSerialStepGroup) {
            return ((WsAutonomousSerialStepGroup) programSteps.get(currentStep)).lastStepHadError();
        }
        return lastStepError;
    }
    
    protected void addStep(WsAutonomousStep newStep) {
        programSteps.add(newStep);
    }
    
    public abstract String toString();
}
