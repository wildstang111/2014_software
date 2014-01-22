package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.inputmanager.inputs.joystick.WsJoystickAxisEnum;
import com.wildstangs.inputmanager.inputs.joystick.WsJoystickButtonEnum;
import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.outputmanager.base.WsOutputManager;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;

/**
 *
 * @author mail929
 */
public class Wings extends WsSubsystem {
    
    public Wings(String name) {
        super(name);
    }

    public void init() {
        (WsOutputManager.getInstance().getOutput(WsOutputManager.WINGS_SOLENOID_INDEX)).set((IOutputEnum) null, new Double(0.0));
        (WsOutputManager.getInstance().getOutput(WsOutputManager.WINGS_SOLENOID_INDEX)).update();
        WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK_INDEX).set(WsJoystickButtonEnum.MANIPULATOR_BUTTON_6, new Double(0.0));
        WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK_INDEX).update();
    }

    public void update() {
    }

    public void notifyConfigChange() {
    }
    
    public void acceptNotification(Subject subjectThatCaused) {
        if(subjectThatCaused.getType() == WsJoystickButtonEnum.MANIPULATOR_BUTTON_6) {
            if(((BooleanSubject)subjectThatCaused).getValue()) {
                WsOutputManager.getInstance().getOutput(WsOutputManager.WINGS_SOLENOID_INDEX).set(null, new Boolean(true));
            }
            else {
                WsOutputManager.getInstance().getOutput(WsOutputManager.WINGS_SOLENOID_INDEX).set(null, new Boolean(false));
            }
        }
    }
}
