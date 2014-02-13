/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous.steps.arms;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.inputmanager.inputs.joystick.JoystickButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.Subject;

/**
 *
 * @author Nathan
 */
public class AutonomousStepAccumulateBack extends WsAutonomousStep{

    public void initialize() {
        Subject subject = InputManager.getInstance().getOiInput(InputManager.MANIPULATOR_JOYSTICK_INDEX).getSubject(JoystickButtonEnum.MANIPULATOR_BUTTON_6);
        ((BooleanSubject) subject).setValue(true);
        finished = true;
    }

    public void update() {
        
    }

    public String toString() {
        return "Accumulate with the front roller";
    }
    
}
