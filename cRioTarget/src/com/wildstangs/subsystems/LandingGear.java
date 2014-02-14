package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.inputs.joystick.JoystickButtonEnum;
import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.outputmanager.base.OutputManager;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author mail929
 */
public class LandingGear extends Subsystem implements IObserver{
    boolean landingGearState = false;
    
    public LandingGear(String name) {
        super(name);
        registerForJoystickButtonNotification(JoystickButtonEnum.DRIVER_BUTTON_5);
    }

    public void init() {
        landingGearState = false;
    }

    public void update() {
        (OutputManager.getInstance().getOutput(OutputManager.LANDING_GEAR_SOLENOID_INDEX)).set(new Boolean(landingGearState));
        SmartDashboard.putBoolean("LandingGearState", landingGearState);
    }
    
    public void acceptNotification(Subject subjectThatCaused){
         if (JoystickButtonEnum.DRIVER_BUTTON_5 == subjectThatCaused.getType()){
            if (((BooleanSubject)subjectThatCaused).getValue()){
                landingGearState = !landingGearState;
            }
        }       
    }
    
    public void notifyConfigChange() {
        
    }
}
