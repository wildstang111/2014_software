package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.inputmanager.inputs.joystick.WsJoystickButtonEnum;
import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.outputmanager.base.WsOutputManager;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Joshua
 */
public class WsTurret extends WsSubsystem implements IObserver {

    private boolean leftButton = false;
    private boolean rightButton = false;
    
    public WsTurret(String name) {
        super(name);
        registerForJoystickButtonNotification(WsJoystickButtonEnum.MANIPULATOR_BUTTON_11); 
        registerForJoystickButtonNotification(WsJoystickButtonEnum.MANIPULATOR_BUTTON_12); 
    }

    public void init() {
        leftButton = false; 
        rightButton = false; 
    }

    public void update() {
        double motorOutput = 0.0; 
        if ( leftButton){
            motorOutput = 0.5; 
        } else if (rightButton){
            motorOutput = -0.5; 
            
        }
        WsOutputManager.getInstance().getOutput(WsOutputManager.TURRET).set((IOutputEnum) null, new Double(motorOutput));

        SmartDashboard.putBoolean("Left Turret Button", leftButton);
        SmartDashboard.putBoolean("Right Turret Button", rightButton);
        SmartDashboard.putNumber("Turret output", motorOutput);
    }

    public void notifyConfigChange() {
    }

    public void acceptNotification(Subject subjectThatCaused) {
        if (subjectThatCaused.getType() == WsJoystickButtonEnum.MANIPULATOR_BUTTON_12) {
            BooleanSubject button = (BooleanSubject) subjectThatCaused;
            rightButton = button.getValue(); 
        } else if (subjectThatCaused.getType() == WsJoystickButtonEnum.MANIPULATOR_BUTTON_11) {
            BooleanSubject button = (BooleanSubject) subjectThatCaused;
            leftButton = button.getValue();
        }
    }

}