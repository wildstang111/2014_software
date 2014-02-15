package com.wildstangs.outputmanager.base;

import com.wildstangs.list.WsList;
import com.wildstangs.outputmanager.outputs.WsDoubleSolenoid;
import com.wildstangs.outputmanager.outputs.WsDriveSpeed;
import com.wildstangs.outputmanager.outputs.WsRelay;
import com.wildstangs.outputmanager.outputs.WsSolenoid;
import com.wildstangs.outputmanager.outputs.WsVictor;
import com.wildstangs.outputmanager.outputs.no.NoOutput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author Nathan
 */
public class OutputManager {

    private static OutputManager instance = null;
    private static WsList outputs = new WsList(10);

    /**
     * Method to obtain the instance of the OutputManager singleton.
     *
     * @return the instance of the OutputManager.
     */
    public static OutputManager getInstance() {
        if (OutputManager.instance == null) {
            OutputManager.instance = new OutputManager();
        }
        return OutputManager.instance;
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
    public static final int CATAPAULT_SOLENOID_INDEX = 7;
    public static final int EARS_SOLENOID_INDEX = 8;
    public static final int FRONT_ARM_VICTOR_INDEX = 10;
    public static final int BACK_ARM_VICTOR_INDEX = 11;
    public static final int FRONT_ARM_ROLLER_VICTOR_INDEX = 12;
    public static final int BACK_ARM_ROLLER_VICTOR_INDEX = 13;
    public static final int LATCH_SOLENOID_INDEX = 14;
    /**
     * Constructor for OutputManager.
     *
     * All new output elements need to be added in the constructor as well as
     * having a key value added above.
     */
    
    protected OutputManager() {
        //Add the facade data elements
        outputs.addToIndex(UNKNOWN_INDEX, new NoOutput());
        outputs.addToIndex(RIGHT_DRIVE_SPEED_INDEX, new WsDriveSpeed("Right Drive Speed", 3, 4));
        outputs.addToIndex(LEFT_DRIVE_SPEED_INDEX, new WsDriveSpeed("Left Drive Speed", 1, 2));
        outputs.addToIndex(SHIFTER_INDEX, new WsDoubleSolenoid("Shifter", 1, 1, 2));
        outputs.addToIndex(LIGHT_CANNON_RELAY_INDEX, new WsRelay(1, 5, Relay.Direction.kForward));
        outputs.addToIndex(WINGS_SOLENOID_INDEX, new WsDoubleSolenoid("Wings Solenoid1", 1, 5, 6));
        outputs.addToIndex(LANDING_GEAR_SOLENOID_INDEX, new WsSolenoid("Landing Gear Solenoid", 2, 3));
        outputs.addToIndex(CATAPAULT_SOLENOID_INDEX, new WsSolenoid("Arm Catapult Solenoid", 1, 7));
        outputs.addToIndex(EARS_SOLENOID_INDEX, new WsDoubleSolenoid("Ears Double Solenoid", 2, 1, 2));
        outputs.addToIndex(FRONT_ARM_VICTOR_INDEX, new WsVictor("Front Arm Victor", 5));       
        outputs.addToIndex(BACK_ARM_VICTOR_INDEX, new WsVictor("Back Arm Victor", 6));
        outputs.addToIndex(FRONT_ARM_ROLLER_VICTOR_INDEX, new WsVictor("Front Arm Roller Victor",7));
        outputs.addToIndex(BACK_ARM_ROLLER_VICTOR_INDEX, new WsVictor("Back Arm Roller Victor", 8));
        outputs.addToIndex(LATCH_SOLENOID_INDEX, new WsSolenoid("Latch Solenoid", 1, 8));
    }
}
