package com.wildstangs.autonomous;

import com.wildstangs.autonomous.steps.AutonomousStep;
import com.wildstangs.autonomous.steps.control.AutonomousStepStopAutonomous;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.logger.Logger;
import edu.wpi.first.wpilibj.networktables2.util.List;

/**
 *
 * @author Nathan
 */
public abstract class AutonomousProgram {

    protected final List programSteps = new List();
    protected int currentStep;
    protected boolean finishedPreviousStep, finished;
    
    protected abstract void defineSteps(); //Use this method to set the steps for this program. Programs execute the steps in the array programSteps serially.
    //Remember to clear everything before all of your steps are finished, because once they are, it immediately drops into Sleeper.

    public void initialize() {
        programSteps.clear();
        defineSteps();
        loadStopPosition();
        currentStep = 0;
        finishedPreviousStep = false;
        finished = false;
        ((AutonomousStep) programSteps.get(0)).initialize();
        Logger.getLogger().debug("Auton", "Step Starting", programSteps.get(0).toString());
    }

    public void cleanup() {
        programSteps.clear();
    }

    public void update() {
        if (finished) {
            return;
        }
        if (finishedPreviousStep) {
            finishedPreviousStep = false;
            currentStep++;
            if (currentStep >= programSteps.size()) {
                finished = true;
                return;
            } else {
                Logger.getLogger().debug("Auton", "Step Start", programSteps.get(currentStep).toString());
                ((AutonomousStep) programSteps.get(currentStep)).initialize();
            }
        }
        AutonomousStep step = (AutonomousStep) programSteps.get(currentStep); //Prevent errors caused by mistyping.
        step.update();
        if (step.isFinished()) {
            Logger.getLogger().debug("Auton", "Step Finished", step);
            finishedPreviousStep = true;
        }
    }

    public AutonomousStep getCurrentStep() {
        return (AutonomousStep) programSteps.get(currentStep);
    }

    public AutonomousStep getNextStep() {
        if (currentStep + 1 < programSteps.size()) {
            return (AutonomousStep) programSteps.get(currentStep + 1);
        } else {
            return null;
        }
    }

    protected void loadStopPosition() {
        IntegerConfigFileParameter forceStopAtStep = new IntegerConfigFileParameter(this.getClass().getName(), "ForceStopAtStep", 0);
        if (forceStopAtStep.getValue() != 0) {
            int forceStop = forceStopAtStep.getValue();
            if ((forceStop <= programSteps.size()) && (forceStop > 0)) {
                programSteps.set(forceStop, new AutonomousStepStopAutonomous());
                Logger.getLogger().always("Auton", "Force Stop", "Program is forced to stop at Step " + forceStop);
            } else {
                Logger.getLogger().error("Auton", "Force Stop", "Force stop value is outside of bounds. (0 to " + (programSteps.size() - 1));
            }
        }
    }

    public boolean isFinished() {
        return finished;
    }
    
    protected void addStep(AutonomousStep newStep) {
        programSteps.add(newStep);
    }

    public abstract String toString();
}
