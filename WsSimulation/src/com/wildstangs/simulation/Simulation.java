package com.wildstangs.simulation;

import com.wildstangs.simulation.solenoids.SolenoidContainer;
import com.wildstangs.autonomous.AutonomousManager;
import com.wildstangs.crio.FrameworkAbstraction;
import com.wildstangs.input.Controllers;
import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.logger.*;
import com.wildstangs.logviewer.LogViewer;
import com.wildstangs.profiling.ProfilingTimer;
import java.io.File;

/**
 *
 * @author ChadS
 */
public class Simulation {

    static String c = "WsSimulation";

    static boolean autonomousRun = false;

    //Display graphs 
    static boolean intakeMotorGraphs = false;
    static boolean driveMotorGraphs = true;
    static boolean flywheelSpeedGraphs = false;
    static boolean driveThrottleGraph = true;

    static ProfilingTimer durationTimer = new ProfilingTimer("Periodic method duration", 50);
    static ProfilingTimer periodTimer = new ProfilingTimer("Periodic method period", 50);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.setProperty("net.java.games.input.librarypath", System.getProperty("user.dir") + File.separator + "lib");

        // testInput();
        //Instantiate the Facades and Containers
        //start the log viewer.
        (new Thread(new LogViewer())).start();
        FileLogger.getFileLogger().startLogger();

        FrameworkAbstraction.robotInit("/Config/ws_config.txt");
//        WsConfigManager.getInstance().dumpConfigData();

        Logger logger = Logger.getLogger();

        //System.out.println(WsConfigManager.getInstance().getConfigItemName("com.wildstangs.InputManager.WsDriverJoystick.trim"));
        //System.out.println(WsConfigManager.getInstance().dumpConfigData());
        logger.always(c, "sim_startup", "Simulation starting.");
        FileLogger.getFileLogger().logData("Sim Started");

//        double pid_setpoint = 10;
//        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).enableDistancePidControl();
//        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).setDriveDistancePidSetpoint(pid_setpoint);
//        DriveBaseEncoders dbEncoders = new DriveBaseEncoders(); 
//        FlywheelEncoders flywheelEncoders = new FlywheelEncoders(); 
//        HopperLimitSwitches limitSwitches = new HopperLimitSwitches(); 
//        AccumulatorLimitSwitch aclimitSwitches = new AccumulatorLimitSwitch(); 
//        FunnelatorLimitSwitch funnellimitSwitches = new FunnelatorLimitSwitch();
//        GyroSimulation gyro = new GyroSimulation();
        periodTimer.startTimingSection();

//        ContinuousAccelFilter accelFilter = new ContinuousAccelFilter(0, 0, 0);
//        double distance_to_go = 60.5;
//        double currentProfileX =0.0; 
//        double currentProfileV =0.0; 
//        double currentProfileA =0.0; 
//        for (int i = 0; i < 60; i++) {
//            //Update measured values 
//            
//            //Update PID using profile velocity as setpoint and measured velocity as PID input 
//            
//            //Update system to get feed forward terms
//            double distance_left = distance_to_go - currentProfileX;
//            logger.debug(c, "AccelFilter", "distance_left: " + distance_left + " p: " + accelFilter.getCurrPos()+ " v: " + accelFilter.getCurrVel() + " a: " + accelFilter.getCurrAcc() );
//            accelFilter.calculateSystem(distance_left , currentProfileV, 0, 600, 102, 0.020);
//            currentProfileX = accelFilter.getCurrPos();
//            currentProfileV = accelFilter.getCurrVel();
//            currentProfileA = accelFilter.getCurrAcc();
//            
//            //Update motor output with PID output and feed forward velocity and acceleration 
//            
//        }
        logger.always(c, "sim_startup", "Simulation init done.");
        if (autonomousRun) {
            AutonomousManager.getInstance().setPosition(1);
            AutonomousManager.getInstance().setProgram(4);
            AutonomousManager.getInstance().startCurrentProgram();
        }

        while (true) {
            periodTimer.endTimingSection();
            periodTimer.startTimingSection();
            durationTimer.startTimingSection();
            
            if (false == autonomousRun || (false == AutonomousManager.getInstance().getRunningProgramName().equalsIgnoreCase("Sleeper"))) {

//                gyro.update();
                //Update the encoders
//                dbEncoders.update();
                InputManager.getInstance().updateSensorData();
                if (autonomousRun) {
                    FrameworkAbstraction.autonomousPeriodic();
                } else {
                    FrameworkAbstraction.teleopPeriodic();
                }
                SolenoidContainer.getInstance().update();

//                flywheelEncoders.update(); 
//                limitSwitches.update();
//                aclimitSwitches.update();
//                funnellimitSwitches.update();
            }

            double spentTime = durationTimer.endTimingSection();
            int spentMS = (int) (spentTime * 1000);
            int timeToSleep = ((20 - spentMS) > 0 ? (20 - spentMS) : 0);
            try {
                Thread.sleep(timeToSleep);
            } catch (InterruptedException e) {
            }
        }
    }

    private static void testInput() {
        try {
            Controllers.create();
            System.out.println("Controllers found: " + Controllers.getControllerCount());
            for (int i = 0; i < Controllers.getControllerCount(); i++) {
                System.out.println("Controller name: " + Controllers.getController(i).getName());
                System.out.println("Controller index: " + Controllers.getController(i).getIndex());
                System.out.println("Controller axis count: " + Controllers.getController(i).getAxisCount());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
