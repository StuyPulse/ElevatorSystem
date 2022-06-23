package com.stuypulse.robot.subsystems.elevator;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.stuylib.control.Controller;

import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj.simulation.ElevatorSim;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class Elevator extends SubsystemBase {

	private final ElevatorFeedforward feedforward;
	private final Controller velFeedback;
	private final Controller feedback;
	private State targetState;

	// Simulation
	protected double volts;

	private final ElevatorSim sim;
	private final Mechanism2d mech;
	private final MechanismRoot2d root;
	private final MechanismLigament2d arm;

	public Elevator(ElevatorFeedforward feedforward, Controller velFeedback, Controller feedback) {
		this.feedforward = feedforward;
		this.velFeedback = velFeedback;
		this.feedback = feedback;
		
		targetState = new State(0, 0);

		sim = Settings.Elevator.System.getSim();
		mech = new Mechanism2d(3, 3);
		root = mech.getRoot("Root", 1.5, 1);
		arm = root.append(
			new MechanismLigament2d("Elevator", 0, 0).append(
				new MechanismLigament2d("Wrist", 1, 90)));
		
		SmartDashboard.putData("Elevator Mech", mech);
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
		sim.setInputVoltage(volts);
		sim.update(Settings.DT);

		double rate = sim.getVelocityMetersPerSecond();
		setDistance(getState().position + rate * Settings.DT);

		SmartDashboard.putNumber("Elevator/Sim/Rate", rate);

		arm.setLength(getState().position);
	}

	@Override
	public void periodic() {
		SmartDashboard.putNumber("Elevator/Target Position", targetState.position);
		SmartDashboard.putNumber("Elevator/Target Velocity", targetState.velocity);
		SmartDashboard.putNumber("Elevator/Vel Error", targetState.velocity - getState().velocity);
		SmartDashboard.putNumber("Elevator/Position Error", targetState.position - getState().position);

		double ff = feedforward.calculate(targetState.velocity);
		double vel = velFeedback.update(targetState.velocity, getState().velocity);
		double pos = feedback.update(targetState.position, getState().position);

		SmartDashboard.putNumber("Elevator/Feedforward", ff);
		SmartDashboard.putNumber("Elevator/Vel Feedback", vel);
		SmartDashboard.putNumber("Elevator/Pos Feedback", pos);
		
		setVoltage(ff + vel + pos);
	}
}
