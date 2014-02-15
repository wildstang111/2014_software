package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.inputs.joystick.JoystickButtonEnum;
import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.outputmanager.base.OutputManager;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.Subsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
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
        SmartDashboard.putBoolean("LandingGearState", landingGearState);
        
      int earValue = 0;
       if(landingGearState == true) {
           earValue = DoubleSolenoid.Value.kReverse_val;
       }
       else {
           earValue = DoubleSolenoid.Value.kForward_val;
       }
       (OutputManager.getInstance().getOutput(OutputManager.LANDING_GEAR_SOLENOID_INDEX)).set(new Integer(earValue));      
    }
    
    public void acceptNotification(Subject subjectThatCaused){
         if(subjectThatCaused.getType() == JoystickButtonEnum.DRIVER_BUTTON_5)
        {
            if(((BooleanSubject)subjectThatCaused).getValue())
            {
                landingGearState = true;
            }
            else
            {
                landingGearState = false;
            }
        }       
    }
    
    public void notifyConfigChange() {
        
    }
}
