/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.catapult;

import com.wildstangs.autonomous.steps.AutonomousStep;
import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.inputmanager.inputs.joystick.JoystickButtonEnum;
import com.wildstangs.inputmanager.inputs.joystick.JoystickDPadButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.BallHandler;
import com.wildstangs.subsystems.base.SubsystemContainer;

/**
 *
 * @author Alex
 */
public class AutonomousStepFireCatapult extends AutonomousStep{
    
    public static final BooleanConfigFileParameter DISABLE_AUTO_CATAPULT_FIRE = new BooleanConfigFileParameter(AutonomousStepFireCatapult.class.getName(), "DisableAutonomousCatapultFire", false);
    BooleanSubject button = null;
    boolean waitCycle = true;
    public void initialize() {
        //This is as a precaution so we don't fire the catapult into the arm. If the pids are disabled then we wouldn't be able to move the arm out of the way
        boolean armPidsDisabled = ((BallHandler) (SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.BALL_HANDLER_INDEX))).areArmPidsCompletelyDisabled();
        Subject subject = InputManager.getInstance().getOiInput(InputManager.MANIPULATOR_JOYSTICK_INDEX).getSubject(DISABLE_AUTO_CATAPULT_FIRE.getValue() || armPidsDisabled ? (ISubjectEnum) JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_LEFT : (ISubjectEnum) JoystickButtonEnum.MANIPULATOR_BUTTON_4);
        button = (BooleanSubject) subject;
        button.setValue(true);
    }

    public void update() {
//        Catapult catapult = (Catapult) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.CATAPULT_INDEX);
        if(waitCycle)
        {
            waitCycle = false;
        }
        else
        {
            button.setValue(false);
            this.finished = true;
        }
    }

    public String toString() {
        return "FIRE!";
    } 
}
