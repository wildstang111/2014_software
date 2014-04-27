package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.inputmanager.inputs.joystick.JoystickButtonEnum;
import com.wildstangs.inputmanager.inputs.joystick.JoystickDPadButtonEnum;
import com.wildstangs.outputmanager.base.OutputManager;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.Subsystem;
import com.wildstangs.timer.WsTimer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Random;

/**
 *
 * @author Nathan
 */
public class Catapult extends Subsystem implements IObserver
{
    protected static final Random RND = new Random();
    
    private boolean armCatapultFlag;
    private boolean fireCatapultFlag;
    private boolean disarmCatapultFlag;
    private boolean maintenanceFlag;
    private boolean passFlag;
    private boolean overrideFlag;
    private boolean latchesOutOverrideFlag;
    private boolean isCatapultDown;
    private boolean isTension;
    private boolean isBallIn;
    private boolean isLatched;
    protected boolean ohNoFlag = false;
    private WsTimer stateChangeTimer;
    private CatapultState catapultState;

    public static class CatapultState
    {

        private String id;

        private CatapultState(String id)
        {
            this.id = id;
        }
        public static final CatapultState DOWN = new CatapultState("Down");
        public static final CatapultState UP = new CatapultState("Up");
        public static final CatapultState FIRING = new CatapultState("Firing");
        public static final CatapultState WAITING_FOR_DOWN = new CatapultState("Waiting For Down");
        public static final CatapultState MAINTENANCE = new CatapultState("Maintenance");

        public String toString()
        {
            return id;
        }
    }

    public Catapult(String name)
    {
        super(name);

        // Arm the catapault
        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_2);
        // Disarm the catapult
        registerForJoystickButtonNotification(JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_LEFT);
        // Fire the catapault
        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_4);
        // Override all the things required to shoot
        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_10);
        //Override latches out
        registerForJoystickButtonNotification(JoystickButtonEnum.DRIVER_BUTTON_9);
        registerForJoystickButtonNotification(JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_DOWN);
        //Switch to Maintenance mode
        registerForJoystickButtonNotification(JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_UP);
        // Limit switch to detect when the catapult is down
        registerForSensorNotification(InputManager.CATAPULT_DOWN_SWITCH_INDEX);
        // Limit switch to detect the position of the latch
        registerForSensorNotification(InputManager.LATCH_POSITION_SWITCH_INDEX);
        // Limit switch to detect if the ball is in the catapult
