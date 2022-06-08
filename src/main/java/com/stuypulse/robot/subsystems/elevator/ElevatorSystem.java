package com.stuypulse.robot.subsystems.elevator;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.Elevator.System;
import com.stuypulse.stuylib.control.Controller;

import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class ElevatorSystem extends SubsystemBase {
	private final ElevatorFeedforward feedforward;
	private final Controller feedback;
	private State targetState;

	// Simulation
	private double simDistance;
	private FlywheelSim sim;

	public ElevatorSystem(ElevatorFeedforward feedforward, Controller feedback) {
		this.feedforward = feedforward;
		this.feedback = feedback;
		
		targetState = new State(0, 0);

		simDistance = 0;
		sim = System.getSystem();
	}

	public void setState(State state) {
		targetState = state;
	}

	protected abstract void setVoltage(double voltage);
	protected abstract double getVoltage();

	protected abstract double getVelocity();

	protected abstract double getEncoderDistance();
	protected abstract void setEncoderDistance(double distance);

	@Override
	public void simulationPeriodic() {
		sim.setInputVoltage(getVoltage());
		sim.update(Settings.DT);

		double rate = sim.getAngularVelocityRPM();
		setEncoderDistance(getEncoderDistance() + rate * Settings.DT);
	}

	@Override
	public void periodic() {
		// TODO: check if acceleration is required for feedforward
		double voltage =
			feedforward.calculate(targetState.velocity) +
			feedback.update(targetState.velocity, getVelocity());
		
		setVoltage(voltage);
	}
}
