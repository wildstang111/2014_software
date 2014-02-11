
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
 * @author Sahil
 */
public class Ears extends Subsystem implements IObserver{

    boolean currentEarState = false;
            
    public Ears(String name) {
        super(name);
        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_3);
    }

    public void init() {
    }
    
    public void update() {
       SmartDashboard.putBoolean("Ears are up", currentEarState);
       int earValue = 0;
       if(currentEarState == true) {
           earValue = DoubleSolenoid.Value.kReverse_val;
       }
       else {
           earValue = DoubleSolenoid.Value.kForward_val;
       }
       (OutputManager.getInstance().getOutput(OutputManager.EARS_SOLENOID_INDEX)).set(new Integer(earValue));      
    }

    public void notifyConfigChange() {
    }

    public void acceptNotification(Subject subjectThatCaused) {
        
            if(subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_3) {
                   if(((BooleanSubject)subjectThatCaused).getValue()) {
                       currentEarState = true;
                   }
                  
                   else {
                      currentEarState = false;
                    }
            }
    }
    public void setEarState (boolean currentEarState) {
        this.currentEarState = currentEarState;
    }
            
}