//        registerForSensorNotification(InputManager.BALL_DETECT_SWITCH_INDEX);
        // Limit switch that shows if there is tension on the catapult
        registerForSensorNotification(InputManager.TENSION_LIMIT_SWITCH_INDEX);

        //"Oh No" button for the driver
        registerForJoystickButtonNotification(JoystickButtonEnum.DRIVER_BUTTON_10);

        stateChangeTimer = new WsTimer();
    }

    public void init()
    {
        overrideFlag = false;
        latchesOutOverrideFlag = false;
        armCatapultFlag = false;
        disarmCatapultFlag = false;
        fireCatapultFlag = false;
        maintenanceFlag = false;
        isCatapultDown = true;
        isTension = false;
        isBallIn = false;
        isLatched = true;
        catapultState = CatapultState.DOWN;
    }

    public void update()
    {
        if (catapultState == CatapultState.DOWN)
        {
            if (armCatapultFlag == true && fireCatapultFlag == false && ((isLatched && isCatapultDown) || overrideFlag))
            {
                catapultState = CatapultState.UP;
            }
            else if (latchesOutOverrideFlag && !overrideFlag)
            {
                catapultState = CatapultState.WAITING_FOR_DOWN;
            }
            else if(maintenanceFlag && !ohNoFlag)
            {
                catapultState = CatapultState.MAINTENANCE;
            }
        }
        else if (catapultState == CatapultState.UP)
        {
            if (ohNoFlag == false && armCatapultFlag == false && fireCatapultFlag == true && ((isTension && isLatched) || overrideFlag))
            {
                catapultState = CatapultState.FIRING;
                stateChangeTimer.stop();
                stateChangeTimer.reset();
                stateChangeTimer.start();
            }
            else if (disarmCatapultFlag)
            {
                catapultState = CatapultState.DOWN;
            }
        }
        else if (catapultState == CatapultState.FIRING)
        {
            if (stateChangeTimer.hasPeriodPassed(0.5) && overrideFlag == false)
            {
                catapultState = CatapultState.WAITING_FOR_DOWN;
                stateChangeTimer.stop();
            }
        }
        else if(catapultState == CatapultState.MAINTENANCE)
        {
            if(!maintenanceFlag || ohNoFlag)
            {
                catapultState = CatapultState.WAITING_FOR_DOWN;
            }
        }
        else
        { //catapult == CatipultState.WAITING_FOR_DOWN
            if ((isCatapultDown == true || overrideFlag == true) && !latchesOutOverrideFlag)
            {
                catapultState = CatapultState.DOWN;
            }
        }

        SmartDashboard.putString("Catapult state", catapultState.toString());
        if (catapultState == CatapultState.DOWN)
        {
            (OutputManager.getInstance().getOutput(OutputManager.CATAPAULT_SOLENOID_INDEX)).set(new Boolean(false));
            (OutputManager.getInstance().getOutput(OutputManager.LATCH_SOLENOID_INDEX)).set(new Boolean(false));
        }
        else if (catapultState == CatapultState.UP)
        {
            (OutputManager.getInstance().getOutput(OutputManager.CATAPAULT_SOLENOID_INDEX)).set(new Boolean(true));
            (OutputManager.getInstance().getOutput(OutputManager.LATCH_SOLENOID_INDEX)).set(new Boolean(false));
        }
        else if (catapultState == CatapultState.FIRING)
        {
            (OutputManager.getInstance().getOutput(OutputManager.CATAPAULT_SOLENOID_INDEX)).set(new Boolean(true));
            (OutputManager.getInstance().getOutput(OutputManager.LATCH_SOLENOID_INDEX)).set(new Boolean(true));
        }
        else if(catapultState == CatapultState.MAINTENANCE)
        {
           (OutputManager.getInstance().getOutput(OutputManager.CATAPAULT_SOLENOID_INDEX)).set(new Boolean(true));
           (OutputManager.getInstance().getOutput(OutputManager.LATCH_SOLENOID_INDEX)).set(new Boolean(true));
        }
        else
        { //catapult == CatipultState.WAITING_FOR_DOWN
            (OutputManager.getInstance().getOutput(OutputManager.CATAPAULT_SOLENOID_INDEX)).set(new Boolean(false));
            (OutputManager.getInstance().getOutput(OutputManager.LATCH_SOLENOID_INDEX)).set(new Boolean(true));

        }
        SmartDashboard.putBoolean("Ball detect switch", isBallIn);
        SmartDashboard.putBoolean("Latch switch", isLatched);
        SmartDashboard.putBoolean("catapult down limit switch", isCatapultDown);
        SmartDashboard.putBoolean("Tension switch", isTension ? (RND.nextInt(2) == 0) : false);

    }

    public void notifyConfigChange()
    {
    }

    public boolean isCatapultDown()
    {
        return isCatapultDown;
    }

    public boolean isBallIn()
    {
        return isBallIn;
    }

    public boolean isTension()
    {
        return isTension;
    }

    public boolean isLatched()
    {
        return isLatched;
    }

    public void acceptNotification(Subject subjectThatCaused)
    {
        if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_2)
        {
            armCatapultFlag = ((BooleanSubject) subjectThatCaused).getValue();
        }
        else if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_4)
        {
            fireCatapultFlag = ((BooleanSubject) subjectThatCaused).getValue();
        }
        else if (subjectThatCaused.equals(InputManager.getInstance().getSensorInput(InputManager.CATAPULT_DOWN_SWITCH_INDEX).getSubject()))
        {
            isCatapultDown = ((BooleanSubject) subjectThatCaused).getValue();
        }
        else if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_10)
        {
            overrideFlag = ((BooleanSubject) subjectThatCaused).getValue();
        }
        else if (subjectThatCaused.equals(InputManager.getInstance().getSensorInput(InputManager.LATCH_POSITION_SWITCH_INDEX).getSubject()))
        {
            isLatched = ((BooleanSubject) subjectThatCaused).getValue();
        }
//        else if (subjectThatCaused.equals(InputManager.getInstance().getSensorInput(InputManager.BALL_DETECT_SWITCH_INDEX).getSubject()))
//        {
//            isBallIn = ((BooleanSubject) subjectThatCaused).getValue();
//        }
        else if (subjectThatCaused.equals(InputManager.getInstance().getSensorInput(InputManager.TENSION_LIMIT_SWITCH_INDEX).getSubject()))
        {
            isTension = ((BooleanSubject) subjectThatCaused).getValue();
        }
        else if (subjectThatCaused.getType() == JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_LEFT)
        {
            disarmCatapultFlag = ((BooleanSubject) subjectThatCaused).getValue();
        }
        else if (subjectThatCaused.getType() == JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_UP)
        {
            passFlag = ((BooleanSubject) subjectThatCaused).getValue();
        }
        else if (subjectThatCaused.getType() == JoystickButtonEnum.DRIVER_BUTTON_9 || subjectThatCaused.getType() == JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_DOWN)
        {
            latchesOutOverrideFlag = ((BooleanSubject) subjectThatCaused).getValue();
        }
        else if (subjectThatCaused.getType() == JoystickButtonEnum.DRIVER_BUTTON_10)
        {
            if (((BooleanSubject) subjectThatCaused).getValue())
            {
                ohNoFlag = !ohNoFlag;
            }
        }
    }
}
