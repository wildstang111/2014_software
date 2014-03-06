package com.wildstangs.subsystems;

import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.inputmanager.inputs.joystick.JoystickAxisEnum;
import com.wildstangs.inputmanager.inputs.joystick.JoystickButtonEnum;
import com.wildstangs.inputmanager.inputs.joystick.JoystickDPadButtonEnum;
import com.wildstangs.list.WsList;
import com.wildstangs.outputmanager.base.OutputManager;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subjects.debouncer.Debouncer;
import com.wildstangs.subsystems.arm.Arm;
import com.wildstangs.subsystems.arm.ArmPreset;
import com.wildstangs.subsystems.arm.ArmRollerEnum;
import com.wildstangs.subsystems.base.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author mail929
 */
public class BallHandler extends Subsystem implements IObserver {

    
    public static WsList presets = new WsList(10);
    public static final ArmPreset DEFAULT_POSITION = new ArmPreset(180, 180, "DefaultPosition");
    public static final ArmPreset CATAPULT_TENSION_PRESET_BACK = new ArmPreset(ArmPreset.IGNORE_VALUE, 160, "ShootPresetBackOnly");
    public static final ArmPreset CATAPULT_TENSION_PRESET_BOTH = new ArmPreset(10, 160, "ShootPresetBothArms");
    
    protected final DoubleConfigFileParameter DEADBAND_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "ArmControlDeadband", 0.05);
    protected final DoubleConfigFileParameter SAFE_FRONT_ARM_SHOOT_TOP_BOUND_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "SafeFrontArmShoot.TopBound", 180);
    protected final DoubleConfigFileParameter SAFE_FRONT_ARM_SHOOT_BOTTOM_BOUND_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "SafeFrontArmShoot.BottomBound", 140);
    protected final BooleanConfigFileParameter DISABLE_CALIBRATION_SWITCHES_CONFIG = new BooleanConfigFileParameter(this.getClass().getName(), "DisableCalibrationSwitches", false);
    
    protected boolean ohShitFlag = false;
    protected boolean forceOverrideToManualFlag = false;
    
    protected Arm frontArm, backArm;
    protected boolean disableCalibrationSwitches = false;
    protected boolean frontIntakeButton = false, frontOutputButton = false;
    protected boolean backIntakeButton = false, backOutputButton = false;
    protected double deadband = 0.05;
    protected double frontArmJoystickValue = 0.0, backArmJoystickValue = 0.0;
    protected double safeFrontArmShootTopBound = 180, safeFrontArmShootBottomBound = 140;
    protected Debouncer frontArmCalibrationDebouncer, backArmCalibrationDebouncer;

    public BallHandler(String name) {
        super(name);

        this.frontArm = new Arm(OutputManager.FRONT_ARM_VICTOR_INDEX, OutputManager.FRONT_ARM_ROLLER_VICTOR_INDEX, InputManager.FRONT_ARM_POT_INDEX, true);
        this.backArm = new Arm(OutputManager.BACK_ARM_VICTOR_INDEX, OutputManager.BACK_ARM_ROLLER_VICTOR_INDEX, InputManager.BACK_ARM_POT_INDEX, false);
        
        this.frontArmCalibrationDebouncer = new Debouncer(60, new Boolean(false));
        this.backArmCalibrationDebouncer = new Debouncer(60, new Boolean(false));
        
        this.deadband = DEADBAND_CONFIG.getValue();
        this.disableCalibrationSwitches = DISABLE_CALIBRATION_SWITCHES_CONFIG.getValue();
        this.safeFrontArmShootBottomBound = SAFE_FRONT_ARM_SHOOT_BOTTOM_BOUND_CONFIG.getValue();
        this.safeFrontArmShootTopBound = SAFE_FRONT_ARM_SHOOT_TOP_BOUND_CONFIG.getValue();
        
        this.frontArmCalibrationDebouncer.attach(this);
        this.backArmCalibrationDebouncer.attach(this);
        
        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_5);
        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_6);
        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_7);
        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_8);
        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_9);
        
        //"Oh Shit" button for the driver
        registerForJoystickButtonNotification(JoystickButtonEnum.DRIVER_BUTTON_10);
        
        Subject subject = InputManager.getInstance().getOiInput(InputManager.MANIPULATOR_JOYSTICK_INDEX).getSubject(JoystickAxisEnum.MANIPULATOR_LEFT_JOYSTICK_X);
        subject.attach(this);

        subject = InputManager.getInstance().getOiInput(InputManager.MANIPULATOR_JOYSTICK_INDEX).getSubject(JoystickAxisEnum.MANIPULATOR_RIGHT_JOYSTICK_X);
        subject.attach(this);
        
