package com.wildstangs.profiling;

import edu.wpi.first.wpilibj.Timer;

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
        return spentTime;
    }
}