package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.inputmanager.inputs.joystick.WsJoystickAxisEnum;
import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.outputmanager.base.WsOutputManager;
import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author Joey
 */
public class WsLightCannon extends WsSubsystem implements IObserver {

    private Relay.Value relayState = Relay.Value.kOff;

    public WsLightCannon(String name) {
        super(name);

        registerForJoystickButtonNotification(WsJoystickAxisEnum.DRIVER_D_PAD_UP_DOWN);
    }

    public void acceptNotification(Subject subjectThatCaused) {
        double value = ((DoubleSubject) subjectThatCaused).getValue();
        if (value > 0.5 || value < -0.5) {
            relayState = Relay.Value.kOn;
        } else {
            relayState = Relay.Value.kOff;
        }
        WsOutputManager.getInstance().getOutput(WsOutputManager.LIGHT_CANNON_RELAY).set((IOutputEnum) null, relayState);
    }
}
