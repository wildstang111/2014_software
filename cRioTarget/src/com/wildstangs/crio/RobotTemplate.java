/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package com.wildstangs.crio;


import com.wildstangs.logger.Logger;
import com.wildstangs.profiling.WsProfilingTimer;
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
        FrameworkAbstraction.robotInit();
        Logger.getLogger().always(this.getClass().getName(), "robotInit", "Startup Completed");
        startupTimer.endTimingSection();

    }
    WsProfilingTimer durationTimer = new WsProfilingTimer("Periodic method duration", 50);
    WsProfilingTimer periodTimer = new WsProfilingTimer("Periodic method period", 50);
    WsProfilingTimer startupTimer = new WsProfilingTimer("Startup duration", 1);
    WsProfilingTimer initTimer = new WsProfilingTimer("Init duration", 1);

    public void disabledInit() {
        initTimer.startTimingSection();
        FrameworkAbstraction.disabledInit();
        initTimer.endTimingSection();
        Logger.getLogger().always(this.getClass().getName(), "disabledInit", "Disabled Init Complete");
        
    }

    public void disabledPeriodic() {
        FrameworkAbstraction.disabledPeriodic();
    }

    public void autonomousInit() {
        FrameworkAbstraction.autonomousInit();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        FrameworkAbstraction.autonomousPeriodic();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit() {
        FrameworkAbstraction.teleopInit();
        periodTimer.startTimingSection();
    }

    public void teleopPeriodic() {
//        periodTimer.endTimingSection();
//        periodTimer.startTimingSection();
//        durationTimer.startTimingSection();
          FrameworkAbstraction.teleopPeriodic();
//        durationTimer.endTimingSection();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        Watchdog.getInstance().feed();
    }
}
