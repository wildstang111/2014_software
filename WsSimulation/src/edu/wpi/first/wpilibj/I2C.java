/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Nathan
 */
public class I2C {

    public I2C(DigitalModule module, int deviceAddress) {
    }

    public synchronized boolean transaction(byte[] dataToSend, int sendSize, byte[] dataReceived, int receiveSize) {
        SmartDashboard.putString("LED Data:", "C:0x" + Integer.toHexString((int)dataToSend[0]) + " D1:0x" +
                Integer.toHexString((int)dataToSend[1]) + " D2:0x" + Integer.toHexString((int)dataToSend[2]));
        return false;
    }
}
