package com.wildstangs.outputmanager.base;

import com.wildstangs.list.WsList;
import com.wildstangs.outputmanager.outputs.WsDoubleSolenoid;
import com.wildstangs.outputmanager.outputs.WsDriveSpeed;
import com.wildstangs.outputmanager.outputs.WsRelay;
import com.wildstangs.outputmanager.outputs.WsSolenoid;
import com.wildstangs.outputmanager.outputs.no.NoOutput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author Nathan
 */
public class WsOutputManager {

    private static WsOutputManager instance = null;
    private static WsList outputs = new WsList(10);

    /**
     * Method to obtain the instance of the WsOutputManager singleton.
     *
     * @return the instance of the WsOutputManager.
     */
    public static WsOutputManager getInstance() {
        if (WsOutputManager.instance == null) {
            WsOutputManager.instance = new WsOutputManager();
        }
        return WsOutputManager.instance;
    }

    /**
     * Method to cause all output elements to update.
     */
    public void init() {
    }

    public void update() {
        for (int i = 0; i < outputs.size(); i++) {
            IOutput out = (IOutput) outputs.get(i);
            if (out != null) {
                out.update();
            }
        }
    }

    /**
     * Method to notify all output elements that a config change has occurred
     * and config values need to be re-read.
     */
    public void notifyConfigChange() {
        for (int i = 0; i < outputs.size(); i++) {
            IOutput out = (IOutput) outputs.get(i);
            if (out != null) {
                out.notifyConfigChange();
            }
        }
    }

    /**
     * Gets an output element based on a key.
     *
     * @param index A string representation of the output element.
     *
     * @return The output element.
     */
    public IOutput getOutput(int index) {
        if (index >= 0 && index < outputs.size()) {
            return (IOutput) outputs.get(index);
        }
        return (IOutput) outputs.get(UNKNOWN_INDEX);
    }
    
    //Key Values - Need to update for each new output element.
    public static final int UNKNOWN_INDEX = 0;
    public static final int RIGHT_DRIVE_SPEED_INDEX = 1;
    public static final int LEFT_DRIVE_SPEED_INDEX = 2;
    public static final int SHIFTER_INDEX = 3;
    public static final int LIGHT_CANNON_RELAY_INDEX = 4;
    public static final int WINGS_SOLENOID_INDEX = 5;
    public static final int LANDING_GEAR_SOLENOID_INDEX = 6;
    public static final int ARM_CATAPAULT_SOLENOID_INDEX = 7;

    /**
     * Constructor for WsOutputManager.
     *
     * All new output elements need to be added in the constructor as well as
     * having a key value added above.
     */
    protected WsOutputManager() {
        //Add the facade data elements
        outputs.addToIndex(UNKNOWN_INDEX, new NoOutput());
        outputs.addToIndex(RIGHT_DRIVE_SPEED_INDEX, new WsDriveSpeed("Right Drive Speed", 3, 4));
        outputs.addToIndex(LEFT_DRIVE_SPEED_INDEX, new WsDriveSpeed("Left Drive Speed", 1, 2));
        outputs.addToIndex(SHIFTER_INDEX, new WsDoubleSolenoid("Shifter", 1, 1, 2));
        outputs.addToIndex(LIGHT_CANNON_RELAY_INDEX, new WsRelay(1, 2, Relay.Direction.kForward));
        outputs.addToIndex(WINGS_SOLENOID_INDEX, new WsSolenoid("Wings Solenoid", 1, 3));
        outputs.addToIndex(LANDING_GEAR_SOLENOID_INDEX, new WsSolenoid("Landing Gear Solenoid", 1, 4));
        outputs.addToIndex(ARM_CATAPAULT_SOLENOID_INDEX, new WsSolenoid("Arm Catapult Solenoid", 1, 5));
    }
}
