/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.catapult;

import com.wildstangs.autonomous.steps.AutonomousStep;
import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.inputmanager.inputs.joystick.JoystickButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.Catapult;
import com.wildstangs.subsystems.base.SubsystemContainer;

/**
 *
 * @author Alex
 */
public class AutonomousStepCatapultOverride extends AutonomousStep{
   BooleanSubject button = null;
    public void initialize() {
    Subject subject = InputManager.getInstance().getOiInput(InputManager.MANIPULATOR_JOYSTICK_INDEX).getSubject(JoystickButtonEnum.MANIPULATOR_BUTTON_10);
       button = (BooleanSubject) subject;
       button.setValue(true);
       this.finished = true;
    }

    public void update() {
    }

    public String toString() {
        return "Set Override";
    }
    
}
