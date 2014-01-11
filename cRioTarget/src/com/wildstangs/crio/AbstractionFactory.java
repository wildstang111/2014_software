/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.crio;

import com.wildstangs.autonomous.WsAutonomousManager;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.logger.Logger;
import com.wildstangs.outputmanager.base.WsOutputManager;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Alex
 */
public class AbstractionFactory {
    public static void autonomousInit(){
        WsSubsystemContainer.getInstance().init();
        Logger.getLogger().readConfig();
        WsAutonomousManager.getInstance().startCurrentProgram();
    }
    public static void teleopInit(){
        WsSubsystemContainer.getInstance().init();
        Logger.getLogger().readConfig();
    }
    public static void robotInit(){
        WsInputManager.getInstance();
        WsOutputManager.getInstance().update();
        WsSubsystemContainer.getInstance().update();
    }
}
