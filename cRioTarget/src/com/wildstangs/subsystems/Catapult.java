package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.base.WsInputManager;
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
 * @author Nathan
 */
public class Catapult extends WsSubsystem implements IObserver {

    private boolean armCatapultFlag;
    private boolean fireCatapultFlag;
    private boolean overrideFlag;
    private boolean isCatapultDown;
    private boolean isTension;
    private boolean isBallIn;
    private boolean isLatched;
    private CatapultState catapultState;

    public static class CatapultState {

        private int index;

        private CatapultState(int index) {
            this.index = index;
        }

        public static final CatapultState DOWN = new CatapultState(0);
        public static final CatapultState UP = new CatapultState(1);
    }

    public Catapult(String name) {
        super(name);

        // Arm the catapault
        registerForJoystickButtonNotification(WsJoystickButtonEnum.MANIPULATOR_BUTTON_2);
        // Fire the catapault
        registerForJoystickButtonNotification(WsJoystickButtonEnum.MANIPULATOR_BUTTON_4);
        // Limit switch to detect when the catapult is down
        registerForSensorNotification(WsInputManager.CATAPULT_DOWN_SWITCH_INDEX);
    }

    public void init() {
        overrideFlag = false;
        armCatapultFlag = false;
        fireCatapultFlag = false;
        isCatapultDown = false;
        isTension = false;
        isBallIn = false;
        isLatched = false;
        catapultState = CatapultState.DOWN;
    }

    public void update() {
        if (armCatapultFlag == true && fireCatapultFlag == false) {
            catapultState = CatapultState.DOWN;
        } else if (armCatapultFlag == false && fireCatapultFlag == true && isTension && isBallIn && isLatched) {
            catapultState = CatapultState.UP;
        } else if (!armCatapultFlag && fireCatapultFlag && overrideFlag) {
            catapultState = CatapultState.UP;
        }
        
        if(catapultState == CatapultState.DOWN) {
            (WsOutputManager.getInstance().getOutput(WsOutputManager.CATAPAULT_SOLENOID_INDEX)).set(new Boolean(false));
            SmartDashboard.putString("Catapult state", "down");
        } else {
            (WsOutputManager.getInstance().getOutput(WsOutputManager.CATAPAULT_SOLENOID_INDEX)).set(new Boolean(true));
            SmartDashboard.putString("Catapult state", "up");
        }
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
        if (subjectThatCaused.getType() == WsJoystickButtonEnum.MANIPULATOR_BUTTON_2) {
            armCatapultFlag = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.getType() == WsJoystickButtonEnum.MANIPULATOR_BUTTON_4) {
            fireCatapultFlag = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.equals(WsInputManager.getInstance().getSensorInput(WsInputManager.CATAPULT_DOWN_SWITCH_INDEX).getSubject())) {
            isCatapultDown = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.getType() == WsJoystickButtonEnum.MANIPULATOR_BUTTON_10) {
            overrideFlag = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.equals(WsInputManager.getInstance().getSensorInput(WsInputManager.LATCH_POSITION_SWITCH_INDEX).getSubject())) {
            isLatched = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.equals(WsInputManager.getInstance().getSensorInput(WsInputManager.BALL_DETECT_SWITCH_INDEX).getSubject())) {
            isBallIn = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.equals(WsInputManager.getInstance().getSensorInput(WsInputManager.TENSION_LIMIT_SWITCH_INDEX).getSubject())) {
            isTension = ((BooleanSubject) subjectThatCaused).getValue();
        }
    }
}
