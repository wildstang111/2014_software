package com.wildstangs.autonomous.steps.floorpickup;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.inputmanager.inputs.joystick.WsJoystickButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.Subject;

/**
 *
 * @author Liam
 */
public class WsAutonomousStepOverrideFunnelatorButtonOn extends WsAutonomousStep {

    public void initialize() {
        Subject subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsJoystickButtonEnum.MANIPULATOR_BUTTON_10);
        BooleanSubject backButton = (BooleanSubject) subject;

        backButton.setValue(true);
        finished = true;
    }

    public void update() {
    }

    public String toString() {
        return "Turn off the funnelator gate";
    }
}
