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
public class AutonomousDoubleStartPositionConfigFileParameter extends AutonomousDoubleConfigFileParameter{

    public AutonomousDoubleStartPositionConfigFileParameter(String pName, double defValue) {
        super(pName + "." + AutonomousManager.getInstance().getStartPosition().toConfigString(), defValue);
    }
    
}