//        subject = InputManager.getInstance().getOiInput(InputManager.ARM_FORCE_OVERRIDE_TO_MANUAL_SWITCH_INDEX).getSubject();
//        subject.attach(this);
        
        registerForSensorNotification(InputManager.FRONT_ARM_CALIBRATION_SWITCH_INDEX);
        registerForSensorNotification(InputManager.BACK_ARM_CALIBRATION_SWITCH_INDEX);
    }

    public void init() {
        frontIntakeButton = false;
        frontOutputButton = false;
        backIntakeButton = false;
        backOutputButton = false;
        frontArmJoystickValue = 0.0;
        backArmJoystickValue = 0.0;
        frontArm.init();
        backArm.init();
        ohShitFlag = false;
        forceOverrideToManualFlag = false;
    }

    public void update() {
        if(!ohShitFlag)
        {
            //Both pressed or both are not pressed
            if (frontIntakeButton == frontOutputButton) {
                frontArm.setRoller(ArmRollerEnum.OFF);
            } else if (frontIntakeButton) {
                frontArm.setRoller(ArmRollerEnum.INTAKE);
            } else if (frontOutputButton) {
                frontArm.setRoller(ArmRollerEnum.OUTPUT);
            }

            //Both pressed or both are not pressed
            if (backIntakeButton == backOutputButton) {
                backArm.setRoller(ArmRollerEnum.OFF);
            } else if (backIntakeButton) {
                backArm.setRoller(ArmRollerEnum.INTAKE);
            } else if (backOutputButton) {
                backArm.setRoller(ArmRollerEnum.OUTPUT);
            }
            
            frontArm.setVictor(Math.abs(frontArmJoystickValue) <= deadband ? 0 : frontArmJoystickValue);
            backArm.setVictor(Math.abs(backArmJoystickValue) <= deadband ? 0 : backArmJoystickValue);
        }
        else
        {
            if(this.areArmsUsingPidControl())
            {
                backArm.setRoller(ArmRollerEnum.INTAKE);
                frontArm.setRoller(ArmRollerEnum.INTAKE);
            }
            else
            {
                backArm.setRoller(ArmRollerEnum.OFF);
                frontArm.setRoller(ArmRollerEnum.OFF);
            }
        }

        frontArm.update();
        backArm.update();

        ArmRollerEnum frontValue = frontArm.getRollerValue();
        ArmRollerEnum backValue = backArm.getRollerValue();
        String frontString = frontValue == ArmRollerEnum.INTAKE ? "Intake" : (frontValue == ArmRollerEnum.OUTPUT ? "Output" : "Off");
        String backString = backValue == ArmRollerEnum.INTAKE ? "Intake" : (backValue == ArmRollerEnum.OUTPUT ? "Output" : "Off");
        
        if(!disableCalibrationSwitches)
        {
            this.frontArmCalibrationDebouncer.update(InputManager.getInstance().getSensorInput(InputManager.FRONT_ARM_CALIBRATION_SWITCH_INDEX).get());
            this.backArmCalibrationDebouncer.update(InputManager.getInstance().getSensorInput(InputManager.BACK_ARM_CALIBRATION_SWITCH_INDEX).get());
        }
        
        SmartDashboard.putNumber("Current Front Arm Angle", frontArm.getCurrentAngle());
        SmartDashboard.putNumber("Wanted Front Arm Angle", frontArm.getWantedAngle());
        SmartDashboard.putNumber("Current Back Arm Angle", backArm.getCurrentAngle());
        SmartDashboard.putNumber("Wanted Back Arm Angle", backArm.getWantedAngle());
        SmartDashboard.putString("Front Arm Roller", frontString);
        SmartDashboard.putString("Back Arm Roller", backString);
        SmartDashboard.putNumber("Front Arm Victor", frontArm.getVictorSpeed());
        SmartDashboard.putNumber("Back Arm Victor", backArm.getVictorSpeed());
        SmartDashboard.putNumber("Front Arm Joystick", frontArmJoystickValue);
        SmartDashboard.putNumber("Back Arm Joystick", backArmJoystickValue);
        SmartDashboard.putBoolean("Front Arm At Zero", ((Boolean) this.frontArmCalibrationDebouncer.getDebouncedValue()).booleanValue());
        SmartDashboard.putBoolean("Back Arm At Zero", ((Boolean) this.backArmCalibrationDebouncer.getDebouncedValue()).booleanValue());
    }

    public void notifyConfigChange() {
        Arm.notifyConfigChangeStatic();
        frontArm.notifyConfigChange();
        backArm.notifyConfigChange();
        for (int i = 0; i < presets.size(); i++) {
            ArmPreset preset = (ArmPreset) presets.get(i);
            if(preset != null) preset.notifyConfigChange();
        }
        this.deadband = DEADBAND_CONFIG.getValue();
        this.disableCalibrationSwitches = DISABLE_CALIBRATION_SWITCHES_CONFIG.getValue();
        this.safeFrontArmShootBottomBound = SAFE_FRONT_ARM_SHOOT_BOTTOM_BOUND_CONFIG.getValue();
        this.safeFrontArmShootTopBound = SAFE_FRONT_ARM_SHOOT_TOP_BOUND_CONFIG.getValue();
    }

    public void acceptNotification(Subject subjectThatCaused)
    {
        if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_5)
        {
            this.frontIntakeButton = ((BooleanSubject) subjectThatCaused).getValue();
        }
        else if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_6)
        {
            this.backIntakeButton = ((BooleanSubject) subjectThatCaused).getValue();
        }
        else if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_7)
        {
            this.frontOutputButton = ((BooleanSubject) subjectThatCaused).getValue();
        }
        else if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_8)
        {
            this.backOutputButton = ((BooleanSubject) subjectThatCaused).getValue();
        }
        else if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_9)
        {
            double frontArmAngle = frontArm.getCurrentAngle();
            if (frontArmAngle <= this.safeFrontArmShootBottomBound || frontArmAngle >= this.safeFrontArmShootTopBound)
            {
                this.setArmPreset(CATAPULT_TENSION_PRESET_BOTH);
            }
            else
            {
                this.setArmPreset(CATAPULT_TENSION_PRESET_BACK);
            }
        }
        else if (subjectThatCaused.getType() == JoystickAxisEnum.MANIPULATOR_LEFT_JOYSTICK_X)
        {
            this.frontArmJoystickValue = ((DoubleSubject) subjectThatCaused).getValue();
        }
        else if (subjectThatCaused.getType() == JoystickAxisEnum.MANIPULATOR_RIGHT_JOYSTICK_X)
        {
            this.backArmJoystickValue = ((DoubleSubject) subjectThatCaused).getValue() * -1;
        }
        else if(subjectThatCaused.getType() == JoystickButtonEnum.DRIVER_BUTTON_10)
        {
            if(((BooleanSubject) subjectThatCaused).getValue())
            {
                ohShitFlag = !ohShitFlag;
                if(ohShitFlag)
                {
                    frontArm.setVictor(0.0);
                    backArm.setVictor(0.0);
                    this.setArmPreset(DEFAULT_POSITION);
                }
            }
        }
        else if (subjectThatCaused == this.frontArmCalibrationDebouncer && !disableCalibrationSwitches)
        {
            if (((Boolean) this.frontArmCalibrationDebouncer.getDebouncedValue()).booleanValue())
            {
                System.out.println("Calibrating Front Arm Now");
                frontArm.calibrate(true);
            }
        }
        else if (subjectThatCaused == this.backArmCalibrationDebouncer && !disableCalibrationSwitches)
        {
            if (((Boolean) this.backArmCalibrationDebouncer.getDebouncedValue()).booleanValue())
            {
                System.out.println("Calibrating Back Arm Now");
                backArm.calibrate(true);
            }
        }
