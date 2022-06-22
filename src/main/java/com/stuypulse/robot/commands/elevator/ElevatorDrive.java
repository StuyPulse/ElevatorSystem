package com.stuypulse.robot.commands.elevator;

import com.stuypulse.robot.subsystems.elevator.Wildcard;
import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.stuylib.streams.IStream;
import com.stuypulse.stuylib.streams.filters.LowPassFilter;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ElevatorDrive extends CommandBase {
	private final Wildcard lift;
	private final IStream input;

	public ElevatorDrive(Gamepad gamepad, Wildcard lift) {
		this.lift = lift;
		input = IStream.create(gamepad::getLeftY)
			.filtered(new LowPassFilter(0.2));

		addRequirements(lift);
	}

	@Override
	public void execute() {
		lift.set(input.get());
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void end(boolean interrupted) {
		lift.set(0);
	}
}
