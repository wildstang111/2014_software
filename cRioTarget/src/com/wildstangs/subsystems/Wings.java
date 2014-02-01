package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.inputmanager.inputs.joystick.WsJoystickAxisEnum;
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
 * @author mail929
 */
public class Wings extends WsSubsystem implements IObserver
{
    boolean currentState = false;
    
    public Wings(String name)
    {
        super(name);
        registerForJoystickButtonNotification(WsJoystickButtonEnum.MANIPULATOR_BUTTON_6);
    }

    public void init()
    {
    }

    public void update()
    {
        SmartDashboard.putBoolean("Wings (up)", currentState);
        int wingsValue = 0;
        if(currentState == true){
            wingsValue = DoubleSolenoid.Value.kReverse_val;
        }
        else {
            wingsValue = DoubleSolenoid.Value.kForward_val;
        }
        (WsOutputManager.getInstance().getOutput(WsOutputManager.WINGS_SOLENOID_RIGHT_INDEX)).set((IOutputEnum) null, new Integer(wingsValue));
        (WsOutputManager.getInstance().getOutput(WsOutputManager.WINGS_SOLENOID_LEFT_INDEX)).set((IOutputEnum) null, new Integer(wingsValue));
        SmartDashboard.putBoolean("Double Solenoid", currentState);
    }

    public void notifyConfigChange()
    {
    }
    
    public void acceptNotification(Subject subjectThatCaused)
    {
        if(subjectThatCaused.getType() == WsJoystickButtonEnum.MANIPULATOR_BUTTON_6)
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
