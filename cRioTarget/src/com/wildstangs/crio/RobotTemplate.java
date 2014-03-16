/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package com.wildstangs.crio;


import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.logger.Logger;
import com.wildstangs.profiling.ProfilingTimer;
import com.wildstangs.subsystems.BallHandler;
import com.wildstangs.subsystems.HotGoalDetector;
import com.wildstangs.subsystems.base.SubsystemContainer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Watchdog;

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
        Logger.getLogger().always(this.getClass().getName(), "robotInit", "Startup Completed");
        startupTimer.endTimingSection();

    }
    
    ProfilingTimer durationTimer = new ProfilingTimer("Periodic method duration", 50);
    ProfilingTimer periodTimer = new ProfilingTimer("Periodic method period", 50);
    ProfilingTimer startupTimer = new ProfilingTimer("Startup duration", 1);
    ProfilingTimer initTimer = new ProfilingTimer("Init duration", 1);

    public void disabledInit() {
        initTimer.startTimingSection();
        FrameworkAbstraction.disabledInit();
        initTimer.endTimingSection();
        Logger.getLogger().always(this.getClass().getName(), "disabledInit", "Disabled Init Complete");

    }

    public void disabledPeriodic() {
        FrameworkAbstraction.disabledPeriodic();
        
        if(((Boolean) InputManager.getInstance().getSensorInput(InputManager.BACK_ARM_CALIBRATION_SWITCH_INDEX).get()).booleanValue())
        {
            ((BallHandler) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.BALL_HANDLER_INDEX)).calibrateBackArm(true);
        }
        
        if(((Boolean) InputManager.getInstance().getSensorInput(InputManager.FRONT_ARM_CALIBRATION_SWITCH_INDEX).get()).booleanValue())
        {
            ((BallHandler) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.BALL_HANDLER_INDEX)).calibrateFrontArm(true);
        }
        
//        ((HotGoalDetector) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.HOT_GOAL_DETECTOR_INDEX)).disabledUpdate();
    }

    public void autonomousInit() {
        FrameworkAbstraction.autonomousInit();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        FrameworkAbstraction.autonomousPeriodic();
        Watchdog.getInstance().feed();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit() {
        FrameworkAbstraction.teleopInit();
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