//        else if(subjectThatCaused == InputManager.getInstance().getOiInput(InputManager.ARM_FORCE_OVERRIDE_TO_MANUAL_SWITCH_INDEX).getSubject())
//        {
//            forceOverrideToManualFlag = ((BooleanSubject) subjectThatCaused).getValue();
//            frontArm.forceOverrideToManual(forceOverrideToManualFlag);
//            backArm.forceOverrideToManual(forceOverrideToManualFlag);
//        }
    }

    public void setArmPreset(ArmPreset preset) {
        
        if(ohShitFlag && preset != DEFAULT_POSITION)
        {
            return;
        }
        
        int frontArmPreset = preset.getwantedAngleMeasureFront();
        int backArmPreset = preset.getwantedAngleMeasureBack();
        if (frontArmPreset <= frontArm.getHighBound() && frontArmPreset >= frontArm.getLowBound()) {
            this.frontArm.setToAngle(frontArmPreset);
        }
        if (backArmPreset <= backArm.getHighBound() && backArmPreset >= backArm.getLowBound()){
            this.backArm.setToAngle(backArmPreset);
        }
    }
    
    public boolean areArmsUsingPidControl()
    {
        return (frontArm.isArmPidActive() || backArm.isArmPidActive());
    }
    
    public void setFrontArmAccumulator(ArmRollerEnum state)
    {
        frontArm.setRoller(state);
        if(state == ArmRollerEnum.INTAKE)
        {
            frontIntakeButton = true;
            frontOutputButton = false;
        }
        else if(state == ArmRollerEnum.OUTPUT)
        {
            frontIntakeButton = false;
            frontOutputButton = true;
        }
        else if(state == ArmRollerEnum.OFF)
        {
            frontIntakeButton = false;
            frontOutputButton = false;
        }
    }
    
    public void setBackArmAccumulator(ArmRollerEnum state)
    {
        backArm.setRoller(state);
        if(state == ArmRollerEnum.INTAKE)
        {
            backIntakeButton = true;
            backOutputButton = false;
        }
        else if(state == ArmRollerEnum.OUTPUT)
        {
            backIntakeButton = false;
            backOutputButton = true;
        }
        else if(state == ArmRollerEnum.OFF)
        {
            backIntakeButton = false;
            backOutputButton = false;
        }
    }
}
