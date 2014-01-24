/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.list;

import edu.wpi.first.wpilibj.networktables2.util.List;

/**
 *
 * @author Joey
 */
public class WsList extends List
{

    public WsList(int initialSize)
    {
        super(initialSize);
    }

    public WsList()
    {
        super();
    }
    
    public void addToIndex(int index, Object object)
    {
        if(index >= this.size)
        {
            Object[] newArray = new Object[index + 1];
            
            System.arraycopy(this.array, 0, newArray, 0, this.size);
            
            this.array = newArray;
            
            size = index + 1;
        }
        
        this.set(index, object);
    }
}
