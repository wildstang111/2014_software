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
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Random;
/**
 *
 * @author Joey
 */
public class Arm
{
    protected static final Random RND = new Random();
    
    protected static final BooleanConfigFileParameter DISABLE_PID_COMPLETELY_CONFIG = new BooleanConfigFileParameter(Arm.class.getName(), "DisablePIDCompletely", false);
    protected static final BooleanConfigFileParameter DISABLE_PID_INIT_CONFIG = new BooleanConfigFileParameter(Arm.class.getName(), "DisablePIDInInit", false);
    protected static final DoubleConfigFileParameter ARM_VICTOR_SPEED_DILUTION_CONFIG = new DoubleConfigFileParameter(Arm.class.getName(), "ArmVictorSpeedDilution", 0.5);
    protected static boolean disablePidCompletely = DISABLE_PID_COMPLETELY_CONFIG.getValue(), diablePidInit = DISABLE_PID_INIT_CONFIG.getValue();
    protected static double armVictorSpeedDilution = ARM_VICTOR_SPEED_DILUTION_CONFIG.getValue();
    
    protected final DoubleConfigFileParameter HARD_TOP_VOLTAGE_VALUE_CONFIG, HARD_BOTTOM_VOLTAGE_VALUE_CONFIG;
    protected final DoubleConfigFileParameter TOP_VOLTAGE_VALUE_CONFIG, BOTTOM_VOLTAGE_VALUE_CONFIG, ROLLER_FORWARD_SPEED_CONFIG, ROLLER_REVERSE_SPEED_CONFIG;
    protected final IntegerConfigFileParameter HIGH_BOUND_CONFIG, LOW_BOUND_CONFIG;
    protected final int VICTOR_ANGLE_INDEX, ARM_ROLLER_VICTOR_INDEX, POT_INDEX;
    
    protected double hardTopVoltage, hardBottomVoltage;
    protected int highBound, lowBound;
    protected int currentAngle = 0, wantedAngle = 0;
    protected PidController pid;
    protected ArmPotPidInput pidInput;
    protected ArmRollerEnum rollerValue = ArmRollerEnum.OFF;
    protected double rollerReverseSpeed;
    protected double rollerForwardSpeed;
    protected double topVoltage;
    protected double bottomVoltage;
    protected boolean forceOverrideToManualFlag;
    protected double armVictorSpeed = 0.0;
    
    //The 'front' boolean is for String differentiation ONLY
    protected boolean front;
    
