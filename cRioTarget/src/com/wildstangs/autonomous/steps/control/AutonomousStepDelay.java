/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.control;

import com.wildstangs.autonomous.steps.AutonomousStep;
import com.wildstangs.logger.Logger;
import edu.wpi.first.wpilibj.Utility;

/**
 *
 * @author coder65535
 */
public class AutonomousStepDelay extends AutonomousStep /* This step delays testing for the specified number of cycles.
 * Note: If used in a parallel step group, it insures that the group waits for at least the specified number of cycles, instead.
 */ {

    private int count;
    protected final int msDelay;
    private double startTime = 0;

    public AutonomousStepDelay(int msDelay) {
        this.msDelay = msDelay;
        if (msDelay < 0) {
            Logger.getLogger().debug(this.getClass().getName(), "AutonomousStepDelay", "Delay must be greater than 0");
        }
    }

    public void initialize() {
    }

    public void update() {
        if (startTime == 0) {
            startTime = Utility.getFPGATime();
        } else if (startTime + msDelay < Utility.getFPGATime()) {
            finished = true;
        }
    }

    public String toString() {
        return "Delay for " + msDelay + "  ms";
    }
}
