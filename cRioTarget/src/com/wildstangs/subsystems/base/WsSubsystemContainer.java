package com.wildstangs.subsystems.base;

import com.wildstangs.list.WsList;
import com.wildstangs.subsystems.BallHandler;
import com.wildstangs.subsystems.Catapult;
import com.wildstangs.subsystems.HotGoalDetector;
import com.wildstangs.subsystems.LandingGear;
import com.wildstangs.subsystems.Wings;
import com.wildstangs.subsystems.WsCompressor;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.WsLED;
import com.wildstangs.subsystems.WsLightCannon;

/**
 *
 * @author Nathan
 */
public class WsSubsystemContainer {

    private static WsSubsystemContainer instance = null;
    private static WsList subsystem = new WsList(10);

    public static WsSubsystemContainer getInstance() {
        if (WsSubsystemContainer.instance == null) {
            WsSubsystemContainer.instance = new WsSubsystemContainer();
        }
        return WsSubsystemContainer.instance;
    }

    public void init() {
        for (int i = 0; i < subsystem.size(); i++) {
            WsSubsystem sys = (WsSubsystem) subsystem.get(i);
            if(sys != null) sys.init();
        }
    }

    /**
     * Retrieves a subsystem based on a key value.
     *
     * @param key The key representing the subsystem.
     * @return A subsystem.
     */
    public WsSubsystem getSubsystem(int index) {
        if(index >= 0 && index < subsystem.size())
        {
            return (WsSubsystem) subsystem.get(index);
        }
        return (WsSubsystem) null;
    }

    /**
     * Triggers all subsystems to be updated.
     */
    public void update() {
        for (int i = 0; i < subsystem.size(); i++) {
            WsSubsystem sys = (WsSubsystem) subsystem.get(i);
            if(sys != null) sys.update();
        }
    }

    /**
     * Notifies all subsystems a config change has occurred and config params
     * should be re-read.
     */
    public void notifyConfigChange() {
        for (int i = 0; i < subsystem.size(); i++) {
            WsSubsystem sys = (WsSubsystem) subsystem.get(i);
            if(sys != null) sys.notifyConfigChange();
        }
    }
    //Subsystem keys - must add a new key for each subsystem.
    public static final String WS_DRIVE_BASE = "WsDriveBase";
    public static final String WS_COMPRESSOR = "WsCompressor";
    public static final String WS_LED = "WsLED";
    public static final String WS_LIGHT_CANNON = "WsLightCannon";
    public static final String BALL_HANDLER = "BallHandler";
    public static final String CATAPULT = "Catapult";
    public static final String LANDING_GEAR = "LandingGear";
    public static final String HOT_GOAL_DETECTOR = "HotGoalsDetector";
    public static final String WINGS = "Wings";
    
    public static final int WS_DRIVE_BASE_INDEX = 0;
    public static final int WS_COMPRESSOR_INDEX = 1;
    public static final int WS_LED_INDEX = 2;
    public static final int WS_LIGHT_CANNON_INDEX = 3;
    public static final int BALL_HANDLER_INDEX = 4;
    public static final int CATAPULT_INDEX = 5;
    public static final int LANDING_GEAR_INDEX = 6;
    public static final int HOT_GOAL_DETECTOR_INDEX = 7;
    public static final int WINGS_INDEX = 8;
    
    /**
     * Constructor for the subsystem container.
     *
     * Each new subsystem must be added here. This is where they are
     * instantiated as well as placed in the subsystem container.
     */
    protected WsSubsystemContainer() {
        subsystem.addToIndex(WS_DRIVE_BASE_INDEX, new WsDriveBase(WS_DRIVE_BASE));
        subsystem.addToIndex(WS_COMPRESSOR_INDEX, new WsCompressor(WS_COMPRESSOR, 1, 1, 1, 1));
        subsystem.addToIndex(WS_LED_INDEX, new WsLED(WS_LED));
        subsystem.addToIndex(WS_LIGHT_CANNON_INDEX, new WsLightCannon(WS_LIGHT_CANNON));
        subsystem.addToIndex(BALL_HANDLER_INDEX, new BallHandler(BALL_HANDLER));
        subsystem.addToIndex(CATAPULT_INDEX, new Catapult(CATAPULT));
        subsystem.addToIndex(LANDING_GEAR_INDEX, new LandingGear(LANDING_GEAR));
        subsystem.addToIndex(HOT_GOAL_DETECTOR_INDEX, new HotGoalDetector(HOT_GOAL_DETECTOR));
        subsystem.addToIndex(WINGS_INDEX, new Wings(WINGS));
    }
}
