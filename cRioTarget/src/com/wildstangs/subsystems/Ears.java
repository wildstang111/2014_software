
package com.wildstangs.subsystems;

import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.inputmanager.inputs.joystick.JoystickButtonEnum;
import com.wildstangs.inputmanager.inputs.joystick.JoystickDPadButtonEnum;
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
    
    protected final IntegerConfigFileParameter FIRST_EAR_UP_CONFIG = new IntegerConfigFileParameter(this.getClass().getName(), "EarPreset.First.Up", 90);
    protected final IntegerConfigFileParameter FIRST_EAR_DOWN_CONFIG = new IntegerConfigFileParameter(this.getClass().getName(), "EarPreset.First.Down", 0);
    protected final IntegerConfigFileParameter SECOND_EAR_UP_CONFIG = new IntegerConfigFileParameter(this.getClass().getName(), "EarPreset.Second.Up", 0);
    protected final IntegerConfigFileParameter SECOND_EAR_DOWN_CONFIG = new IntegerConfigFileParameter(this.getClass().getName(), "EarPreset.Second.down", 90);
    
    boolean currentEarState = false;
    
    protected int firstEarUp, secondEarUp, firstEarDown, secondEarDown;
    
    public Ears(String name) {
        super(name);
        registerForJoystickButtonNotification(JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_UP);
        registerForJoystickButtonNotification(JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_DOWN);
        
        firstEarUp = FIRST_EAR_UP_CONFIG.getValue();
        secondEarUp = SECOND_EAR_UP_CONFIG.getValue();
        
        firstEarDown = FIRST_EAR_DOWN_CONFIG.getValue();
        secondEarDown = SECOND_EAR_DOWN_CONFIG.getValue();
    }

    public void init() {
        currentEarState = false;
    }
    
    public void update() {
       SmartDashboard.putBoolean("Ears are up", currentEarState);
       
       (OutputManager.getInstance().getOutput(OutputManager.FIRST_EAR_SERVO)).set(new Integer(currentEarState ? firstEarUp : firstEarDown));      
       (OutputManager.getInstance().getOutput(OutputManager.SECOND_EAR_SERVO)).set(new Integer(currentEarState ? secondEarUp : secondEarDown));  
    }

    public void notifyConfigChange() {
        firstEarUp = FIRST_EAR_UP_CONFIG.getValue();
        secondEarUp = SECOND_EAR_UP_CONFIG.getValue();
        
        firstEarDown = FIRST_EAR_DOWN_CONFIG.getValue();
        secondEarDown = SECOND_EAR_DOWN_CONFIG.getValue();
    }

    public void acceptNotification(Subject subjectThatCaused) 
    {
        boolean buttonState = ((BooleanSubject) subjectThatCaused).getValue();
        if(buttonState)
        {
            if (subjectThatCaused.getType() == JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_UP)
            {
                this.currentEarState = true;
            }
            else if(subjectThatCaused.getType() == JoystickDPadButtonEnum.MANIPULATOR_D_PAD_BUTTON_DOWN)
            {
                this.currentEarState = false;
            }
        }
    }
    
    public void setEarState (boolean currentEarState) {
        this.currentEarState = currentEarState;
    }

}