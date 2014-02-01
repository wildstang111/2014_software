package com.wildstangs.inputmanager.base;

import com.wildstangs.inputmanager.inputs.WsAnalogInput;
import com.wildstangs.inputmanager.inputs.WsDigitalInput;
import com.wildstangs.inputmanager.inputs.driverstation.WsDSAnalogInput;
import com.wildstangs.inputmanager.inputs.driverstation.WsDSDigitalInput;
import com.wildstangs.inputmanager.inputs.joystick.driver.WsDriverJoystick;
import com.wildstangs.inputmanager.inputs.joystick.WsJoystickButtonEnum;
import com.wildstangs.inputmanager.inputs.joystick.manipulator.WsManipulatorJoystick;
import com.wildstangs.inputmanager.inputs.no.NoInput;
import com.wildstangs.list.WsList;
import com.wildstangs.logger.Logger;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;

/**
 *
 * @author Nathan
 */
public class WsInputManager
{

    private static WsInputManager instance = null;
    private static WsList oiInputs = new WsList(10);
    private static WsList sensorInputs = new WsList(10);

    /**
     * Method to get the instance of this singleton object.
     *
     * @return The instance of WsInputManager
     */
    public static WsInputManager getInstance()
    {
        if (instance == null)
        {
            instance = new WsInputManager();
        }
        return instance;
    }

    public void init()
    {
    }

    /**
     * Method to trigger updates of all the sensor data input containers
     */
    public void updateSensorData()
    {
        IInput sIn;
        for (int i = 0; i < sensorInputs.size(); i++)
        {
            sIn = (IInput) sensorInputs.get(i);
            if (sIn == null)
            {
                continue;
            }
            sIn.pullData();
            sIn.update();
        }
    }

    /**
     * Method to trigger updates of all the oi data input containers.
     */
    public void updateOiData()
    {
        IInput oiIn;
        for (int i = 0; i < oiInputs.size(); i++)
        {
            oiIn = (IInput) oiInputs.get(i);
            if (oiIn == null)
            {
                continue;
            }
            oiIn.pullData();
            oiIn.update();
        }
    }

    public void updateOiDataAutonomous()
    {
        IInput oiIn;
        for (int i = 0; i < oiInputs.size(); i++)
        {
            oiIn = (IInput) oiInputs.get(i);
            if (oiIn == null)
            {
                continue;
            }
            if (!(oiIn instanceof WsDriverJoystick || oiIn instanceof WsManipulatorJoystick))
            {
                oiIn.pullData();
            }
            oiIn.update();
        }
    }

    /**
     * Method to notify all input containers that a config update occurred.
     *
     * Used by the ConfigFacade when the config is re-read.
     */
    public void notifyConfigChange()
    {
        for (int i = 0; i < sensorInputs.size(); i++)
        {
            IInput sIn = (IInput) sensorInputs.get(i);
            if (sIn != null)
            {
                sIn.notifyConfigChange();
            }
        }
        for (int i = 0; i < oiInputs.size(); i++)
        {
            IInput oiIn = (IInput) oiInputs.get(i);
            if (oiIn != null)
            {
                oiIn.notifyConfigChange();
            }
        }
    }

    /**
     * Gets an OI container, based on a key.
     *
     * @param key The key that represents the OI input container
     * @return A WsInputInterface.
     */
    public IInput getOiInput(int index)
    {
        if (index >= 0 && index < oiInputs.size())
        {
            return (IInput) oiInputs.get(index);
        }
        return (IInput) oiInputs.get(UNKNOWN_INDEX);
    }

    /**
     * Gets a sensor container, based on a key.
     *
     * @param key The key that represents the sensor input container
     * @return A WsInputInterface.
     */
    public IInput getSensorInput(int index)
    {
        if (index >= 0 || index < sensorInputs.size())
        {
            return (IInput) sensorInputs.get(index);
        }
        return (IInput) sensorInputs.get(UNKNOWN_INDEX);
    }

