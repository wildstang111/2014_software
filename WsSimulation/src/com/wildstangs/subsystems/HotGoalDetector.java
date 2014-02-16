/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems;

import com.wildstangs.subsystems.base.Subsystem;

/**
 *
 * @author Joey
 */
//We need this because of the camera access in the robot subsystem
public class HotGoalDetector extends Subsystem
{
    public HotGoalDetector(String name)
    {
        super(name);
    }

    @Override
    public void init()
    {
    }

    @Override
    public void update()
    {
    }

    @Override
    public void notifyConfigChange()
    {
    }
}
