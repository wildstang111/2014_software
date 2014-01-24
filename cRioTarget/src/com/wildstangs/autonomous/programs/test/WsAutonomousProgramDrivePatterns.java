/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs.test;

import com.wildstangs.autonomous.WsAutonomousManager;
import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepQuickTurn;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepStartDriveUsingMotionProfile;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepStopDriveUsingMotionProfile;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepWaitForDriveMotionProfile;
import com.wildstangs.config.DoubleConfigFileParameter;

/**
 *
 * @author Joey
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class WsAutonomousProgramDrivePatterns extends WsAutonomousProgram {

    private DoubleConfigFileParameter firstAngle, secondAngle, firstDriveDistance, firstDriveVelocity,
            secondDriveDistance, secondDriveVelocity;

    public void defineSteps() {
        firstAngle = new DoubleConfigFileParameter(
                this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstRelativeAngle", 45);
        secondAngle = new DoubleConfigFileParameter(
                this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondRelativeAngle", 45);
        firstDriveDistance = new DoubleConfigFileParameter(
                this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstDriveDistance", -100);
        firstDriveVelocity = new DoubleConfigFileParameter(
                this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstDriveVelocity", 0.0);
        secondDriveDistance = new DoubleConfigFileParameter(
                this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondDriveDistance", -30);
        secondDriveVelocity = new DoubleConfigFileParameter(
                this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondDriveVelocity", 0.0);

        addStep(new WsAutonomousStepStartDriveUsingMotionProfile(firstDriveDistance.getValue(), firstDriveVelocity.getValue()));
        addStep(new WsAutonomousStepWaitForDriveMotionProfile());
        addStep(new WsAutonomousStepStopDriveUsingMotionProfile());
        addStep(new WsAutonomousStepQuickTurn(firstAngle.getValue()));
        addStep(new WsAutonomousStepStartDriveUsingMotionProfile(secondDriveDistance.getValue(), secondDriveVelocity.getValue()));
        addStep(new WsAutonomousStepWaitForDriveMotionProfile());
        addStep(new WsAutonomousStepStopDriveUsingMotionProfile());
        addStep(new WsAutonomousStepQuickTurn(secondAngle.getValue()));
    }

    public String toString() {
        return "Testing drive patterns for after shoot 5";
    }
}
