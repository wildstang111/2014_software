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
public class AutonomousStepFireCatapult extends AutonomousStep{
   BooleanSubject button = null;
    public void initialize() {
    Subject subject = InputManager.getInstance().getOiInput(InputManager.MANIPULATOR_JOYSTICK_INDEX).getSubject(JoystickButtonEnum.MANIPULATOR_BUTTON_4);
       button = (BooleanSubject) subject;
       button.setValue(true);
    }

    public void update() {
       Catapult catapult = (Catapult) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.CATAPULT_INDEX);
       boolean ballInState = catapult.isBallIn();
       if (ballInState == false){
           button.setValue(false);
           this.finished = true;
       }
    }

    public String toString() {
        return "FIRE!";
    } 
}
