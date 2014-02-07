/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.inputs.joystick.JoystickButtonEnum;
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
public class BackupWings extends Subsystem implements IObserver
{
    boolean currentState = false;
    
    public BackupWings(String name)
    {
        super(name);
        registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_1);
        
    }

    public void init()
    {
        
    }
    
    public void update()
    {
        //these are currently double solenoids
        (OutputManager.getInstance().getOutput(OutputManager.WINGS_SOLENOID_LEFT_INDEX)).set(new Boolean(currentState));
        (OutputManager.getInstance().getOutput(OutputManager.WINGS_SOLENOID_RIGHT_INDEX)).set(new Boolean(currentState));
        SmartDashboard.putBoolean("Wings", currentState);
    }
    public void acceptNotification(Subject subjectThatCaused)
    {
        if(subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_1)
        {
            currentState = ((BooleanSubject)subjectThatCaused).getValue();
        }
    }
}
