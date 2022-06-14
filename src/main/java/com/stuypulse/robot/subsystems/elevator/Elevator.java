package com.stuypulse.robot.subsystems.elevator;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.stuypulse.robot.constants.Settings.Elevator.Feedback;
import com.stuypulse.robot.constants.Settings.Elevator.Feedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;

public class Elevator extends ElevatorSystem {
	private final CANSparkMax motor;
	private final RelativeEncoder encoder;

	public Elevator() {
		super(Feedforward.getFeedforward(), Feedback.getFeedback());

		motor = new CANSparkMax(-1, MotorType.kBrushless);
		encoder = motor.getEncoder();
	}

	@Override
	public State getState() {
		return new State(encoder.getPosition(), encoder.getVelocity());
	}

	@Override
	protected void setVoltage(double voltage) {
		motor.set(voltage);
	}

	@Override
	protected void setEncoderDistance(double distance) {
		encoder.setPosition(distance);
	}
}