    public Arm(int victorAngleIndex, int armRollerVictorIndex, int potIndex, boolean front)
    {
        this.VICTOR_ANGLE_INDEX = victorAngleIndex;
        this.ARM_ROLLER_VICTOR_INDEX = armRollerVictorIndex;
        this.POT_INDEX = potIndex;
        
        HARD_TOP_VOLTAGE_VALUE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), (front ? "Front" : "Back") + ".HardTopVoltageValue", 4.8);
        HARD_BOTTOM_VOLTAGE_VALUE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), (front ? "Front" : "Back") + ".HardBottomVoltageValue", 0.2);
        
        TOP_VOLTAGE_VALUE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), (front ? "Front" : "Back") + ".TopVoltageValue", 5.1);
        BOTTOM_VOLTAGE_VALUE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), (front ? "Front" : "Back") + ".BottomVoltageValue", 0.0);
        ROLLER_FORWARD_SPEED_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), (front ? "Front" : "Back") + ".RollerForwardSpeed", 0.5);
        ROLLER_REVERSE_SPEED_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), (front ? "Front" : "Back") + ".RollerReverseSpeed", -0.5);
        HIGH_BOUND_CONFIG = new IntegerConfigFileParameter(Arm.class.getName(), (front ? "Front" : "Back") + ".HighAngle", 359);
        LOW_BOUND_CONFIG = new IntegerConfigFileParameter(Arm.class.getName(), (front ? "Front" : "Back") + ".LowAngle", -20);
        
        rollerForwardSpeed = ROLLER_FORWARD_SPEED_CONFIG.getValue();
        rollerReverseSpeed = ROLLER_REVERSE_SPEED_CONFIG.getValue();
        topVoltage = TOP_VOLTAGE_VALUE_CONFIG.getValue();
        bottomVoltage = BOTTOM_VOLTAGE_VALUE_CONFIG.getValue();
        hardTopVoltage = HARD_TOP_VOLTAGE_VALUE_CONFIG.getValue();
        hardBottomVoltage = HARD_BOTTOM_VOLTAGE_VALUE_CONFIG.getValue();
        
        this.front = front;
        this.pidInput = new ArmPotPidInput(potIndex, topVoltage, bottomVoltage);
        this.pid = new PidController(this.pidInput, new ArmVictorPidOutput(victorAngleIndex), front ? "FrontArmPid" : "BackArmPid");
        this.pid.disable();
        
        
    }
    
    public void setToAngle(int angle)
    {
        if(disablePidCompletely || forceOverrideToManualFlag) 
        {
            return;
        }
        
        double potVoltage = this.getPotVoltage();
        if(potVoltage > hardTopVoltage || potVoltage < hardBottomVoltage)
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
        
        if(!forceOverrideToManualFlag)
        {
            double potVoltage = this.getPotVoltage();

            if((currentAngle <= lowBound || potVoltage < hardBottomVoltage) && speed < 0)
            {
                speed = 0.0;
            }
            else if((currentAngle >= highBound || potVoltage > hardTopVoltage) && speed > 0)
            {
                speed = 0.0;
            }
        }
        armVictorSpeed = speed;
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
        if(!diablePidInit && !disablePidCompletely && !forceOverrideToManualFlag)
        {
            setToAngle(0);
        }
    } 
    
    public void update()
    {
        if(pid.isEnabled()) 
        {
            if(disablePidCompletely || forceOverrideToManualFlag)
            {
                pid.disable();
            }
            pid.calcPid();
        }
        if(pid.isStabilized())
        {
            pid.disable();
        }
        double currentVoltage = this.getPotVoltage();
        
        SmartDashboard.putNumber("Pot Voltage " + (front ? "Front" : "Back"), currentVoltage);
        
        boolean safeVoltage = !(currentVoltage <= 0.6 || currentVoltage >= 4.4);
        
        SmartDashboard.putBoolean((front ? "Front" : "Back") + " Arm Pot at Safe Voltage", safeVoltage ? true : RND.nextInt(2) == 0);
        //The random boolean is so that it hopefully catches someone's attention that the arm pot isn't at a safe position
        
        double victorSpeed = this.getVictorSpeed();
        currentAngle = (int) this.getCurrentAngle();
        
        if(currentAngle <= lowBound && victorSpeed < 0)
        {
            armVictorSpeed = 0.0;
            if(pid.isEnabled()) pid.disable();
        }
        else if(currentAngle >= highBound && victorSpeed > 0)
        {
            armVictorSpeed = 0.0;
            if(pid.isEnabled()) pid.disable();
        }
        
        if(!pid.isEnabled())
        {
            OutputManager.getInstance().getOutput(VICTOR_ANGLE_INDEX).set(new Double(armVictorSpeed));
        }
        
        if(rollerValue == ArmRollerEnum.INTAKE){ 
            OutputManager.getInstance().getOutput(ARM_ROLLER_VICTOR_INDEX).set(new Double(rollerForwardSpeed));
        }
        else if(rollerValue == ArmRollerEnum.OFF){
            OutputManager.getInstance().getOutput(ARM_ROLLER_VICTOR_INDEX).set(new Double(0.0));
        }  
        else if(rollerValue == ArmRollerEnum.OUTPUT){
            OutputManager.getInstance().getOutput(ARM_ROLLER_VICTOR_INDEX).set(new Double(rollerReverseSpeed));
        }  
        
        SmartDashboard.putString(this.pid.getName(), this.pid.getState().toString());
    }
    
    public void notifyConfigChange()
    {
        rollerForwardSpeed = ROLLER_FORWARD_SPEED_CONFIG.getValue();
        rollerReverseSpeed = ROLLER_REVERSE_SPEED_CONFIG.getValue();
        highBound = HIGH_BOUND_CONFIG.getValue();
        lowBound = LOW_BOUND_CONFIG.getValue();
        topVoltage = TOP_VOLTAGE_VALUE_CONFIG.getValue();
        bottomVoltage = BOTTOM_VOLTAGE_VALUE_CONFIG.getValue();
        pidInput.setVoltageValues(topVoltage, bottomVoltage);
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
    
    public void calibrate(boolean high)
    {
        double voltageDifferance = topVoltage - bottomVoltage;
        if(high)
        {
            topVoltage = getPotVoltage();
            bottomVoltage = topVoltage - voltageDifferance;
        }
        else
        {
            bottomVoltage = getPotVoltage();
            topVoltage = bottomVoltage + voltageDifferance;
        }
        
        pidInput.setVoltageValues(topVoltage, bottomVoltage);
    }

    public void acceptNotification(Subject subjectThatCaused)
    {
        this.forceOverrideToManualFlag = ((BooleanSubject) subjectThatCaused).getValue();
    }

    public void forceOverrideToManual(boolean forceOverrideToManualFlag)
    {
        this.forceOverrideToManualFlag = forceOverrideToManualFlag;
        if(this.forceOverrideToManualFlag)
        {
            pid.disable();
        }
    }
}
