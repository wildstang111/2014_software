/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems.arm;

import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
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
    protected static final IntegerConfigFileParameter HIGH_BOUND_CONFIG = new IntegerConfigFileParameter(Arm.class.getName(), "HighAngle", 359);
    protected static final IntegerConfigFileParameter LOW_BOUND_CONFIG = new IntegerConfigFileParameter(Arm.class.getName(), "LowAngle", 0);
    protected static int highBound = HIGH_BOUND_CONFIG.getValue(), lowBound = LOW_BOUND_CONFIG.getValue();
    
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
        
        TOP_VOLTAGE_VALUE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "TopVoltageValue-" + (front ? "Front" : "Back"), 5.1);
        BOTTOM_VOLTAGE_VALUE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "BottomVoltageValue-" + (front ? "Front" : "Back"), 0.0);
        
        this.front = front;
        this.pidInput = new ArmPotPidInput(potIndex, TOP_VOLTAGE_VALUE_CONFIG.getValue(), BOTTOM_VOLTAGE_VALUE_CONFIG.getValue());
        this.pid = new WsPidController(this.pidInput, new ArmVictorPidOutput(victorIndex), front ? "FrontArmPid" : "BackArmPid");
        this.pid.disable();
    }
    
    public void setToAngle(int angle)
    {
        if(angle < lowBound) angle = lowBound;
        else if(angle > highBound) angle = highBound;
        
        this.wantedAngle = angle;
        pid.setSetPoint(this.wantedAngle);
        if(!pid.isEnabled()) pid.enable();
    }
    
    public double getCurrentAngle()
    {
        return this.pidInput.pidRead();
    }
    
    public int getWantedAngle()
    {
        return this.wantedAngle;
    }
    
    public void setVictor(double speed)
    {
        if(pid.isEnabled()) return;
        
        speed *= .25;
        
        if(speed < -1.0) speed = -1.0;
        else if(speed > 1.0) speed = 1.0;
        
        double currentAngle = this.getCurrentAngle();
        
        if(currentAngle <= lowBound && speed < 0)
        {
            speed = 0.0;
        }
        else if(currentAngle >= highBound && speed > 0)
        {
            speed = 0.0;
        }
        
        WsOutputManager.getInstance().getOutput(VICTOR_INDEX).set(new Double(speed));
    }
    
    public double getVictorSpeed()
    {
        return ((Double) WsOutputManager.getInstance().getOutput(VICTOR_INDEX).get()).doubleValue();
    }
    
    public void setRoller(Relay.Value value)
    {
        this.rollerValue = value;
        WsOutputManager.getInstance().getOutput(this.RELAY_INDEX).set(rollerValue);
    }
    
    public Relay.Value getRollerValue()
    {
        return this.rollerValue;
    }
    
    public void update()
    {
        if(pid.isEnabled()) pid.calcPid();
        if(pid.isOnTarget()) pid.disable();
        
        double victorSpeed = this.getVictorSpeed();
        double currentAngle = this.getCurrentAngle();
        
        if(currentAngle <= lowBound && victorSpeed < 0)
        {
            this.setVictor(0.0);
            if(pid.isEnabled()) pid.disable();
        }
        else if(currentAngle >= highBound && victorSpeed > 0)
        {
            this.setVictor(0.0);
            if(pid.isEnabled()) pid.disable();
        }
    }
    
    public void notifyConfigChange()
    {
        pidInput.setVoltageValues(TOP_VOLTAGE_VALUE_CONFIG.getValue(), BOTTOM_VOLTAGE_VALUE_CONFIG.getValue());
    }
    
    public static void notifyConfigChangeStatic()
    {
        highBound = HIGH_BOUND_CONFIG.getValue();
        lowBound = LOW_BOUND_CONFIG.getValue();
    }
    
    public static int getHighBound()
    {
        return highBound;
    }
    
    public static int getLowBound()
    {
        return lowBound;
    }
    
    public boolean isArmPidActive()
    {
        return pid.isEnabled();
    }
}
