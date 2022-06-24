package com.stuypulse.robot.commands.elevator;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.Elevator;
import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.stuylib.streams.IStream;
import com.stuypulse.stuylib.streams.filters.LowPassFilter;

import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ElevatorDrive extends CommandBase {
	private final Elevator lift;
	private final IStream input;

	public ElevatorDrive(Gamepad gamepad, Elevator lift) {
		this.lift = lift;
		input = IStream.create(gamepad::getLeftY)
			.filtered(new LowPassFilter(0.2), x->-x, x->x*3.0);

		addRequirements(lift);
	}

	@Override
	public void execute() {
		SmartDashboard.putNumber("poop balls", input.get());
		// lift.set(input.get());
		lift.setTargetState(new State(0, input.get()));
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void end(boolean interrupted) {
		lift.setTargetState(new State(lift.getDistance(), 0));
	}
}
