/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems.arm;

import com.wildstangs.config.IntegerConfigFileParameter;

/**
 *
 * @author Jason
 */
public class ArmPreset {
    
    protected IntegerConfigFileParameter wantedAngleMeasureFront_config, wantedAngleMeasureBack_config;
    protected int wantedAngleMeasureFront, wantedAngleMeasureBack;
    
    public ArmPreset(int wantedAngleMeasureFrontDefault, int wantedAngleMeasureBackDefault, String presetName){
        
        this.wantedAngleMeasureBack_config = new IntegerConfigFileParameter(this.getClass().getName() + "." + presetName, "WantedBackArmAngle", wantedAngleMeasureBackDefault);
        this.wantedAngleMeasureFront_config = new IntegerConfigFileParameter(this.getClass().getName() + "." + presetName, "WantedFrontArmAngle", wantedAngleMeasureFrontDefault);
        
        this.wantedAngleMeasureBack = wantedAngleMeasureBack_config.getValue();
        this.wantedAngleMeasureFront = wantedAngleMeasureFront_config.getValue();
    }
    
    public int getwantedAngleMeasureFront(){
        return wantedAngleMeasureFront;
    }
    
    public int getwantedAngleMeasureBack(){
        return wantedAngleMeasureBack;
    }
    
    public void notifyConfigChange()
    {
        this.wantedAngleMeasureBack = this.wantedAngleMeasureBack_config.getValue();
        this.wantedAngleMeasureFront = this.wantedAngleMeasureFront_config.getValue();
    }
}