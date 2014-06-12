/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.inputmanager.inputs.joystick.JoystickButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.Subsystem;
import com.wildstangs.subsystems.base.SubsystemContainer;
import com.wildstangs.timer.WsTimer;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Joey
 */
public class NonI2CLED extends Subsystem implements IObserver
{

    protected static class LEDControlStateEnum
    {

        protected String stateName;

        protected LEDControlStateEnum(String stateName)
        {
            this.stateName = stateName;
        }

        public String toString()
        {
            return stateName;
        }
    }

    protected static class LEDPattern
    {

        protected static final DigitalOutput LED_PIN_1 = new DigitalOutput(7);
        protected static final DigitalOutput LED_PIN_2 = new DigitalOutput(12);
        protected static final DigitalOutput LED_PIN_3 = new DigitalOutput(13);
        protected static final DigitalOutput LED_PIN_4 = new DigitalOutput(14);
        protected int patternID;

        public LEDPattern(int patternID)
        {
            this.patternID = patternID;
        }

        public boolean sendPattern()
        {
            return this.sendPattern(true);
        }

        public boolean sendPattern(boolean runTimer)
        {
            SmartDashboard.putNumber("LED Pattern", patternID);
            SmartDashboard.putNumber("LED Pin 1", patternID & 0x01);
            SmartDashboard.putNumber("LED Pin 2", patternID & 0x02);
            SmartDashboard.putNumber("LED Pin 3", patternID & 0x04);
            SmartDashboard.putNumber("LED Pin 4", patternID & 0x08);
            //Convert the pattern into binary representation on the Digital Pins
            LED_PIN_1.set((patternID & 0x01) != 0);
            LED_PIN_2.set((patternID & 0x02) != 0);
            LED_PIN_3.set((patternID & 0x04) != 0);
            LED_PIN_4.set((patternID & 0x08) != 0);

            if (runTimer)
            {
                clearTimer.reset();
                clearTimer.start();
                runningTimer = true;
            }

            return true;
        }
    }
    public static final LEDControlStateEnum DISABLED = new LEDControlStateEnum("Disabled");
    public static final LEDControlStateEnum AUTONOMOUS = new LEDControlStateEnum("Autonomous");
    public static final LEDControlStateEnum TELEOPERATED = new LEDControlStateEnum("TeleOperated");
    //Pattern 0 is not used
    protected static final LEDPattern NO_PATTERN = new LEDPattern(0);
    protected static final LEDPattern RED_ALLIANCE_PATTERN = new LEDPattern(1);
    protected static final LEDPattern BLUE_ALLIANCE_PATTERN = new LEDPattern(2);
    protected static final LEDPattern AUTONOMOUS_PATTERN = new LEDPattern(3);
    protected static final LEDPattern ENTER_TELEOP_PATTERN = new LEDPattern(4);
    protected static final LEDPattern TENSIONED_PATTERN = new LEDPattern(5);
    protected static final LEDPattern SHOOT_PATTERN = new LEDPattern(6);
    protected static final LEDPattern BLANK_PATTERN = new LEDPattern(7);
    protected static final LEDPattern PATTERN_8 = new LEDPattern(8);
    protected static final LEDPattern PATTERN_9 = new LEDPattern(9);
    protected static final LEDPattern PATTERN_10 = new LEDPattern(10);
    protected static final LEDPattern PATTERN_11 = new LEDPattern(11);
    protected static final LEDPattern PATTERN_12 = new LEDPattern(12);
    protected static final LEDPattern PATTERN_13 = new LEDPattern(13);
    protected static final LEDPattern PATTERN_14 = new LEDPattern(14);
    protected static final LEDPattern PATTERN_15 = new LEDPattern(15);
    protected boolean disableDataSent = false;
    protected boolean autoDataSent = false;
    protected boolean teleOpDataSent = false;
    protected boolean clear = false;
    protected static WsTimer clearTimer = new WsTimer();
    protected static WsTimer waitForBootUp = new WsTimer();
    protected static boolean booted = false;
    protected static boolean runningTimer = false;
    protected WsTimer disabledTimeOut = new WsTimer();
    protected boolean blankPatternDisabled = false;
    protected boolean redAlliance = false;
    protected LEDControlStateEnum currentControlState = DISABLED;

    public NonI2CLED(String name)
    {
        super(name);

        //Shoot Button
        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_4);
        registerForSensorNotification(InputManager.TENSION_LIMIT_SWITCH_INDEX);

        NO_PATTERN.sendPattern(false);

