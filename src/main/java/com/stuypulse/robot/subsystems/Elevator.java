package com.stuypulse.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.stuypulse.stuylib.control.PIDController;
import com.stuypulse.stuylib.util.StopWatch;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends SubsystemBase {
	private final CANSparkMax motor;
	private final RelativeEncoder encoder;

	private final StopWatch timer;

	// Feedforward and Feedback
	private SimpleMotorFeedforward feedforward;
	private PIDController feedback;
	private State targetState;

	private double lastVelocity;

	public Elevator() {
		motor = new CANSparkMax(-1, MotorType.kBrushless);
		encoder = motor.getEncoder();

		timer = new StopWatch();
	}

	public void setState(State state) {
		targetState = state;
	}

	@Override
	public void periodic() {
		double volts =
			feedforward.calculate(lastVelocity, targetState.velocity, timer.reset())  +
			feedback.update(targetState.velocity - encoder.getVelocity());

		lastVelocity = targetState.velocity;

		motor.set(volts);
	}
}
