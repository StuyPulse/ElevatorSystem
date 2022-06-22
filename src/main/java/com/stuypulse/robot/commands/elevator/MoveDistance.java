package com.stuypulse.robot.commands.elevator;

import com.stuypulse.robot.subsystems.elevator.Elevator;

import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.TrapezoidProfileCommand;

public class MoveDistance extends TrapezoidProfileCommand {

	public MoveDistance(Elevator elevator, double distance) { 
		this(elevator, 0.0, 0.0, distance);
	}

	public MoveDistance(Elevator elevator, double maxVelocity, double maxAcceleration, double distance) {
		super(new TrapezoidProfile(
				new TrapezoidProfile.Constraints(maxVelocity, maxAcceleration),
				new TrapezoidProfile.State(distance, 0)
			),
			elevator::setTargetState,
			elevator
		);
	}

	public MoveDistance(Elevator elevator, TrapezoidProfile profile) {
		super(profile, elevator::setTargetState, elevator);
	}

}
