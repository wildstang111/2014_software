/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.AutonomousProgram;
import com.wildstangs.autonomous.steps.AutonomousParallelStepGroup;
import com.wildstangs.autonomous.steps.AutonomousSerialStepGroup;
import com.wildstangs.autonomous.steps.arms.AutonomousStepSetArmPresets;
import com.wildstangs.autonomous.steps.catapult.AutonomousStepArmCatapult;
import com.wildstangs.autonomous.steps.catapult.AutonomousStepFireCatapult;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepSetShifter;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepStartDriveUsingMotionProfile;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepStopDriveUsingMotionProfile;
import com.wildstangs.autonomous.steps.drivebase.AutonomousStepWaitForDriveMotionProfile;
import com.wildstangs.autonomous.steps.ears.AutonomousStepSetEarState;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.subsystems.BallHandler;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 *
 * @author Joey
 */
public class AutonomousProgramDriveAndShootWithEars extends AutonomousProgram
{
    protected final DoubleConfigFileParameter DISTANCE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "DistanceToDrive", 140.0);
    
    protected void defineSteps()
    {
//        addStep(new AutonomousStepSetShifter(DoubleSolenoid.Value.kReverse));
        AutonomousParallelStepGroup tensionAndDrive = new AutonomousParallelStepGroup("Tension And Drive");
        tensionAndDrive.addStep(new AutonomousStepArmCatapult());
        tensionAndDrive.addStep(new AutonomousStepSetArmPresets(BallHandler.CATAPULT_TENSION_PRESET_BACK));
        tensionAndDrive.addStep(new AutonomousStepSetEarState(true));
        
        AutonomousSerialStepGroup drive = new AutonomousSerialStepGroup("Drive");
        drive.addStep(new AutonomousStepStartDriveUsingMotionProfile(DISTANCE_CONFIG.getValue(), 1.0));
        drive.addStep(new AutonomousStepWaitForDriveMotionProfile());
        drive.addStep(new AutonomousStepStopDriveUsingMotionProfile());
        
        tensionAndDrive.addStep(drive);
        
        addStep(tensionAndDrive);
        addStep(new AutonomousStepFireCatapult());
        addStep(new AutonomousStepSetEarState(false));
    }

    public String toString()
    {
        return "Drive and Shoot for the Goal in front of us With Ears";
    }
}
