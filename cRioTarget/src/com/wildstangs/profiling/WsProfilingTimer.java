package com.wildstangs.profiling;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class WsProfilingTimer {

    double startingTime;
    double endingTime;
    int iterations;
    String name;

    public WsProfilingTimer(String name, int iterations) {
        this.iterations = iterations;
        this.name = name;
    }

    public void startTimingSection() {
        startingTime = Timer.getFPGATimestamp();
    }

    public double endTimingSection() {
        double spentTime = 0;
        endingTime = Timer.getFPGATimestamp();
        spentTime = endingTime - startingTime;
//        SmartDashboard.putNumber(name, spentTime);
        return spentTime;
    }
}