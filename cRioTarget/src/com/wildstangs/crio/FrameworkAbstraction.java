/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.crio;

import com.wildstangs.autonomous.AutonomousManager;
import com.wildstangs.configmanager.ConfigManager;
import com.wildstangs.configmanager.ConfigManagerException;
import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.logger.Logger;
import com.wildstangs.outputmanager.base.OutputManager;
import com.wildstangs.subsystems.base.SubsystemContainer;

/**
 *
 * @author Alex
 */
public class FrameworkAbstraction {

    public static void autonomousInit() {
        SubsystemContainer.getInstance().init();
        Logger.getLogger().readConfig();
        AutonomousManager.getInstance().startCurrentProgram();
    }

    public static void teleopInit() {
        SubsystemContainer.getInstance().init();
        Logger.getLogger().readConfig();
    }

    public static void robotInit(String fileName) {
        try {
            ConfigManager.getInstance().setConfigFileName(fileName);
            ConfigManager.getInstance().readConfig();
        } catch (ConfigManagerException wscfe) {
            System.out.println(wscfe.toString());
        }

        InputManager.getInstance();
        OutputManager.getInstance();
        SubsystemContainer.getInstance().init();
        Logger.getLogger().readConfig();
        AutonomousManager.getInstance();
    }

    public static void teleopPeriodic() {
        InputManager.getInstance().updateOiData();
        InputManager.getInstance().updateSensorData();
        SubsystemContainer.getInstance().update();
        OutputManager.getInstance().update();
    }

    public static void autonomousPeriodic() {
        InputManager.getInstance().updateOiDataAutonomous();
        InputManager.getInstance().updateSensorData();
        AutonomousManager.getInstance().update();
        SubsystemContainer.getInstance().update();
        OutputManager.getInstance().update();
    }

    public static void disabledPeriodic() {
        InputManager.getInstance().updateOiData();
        InputManager.getInstance().updateSensorData();
    }

    public static void disabledInit() {
        AutonomousManager.getInstance().clear();
        try {
            ConfigManager.getInstance().readConfig();
        } catch (ConfigManagerException e) {
            System.out.println(e.getMessage());
        }

        SubsystemContainer.getInstance().init();
        Logger.getLogger().readConfig();
    }

}
