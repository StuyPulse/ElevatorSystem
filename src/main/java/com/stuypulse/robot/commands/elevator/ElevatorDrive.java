package com.stuypulse.robot.commands.elevator;

import static com.stuypulse.robot.constants.Settings.Elevator.*;

import com.stuypulse.robot.subsystems.IElevator;
import com.stuypulse.robot.util.Derivative;
import com.stuypulse.stuylib.input.Gamepad;

import com.stuypulse.stuylib.streams.IStream;

import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ElevatorDrive extends CommandBase {
	private final IElevator lift;

	private final ElevatorFeedforward feedforward;
	
	private final IStream velocity;
	private final IStream acceleration;

	public ElevatorDrive(IElevator lift, Gamepad gamepad) {
		this.lift = lift;

		feedforward = Feedforward.getFeedforward();

		velocity = IStream.create(gamepad::getLeftY)
			.filtered(
				x -> x * Controls.MAX_VELOCITY.get()
				// new RateLimit(Controls.MAX_ACCELERATION)
			);

		acceleration = IStream.create(gamepad::getLeftY)
			.filtered(
				x -> x * Controls.MAX_VELOCITY.get(),
				// new RateLimit(Controls.MAX_ACCELERATION),
				new Derivative()
			);

		addRequirements(lift);
	}

	@Override
	public void execute() {
		double vel = velocity.get();
		double acc = acceleration.get();

		SmartDashboard.putNumber("Elevator/Target Velocity", vel);
		SmartDashboard.putNumber("Elevator/Target Acceleration", acc);

		double ff = feedforward.calculate(vel, acc);

		SmartDashboard.putNumber("Elevator/FF", ff);

		lift.setVoltage(ff);
	}

	@Override
	public boolean isFinished() { 
		return false;
	}
}
