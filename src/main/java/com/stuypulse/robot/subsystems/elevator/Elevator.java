package com.stuypulse.robot.subsystems.elevator;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.Elevator.System;
import com.stuypulse.stuylib.control.Controller;

import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
// elevatorsim, rpm to meters per second
public abstract class Elevator extends SubsystemBase {

	private final ElevatorFeedforward feedforward;
	private final Controller feedback;
	private State targetState;

	// Simulation
	private FlywheelSim sim;

	public Elevator(ElevatorFeedforward feedforward, Controller feedback) {
		this.feedforward = feedforward;
		this.feedback = feedback;
		
		targetState = new State(0, 0);

		sim = System.getSystem();
	}

	// State

	public void setTargetState(State state) {
		targetState = state;
	}

	public State getTargetState() {
		return targetState;
	}

	public abstract State getState();

	// Hardware methods

	protected abstract void setVoltage(double voltage);

	protected abstract void setDistance(double distance);

	@Override
	public void simulationPeriodic() {
		sim.setInputVoltage(nowVolts);
		sim.update(Settings.DT);

		double rate = sim.getAngularVelocityRPM();
		setDistance(getState().position + rate * Settings.DT);
	}

	double nowVolts = 0;

	@Override
	public void periodic() {
		double voltage =
			feedforward.calculate(targetState.velocity) +
			feedback.update(targetState.velocity, getState().velocity);
		
		setVoltage(nowVolts = voltage);
	}
}
