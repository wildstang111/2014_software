/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package com.wildstangs.crio;


import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.logger.Logger;
import com.wildstangs.profiling.ProfilingTimer;
import com.wildstangs.subsystems.BallHandler;
import com.wildstangs.subsystems.HotGoalDetector;
import com.wildstangs.subsystems.NonI2CLED;
import com.wildstangs.subsystems.base.SubsystemContainer;
import com.wildstangs.timer.WsTimer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        startupTimer.startTimingSection();
        FrameworkAbstraction.robotInit("/ws_config.txt");
        startupTimer.endTimingSection();
        Logger.getLogger().always(this.getClass().getName(), "robotInit", "Startup Completed");

    }
    
    ProfilingTimer durationTimer = new ProfilingTimer("Periodic method duration", 50);
    ProfilingTimer periodTimer = new ProfilingTimer("Periodic method period", 50);
    ProfilingTimer startupTimer = new ProfilingTimer("Startup duration", 1);
    ProfilingTimer initTimer = new ProfilingTimer("Init duration", 1);
    
    //We may not want to check the calibration switches right away incase they have bad states from start up
    protected WsTimer waitForCalibrationTimer = new WsTimer();
    protected boolean waitingForArmCalibration = false;
    
    public void disabledInit() {
        initTimer.startTimingSection();
        FrameworkAbstraction.disabledInit();
        initTimer.endTimingSection();
        Logger.getLogger().always(this.getClass().getName(), "disabledInit", "Disabled Init Complete");
        waitForCalibrationTimer.reset();
        waitForCalibrationTimer.start();
        waitingForArmCalibration = true;
        ((NonI2CLED) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.LED_INDEX)).setControlState(NonI2CLED.DISABLED);
    }
    
    public void disabledPeriodic() {
        FrameworkAbstraction.disabledPeriodic();
        
        SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.LED_INDEX).update();
        
        ((BallHandler) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.BALL_HANDLER_INDEX)).checkArmPidsDisabled();
        
        boolean backArmCalibrationSwitch = ((Boolean) InputManager.getInstance().getSensorInput(InputManager.BACK_ARM_CALIBRATION_SWITCH_INDEX).get()).booleanValue();
        boolean frontArmCalibrationSwitch = ((Boolean) InputManager.getInstance().getSensorInput(InputManager.FRONT_ARM_CALIBRATION_SWITCH_INDEX).get()).booleanValue();
        
        SmartDashboard.putBoolean("BackArmCalibrationSwitch", backArmCalibrationSwitch);
        SmartDashboard.putBoolean("FrontArmCalibrationSwitch", frontArmCalibrationSwitch);
        
        if(!waitingForArmCalibration)
        {
            if(backArmCalibrationSwitch)
            {
                ((BallHandler) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.BALL_HANDLER_INDEX)).calibrateBackArm();
            }

            if(frontArmCalibrationSwitch)
            {
                ((BallHandler) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.BALL_HANDLER_INDEX)).calibrateFrontArm();
            }
        }
        else
        {
            if(waitForCalibrationTimer.hasPeriodPassed(1.25))
            {
                waitForCalibrationTimer.stop();
                waitingForArmCalibration = false;
            }
        }
        
    }

    public void autonomousInit() {
        ((NonI2CLED) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.LED_INDEX)).setControlState(NonI2CLED.AUTONOMOUS);
        FrameworkAbstraction.autonomousInit();
//        ((HotGoalDetector) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.HOT_GOAL_DETECTOR_INDEX)).initCamera();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        FrameworkAbstraction.autonomousPeriodic();
        Watchdog.getInstance().feed();
    }
    protected final BooleanConfigFileParameter KILL_CAMERA_CONFIG = new BooleanConfigFileParameter(this.getClass().getName(), "KillCamera", true);
    /**
     * This function is called periodically during operator control
     */
    public void teleopInit() {
        ((NonI2CLED) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.LED_INDEX)).setControlState(NonI2CLED.TELEOPERATED);
        FrameworkAbstraction.teleopInit();
        //Killing the camera so it doesn't cause our CPU usage to go to 100%
        if(KILL_CAMERA_CONFIG.getValue()) ((HotGoalDetector) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.HOT_GOAL_DETECTOR_INDEX)).killCamera();
        periodTimer.startTimingSection();
    }

    public void teleopPeriodic() {
          FrameworkAbstraction.teleopPeriodic();
          Watchdog.getInstance().feed();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        Watchdog.getInstance().feed();
    }
}