        disabledTimeOut.reset();
        disabledTimeOut.start();
    }

    public void init()
    {
        clearTimer.stop();
        clearTimer.reset();
    }

    public void notifyConfigChange()
    {
    }
    int pattern = 0, lastPattern = 0;

    public void update()
    {
        if (runningTimer)
        {
            if (clearTimer.hasPeriodPassed(2.5))
            {
                clear = true;
                runningTimer = false;
                clearTimer.stop();
                clearTimer.reset();
            }
        }
        // Get all inputs relevant to the LEDs
        boolean isRobotTestMode = DriverStation.getInstance().isTest();
        boolean isRobotEnabled = DriverStation.getInstance().isEnabled();
        boolean isRobotTeleop = DriverStation.getInstance().isOperatorControl();
        boolean isRobotAuton = DriverStation.getInstance().isAutonomous();
        DriverStation.Alliance alliance = DriverStation.getInstance().getAlliance();
        
        SmartDashboard.putString("LED Control State", this.currentControlState.toString());
        
        if (currentControlState == TELEOPERATED)
        {
            if (!teleOpDataSent)
            {
                teleOpDataSent = ENTER_TELEOP_PATTERN.sendPattern(false);
                autoDataSent = false;
                disableDataSent = false;
            }
            if (clear)
            {
                clear = !ENTER_TELEOP_PATTERN.sendPattern(false);
            }
        }
        else if (currentControlState == AUTONOMOUS)
        {
            if (!autoDataSent)
            {
                autoDataSent = AUTONOMOUS_PATTERN.sendPattern(false);
                teleOpDataSent = false;
                disableDataSent = false;
            }
            if (clear)
            {
                clear = !AUTONOMOUS_PATTERN.sendPattern(false);
            }

        }
        else if (currentControlState == DISABLED)
        {
            if(disabledTimeOut.hasPeriodPassed(60.0))
            {
                BLANK_PATTERN.sendPattern(false);
            }
            else
            {
                switch (alliance.value)
                {
                    case DriverStation.Alliance.kRed_val:
                    {
                        if (!disableDataSent || !redAlliance)
                        {
                            redAlliance = true;
                            disableDataSent = RED_ALLIANCE_PATTERN.sendPattern(false);
                        }
                        if (clear)
                        {
                            clear = !RED_ALLIANCE_PATTERN.sendPattern(false);
                        }
                    }
                    break;

                    case DriverStation.Alliance.kBlue_val:
                    {
                        if (!disableDataSent || redAlliance)
                        {
                            redAlliance = false;
                            disableDataSent = BLUE_ALLIANCE_PATTERN.sendPattern(false);
                        }
                        if (clear)
                        {
                            clear = !BLUE_ALLIANCE_PATTERN.sendPattern(false);
                        }
                    }
                    break;
                    default:
                    {
                        disableDataSent = false;
                        NO_PATTERN.sendPattern(false);
                    }
                    break;
                }
            }
        }

//        if(isRobotTestMode)
//        {
//            try
//            {
//                pattern = (int) SmartDashboard.getNumber("LED Pattern To Send");
//                System.out.println("Retrieved Pattern: " + pattern);
//            }
//            catch(Throwable t)
//            {
//                SmartDashboard.putNumber("LED Pattern To Send", 0);
//            }
//            
//            if(pattern != lastPattern)
//            {
//                lastPattern = pattern;
//                switch(pattern)
//                {
//                    case 1:
//                    {
//                        RED_ALLIANCE_PATTERN.sendPattern(false);
//                        break;
//                    }
//                    case 2:
//                    {
//                        BLUE_ALLIANCE_PATTERN.sendPattern(false);
//                        break;
//                    }
//                    case 3:
//                    {
//                        AUTONOMOUS_PATTERN.sendPattern(false);
//                        break;
//                    }
//                    case 4:
//                    {
//                        ENTER_TELEOP_PATTERN.sendPattern(false);
//                        break;
//                    }
//                    case 5:
//                    {
//                        TENSIONED_PATTERN.sendPattern(false);
//                        break;
//                    }
//                    case 6:
//                    {
//                        SHOOT_PATTERN.sendPattern(false);
//                        break;
//                    }
//                    case 7:
//                    {
//                        BLANK_PATTERN.sendPattern(false);
//                        break;
//                    }
//                    case 8:
//                    {
//                        PATTERN_8.sendPattern(false);
//                        break;
//                    }
//                    case 9:
//                    {
//                        PATTERN_9.sendPattern(false);
//                        break;
//                    }
//                    case 10:
//                    {
//                        PATTERN_10.sendPattern(false);
//                        break;
//                    }
//                    case 11:
//                    {
//                        PATTERN_11.sendPattern(false);
//                        break;
//                    }
//                    case 12:
//                    {
//                        PATTERN_12.sendPattern(false);
//                        break;
//                    }
//                    case 13:
//                    {
//                        PATTERN_13.sendPattern(false);
//                        break;
//                    }
//                    case 14:
//                    {
//                        PATTERN_14.sendPattern(false);
//                        break;
//                    }
//                    case 15:
//                    {
//                        PATTERN_15.sendPattern(false);
//                        break;
//                    }
//                    default:
//                    {
//                        NO_PATTERN.sendPattern(false);
//                        break;
//                    }
//                }
//            }
//        }
    }

    public void setControlState(LEDControlStateEnum newState)
    {
        this.currentControlState = newState;
        if(currentControlState == DISABLED)
        {
            disableDataSent = false;
            blankPatternDisabled = false;
            disabledTimeOut.stop();
            disabledTimeOut.reset();
            disabledTimeOut.start();
        }
        else if(currentControlState == TELEOPERATED)
        {
            teleOpDataSent = false;
        }
        else if(currentControlState == AUTONOMOUS)
        {
            autoDataSent = false;
        }
    }

    public void acceptNotification(Subject subjectThatCaused)
    {
        if (DriverStation.getInstance().isEnabled())
        {
            boolean buttonState = ((BooleanSubject) subjectThatCaused).getValue();

            if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_4)
            {
                if (buttonState)
                {
                    SHOOT_PATTERN.sendPattern();
                }
            }
            else if (subjectThatCaused.equals(InputManager.getInstance().getSensorInput(InputManager.TENSION_LIMIT_SWITCH_INDEX).getSubject()))
            {
                if (buttonState)
                {
                    TENSIONED_PATTERN.sendPattern();
                }
            }
        }
    }
}
