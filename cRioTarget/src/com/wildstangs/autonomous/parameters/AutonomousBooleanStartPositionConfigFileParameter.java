/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.parameters;

import com.wildstangs.autonomous.AutonomousManager;

/**
 *
 * @author chadschmidt
 */
public class AutonomousBooleanStartPositionConfigFileParameter extends AutonomousBooleanConfigFileParameter{

    public AutonomousBooleanStartPositionConfigFileParameter(String pName, boolean defValue) {
        super(pName + "." + AutonomousManager.getInstance().getStartPosition().toConfigString(), defValue);
    }
    
}
