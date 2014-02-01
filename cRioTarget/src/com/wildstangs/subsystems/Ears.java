
package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.inputs.joystick.WsJoystickButtonEnum;
import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.outputmanager.base.WsOutputManager;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Sahil
 */
public class Ears extends WsSubsystem implements IObserver{

    boolean currentEarState = false;
            
    public Ears(String name) {
        super(name);
        registerForJoystickButtonNotification(WsJoystickButtonEnum.MANIPULATOR_BUTTON_3);
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
       (WsOutputManager.getInstance().getOutput(WsOutputManager.EARS_SOLENOID_INDEX)).set((IOutputEnum) null, new Integer(earValue));      
    }

    public void notifyConfigChange() {
    }

    public void acceptNotification(Subject subjectThatCaused) {
        
            if(subjectThatCaused.getType() == WsJoystickButtonEnum.MANIPULATOR_BUTTON_3) {
                   if(((BooleanSubject)subjectThatCaused).getValue()) {
                       currentEarState = true;
                   }
                  
                   else {
                      currentEarState = false;
                    }
            }
    }
}