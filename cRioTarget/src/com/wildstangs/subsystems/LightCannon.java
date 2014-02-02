package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.base.InputManager;
import com.wildstangs.inputmanager.inputs.joystick.JoystickAxisEnum;
import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.outputmanager.base.OutputManager;
import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.Subsystem;
import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author Joey
 */
public class LightCannon extends Subsystem implements IObserver {

    private Relay.Value relayState = Relay.Value.kOff;

    public LightCannon(String name) {
        super(name);

        registerForJoystickButtonNotification(JoystickAxisEnum.MANIPULATOR_DPAD_Y);
    }

    public void acceptNotification(Subject subjectThatCaused) {
        double value = ((DoubleSubject) subjectThatCaused).getValue();
        if (value > 0.5 || value < -0.5) {
            relayState = Relay.Value.kOn;
        } else {
            relayState = Relay.Value.kOff;
        }
        OutputManager.getInstance().getOutput(OutputManager.LIGHT_CANNON_RELAY_INDEX).set(relayState);
    }
}
