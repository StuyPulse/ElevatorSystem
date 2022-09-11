package com.stuypulse.robot.commands;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.IElevator;

public class ElevatorToTop extends ElevatorToHeight {
    
    public ElevatorToTop(IElevator elevator) {
        super(elevator, Settings.Elevator.MAX_HEIGHT);
    }

}
