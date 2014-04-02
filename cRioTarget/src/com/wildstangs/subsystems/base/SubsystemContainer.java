package com.wildstangs.subsystems.base;

import com.wildstangs.list.WsList;
import com.wildstangs.subsystems.AutoMovementControl;
import com.wildstangs.subsystems.BallHandler;
import com.wildstangs.subsystems.Catapult;
import com.wildstangs.subsystems.HotGoalDetector;
import com.wildstangs.subsystems.LandingGear;
import com.wildstangs.subsystems.Wings;
import com.wildstangs.subsystems.WsCompressor;
import com.wildstangs.subsystems.DriveBase;
import com.wildstangs.subsystems.Ears;
import com.wildstangs.subsystems.LED;
import com.wildstangs.subsystems.LightCannon;

/**
 *
 * @author Nathan
 */
public class SubsystemContainer {

    private static SubsystemContainer instance = null;
    private static WsList subsystem = new WsList(10);

    public static SubsystemContainer getInstance() {
        if (SubsystemContainer.instance == null) {
            SubsystemContainer.instance = new SubsystemContainer();
        }
        return SubsystemContainer.instance;
    }

    public void init() {
        for (int i = 0; i < subsystem.size(); i++) {
            Subsystem sys = (Subsystem) subsystem.get(i);
            if(sys != null) sys.init();
        }
    }

    /**
     * Retrieves a subsystem based on a key value.
     *
     * @param key The key representing the subsystem.
     * @return A subsystem.
     */
    public Subsystem getSubsystem(int index) {
        if(index >= 0 && index < subsystem.size())
        {
            return (Subsystem) subsystem.get(index);
        }
        return (Subsystem) null;
    }

    /**
     * Triggers all subsystems to be updated.
     */
    public void update() {
        for (int i = 0; i < subsystem.size(); i++) {
            Subsystem sys = (Subsystem) subsystem.get(i);
            if(sys != null) sys.update();
        }
    }

    /**
     * Notifies all subsystems a config change has occurred and config params
     * should be re-read.
     */
    public void notifyConfigChange() {
        for (int i = 0; i < subsystem.size(); i++) {
            Subsystem sys = (Subsystem) subsystem.get(i);
            if(sys != null) sys.notifyConfigChange();
        }
    }
    //Subsystem keys - must add a new key for each subsystem.
    public static final String DRIVE_BASE = "DriveBase";
    public static final String WS_COMPRESSOR = "WsCompressor";
    public static final String LED = "LED";
    public static final String LIGHT_CANNON = "LightCannon";
    public static final String BALL_HANDLER = "BallHandler";
    public static final String CATAPULT = "Catapult";
    public static final String LANDING_GEAR = "LandingGear";
    public static final String HOT_GOAL_DETECTOR = "HotGoalsDetector";
    public static final String WINGS = "Wings";
    
    public static final int DRIVE_BASE_INDEX = 0;
    public static final int WS_COMPRESSOR_INDEX = 1;
    public static final int LED_INDEX = 2;
    public static final int LIGHT_CANNON_INDEX = 3;
    public static final int BALL_HANDLER_INDEX = 4;
    public static final int CATAPULT_INDEX = 5;
    public static final int LANDING_GEAR_INDEX = 6;
    public static final int HOT_GOAL_DETECTOR_INDEX = 7;
    public static final int WINGS_INDEX = 8;
    public static final int EAR_INDEX = 9;
    public static final int AUTO_MOVEMENT_CONTROLLER = 10;
    
    /**
     * Constructor for the subsystem container.
     *
     * Each new subsystem must be added here. This is where they are
     * instantiated as well as placed in the subsystem container.
     */
    protected SubsystemContainer() {
        subsystem.addToIndex(DRIVE_BASE_INDEX, new DriveBase(DRIVE_BASE));
        subsystem.addToIndex(WS_COMPRESSOR_INDEX, new WsCompressor(WS_COMPRESSOR, 1, 1, 1, 1));
        subsystem.addToIndex(LED_INDEX, new LED(LED));
        subsystem.addToIndex(LIGHT_CANNON_INDEX, new LightCannon(LIGHT_CANNON));
        subsystem.addToIndex(BALL_HANDLER_INDEX, new BallHandler(BALL_HANDLER));
        subsystem.addToIndex(CATAPULT_INDEX, new Catapult(CATAPULT));
        subsystem.addToIndex(LANDING_GEAR_INDEX, new LandingGear(LANDING_GEAR));
        subsystem.addToIndex(HOT_GOAL_DETECTOR_INDEX, new HotGoalDetector(HOT_GOAL_DETECTOR));
        subsystem.addToIndex(WINGS_INDEX, new Wings(WINGS));
//        subsystem.addToIndex(EAR_INDEX, new Ears("EARS"));
        subsystem.addToIndex(AUTO_MOVEMENT_CONTROLLER, new AutoMovementControl("Auto Movement Control"));
    }
}
