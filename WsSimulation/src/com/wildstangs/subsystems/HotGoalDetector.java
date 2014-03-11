/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems;

import com.wildstangs.subsystems.base.Subsystem;
import java.util.Random;

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
    
    public void setLedState(boolean on)
    {
    }
    
    protected Random rnd = new Random();
    
    public boolean checkForHotGoal()
    {
        return rnd.nextBoolean();
    }
    
    public boolean wasLastTargetHot()
    {
        return rnd.nextBoolean();
    }
    
    public static class HotGoalSideEnum
    {
        protected String denotion;
        protected HotGoalSideEnum(String denotion)
        {
            this.denotion = denotion;
        }

        public String toString()
        {
            return denotion;
        }
        
        public static final HotGoalSideEnum LEFT = new HotGoalSideEnum("LEFT");
        public static final HotGoalSideEnum RIGHT = new HotGoalSideEnum("RIGHT");
        public static final HotGoalSideEnum NONE = new HotGoalSideEnum("NONE");
        public static final HotGoalSideEnum EITHER = new HotGoalSideEnum("EITHER"); //This is mostly for Auto
    }
    
    public HotGoalSideEnum getLastReportSide()
    {
        switch(rnd.nextInt(3))
        {
            case 0:
            {
                return HotGoalSideEnum.LEFT;
            }
            case 1:
            {
                return HotGoalSideEnum.RIGHT;
            }
            default:
            {
                return HotGoalSideEnum.NONE;
            }
        }
    }
}
