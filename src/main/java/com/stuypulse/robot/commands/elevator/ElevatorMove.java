package com.stuypulse.robot.commands.elevator;

import com.stuypulse.robot.subsystems.elevator.Elevator;

import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.TrapezoidProfileCommand;

public class ElevatorMove extends TrapezoidProfileCommand {

	public ElevatorMove( Elevator elevator, double distance ) { 
		this (elevator, 0.0, 0.0, distance);
	}

	public ElevatorMove(Elevator elevator, double maxVelocity, double maxAcceleration, double distance) {
		super(new TrapezoidProfile(
				new TrapezoidProfile.Constraints(maxVelocity, maxAcceleration),
				new TrapezoidProfile.State(distance, 0)
			),
			elevator::setTargetState,
			elevator
		);
	}

	public ElevatorMove(Elevator elevator, TrapezoidProfile profile) {
		super(profile, elevator::setTargetState, elevator);
	}


}
