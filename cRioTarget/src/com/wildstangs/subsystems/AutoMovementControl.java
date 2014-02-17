package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.inputmanager.inputs.joystick.JoystickButtonEnum;
import com.wildstangs.inputmanager.inputs.joystick.JoystickDPadButtonEnum;
import com.wildstangs.subsystems.base.AutoMovement;
import com.wildstangs.logger.Logger;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.automovements.AutoMovementAccumulateBackArm;
import com.wildstangs.subsystems.automovements.AutoMovementAccumulateFrontArm;
import com.wildstangs.subsystems.automovements.AutoMovementArmsToTension;
import com.wildstangs.subsystems.automovements.AutoMovementPassBackToFront;
import com.wildstangs.subsystems.automovements.AutoMovementPassFrontToBack;
import com.wildstangs.subsystems.base.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Joey
 */
public class AutoMovementControl extends Subsystem implements IObserver {

    private AutoMovement runningProgram;
    private AutoMovement programToRun;
    private boolean programInProgress;

    public AutoMovementControl(String name) {
        super(name);
        
        Subject subject = InputManager.getInstance().getOiInput(InputManager.MANIPULATOR_JOYSTICK_INDEX).getSubject(JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_DOWN);
        subject.attach(this);
        
        subject = InputManager.getInstance().getOiInput(InputManager.MANIPULATOR_JOYSTICK_INDEX).getSubject(JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_UP);
        subject.attach(this);
        
        subject = InputManager.getInstance().getOiInput(InputManager.MANIPULATOR_JOYSTICK_INDEX).getSubject(JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_LEFT);
        subject.attach(this);
        
        subject = InputManager.getInstance().getOiInput(InputManager.MANIPULATOR_JOYSTICK_INDEX).getSubject(JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_RIGHT);
        subject.attach(this);
        
        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_9);
    }

    public void init() {
        runningProgram = null;
        programToRun = null;
        programInProgress = false;
    }

    public void update() {
        if (runningProgram != null) {
            runningProgram.update();
            if(runningProgram.isFinished())
            {
                cleanUpRunningProgram();
            }
        }
    }

    protected void startProgram() {
        runningProgram = programToRun;
        Logger.getLogger().always("AutoMovementCtrl", "Running Auto Movement", runningProgram.toString());
        runningProgram.initialize();
        SmartDashboard.putString("Auto Movement:", runningProgram.toString());
    }

    public void notifyConfigChange() {
    }

    public void acceptNotification(Subject subjectThatCaused) {
        if(((BooleanSubject) subjectThatCaused).getValue() == false)
        {
            return;
        }
        
        if(subjectThatCaused.getType() == JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_DOWN)
        {
            this.requestMovement(new AutoMovementAccumulateBackArm());
        }
        else if(subjectThatCaused.getType() == JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_UP)
        {
            this.requestMovement(new AutoMovementAccumulateFrontArm());
        }
        else if(subjectThatCaused.getType() == JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_LEFT)
        {
            this.requestMovement(new AutoMovementArmsToTension());
        }
        else if(subjectThatCaused.getType() == JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_RIGHT)
        {
            this.requestMovement(new AutoMovementPassBackToFront());
        }
        else if(subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_9)
        {
            this.requestMovement(new AutoMovementPassFrontToBack());
        }
    }

    public void requestMovement(AutoMovement movement) {
        programToRun = movement;
        if (programInProgress && runningProgram != null) {
            // terminate current program and start new one.
            cancelProgram();
        }
        startProgram();
        programInProgress = true;
    }

    public void cancelMovement() {
        if(runningProgram != null)
        {
            cancelProgram();
        }
    }

    private void cancelProgram() {
        Logger.getLogger().always("AutoMovementCtrl", "Abort Auto Movement", runningProgram.toString());
        runningProgram.abort();
        cleanUpRunningProgram();
    }
    
    protected void cleanUpRunningProgram()
    {
        runningProgram.cleanup();
        runningProgram = null;
        programInProgress = false;
        SmartDashboard.putString("Auto Movement:", "");
    }
}
