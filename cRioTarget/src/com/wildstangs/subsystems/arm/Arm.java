/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems.arm;

import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.outputmanager.base.WsOutputManager;
import com.wildstangs.pid.controller.base.WsPidController;
import com.wildstangs.pid.inputs.ArmPotPidInput;
import com.wildstangs.pid.outputs.ArmVictorPidOutput;
import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author Joey
 */
public class Arm
{
    protected static final int HIGH_BOUND = 359, LOW_BOUND = 0;
    
    protected final DoubleConfigFileParameter TOP_VOLTAGE_VALUE_CONFIG, BOTTOM_VOLTAGE_VALUE_CONFIG;
    protected final int VICTOR_INDEX, RELAY_INDEX, POT_INDEX;
    
    protected int currentAngle = 0, wantedAngle = 0;
    protected WsPidController pid;
    protected ArmPotPidInput pidInput;
    protected Relay.Value rollerValue = Relay.Value.kOff;
    
    //The 'front' boolean is for String differentiation ONLY
    protected boolean front;
    
    public Arm(int victorIndex, int relayIndex, int potIndex, boolean front)
    {
        this.VICTOR_INDEX = victorIndex;
        this.RELAY_INDEX = relayIndex;
        this.POT_INDEX = potIndex;
        
        TOP_VOLTAGE_VALUE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "TopVoltageValue-" + (front ? "Front" : "Back"), 10.0);
        BOTTOM_VOLTAGE_VALUE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "BottomVoltageValue-" + (front ? "Front" : "Back"), 0.0);
        
        this.front = front;
        this.pidInput = new ArmPotPidInput(potIndex, TOP_VOLTAGE_VALUE_CONFIG.getValue(), BOTTOM_VOLTAGE_VALUE_CONFIG.getValue());
        this.pid = new WsPidController(this.pidInput, new ArmVictorPidOutput(victorIndex), front ? "FrontArmPid" : "BackArmPid");
    }
    
    public void setToAngle(int angle)
    {
        if(angle < LOW_BOUND) angle = LOW_BOUND;
        else if(angle > HIGH_BOUND) angle = HIGH_BOUND;
        
        this.wantedAngle = angle;
        pid.setSetPoint(this.wantedAngle);
    }
    
    public double getCurrentAngle()
    {
        return this.pidInput.pidRead();
    }
    
    public int getWantedAngle()
    {
        return this.wantedAngle;
    }
    
    public void setRoller(Relay.Value value)
    {
        this.rollerValue = value;
        WsOutputManager.getInstance().getOutput(this.RELAY_INDEX).set((IOutputEnum) null, rollerValue);
    }
    
    public Relay.Value getRollerValue()
    {
        return this.rollerValue;
    }
    
    public void update()
    {
        pid.calcPid();
    }
    
    public void notifyConfigChange()
    {
        pidInput.setVoltageValues(TOP_VOLTAGE_VALUE_CONFIG.getValue(), BOTTOM_VOLTAGE_VALUE_CONFIG.getValue());
    }
}
