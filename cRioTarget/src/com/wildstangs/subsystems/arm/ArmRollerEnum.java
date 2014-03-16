/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems.arm;

/**
 *
 * @author Jason
 */
public class ArmRollerEnum {
    
    public static final ArmRollerEnum INTAKE = new ArmRollerEnum("Intake");
    
    public static final ArmRollerEnum  OUTPUT = new ArmRollerEnum("Output");
    
    public static final ArmRollerEnum OFF = new ArmRollerEnum("Off");
    
    protected String name;
    protected ArmRollerEnum(String name){
        this.name = name;
    }

    public String toString()
    {
        return name;
    }
}
