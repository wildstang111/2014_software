/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.outputmanager.base;

import com.wildstangs.outputmanager.base.IOutput;
import com.wildstangs.outputmanager.base.IOutputEnum;

/**
 *
 * @author chadschmidt
 */
public interface IServo extends IOutput{

    void setAngle(IOutputEnum key, Object value);
    
}
