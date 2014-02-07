/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs.test;

import com.wildstangs.autonomous.AutonomousManager;
import com.wildstangs.autonomous.AutonomousProgram;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepStartDriveUsingMotionProfileAndHeading;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepStopDriveUsingMotionProfile;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepWaitForDriveMotionProfile;
import com.wildstangs.config.DoubleConfigFileParameter;

/**
 *
 * @author Nathan
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class AutonomousProgramDriveDistanceMotionProfile extends AutonomousProgram {

    private DoubleConfigFileParameter distance;
    private DoubleConfigFileParameter heading;

    public void defineSteps() {
        distance = new DoubleConfigFileParameter(this.getClass().getName(), AutonomousManager.getInstance().getStartPosition().toConfigString() + ".distance", 10.0);
        heading = new DoubleConfigFileParameter(this.getClass().getName(), AutonomousManager.getInstance().getStartPosition().toConfigString() + ".heading", 0.0);
        addStep(new AutonomousStepStartDriveUsingMotionProfileAndHeading(distance.getValue(), 0.0, heading.getValue()));
        addStep(new AutonomousStepWaitForDriveMotionProfile()); 
        addStep(new AutonomousStepStopDriveUsingMotionProfile());

//        programSteps[3] = new AutonomousStepEnableDriveDistancePid();
//        programSteps[4] = new AutonomousStepSetDriveDistancePidSetpoint(distance.getValue());
//        programSteps[5] = new AutonomousStepWaitForDriveDistancePid();
//        programSteps[6] = new AutonomousStepStartDriveUsingMotionProfile(distance.getValue(), 0.0);
//        programSteps[7] = new AutonomousStepWaitForDriveMotionProfile(); 
//        programSteps[8] = new AutonomousStepStopDriveUsingMotionProfile();
    }

    public String toString() {
        return "TEST Motion profile distance";
    }
}
