/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.crio;

import com.wildstangs.autonomous.WsAutonomousManager;
import com.wildstangs.configmanager.WsConfigManager;
import com.wildstangs.configmanager.WsConfigManagerException;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.logger.Logger;
import com.wildstangs.outputmanager.base.WsOutputManager;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.Watchdog;

/**
 *
 * @author Alex
 */
public class FrameworkAbstraction {
    public static void autonomousInit(){
        WsSubsystemContainer.getInstance().init();
        Logger.getLogger().readConfig();
        WsAutonomousManager.getInstance().startCurrentProgram();
    }
    public static void teleopInit(){
        WsSubsystemContainer.getInstance().init();
        Logger.getLogger().readConfig();
    }
    public static void robotInit(String fileName){
        System.out.println("RobotInit Start");
        //Enables the filelogger thread.
        //FileLogger.getFileLogger().startLogger();
        try {
            WsConfigManager.getInstance().setFileName(fileName);
            WsConfigManager.getInstance().readConfig();
            //WsConfigFacade.getInstance().dumpConfigData();
        } catch (WsConfigManagerException wscfe) {
            System.out.println(wscfe.toString());
        }

        WsInputManager.getInstance();
        WsOutputManager.getInstance();
//        Logger.getLogger().always(this.getClass().getName(), "robotInit", "Facades Completed");
        WsSubsystemContainer.getInstance().init();
//        Logger.getLogger().always(this.getClass().getName(), "robotInit", "Subsystem Completed");
        Logger.getLogger().readConfig();
//        Logger.getLogger().always(this.getClass().getName(), "robotInit", "Logger Read Config Completed");
        WsAutonomousManager.getInstance();
    }
    public static void teleopPeriodic() {
        WsInputManager.getInstance().updateOiData();
        WsInputManager.getInstance().updateSensorData();
        WsSubsystemContainer.getInstance().update();
        WsOutputManager.getInstance().update();
    }
    public static void autonomousPeriodic() {
        WsInputManager.getInstance().updateOiDataAutonomous();
        WsInputManager.getInstance().updateSensorData();
        WsAutonomousManager.getInstance().update();
        WsSubsystemContainer.getInstance().update();
        WsOutputManager.getInstance().update();
    }
    public static void disabledPeriodic() {
        WsInputManager.getInstance().updateOiData();
        //Make LED stuff go in disabled.
        //((WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_LED))).update();
    }
    public static void disabledInit(){
        WsAutonomousManager.getInstance().clear();
        try {
            WsConfigManager.getInstance().readConfig();
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }

        //Logger.getLogger().always(this.getClass().getName(), "disbledInit", "Config Completed");
        WsSubsystemContainer.getInstance().init();
        Logger.getLogger().readConfig();
        //WsConfigFacade.getInstance().dumpConfigData();
    }
    
}
