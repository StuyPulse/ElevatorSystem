package com.stuypulse.robot.subsystems.elevator;

import com.stuypulse.stuylib.control.Controller;

import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// implement elevator in system
public abstract class ElevatorSystem extends SubsystemBase {
	private final ElevatorFeedforward feedforward;
	private final Controller feedback;
	private State targetState;

	// Simulation
	private double simEncoderDistance;
	private FlywheelSim sim;

	public ElevatorSystem(ElevatorFeedforward feedforward, Controller feedback) {
		this.feedforward = feedforward;
		this.feedback = feedback;
		
		targetState = new State(0, 0);

		simEncoderDistance = 0;
		// sim = new FlywheelSim(gearbox, gearing, jKgMetersSquared);
	}

	public void setState(State state) {
		targetState = state;
	}

	protected abstract void setVoltage(double voltage);
	protected abstract double getVelocity();

	protected double getDistance() {
		return simEncoderDistance;
	}

	// TODO: add encoder set abstract functions

	@Override
	public void simulationPeriodic() {

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
