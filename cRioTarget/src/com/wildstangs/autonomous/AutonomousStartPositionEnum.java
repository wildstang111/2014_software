/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous;

/**
 *
 * @author coder65535
 */
public class AutonomousStartPositionEnum {

    private int index;
    private String description, configName;
    public static final int POSITION_COUNT = 7;//Remember to change when defining new positions.

    public AutonomousStartPositionEnum(int index, String configName, String description) {
        this.configName = configName;
        this.index = index;
        this.description = description;
    }
    public static final AutonomousStartPositionEnum UNKNOWN = new AutonomousStartPositionEnum(0, "unknown", "Unknown Position");
    public static final AutonomousStartPositionEnum CENTER_BACK_PYRAMID = new AutonomousStartPositionEnum(1, "insidePyramidBackCenter", "Inside pyramid, back center");
    public static final AutonomousStartPositionEnum POSITION2 = new AutonomousStartPositionEnum(2, "unknown", "Unknown Position");
    public static final AutonomousStartPositionEnum BACK_LEFT_PYRAMID_INSIDE = new AutonomousStartPositionEnum(3, "insidePyramidBackLeft", "Inside pyramid, back left");
    public static final AutonomousStartPositionEnum BACK_RIGHT_PYRAMID_INSIDE = new AutonomousStartPositionEnum(4, "insidePyramidBackRight", "Inside Pyramid, back right");
    public static final AutonomousStartPositionEnum BACK_RIGHT_PYRAMID_OUTSIDE = new AutonomousStartPositionEnum(5, "outsidePyramidBackRight", "Outside Pyramid, back right");
    public static final AutonomousStartPositionEnum BACK_LEFT_PYRAMID_OUTSIDE = new AutonomousStartPositionEnum(6, "outsidePyramidBackLeft", "Outside Pyramid, back left");
    public static final AutonomousStartPositionEnum POSITION7 = new AutonomousStartPositionEnum(7, "unknown", "Unknown Position");
    public static final AutonomousStartPositionEnum POSITION8 = new AutonomousStartPositionEnum(8, "unknown", "Unknown Position");
    public static final AutonomousStartPositionEnum POSITION9 = new AutonomousStartPositionEnum(9, "unknown", "Unknown Position");

    /**
     * Converts the enum type to a String.
     *
     * @return A string representing the enum.
     */
    public String toString() {
        return description;
    }

    public String toConfigString() {
        return configName;
    }

    /**
     * Converts the enum type to a numeric value.
     *
     * @return An integer representing the enum.
     */
    public int toValue() {
        return index;
    }

    public static AutonomousStartPositionEnum getEnumFromValue(int i) {
        switch (i) {
            case 0:
                return AutonomousStartPositionEnum.UNKNOWN;
            case 1:
                return AutonomousStartPositionEnum.CENTER_BACK_PYRAMID;
            case 2:
                return AutonomousStartPositionEnum.POSITION2;
            case 3:
                return AutonomousStartPositionEnum.BACK_LEFT_PYRAMID_INSIDE;
            case 4:
                return AutonomousStartPositionEnum.BACK_RIGHT_PYRAMID_INSIDE;
            case 5:
                return AutonomousStartPositionEnum.BACK_RIGHT_PYRAMID_OUTSIDE;
            case 6:
                return AutonomousStartPositionEnum.BACK_LEFT_PYRAMID_OUTSIDE;
            case 7:
                return AutonomousStartPositionEnum.POSITION7;
            case 8:
                return AutonomousStartPositionEnum.POSITION8;
            case 9:
                return AutonomousStartPositionEnum.POSITION9;
            default:
                return null;
        }
    }
}
