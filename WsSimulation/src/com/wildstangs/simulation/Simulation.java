package com.wildstangs.simulation;

import com.wildstangs.simulation.solenoids.SolenoidContainer;
import com.wildstangs.crio.FrameworkAbstraction;
import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.logger.*;
import com.wildstangs.logviewer.LogViewer;
import com.wildstangs.profiling.ProfilingTimer;
import com.wildstangs.simulation.sensorsimulation.base.SensorSimulationContainer;
import com.wildstangs.subsystems.AutoMovementControl;
import com.wildstangs.subsystems.base.SubsystemContainer;
import edu.wpi.first.wpilibj.DriverStation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author ChadS
 */
public class Simulation implements ActionListener, ChangeListener, SequentialTaskExecutor.SequentialTaskExecutorCompletionListener {

    private static Simulation instance;

    static String c = "Simulation";

    static boolean autonomousRun = false;

    //Display graphs 
    static boolean intakeMotorGraphs = false;
    static boolean driveMotorGraphs = true;
    static boolean flywheelSpeedGraphs = false;
    static boolean driveThrottleGraph = true;

    // JComponents for control
    private JFrame modeSwitcher;
    private JRadioButton teleoperatedButton;
    private JRadioButton autonomousButton;
    private JRadioButton practiceButton;
    private JButton enable;
    private JButton disable;
    private JSlider autonProgram;
    private JSlider autonPosition;
    private JToggleButton autonLockIn;

    // Store what state we're in
    private boolean isEnabled = false;

    private enum SimulationState {

        AUTON, TELEOP, PRACTICE
    }
    private SimulationState state;

    // Timers/TimerTasks for executing our functions periodically
    Timer timer;
    AutonPeriodicTask autonTask;
    TeleopPeriodicTask teleopTask;
    DisabledPeriodicTask disabledTask;
    SequentialTaskExecutor practiceModeExecutor;

    static ProfilingTimer durationTimer = new ProfilingTimer("Periodic method duration", 50);
    static ProfilingTimer periodTimer = new ProfilingTimer("Periodic method period", 50);

    public Simulation() {
        timer = new Timer();

        state = SimulationState.TELEOP;

        modeSwitcher = new JFrame("WildStang Simulation");
        teleoperatedButton = new JRadioButton("Teleoperated");
        teleoperatedButton.addActionListener(this);
        autonomousButton = new JRadioButton("Autonomous");
        autonomousButton.addActionListener(this);
        practiceButton = new JRadioButton("Practice");
        practiceButton.addActionListener(this);
        enable = new JButton("Enable");
        enable.addActionListener(this);
        disable = new JButton("Disable");
        disable.addActionListener(this);
        disable.setEnabled(false);
        autonProgram = new JSlider(0, 500, 0);
        autonProgram.addChangeListener(this);
        autonPosition = new JSlider(0, 500, 0);
        autonPosition.addChangeListener(this);
        autonLockIn = new JToggleButton("Autonomous not locked in");
        autonLockIn.addActionListener(this);

        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(teleoperatedButton);
        modeGroup.add(autonomousButton);
        modeGroup.add(practiceButton);

        JPanel modeButtons = new JPanel();
        modeButtons.setLayout(new GridLayout(0, 1));
        modeButtons.add(teleoperatedButton);
        modeButtons.add(autonomousButton);
        modeButtons.add(practiceButton);
        teleoperatedButton.setSelected(true);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;

        JComponent content = (JComponent) modeSwitcher.getContentPane();
        content.setLayout(new GridBagLayout());
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        c.gridx = 0;
        c.gridy = 0;
        content.add(modeButtons, c);
        c.gridy = 1;
        content.add(enable, c);
        c.gridx = 1;
        content.add(disable, c);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        JLabel autonProgramLabel = new JLabel("Auton Program");
        autonProgramLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        content.add(autonProgramLabel, c);
        c.gridy = 3;
        autonProgram.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        content.add(autonProgram, c);
        c.gridy = 4;
        content.add(new JLabel("Auton Position"), c);
        c.gridy = 5;
        autonPosition.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        content.add(autonPosition, c);
        c.gridy = 6;
        autonLockIn.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        content.add(autonLockIn, c);

        modeSwitcher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        modeSwitcher.setSize(new Dimension(500, 500));
        modeSwitcher.setResizable(false);
        modeSwitcher.pack();
        modeSwitcher.setLocation(new Point(600, 650));
        modeSwitcher.setVisible(true);
    }