    final public void attachJoystickButton(IInputEnum button, IObserver observer)
    {
        if (button instanceof WsJoystickButtonEnum)
        {
            Subject subject = WsInputManager.getInstance().getOiInput(((WsJoystickButtonEnum) button).isDriver() ? WsInputManager.DRIVER_JOYSTICK_INDEX : WsInputManager.MANIPULATOR_JOYSTICK_INDEX).getSubject(button);
            subject.attach(observer);
        }
        else
        {
            Logger.getLogger().debug(this.getClass().getName(), "attachJoystickButton", "Oops! Check that the inputs implement the required interfaces.");
        }
    }
    /**
     * Keys to represent OI Inputs
     */
    public static final int UNKNOWN_INDEX = 0;
    public static final int DRIVER_JOYSTICK_INDEX = 1;
    public static final int MANIPULATOR_JOYSTICK_INDEX = 2;
    public static final int AUTO_PROGRAM_SELECTOR_INDEX = 3;
    public static final int LOCK_IN_SWITCH_INDEX = 4;
    public static final int START_POSITION_SELECTOR_INDEX = 5;
    //Sensor Inputs
    public static final int PRESSURE_TRANSDUCER_INDEX = 7;
    public static final int FRONT_ARM_POT_INDEX = 8;
    public static final int BACK_ARM_POT_INDEX = 9;
    public static final int LEFT_ENCODER_A = 8;
    public static final int LEFT_ENCODER_B = 9;
    public static final int RIGHT_ENCODER_A = 10;
    public static final int RIGHT_ENCODER_B = 11;
    public static final int TENSION_LIMIT_SWITCH = 12;
    public static final int BALL_DETECT_SWITCH = 13;
    public static final int LATCH_POSITION_SWITCH = 14;
    public static final int CATAPULT_DOWN_SWITCH = 15;
    /**
     * Constructor for the WsInputManager.
     *
     * Each new data element to be added to the facade must be added here and
     * have keys added above.
     */
    protected WsInputManager()
    {
        //Add the facade data elements
        sensorInputs.addToIndex(UNKNOWN_INDEX, new NoInput());
        sensorInputs.addToIndex(PRESSURE_TRANSDUCER_INDEX, new WsAnalogInput(4));
        sensorInputs.addToIndex(FRONT_ARM_POT_INDEX, new WsAnalogInput(1));
        sensorInputs.addToIndex(BACK_ARM_POT_INDEX, new WsAnalogInput(2));
        sensorInputs.addToIndex(PRESSURE_SWITCH, new WsDigitalInput(1));
        sensorInputs.addToIndex(LEFT_ENCODER_A, new WsDigitalInput(2));
        sensorInputs.addToIndex(LEFT_ENCODER_B, new WsDigitalInput(3));
        sensorInputs.addToIndex(RIGHT_ENCODER_A, new WsDigitalInput(4));
        sensorInputs.addToIndex(RIGHT_ENCODER_B, new WsDigitalInput(5));
        sensorInputs.addToIndex(TENSION_LIMIT_SWITCH, new WsDigitalInput(6));
        sensorInputs.addToIndex(BALL_DETECT_SWITCH, new WsDigitalInput(7));
        sensorInputs.addToIndex(LATCH_POSITION_SWITCH, new WsDigitalInput(8));
        sensorInputs.addToIndex(CATAPULT_DOWN_SWITCH, new WsDigitalInput(9));

        oiInputs.addToIndex(UNKNOWN_INDEX, new NoInput();
        oiInputs.addToIndex(DRIVER_JOYSTICK_INDEX, new WsDriverJoystick());
        oiInputs.addToIndex(MANIPULATOR_JOYSTICK_INDEX, new WsManipulatorJoystick());
        oiInputs.addToIndex(AUTO_PROGRAM_SELECTOR_INDEX, new WsDSAnalogInput(1));
        oiInputs.addToIndex(LOCK_IN_SWITCH_INDEX, new WsDSDigitalInput(1));
        oiInputs.addToIndex(START_POSITION_SELECTOR_INDEX, new WsDSAnalogInput(2));
        
    }
}
