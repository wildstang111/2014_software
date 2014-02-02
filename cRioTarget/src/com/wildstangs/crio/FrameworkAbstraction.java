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

/**
 *
 * @author Alex
 */
public class FrameworkAbstraction {

    public static void autonomousInit() {
        WsSubsystemContainer.getInstance().init();
        Logger.getLogger().readConfig();
        WsAutonomousManager.getInstance().startCurrentProgram();
    }

    public static void teleopInit() {
        WsSubsystemContainer.getInstance().init();
        Logger.getLogger().readConfig();
    }

    public static void robotInit(String fileName) {
        try {
            WsConfigManager.getInstance().setConfigFileName(fileName);
            WsConfigManager.getInstance().readConfig();
        } catch (WsConfigManagerException wscfe) {
            System.out.println(wscfe.toString());
        }

        WsInputManager.getInstance();
        WsOutputManager.getInstance();
        WsSubsystemContainer.getInstance().init();
        Logger.getLogger().readConfig();
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
    }

    public static void disabledInit() {
        WsAutonomousManager.getInstance().clear();
        try {
            WsConfigManager.getInstance().readConfig();
        } catch (WsConfigManagerException e) {
            System.out.println(e.getMessage());
        }

        WsSubsystemContainer.getInstance().init();
        Logger.getLogger().readConfig();
    }

}
