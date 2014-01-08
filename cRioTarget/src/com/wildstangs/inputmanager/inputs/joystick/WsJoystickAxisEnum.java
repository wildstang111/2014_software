/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.inputmanager.inputs.joystick;

import com.wildstangs.inputmanager.base.IInputEnum;

/**
 *
 * @author Joey
 */
public class WsJoystickAxisEnum implements IInputEnum {
    
    protected int index;
    protected String name;

    protected WsJoystickAxisEnum(int index, String desc) 
    {
        this.index = index;
        this.name = desc;
    }
    
    //Driver Axis Control
    public static final WsJoystickAxisEnum DRIVER_THROTTLE = new WsJoystickAxisEnum(0, "DRIVER_THROTTLE");
    public static final WsJoystickAxisEnum DRIVER_HEADING = new WsJoystickAxisEnum(1, "DRIVER_HEADING");
    public static final WsJoystickAxisEnum DRIVER_D_PAD_UP_DOWN = new WsJoystickAxisEnum(2, "DRIVER_D_PAD_UP_DOWN");
    
    //Manipulator Axis Control
    public static final WsJoystickAxisEnum MANIPULATOR_ENTER_FLYWHEEL_ADJUSTMENT = new WsJoystickAxisEnum(0, "MANIPULATOR_ENTER_FLYWHEEL_ADJUSTMENT");
    public static final WsJoystickAxisEnum MANIPULATOR_EXIT_FLYWHEEL_ADJUSTMENT = new WsJoystickAxisEnum(1, "MANIPULATOR_EXIT_FLYWHEEL_ADJUSTMENT");
    public static final WsJoystickAxisEnum MANIPULATOR_D_PAD_UP_DOWN = new WsJoystickAxisEnum(2, "MANIPULATOR_D_PAD_UP_DOWN");
    public static final WsJoystickAxisEnum MANIPULATOR_D_PAD_LEFT_RIGHT = new WsJoystickAxisEnum(3, "MANIPULATOR_D_PAD_LEFT_RIGHT");

    public String toString() 
    {
        return name;
    }
    
    public int toValue()
    {
        return index;
    }
    
    
}
