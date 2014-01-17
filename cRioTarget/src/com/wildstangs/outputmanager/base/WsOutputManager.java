package com.wildstangs.outputmanager.base;

import com.wildstangs.list.WsList;
import com.wildstangs.outputmanager.outputs.WsDoubleSolenoid;
import com.wildstangs.outputmanager.outputs.WsDriveSpeed;
import com.wildstangs.outputmanager.outputs.WsRelay;
import com.wildstangs.outputmanager.outputs.no.NoOutput;
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
            if(out != null) out.update();
        }
    }

    /**
     * Method to notify all output elements that a config change has occurred
     * and config values need to be re-read.
     */
    public void notifyConfigChange() {
        for (int i = 0; i < outputs.size(); i++) {
            IOutput out = (IOutput) outputs.get(i);
            if(out != null) out.notifyConfigChange();
        }
    }

    /**
     * Gets an output element based on a key.
     *
     * @param key A string representation of the output element.
     *
     * @return The output element.
     */
    public IOutput getOutput(int index) {
        if(index >= 0 && index < outputs.size())
        {
            return (IOutput) outputs.get(index);
        }
        return (IOutput) outputs.get(UNKNOWN_INDEX);
    }
    //Key Values - Need to update for each new output element.
    public static final String RIGHT_DRIVE_SPEED = "RightDriveSpeed";
    public static final String LEFT_DRIVE_SPEED = "LeftDriveSpeed";
    public static final String SHIFTER = "Shifter";
    public static final String LIFT = "Lift";
    public static final String KICKER = "Kicker";
    public static final String SHOOTER_VICTOR_ENTER = "ShooterVictorEnter";
    public static final String SHOOTER_VICTOR_EXIT = "ShooterVictorExit";
    public static final String SHOOTER_ANGLE = "ShooterAngle";
    public static final String FRISBIE_CONTROL = "FrisbieControl";
    public static final String ACCUMULATOR_SOLENOID = "AccumulatorSolenoid";
    public static final String ACCUMULATOR_SECONDARY_SOLENOID = "AccumulatorSecondarySolenoid";
    public static final String ACCUMULATOR_VICTOR = "AccumulatorVictor";
    public static final String FUNNELATOR_ROLLER = "FunnelatorRoller";
    public static final String LOADING_RAMP = "LoadingRamp";
    public static final String CLIMBER = "Climber";
    public static final String TOMAHAWK_SERVO = "TomahawkServo";
    public static final String LIGHT_CANNON_RELAY = "LightCannonRelay";
    public static final String TURRET = "Turret";
    
    public static final int UNKNOWN_INDEX = 0;
    public static final int RIGHT_DRIVE_SPEED_INDEX = 1;
    public static final int LEFT_DRIVE_SPEED_INDEX = 2;
    public static final int SHIFTER_INDEX = 3;
    public static final int LIGHT_CANNON_RELAY_INDEX = 4;
    /**
     * Constructor for WsOutputManager.
     *
     * All new output elements need to be added in the constructor as well as
     * having a key value added above.
     */
    protected WsOutputManager() {
        //Add the facade data elements
        outputs.addToIndex(UNKNOWN_INDEX, new NoOutput());
        outputs.addToIndex(RIGHT_DRIVE_SPEED_INDEX, new WsDriveSpeed(RIGHT_DRIVE_SPEED, 1, 2));
        outputs.addToIndex(LEFT_DRIVE_SPEED_INDEX, new WsDriveSpeed(LEFT_DRIVE_SPEED, 3, 4));
        outputs.addToIndex(SHIFTER_INDEX, new WsDoubleSolenoid(SHIFTER, 2, 1, 2));
        outputs.addToIndex(LIGHT_CANNON_RELAY_INDEX, new WsRelay(1, 2, Relay.Direction.kForward));
    }
}
