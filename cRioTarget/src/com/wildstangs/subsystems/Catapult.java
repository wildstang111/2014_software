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
    private boolean isCatapultDown;
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
        registerForSensorNotification(WsInputManager.CATAPULT_DOWN_LIMIT_SWITCH_INDEX);
    }

    public void init() {
        armCatapultFlag = false;
        fireCatapultFlag = false;
        isCatapultDown = false;
        catapultState = CatapultState.DOWN;
    }

    public void update() {
        if (armCatapultFlag == true && fireCatapultFlag == false) {
            catapultState = CatapultState.DOWN;
        } else if (armCatapultFlag == false && fireCatapultFlag == true) {
            catapultState = CatapultState.UP;
        }
        
        if(catapultState == CatapultState.DOWN) {
            (WsOutputManager.getInstance().getOutput(WsOutputManager.CATAPAULT_SOLENOID_INDEX)).set((IOutputEnum) null, new Boolean(false));
            SmartDashboard.putString("Catapult state", "down");
        } else {
            (WsOutputManager.getInstance().getOutput(WsOutputManager.CATAPAULT_SOLENOID_INDEX)).set((IOutputEnum) null, new Boolean(true));
            SmartDashboard.putString("Catapult state", "up");
        }
    }

    public void notifyConfigChange() {

    }

    public boolean isCatapultDown() {
        return isCatapultDown;
    }

    public void acceptNotification(Subject subjectThatCaused) {
        if (subjectThatCaused.getType() == WsJoystickButtonEnum.MANIPULATOR_BUTTON_2) {
            armCatapultFlag = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.getType() == WsJoystickButtonEnum.MANIPULATOR_BUTTON_4) {
            fireCatapultFlag = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.equals(WsInputManager.getInstance().getSensorInput(WsInputManager.CATAPULT_DOWN_LIMIT_SWITCH_INDEX).getSubject(null))) {
            isCatapultDown = ((BooleanSubject) subjectThatCaused).getValue();
        }
    }
}
