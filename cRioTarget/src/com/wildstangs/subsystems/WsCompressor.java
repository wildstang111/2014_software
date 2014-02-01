package com.wildstangs.subsystems;

import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.inputmanager.base.IInput;
import com.wildstangs.inputmanager.base.IInputEnum;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Liam
 */
public class WsCompressor extends WsSubsystem
{
    protected final DoubleConfigFileParameter LOW_VOLTAGE_CONFIG, HIGH_VOLTAGE_CONFIG, MAX_PSI_CONFIG;
    protected double lowVoltage, highVoltage, maxPSI;
    
    protected Compressor compressor;

    public WsCompressor(String name, int pressureSwitchSlot, int pressureSwitchChannel, int compresssorRelaySlot, int compressorRelayChannel)
    {
        super(name);
        compressor = new Compressor(pressureSwitchSlot, pressureSwitchChannel, compresssorRelaySlot, compressorRelayChannel);
        compressor.start();
        
        LOW_VOLTAGE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "LowVoltage", 0.5);
        HIGH_VOLTAGE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "HighVoltage", 4.0);
        MAX_PSI_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "MaxPSI", 115);
        
        lowVoltage = LOW_VOLTAGE_CONFIG.getValue();
        highVoltage = HIGH_VOLTAGE_CONFIG.getValue();
        maxPSI = MAX_PSI_CONFIG.getValue();
    }

    public void init()
    {
    }

    public void update()
    {
        IInput pressureTransducer = WsInputManager.getInstance().getSensorInput(WsInputManager.PRESSURE_TRANSDUCER_INDEX);
        
        double voltage = ((Double) pressureTransducer.get((IInputEnum) null)).doubleValue();
        
        SmartDashboard.putNumber("Pressure Transducer Voltage", voltage);
        
        double psi = maxPSI * ((voltage - lowVoltage) / (highVoltage - lowVoltage));
        
        SmartDashboard.putNumber("PSI", psi);
    }

    public void notifyConfigChange()
    {
        lowVoltage = LOW_VOLTAGE_CONFIG.getValue();
        highVoltage = HIGH_VOLTAGE_CONFIG.getValue();
        maxPSI = MAX_PSI_CONFIG.getValue();
    }
}
