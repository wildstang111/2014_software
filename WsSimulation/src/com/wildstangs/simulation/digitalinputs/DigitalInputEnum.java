package com.wildstangs.simulation.digitalinputs;

import com.wildstangs.inputmanager.base.IInputEnum;

/**
 *  Enum types used for the Driver Joystick class.  
 * 
 * @author chadschmidt
 */
public class DigitalInputEnum implements IInputEnum {
    


  private int index;
  private String name;
 
private DigitalInputEnum(int index, String desc){
    this.index = index; 
    this.name = desc; 
    
} 

public static final DigitalInputEnum INPUT1 = new DigitalInputEnum(1, "INPUT1"); 
public static final DigitalInputEnum INPUT2 = new DigitalInputEnum(2, "INPUT2"); 
public static final DigitalInputEnum INPUT3 = new DigitalInputEnum(3, "INPUT3"); 
public static final DigitalInputEnum INPUT4 = new DigitalInputEnum(4, "INPUT4"); 
public static final DigitalInputEnum INPUT5 = new DigitalInputEnum(5, "INPUT5"); 
public static final DigitalInputEnum INPUT6 = new DigitalInputEnum(6, "INPUT6"); 
public static final DigitalInputEnum INPUT7 = new DigitalInputEnum(7, "INPUT7"); 
public static final DigitalInputEnum INPUT8 = new DigitalInputEnum(8, "INPUT8"); 
public static final DigitalInputEnum INPUT9 = new DigitalInputEnum(9, "INPUT9"); 
public static final DigitalInputEnum INPUT10 = new DigitalInputEnum(0, "INPUT10"); 
public static final DigitalInputEnum INPUT11 = new DigitalInputEnum(10, "INPUT11"); 
public static final DigitalInputEnum INPUT12 = new DigitalInputEnum(11, "INPUT12"); 
public static final DigitalInputEnum INPUT13 = new DigitalInputEnum(12, "INPUT13"); 
public static final DigitalInputEnum INPUT14 = new DigitalInputEnum(13, "INPUT14"); 
public static final DigitalInputEnum INPUT15 = new DigitalInputEnum(14, "INPUT15"); 
public static final DigitalInputEnum INPUT16 = new DigitalInputEnum(15, "INPUT16"); 

public static DigitalInputEnum getEnumFromChannel(int channel)
{
    switch (channel)
    {
        case 1:
            return INPUT1;
        case 2:
            return INPUT2;
        case 3:
            return INPUT3;
        case 4:
            return INPUT4;
        case 5:
            return INPUT5;
        case 6:
            return INPUT6;
        case 7:
            return INPUT7;
        case 8:
            return INPUT8;
        case 9:
            return INPUT9;
        case 10:
            return INPUT10;
        case 11:
            return INPUT11;
        case 12:
            return INPUT12;
        case 13:
            return INPUT13;
        case 14:
            return INPUT14;
        case 15:
            return INPUT15;
        case 16:
            return INPUT16;
        default:
            return null;
    }
}
/**
 * Converts the enum type to a String. 
 * 
 * @return A string representing the enum.
 */
    public String toString() {
        return name;
    }
    
/** 
 * Converts the enum type to a numeric value.
 * 
 * @return An integer representing the enum.
 */
    public int toValue() {
        return index;
    }

} 