    public static Simulation getInstance() {
        if (instance == null) {
            instance = new Simulation();
        }
        return instance;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        getInstance();

        // Set the property for the JInput library path
        System.setProperty("net.java.games.input.librarypath", System.getProperty("user.dir") + File.separator + "lib");

        // Start the log viewer.
        (new Thread(new LogViewer())).start();
        FileLogger.getFileLogger().startLogger();

        FrameworkAbstraction.robotInit("/Config/ws_config.txt");

        Logger logger = Logger.getLogger();

        logger.always(c, "sim_startup", "Simulation starting.");
        FileLogger.getFileLogger().logData("Sim Started");

        logger.always(c, "sim_startup", "Simulation init done.");

        getInstance().startDisabled();
        
        DriverStation.getInstance().setDigitalIn(1, true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        if (source == autonomousButton) {
            state = SimulationState.AUTON;
            System.out.println("Autonomous");
        } else if (source == teleoperatedButton) {
            state = SimulationState.TELEOP;
            System.out.println("Teleop");
        } else if (source == practiceButton) {
            state = SimulationState.PRACTICE;
            System.out.println("Practice");
        } else if (source == enable) {
            System.out.println("Enable");
            // Disable the enable button
            enable.setEnabled(false);
            disable.setEnabled(true);

            // Disable the mode-switching buttons
            teleoperatedButton.setEnabled(false);
            autonomousButton.setEnabled(false);
            practiceButton.setEnabled(false);

            // Stop the disabledPeriodic task
            disabledTask.cancel();

            // Set the enabled flag
            isEnabled = true;

            switch (state) {
                case AUTON:
                    FrameworkAbstraction.autonomousInit();
                    SensorSimulationContainer.getInstance().init();
                    startAuton();
                    break;
                case TELEOP:
                    FrameworkAbstraction.teleopInit();
                    SensorSimulationContainer.getInstance().init();
                    startTeleop();
                    break;
                case PRACTICE:
                    startPracticeMode();
                    break;
            }
        } else if (source == disable) {
            System.out.println("Disable");
            // Disable the disable button
            enable.setEnabled(true);
            disable.setEnabled(false);

            // Enable the mode-switching buttons
            teleoperatedButton.setEnabled(true);
            autonomousButton.setEnabled(true);
            practiceButton.setEnabled(true);

            // Stop the execution of all our tasks
            switch (state) {
                case AUTON:
                    autonTask.cancel();
                    break;
                case TELEOP:
                    teleopTask.cancel();
                    break;
                case PRACTICE:
                    practiceModeExecutor.cancelExecution();
                    break;
            }

            isEnabled = false;
            FrameworkAbstraction.disabledInit();
            startDisabled();
        } else if (source == autonLockIn) {
            System.out.println("Simulated lockin switch: " + autonLockIn.isSelected());
            DriverStation.getInstance().setDigitalIn(1, !autonLockIn.isSelected());
            if (autonLockIn.isSelected()) {
                autonLockIn.setText("Autonomous locked in");
            } else {
                autonLockIn.setText("Autonomous not locked in");
            }
        }
    }

    @Override
    public void executionCompleted(SequentialTaskExecutor executor) {
        if (executor == practiceModeExecutor) {
            System.out.println("Disable");
            // Disable the disable button
            enable.setEnabled(true);
            disable.setEnabled(false);

            // Enable the mode-switching buttons
            teleoperatedButton.setEnabled(true);
            autonomousButton.setEnabled(true);
            practiceButton.setEnabled(true);

            isEnabled = false;
            FrameworkAbstraction.disabledInit();
            startDisabled();
        }
    }

    private void startAuton() {
        autonTask = new AutonPeriodicTask();
        timer.scheduleAtFixedRate(autonTask, 0, 20);
    }

    private void startTeleop() {
        teleopTask = new TeleopPeriodicTask();
        timer.scheduleAtFixedRate(teleopTask, 0, 20);
    }

    private void startDisabled() {
        disabledTask = new DisabledPeriodicTask();
        timer.scheduleAtFixedRate(disabledTask, 0, 20);
    }

    private void startPracticeMode() {
        practiceModeExecutor = new SequentialTaskExecutor();
        practiceModeExecutor.addTask(new AutonInitTask());
        practiceModeExecutor.addTask(new AutonPeriodicTask(), 10 * 1000, 20);
        practiceModeExecutor.addTask(new DisabledInitTask());
        practiceModeExecutor.addTask(new DisabledPeriodicTask(), 1 * 1000, 20);
        practiceModeExecutor.addTask(new TeleopInitTask());
        practiceModeExecutor.addTask(new TeleopPeriodicTask(), 140 * 1000, 20);
        practiceModeExecutor.setCompletionListener(this);
        practiceModeExecutor.executeScheduledTasks();
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        Object source = ce.getSource();
        if (source == autonProgram) {
            DriverStation.getInstance().setAnalogIn(1, ((double) autonProgram.getValue()) / 100);
        } else if (source == autonPosition) {
            DriverStation.getInstance().setAnalogIn(2, ((double) autonPosition.getValue()) / 100);
        }
    }

    private class TeleopPeriodicTask extends TimerTask {

        @Override
        public void run() {
            periodTimer.endTimingSection();
            periodTimer.startTimingSection();
            durationTimer.startTimingSection();

            InputManager.getInstance().updateSensorData();
            FrameworkAbstraction.teleopPeriodic();
            SolenoidContainer.getInstance().update();
            SensorSimulationContainer.getInstance().update();
        }
    }

    private class AutonPeriodicTask extends TimerTask {

        @Override
        public void run() {
            periodTimer.endTimingSection();
            periodTimer.startTimingSection();
            durationTimer.startTimingSection();

            InputManager.getInstance().updateSensorData();
            FrameworkAbstraction.autonomousPeriodic();
            SolenoidContainer.getInstance().update();
            SensorSimulationContainer.getInstance().update();
        }
    }

    private class DisabledPeriodicTask extends TimerTask {

        @Override
        public void run() {
            periodTimer.endTimingSection();
            periodTimer.startTimingSection();
            durationTimer.startTimingSection();

            FrameworkAbstraction.disabledPeriodic();
        }
    }

    private class DisabledInitTask extends TimerTask {

        @Override
        public void run() {
            FrameworkAbstraction.disabledInit();
        }
    }

    private class AutonInitTask extends TimerTask {

        @Override
        public void run() {
            FrameworkAbstraction.autonomousInit();
        }
    }

    private class TeleopInitTask extends TimerTask {

        @Override
        public void run() {
            FrameworkAbstraction.teleopInit();
        }
    }
}
