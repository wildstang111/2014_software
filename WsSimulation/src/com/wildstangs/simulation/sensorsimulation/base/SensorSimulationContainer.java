package com.wildstangs.simulation.sensorsimulation.base;

import com.wildstangs.list.WsList;
import com.wildstangs.simulation.gyro.GyroSimulation;
import com.wildstangs.simulation.encoders.DriveBaseEncoders;
import com.wildstangs.simulation.sensorsimulation.BallDetectSwitch;
import com.wildstangs.simulation.sensorsimulation.CatapultDownSwitch;
import com.wildstangs.simulation.sensorsimulation.LatchPositionSwitch;
import com.wildstangs.simulation.sensorsimulation.TensionLimitSwitch;
/**
 *
 * @author Nathan
 */
public class SensorSimulationContainer {

    private static SensorSimulationContainer instance = null;
    private static WsList sensorSimulation = new WsList(10);

    public static SensorSimulationContainer getInstance() {
        if (SensorSimulationContainer.instance == null) {
            SensorSimulationContainer.instance = new SensorSimulationContainer();
        }
        return SensorSimulationContainer.instance;
    }

    public void init() {
        for (int i = 0; i < sensorSimulation.size(); i++) {
            ISensorSimulation sys = (ISensorSimulation) sensorSimulation.get(i);
            if(sys != null) sys.init();
        }
    }

    public void update() {
        for (int i = 0; i < sensorSimulation.size(); i++) {
            ISensorSimulation sys = (ISensorSimulation) sensorSimulation.get(i);
            if(sys != null) sys.update();
        }
    }
    /**
     * Constructor for the subsystem container.
     *
     * Each new subsystem must be added here. This is where they are
     * instantiated as well as placed in the subsystem container.
     */
    protected SensorSimulationContainer() {
      sensorSimulation.add(new TensionLimitSwitch());
      sensorSimulation.add(new GyroSimulation());
      sensorSimulation.add(new DriveBaseEncoders());
      sensorSimulation.add(new CatapultDownSwitch());
      sensorSimulation.add(new LatchPositionSwitch());
      sensorSimulation.add(new BallDetectSwitch());
    }
}
