package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.inputmanager.inputs.joystick.JoystickAxisEnum;
import com.wildstangs.inputmanager.inputs.joystick.JoystickButtonEnum;
import com.wildstangs.outputmanager.base.OutputManager;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.arm.Arm;
import com.wildstangs.subsystems.arm.ArmPreset;
import com.wildstangs.subsystems.arm.ArmRollerEnum;
import com.wildstangs.subsystems.base.Subsystem;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author mail929
 */
public class BallHandler extends Subsystem implements IObserver {

    public static final ArmPreset DEFAULT_POSITION = new ArmPreset(0, 0, "DefaultPosition");
    
    protected Arm frontArm, backArm;
    protected boolean frontForwardButton = false, frontReverseButton = false;
    protected boolean backForwardButton = false, backReverseButton = false;
    protected double frontArmJoystickValue = 0.0, backArmJoystickValue = 0.0;
    protected double lastValueFront = 0.0, lastValueBack = 0.0;

    public BallHandler(String name) {
        super(name);

        this.frontArm = new Arm(OutputManager.FRONT_ARM_VICTOR_INDEX, OutputManager.FRONT_ARM_ROLLER_VICTOR_INDEX, InputManager.FRONT_ARM_POT_INDEX, true);
        this.backArm = new Arm(OutputManager.BACK_ARM_VICTOR_INDEX, OutputManager.BACK_ARM_ROLLER_VICTOR_INDEX, InputManager.BACK_ARM_POT_INDEX, false);

        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_5);
        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_6);
        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_7);
        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_8);
        
        Subject subject = InputManager.getInstance().getOiInput(InputManager.MANIPULATOR_JOYSTICK_INDEX).getSubject(JoystickAxisEnum.MANIPULATOR_BACK_ARM_CONTROL);
        subject.attach(this);

        subject = InputManager.getInstance().getOiInput(InputManager.MANIPULATOR_JOYSTICK_INDEX).getSubject(JoystickAxisEnum.MANIPULATOR_FRONT_ARM_CONTROL);
        subject.attach(this);
    }

    public void init() {
        
    }

    public void update() {
        //Both pressed or both are not pressed
        if (frontForwardButton == frontReverseButton) {
            frontArm.setRoller(ArmRollerEnum.OFF);
        } else if (frontForwardButton) {
            frontArm.setRoller(ArmRollerEnum.FORWARD);
        } else if (frontReverseButton) {
            frontArm.setRoller(ArmRollerEnum.REVERSE);
        }

        //Both pressed or both are not pressed
        if (backForwardButton == backReverseButton) {
            backArm.setRoller(ArmRollerEnum.OFF);
        } else if (backForwardButton) {
            backArm.setRoller(ArmRollerEnum.FORWARD);
        } else if (backReverseButton) {
            backArm.setRoller(ArmRollerEnum.REVERSE);
        }
        
        frontArm.setVictor(frontArmJoystickValue);
        backArm.setVictor(backArmJoystickValue);

        frontArm.update();
        backArm.update();

        ArmRollerEnum frontValue = frontArm.getRollerValue();
        ArmRollerEnum backValue = backArm.getRollerValue();
        String frontString = frontValue == ArmRollerEnum.FORWARD ? "Forward" : (frontValue == ArmRollerEnum.REVERSE ? "Reverse" : "Off");
        String backString = backValue == ArmRollerEnum.FORWARD ? "Forward" : (backValue == ArmRollerEnum.REVERSE ? "Reverse" : "Off");
        
        double voltageChangeFront = frontArm.getPotVoltage() - lastValueFront;
        double voltageChangeBack = backArm.getPotVoltage() - lastValueBack;
        
        lastValueFront = frontArm.getPotVoltage();
        lastValueBack = backArm.getPotVoltage();
        
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
    }

    public void notifyConfigChange() {
        frontArm.notifyConfigChange();
        backArm.notifyConfigChange();
        Arm.notifyConfigChangeStatic();
    }

    public void acceptNotification(Subject subjectThatCaused) {
        
        if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_5) {
            this.frontForwardButton = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_6) {
            this.backForwardButton = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_7) {
            this.frontReverseButton = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_8) {
            this.backReverseButton = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.getType() == JoystickAxisEnum.MANIPULATOR_FRONT_ARM_CONTROL) {
            this.frontArmJoystickValue = ((DoubleSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.getType() == JoystickAxisEnum.MANIPULATOR_BACK_ARM_CONTROL) {
            this.backArmJoystickValue = ((DoubleSubject) subjectThatCaused).getValue();
        }
    }

    public void setArmPreset(ArmPreset preset) {
        int frontArmPreset = preset.getwantedAngleMeasureFront();
        int backArmPreset = preset.getwantedAngleMeasureBack();
        if (frontArmPreset <= Arm.getHighBound() && frontArmPreset >= Arm.getLowBound()) {
            this.frontArm.setToAngle(frontArmPreset);
        }
        if (backArmPreset <= Arm.getHighBound() && backArmPreset >= Arm.getLowBound()){
            this.backArm.setToAngle(backArmPreset);
        }
    }
    
    public boolean areArmsUsingPidControl()
    {
        return (frontArm.isArmPidActive() && backArm.isArmPidActive());
    }
}
