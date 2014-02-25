/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems.arm;

import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.outputmanager.base.OutputManager;
import com.wildstangs.pid.controller.base.PidController;
import com.wildstangs.pid.inputs.ArmPotPidInput;
import com.wildstangs.pid.outputs.ArmVictorPidOutput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 *
 * @author Joey
 */
public class Arm
{
    protected static final BooleanConfigFileParameter DISABLE_PID_COMPLETELY_CONFIG = new BooleanConfigFileParameter(Arm.class.getName(), "DisablePIDCompletely", false);
    protected static final BooleanConfigFileParameter DISABLE_PID_INIT_CONFIG = new BooleanConfigFileParameter(Arm.class.getName(), "DisablePIDInInit", false);
    protected static final DoubleConfigFileParameter ARM_VICTOR_SPEED_DILUTION_CONFIG = new DoubleConfigFileParameter(Arm.class.getName(), "ArmVictorSpeedDilution", 0.5);
    protected static boolean disablePidCompletely = DISABLE_PID_COMPLETELY_CONFIG.getValue(), diablePidInit = DISABLE_PID_INIT_CONFIG.getValue();
    protected static double armVictorSpeedDilution = ARM_VICTOR_SPEED_DILUTION_CONFIG.getValue();
    
    protected final DoubleConfigFileParameter TOP_VOLTAGE_VALUE_CONFIG, BOTTOM_VOLTAGE_VALUE_CONFIG, ROLLER_FORWARD_SPEED_CONFIG, ROLLER_REVERSE_SPEED_CONFIG;
    protected final IntegerConfigFileParameter HIGH_BOUND_CONFIG, LOW_BOUND_CONFIG;
    protected final int VICTOR_ANGLE_INDEX, ARM_ROLLER_VICTOR_INDEX, POT_INDEX;
    
    protected int highBound, lowBound;
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
        
        TOP_VOLTAGE_VALUE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), (front ? "Front" : "Back") + ".TopVoltageValue", 5.1);
        BOTTOM_VOLTAGE_VALUE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), (front ? "Front" : "Back") + ".BottomVoltageValue", 0.0);
        ROLLER_FORWARD_SPEED_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), (front ? "Front" : "Back") + ".RollerForwardSpeed", 0.5);
        ROLLER_REVERSE_SPEED_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), (front ? "Front" : "Back") + ".RollerReverseSpeed", -0.5);
        HIGH_BOUND_CONFIG = new IntegerConfigFileParameter(Arm.class.getName(), (front ? "Front" : "Back") + ".HighAngle", 359);
        LOW_BOUND_CONFIG = new IntegerConfigFileParameter(Arm.class.getName(), (front ? "Front" : "Back") + ".LowAngle", -20);
        rollerForwardSpeed = ROLLER_FORWARD_SPEED_CONFIG.getValue();
        rollerReverseSpeed = ROLLER_REVERSE_SPEED_CONFIG.getValue();
        
        
        this.front = front;
        this.pidInput = new ArmPotPidInput(potIndex, TOP_VOLTAGE_VALUE_CONFIG.getValue(), BOTTOM_VOLTAGE_VALUE_CONFIG.getValue(), highBound, lowBound);
        this.pid = new PidController(this.pidInput, new ArmVictorPidOutput(victorAngleIndex), front ? "FrontArmPid" : "BackArmPid");
        this.pid.disable();
    }
    
    public void setToAngle(int angle)
    {
        if(disablePidCompletely) 
        {
            return;
        }
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
        speed *= armVictorSpeedDilution;
        
        if(speed < -1.0) speed = -1.0;
        else if(speed > 1.0) speed = 1.0;
        
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
    
    public void init()
    {
        rollerValue = ArmRollerEnum.OFF;
        OutputManager.getInstance().getOutput(ARM_ROLLER_VICTOR_INDEX).set(new Double(0.0));
        pid.disable();
        if(!diablePidInit && !disablePidCompletely)
        {
            setToAngle(0);
        }
    } 
    
    public void update()
    {
        if(pid.isEnabled()) 
        {
            if(disablePidCompletely)
            {
                pid.disable();
            }
            pid.calcPid();
        }
        if(pid.isStabilized())
        {
            pid.disable();
        }
        double currentVoltage = ((Double) InputManager.getInstance().getSensorInput(POT_INDEX).get()).doubleValue();
        
        SmartDashboard.putNumber("Pot Voltage " + (front ? "Front" : "Back"), currentVoltage);
        
        double victorSpeed = this.getVictorSpeed();
        currentAngle = (int) this.getCurrentAngle();
        
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
        if(rollerValue == ArmRollerEnum.INTAKE){ 
            OutputManager.getInstance().getOutput(ARM_ROLLER_VICTOR_INDEX).set(new Double(rollerReverseSpeed));
        }
        else if(rollerValue == ArmRollerEnum.OFF){
            OutputManager.getInstance().getOutput(ARM_ROLLER_VICTOR_INDEX).set(new Double(0.0));
        }  
        else if(rollerValue == ArmRollerEnum.OUTPUT){
            OutputManager.getInstance().getOutput(ARM_ROLLER_VICTOR_INDEX).set(new Double(rollerForwardSpeed));
        }  
        
        SmartDashboard.putString(this.pid.getName(), this.pid.getState().toString());
    }
    
    public void notifyConfigChange()
    {
        rollerForwardSpeed = ROLLER_FORWARD_SPEED_CONFIG.getValue();
        rollerReverseSpeed = ROLLER_REVERSE_SPEED_CONFIG.getValue();
        highBound = HIGH_BOUND_CONFIG.getValue();
        lowBound = LOW_BOUND_CONFIG.getValue();
        pidInput.setAngleBounds(lowBound, highBound);
        pidInput.setVoltageValues(TOP_VOLTAGE_VALUE_CONFIG.getValue(), BOTTOM_VOLTAGE_VALUE_CONFIG.getValue());
        pid.notifyConfigChange();
    }
    
    public static void notifyConfigChangeStatic()
    {
        disablePidCompletely = DISABLE_PID_COMPLETELY_CONFIG.getValue();
        diablePidInit = DISABLE_PID_INIT_CONFIG.getValue();
        armVictorSpeedDilution = ARM_VICTOR_SPEED_DILUTION_CONFIG.getValue();
    }
    
    public int getHighBound()
    {
        return highBound;
    }
    
    public int getLowBound()
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
