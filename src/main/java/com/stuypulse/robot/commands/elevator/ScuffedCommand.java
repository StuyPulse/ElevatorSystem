package com.stuypulse.robot.commands.elevator;

import com.stuypulse.robot.subsystems.elevator.LiftTest;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ScuffedCommand extends CommandBase {
	private final LiftTest lift;

	public ScuffedCommand(LiftTest lift) {
		this.lift = lift;

		addRequirements(lift);
	}

	@Override
	public void initialize() {
		lift.set(0.1);
		System.out.println("running");
	}

	@Override
	public boolean isFinished() {
		return lift.getDistance() > 5;
	}

	@Override
	public void end(boolean interrupted) {
		lift.set(0);
	}
}
