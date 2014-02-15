package com.wildstangs.pid.controller.base;

/**
 *
 * @author Nathan
 */
public class PidStateType {

    private String title;

    public PidStateType(String name) {
        this.title = name;
    }
    public static PidStateType PID_DISABLED_STATE = new PidStateType("PID_DISABLED_STATE");
    public static PidStateType PID_INITIALIZE_STATE = new PidStateType("PID_INITIALIZE_STATE");
    public static PidStateType PID_BELOW_TARGET_STATE = new PidStateType("PID_BELOW_TARGET_STATE");
    public static PidStateType PID_ON_TARGET_STATE = new PidStateType("PID_ON_TARGET_STATE");
    public static PidStateType PID_STABILIZED_STATE = new PidStateType("PID_STABILIZED_STATE");
    public static PidStateType PID_ABOVE_TARGET_STATE = new PidStateType("PID_ABOVE_TARGET_STATE");
    
    public String toString(){
        return title; 
    }
}
