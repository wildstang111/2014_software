/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems.arm;

import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.outputmanager.base.OutputManager;
import com.wildstangs.pid.controller.base.PidController;
import com.wildstangs.pid.inputs.ArmPotPidInput;
import com.wildstangs.pid.outputs.ArmVictorPidOutput;
/**
 *
 * @author Joey
 */
public class Arm
{
    protected static final IntegerConfigFileParameter HIGH_BOUND_CONFIG = new IntegerConfigFileParameter(Arm.class.getName(), "HighAngle", 359);
    protected static final IntegerConfigFileParameter LOW_BOUND_CONFIG = new IntegerConfigFileParameter(Arm.class.getName(), "LowAngle", 0);
    protected static int highBound = HIGH_BOUND_CONFIG.getValue(), lowBound = LOW_BOUND_CONFIG.getValue();
    
    protected final DoubleConfigFileParameter TOP_VOLTAGE_VALUE_CONFIG, BOTTOM_VOLTAGE_VALUE_CONFIG, ROLLER_FORWARD_SPEED_CONFIG, ROLLER_REVERSE_SPEED_CONFIG;
    protected final int VICTOR_ANGLE_INDEX, ARM_ROLLER_VICTOR_INDEX, POT_INDEX;
    
    protected int currentAngle = 0, wantedAngle = 0;
    protected PidController pid;
    protected ArmPotPidInput pidInput;
    protected ArmRollerEnum rollerValue = ArmRollerEnum.OFF;
    protected double rollerReverseSpeed;
    protected double rollerForwardSpeed;
            
    
    
    //The 'front' boolean is for String differentiation ONLY
    protected boolean front;
    
    public Arm(int victorAngleIndex, int armRollerVictorIndex, int potIndex, boolean front)
    {
        this.VICTOR_ANGLE_INDEX = victorAngleIndex;
        this.ARM_ROLLER_VICTOR_INDEX = armRollerVictorIndex;
        this.POT_INDEX = potIndex;
        
        TOP_VOLTAGE_VALUE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "TopVoltageValue-" + (front ? "Front" : "Back"), 5.1);
        BOTTOM_VOLTAGE_VALUE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "BottomVoltageValue-" + (front ? "Front" : "Back"), 0.0);
        ROLLER_FORWARD_SPEED_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "RollerForwardSpeed-" + (front ? "Front" : "Back"), 0.5);
        ROLLER_REVERSE_SPEED_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "RollerReverseSpeed-" + (front ? "Front" : "Back"), -0.5);
        rollerForwardSpeed = ROLLER_FORWARD_SPEED_CONFIG.getValue();
        rollerReverseSpeed = ROLLER_REVERSE_SPEED_CONFIG.getValue();
        
        
        this.front = front;
        this.pidInput = new ArmPotPidInput(potIndex, TOP_VOLTAGE_VALUE_CONFIG.getValue(), BOTTOM_VOLTAGE_VALUE_CONFIG.getValue());
        this.pid = new PidController(this.pidInput, new ArmVictorPidOutput(victorAngleIndex), front ? "FrontArmPid" : "BackArmPid");
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
//        if(pid.isEnabled()) return;
        
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
        
        OutputManager.getInstance().getOutput(VICTOR_ANGLE_INDEX).set(new Double(speed));
    }
    
    public double getVictorSpeed()
    {
        return ((Double) OutputManager.getInstance().getOutput(VICTOR_ANGLE_INDEX).get()).doubleValue();
    }
    
    public void setRoller(ArmRollerEnum value)
    {
        this.rollerValue = value;
    }
    
    public ArmRollerEnum getRollerValue()
    {
        return this.rollerValue;
    }
    
    public void init(){
    rollerValue = ArmRollerEnum.OFF;
    OutputManager.getInstance().getOutput(ARM_ROLLER_VICTOR_INDEX).set(new Double(0.0));
    setToAngle(0);
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
        if(rollerValue == ArmRollerEnum.FORWARD){ 
            OutputManager.getInstance().getOutput(ARM_ROLLER_VICTOR_INDEX).set(new Double(rollerForwardSpeed));
        }
        else if(rollerValue == ArmRollerEnum.OFF){
            OutputManager.getInstance().getOutput(ARM_ROLLER_VICTOR_INDEX).set(new Double(0.0));
        }  
        else if(rollerValue == ArmRollerEnum.REVERSE){
            OutputManager.getInstance().getOutput(ARM_ROLLER_VICTOR_INDEX).set(new Double(rollerReverseSpeed));
        }  
    }
    
    public void notifyConfigChange()
    {
        pidInput.setVoltageValues(TOP_VOLTAGE_VALUE_CONFIG.getValue(), BOTTOM_VOLTAGE_VALUE_CONFIG.getValue());
        rollerForwardSpeed = ROLLER_FORWARD_SPEED_CONFIG.getValue();
        rollerReverseSpeed = ROLLER_REVERSE_SPEED_CONFIG.getValue();
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

    public double getPotVoltage()
    {
        return ((Double) InputManager.getInstance().getSensorInput(this.POT_INDEX).get()).doubleValue();
    }
}
