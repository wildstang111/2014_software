package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.inputs.joystick.WsJoystickButtonEnum;
import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.outputmanager.base.WsOutputManager;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author mail929
 */
public class LandingGear extends WsSubsystem implements IObserver{
    boolean landingGearState = false;
    
    public LandingGear(String name) {
        super(name);
        registerForJoystickButtonNotification(WsJoystickButtonEnum.DRIVER_BUTTON_6);
    }

    public void init() {
    }

    public void update() {
        (WsOutputManager.getInstance().getOutput(WsOutputManager.LANDING_GEAR_SOLENOID_INDEX)).set((IOutputEnum) null, new Boolean(landingGearState));
        SmartDashboard.putBoolean("LandingGearState", landingGearState);
    }
    
    public void acceptNotification(Subject subjectThatCaused){
         if (WsJoystickButtonEnum.DRIVER_BUTTON_6 == subjectThatCaused.getType()){
            if (((BooleanSubject)subjectThatCaused).getValue()){
                landingGearState = !landingGearState;
            }
        }       
    }
    
    public void notifyConfigChange() {
        
    }
}
