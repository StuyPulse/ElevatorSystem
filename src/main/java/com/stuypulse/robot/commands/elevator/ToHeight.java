package com.stuypulse.robot.commands.elevator;

import com.stuypulse.robot.subsystems.elevator.Elevator;

public class ToHeight extends MoveDistance {

	public ToHeight(Elevator elevator, double height) { 
		super(elevator, height - elevator.getState().position);
	}
    
}
