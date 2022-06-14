package com.stuypulse.robot.commands.elevator;

import com.stuypulse.robot.subsystems.elevator.LiftTest;
import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.stuylib.streams.IStream;
import com.stuypulse.stuylib.streams.filters.LowPassFilter;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class PoopedCommand extends CommandBase {
	private final LiftTest lift;
	private final IStream input;

	public PoopedCommand(Gamepad gamepad, LiftTest lift) {
		this.lift = lift;
		input = IStream.create(gamepad::getLeftY)
			.filtered(new LowPassFilter(0.2));

		addRequirements(lift);
	}

	@Override
	public void initialize() {
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
