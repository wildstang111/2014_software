package com.wildstangs.subsystems;

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
public class WsClimber extends WsSubsystem implements IObserver {

    private static final boolean CLIMB_DEFAULT_VALUE = false;
    private boolean climbState;

    public WsClimber(String name) {
        super(name);
        registerForJoystickButtonNotification(WsJoystickButtonEnum.DRIVER_BUTTON_2);
    }

    public void init() {
        climbState = CLIMB_DEFAULT_VALUE;
    }

    public void update() {
        WsOutputManager.getInstance().getOutput(WsOutputManager.CLIMBER).set((IOutputEnum) null, new Boolean(climbState));

        SmartDashboard.putBoolean("Climb State", climbState);
    }

    public void notifyConfigChange() {
    }

    public void acceptNotification(Subject subjectThatCaused) {
        if (subjectThatCaused.getType() == WsJoystickButtonEnum.DRIVER_BUTTON_2) {
            BooleanSubject button = (BooleanSubject) subjectThatCaused;
            if (true == button.getValue() && false == button.getPreviousValue()) {
                climbState = !climbState;
            }
        }
    }

    public boolean getClimbState() {
        return climbState;
    }
}