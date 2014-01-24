/*
 *  This file is part of frcjcss.
 *
 *  frcjcss is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  frcjcss is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with frcjcss.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.wpi.first.wpilibj;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * FRC Servo simulation.
 *
 * @author Nick DiRienzo
 * @version 11.23.2010.0
 */
public class Servo {
    //TODO: Implement angle. The servo angle is linear to the PWM value (assumed by FIRST).
    //			Check the Servo FRC documentation regarding angles.

    private double position;
    protected int channel;

    public Servo(int channel) {
        position = 0;
        this.channel = channel;
    }

    /**
     * Sets the position. Position ranges from 0.0 (full left) to 1.0 (full
     * right).
     *
     * @param value Position ranging from 0.0 to 1.0.
     */
    public void set(double value) {
        position = limit(value);
        SmartDashboard.putNumber("Servo: " + channel + " Position", position);
    }

    public void setAngle(double angle) {
        position = angle;
        SmartDashboard.putNumber("Servo: " + channel + " Position (degrees)", position);
    }

    /**
     * Gets the position.
     */
    public double get() {
        return position;
    }

    /**
     * Limits to -1.0 and 1.0.
     *
     * @param value The value that needs to be limited.
     * @return The limited value if greater than 1.0 or less than -1.0,
     * otherwise the original value.
     */
    private double limit(double value) {
        double l = 0.0;
        if (value > 1.0) {
            l = 1.0;
        } else if (value < -1.0) {
            l = -1.0;
        }
        return l;
    }
}
