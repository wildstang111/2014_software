package com.wildstangs.pid.controller.base;

/**
 *
 * @author Nathan
 */
public class PidStateType {

    private String title;

    public PidStateType(String name) {
        this.title = title;
    }
    public static PidStateType WS_PID_DISABLED_STATE = new PidStateType("WS_PID_DISABLED_STATE");
    public static PidStateType WS_PID_INITIALIZE_STATE = new PidStateType("WS_PID_INITIALIZE_STATE");
    public static PidStateType WS_PID_BELOW_TARGET_STATE = new PidStateType("WS_PID_BELOW_TARGET_STATE");
    public static PidStateType WS_PID_ON_TARGET_STATE = new PidStateType("WS_PID_ON_TARGET_STATE");
    public static PidStateType WS_PID_STABILIZED_STATE = new PidStateType("WS_PID_STABILIZED_STATE");
    public static PidStateType WS_PID_ABOVE_TARGET_STATE = new PidStateType("WS_PID_ABOVE_TARGET_STATE");
}
