package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.inputmanager.inputs.joystick.JoystickButtonEnum;
import com.wildstangs.outputmanager.base.OutputManager;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.Subsystem;
import com.wildstangs.timer.WsTimer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Nathan
 */
public class Catapult extends Subsystem implements IObserver {

    private boolean armCatapultFlag;
    private boolean fireCatapultFlag;
    private boolean disarmCatapultFlag;
    private boolean overrideFlag;
    private boolean latchesOutOverrideFlag;
    private boolean isCatapultDown;
    private boolean isTension;
    private boolean isBallIn;
    private boolean isLatched;
    protected boolean ohShitFlag = false;
    private WsTimer stateChangeTimer;
    private CatapultState catapultState;

    public static class CatapultState {

        private int index;

        private CatapultState(int index) {
            this.index = index;
        }

        public static final CatapultState DOWN = new CatapultState(0);
        public static final CatapultState UP = new CatapultState(1);
        public static final CatapultState FIRING = new CatapultState(2);
        public static final CatapultState WAITING_FOR_DOWN = new CatapultState(3);
    }

    public Catapult(String name) {
        super(name);

        // Arm the catapault
        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_2);
        // Disarm the catapult
        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_3);
        // Fire the catapault
        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_4);
        // Override all the things required to shoot
        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_10);
        //Override latches out
        registerForJoystickButtonNotification(JoystickButtonEnum.DRIVER_BUTTON_9);
        // Limit switch to detect when the catapult is down
        registerForSensorNotification(InputManager.CATAPULT_DOWN_SWITCH_INDEX);
        // Limit switch to detect the position of the latch
        registerForSensorNotification(InputManager.LATCH_POSITION_SWITCH_INDEX);
        // Limit switch to detect if the ball is in the catapult
        registerForSensorNotification(InputManager.BALL_DETECT_SWITCH_INDEX);
        // Limit switch that shows if there is tension on the catapult
        registerForSensorNotification(InputManager.TENSION_LIMIT_SWITCH_INDEX);
        
        //"Oh Shit" button for the driver
        registerForJoystickButtonNotification(JoystickButtonEnum.DRIVER_BUTTON_10);
        
        stateChangeTimer = new WsTimer();
    }

    public void init() {
        overrideFlag = false;
        latchesOutOverrideFlag = false;
        armCatapultFlag = false;
        disarmCatapultFlag = false;
        fireCatapultFlag = false;
        isCatapultDown = false;
        isTension = false;
        isBallIn = false;
        isLatched = false;
        catapultState = CatapultState.DOWN;
    }

    public void update() {
        if(catapultState == CatapultState.DOWN) {
            if(armCatapultFlag == true && fireCatapultFlag == false && ((isLatched && isCatapultDown) || overrideFlag)){
                catapultState = CatapultState.UP;
            }
            if(latchesOutOverrideFlag && !overrideFlag)
            {
                catapultState = CatapultState.WAITING_FOR_DOWN;
            }
        } else if(catapultState == CatapultState.UP){
            if(ohShitFlag == false && armCatapultFlag == false && fireCatapultFlag == true && ((isTension && isLatched) || overrideFlag)){
                catapultState = CatapultState.FIRING;
                stateChangeTimer.stop();
                stateChangeTimer.reset();
                stateChangeTimer.start();
            }
            else if(disarmCatapultFlag)
            {
                catapultState = CatapultState.DOWN;
            }
        } else if(catapultState == CatapultState.FIRING){
            if(stateChangeTimer.hasPeriodPassed(3.0) && overrideFlag == false){
                catapultState = CatapultState.WAITING_FOR_DOWN;
                stateChangeTimer.stop();
            }
        } else { //catapult == CatipultState.WAITING_FOR_DOWN
            if((isCatapultDown == true || overrideFlag == true) && !latchesOutOverrideFlag){
                catapultState = CatapultState.DOWN;
            }
        }
        
        if(catapultState == CatapultState.DOWN) {
            (OutputManager.getInstance().getOutput(OutputManager.CATAPAULT_SOLENOID_INDEX)).set(new Boolean(false));
            (OutputManager.getInstance().getOutput(OutputManager.LATCH_SOLENOID_INDEX)).set(new Boolean(false));
            SmartDashboard.putString("Catapult state", "down");
        } else if(catapultState == CatapultState.UP){
            (OutputManager.getInstance().getOutput(OutputManager.CATAPAULT_SOLENOID_INDEX)).set(new Boolean(true));
            (OutputManager.getInstance().getOutput(OutputManager.LATCH_SOLENOID_INDEX)).set(new Boolean(false));
            SmartDashboard.putString("Catapult state", "up");
        } else if(catapultState == CatapultState.FIRING){
            (OutputManager.getInstance().getOutput(OutputManager.CATAPAULT_SOLENOID_INDEX)).set(new Boolean(true));
            (OutputManager.getInstance().getOutput(OutputManager.LATCH_SOLENOID_INDEX)).set(new Boolean(true));
            SmartDashboard.putString("Catapult state", "Firing");
        } else { //catapult == CatipultState.WAITING_FOR_DOWN
            (OutputManager.getInstance().getOutput(OutputManager.CATAPAULT_SOLENOID_INDEX)).set(new Boolean(false));
            (OutputManager.getInstance().getOutput(OutputManager.LATCH_SOLENOID_INDEX)).set(new Boolean(true));
            SmartDashboard.putString("Catapult state", "Waiting for catapult down");
            
        }
        SmartDashboard.putBoolean("Ball detect switch",isBallIn);
        SmartDashboard.putBoolean("Latch switch",isLatched);
        SmartDashboard.putBoolean("catapult down limit switch",isCatapultDown);
        SmartDashboard.putBoolean("Tension switch",isTension);
        
    }
    public void notifyConfigChange() {

    }

    public boolean isCatapultDown() {
        return isCatapultDown;
    }
    
    public boolean isBallIn() {
        return isBallIn;
    }
    
    public boolean isTension() {
        return isTension;
    }
    
    public boolean isLatched() {
        return isLatched;
    }

    public void acceptNotification(Subject subjectThatCaused) {
        if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_2) {
            armCatapultFlag = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_4) {
            fireCatapultFlag = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.equals(InputManager.getInstance().getSensorInput(InputManager.CATAPULT_DOWN_SWITCH_INDEX).getSubject())) {
            isCatapultDown = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_10) {
            overrideFlag = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.equals(InputManager.getInstance().getSensorInput(InputManager.LATCH_POSITION_SWITCH_INDEX).getSubject())) {
            isLatched = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.equals(InputManager.getInstance().getSensorInput(InputManager.BALL_DETECT_SWITCH_INDEX).getSubject())) {
            isBallIn = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.equals(InputManager.getInstance().getSensorInput(InputManager.TENSION_LIMIT_SWITCH_INDEX).getSubject())) {
            isTension = ((BooleanSubject) subjectThatCaused).getValue();
        } 
        else if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_3)
        {
            disarmCatapultFlag = ((BooleanSubject) subjectThatCaused).getValue();
        }
        else if (subjectThatCaused.getType() == JoystickButtonEnum.DRIVER_BUTTON_9)
        {
            latchesOutOverrideFlag = ((BooleanSubject) subjectThatCaused).getValue();
        }
        else if(subjectThatCaused.getType() == JoystickButtonEnum.DRIVER_BUTTON_10)
        {
            if(((BooleanSubject)subjectThatCaused).getValue())
            {
                ohShitFlag = !ohShitFlag;
            }
        }
    }
}
