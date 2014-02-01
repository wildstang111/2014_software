/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems.arm;

/**
 *
 * @author Jason
 */
public class ArmPreset {
    
    int wantedAngleMeasureFront, wantedAngleMeasureBack;     
    
    public ArmPreset(int wantedAngleMeasureFront, int wantedAngleMeasureBack){
        
        this.wantedAngleMeasureBack = wantedAngleMeasureBack;
        this.wantedAngleMeasureFront = wantedAngleMeasureFront;
    }
    
    public int getwantedAngleMeasureFront(){
        return wantedAngleMeasureFront;
    }
    
    public int getwantedAngleMeasureBack(){
        return wantedAngleMeasureBack;
    
    }
}