package com.stuypulse.robot.commands;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.IElevator;

public class ElevatorToBottom extends ElevatorToHeight {
    
    public ElevatorToBottom(IElevator elevator) {
        super(elevator, Settings.Elevator.MIN_HEIGHT);
    }

}
