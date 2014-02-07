package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.inputmanager.inputs.joystick.JoystickAxisEnum;
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
public class Wings extends Subsystem implements IObserver
{
    boolean currentState = false;
    
    public Wings(String name)
    {
        super(name);
        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_1);
    }

    public void init()
    {
    }

    public void update()
    {
        int wingsValue = 0;
        if(currentState == true){
            wingsValue = DoubleSolenoid.Value.kReverse_val;
        }
        else {
            wingsValue = DoubleSolenoid.Value.kForward_val;
        }
        (OutputManager.getInstance().getOutput(OutputManager.WINGS_SOLENOID_RIGHT_INDEX)).set(new Integer(wingsValue));
        (OutputManager.getInstance().getOutput(OutputManager.WINGS_SOLENOID_LEFT_INDEX)).set(new Integer(wingsValue));
        SmartDashboard.putBoolean("Wing State", currentState);
    }

    public void notifyConfigChange()
    {
    }
    
    public void acceptNotification(Subject subjectThatCaused)
    {
        if(subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_1)
        {
            if(((BooleanSubject)subjectThatCaused).getValue())
            {
                currentState = true;
            }
            else
            {
                currentState = false;
            }
        }
    }
}
