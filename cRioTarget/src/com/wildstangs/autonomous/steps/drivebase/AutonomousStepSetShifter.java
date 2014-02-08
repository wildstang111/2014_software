/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.drivebase;

import com.wildstangs.autonomous.steps.AutonomousStep;
import com.wildstangs.subsystems.DriveBase;
import com.wildstangs.subsystems.base.SubsystemContainer;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 *
 * @author Joey
 */
public class AutonomousStepSetShifter extends AutonomousStep
{
    protected DoubleSolenoid.Value state;
    public AutonomousStepSetShifter(DoubleSolenoid.Value state)
    {
        this.state = state;
    }
    
    
    public void initialize()
    {
        ((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.DRIVE_BASE_INDEX)).setShifter(state);
        this.finished = true;
    }

    public void update()
    {
    }

    public String toString()
    {
        return "Set Shifter State";
    }
    
}
