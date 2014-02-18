/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.control;

import com.wildstangs.autonomous.steps.AutonomousStep;
import com.wildstangs.logger.Logger;
import com.wildstangs.timer.WsTimer;
import edu.wpi.first.wpilibj.Utility;

/**
 *
 * @author coder65535
 */
public class AutonomousStepDelay extends AutonomousStep /* This step delays testing for the specified number of cycles.
 * Note: If used in a parallel step group, it insures that the group waits for at least the specified number of cycles, instead.
 */ {

    protected final double delay;
    protected WsTimer timer;

    public AutonomousStepDelay(int msDelay) {
        this.delay = msDelay / 1000.0;
        this.timer = new WsTimer();
        if (msDelay < 0) {
            Logger.getLogger().debug(this.getClass().getName(), "AutonomousStepDelay", "Delay must be greater than 0");
        }
    }

    public void initialize() {
        timer.reset();
        timer.start();
        System.out.println("Timer in init " + timer.get());
    }

    public void update() {
        if(timer.hasPeriodPassed(delay))
        {
            timer.stop();
            finished = true;
        System.out.println("Timer at finished " + timer.get());
        }
    }

    public String toString() {
        return "Delay for " + (delay * 1000) + "  ms";
    }
}
