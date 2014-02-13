/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous.steps.arms;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.ISubjectEnum;

/**
 *
 * @author Nathan
 */
public class AutonomousStepWaitUntilBallInRobot extends WsAutonomousStep{

    public void initialize() {
        // Do nothing
    }

    public void update() {
        boolean ballInSwitchState = ((BooleanSubject) InputManager.getInstance().getSensorInput(InputManager.BALL_DETECT_SWITCH_INDEX).getSubject((ISubjectEnum) null)).getValue();
        if (true == ballInSwitchState) {
            finished = true;
        }
    }

    public String toString() {
        return "Wait for ball to be accumulated.";
    }
    
}
