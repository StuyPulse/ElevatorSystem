package com.stuypulse.robot.subsystems;

import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Motors.Config;
import com.stuypulse.robot.constants.Settings.Elevator.Feedback;
import com.stuypulse.robot.constants.Settings.Elevator.Feedforward;
import com.stuypulse.robot.constants.Settings.Elevator.VelFeedback;
import com.stuypulse.stuylib.control.Controller;

public class Elevator extends SubsystemBase {

	private final ElevatorFeedforward feedforward;
	private final Controller feedback, velFeedback;
	private State targetState;

	// Hardware

	private final WPI_TalonSRX sideFollower;
	private final WPI_TalonSRX sideMaster;
 
	private final WPI_VictorSPX left;
	private final WPI_VictorSPX right;
 
	private final DigitalInput topLimit;
	private final DigitalInput bottomLimit;

	public Elevator() {
		feedforward = Feedforward.getFeedforward();
		feedback = Feedback.getFeedback();
		velFeedback = VelFeedback.getFeedback();
		targetState = new State(0, 0);

		sideMaster = Config.configTalonSRX(Ports.Elevator.TALON_SIDE_MASTER, false);
		sideFollower = Config.configTalonSRX(Ports.Elevator.TALON_SIDE_FOLLOWER, false);
		left = Config.configVictorSRX(Ports.Elevator.VICTOR_LEFT, false);
		right = Config.configVictorSRX(Ports.Elevator.VICTOR_RIGHT, false);

		// Encoder

		sideMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
		sideMaster.setSelectedSensorPosition(0);
		sideMaster.setSensorPhase(false);

		// Limit switches

		topLimit = new DigitalInput(Ports.Elevator.TOP_LIMIT_SWITCH);
		bottomLimit = new DigitalInput(Ports.Elevator.BOTTOM_LIMIT_SWITCH);
	}

	public double getVelocity() { 
		return sideMaster.getSelectedSensorVelocity() * -Settings.Elevator.System.ENCODER_MULTIPLIER / 60.0;
	}

	public double getDistance() {
		return sideMaster.getSelectedSensorPosition() * -Settings.Elevator.System.ENCODER_MULTIPLIER;
	}

	public boolean atTop() {
		return !topLimit.get();
	}

	public boolean atBottom() {
		return !bottomLimit.get();
	}

	public void setTargetState(State state) {
		targetState = state;
	}

	private void setVoltage(double voltage) {
		if (atBottom() && voltage < 0) {
			DriverStation.reportWarning("Bottom Limit Switch reached", false);
			sideMaster.setSelectedSensorPosition(0);
			
			sideMaster.setVoltage(0);
			sideFollower.setVoltage(0);
			left.setVoltage(0);
			right.setVoltage(0);
		} else if (atTop() && voltage > 0) {
			DriverStation.reportWarning("Top Limit Switch reached", false);
			setTargetState(new State(getDistance(), 0));
		} else {
			sideMaster.setVoltage(voltage);
			sideFollower.setVoltage(voltage);
			left.setVoltage(voltage);
			right.setVoltage(voltage);
		}
	}

	@Override
	public void periodic() {
		SmartDashboard.putNumber("Elevator/Target Position", targetState.position);
		SmartDashboard.putNumber("Elevator/Target Velocity", targetState.velocity);
		SmartDashboard.putNumber("Elevator/Vel Error", targetState.velocity - getVelocity());
		SmartDashboard.putNumber("Elevator/Position Error", targetState.position - getDistance());

		SmartDashboard.putNumber("Elevator/Velocity", getVelocity());
		SmartDashboard.putNumber("Elevator/Position", getDistance());

		double ff = feedforward.calculate(targetState.velocity);
		double vel = 0;// velFeedback.update(targetState.velocity, getState().velocity);
		double pos = 0;// feedback.update(targetState.position, getState().position);

		SmartDashboard.putNumber("Elevator/Feedforward", ff);
		SmartDashboard.putNumber("Elevator/Vel Feedback", vel);
		SmartDashboard.putNumber("Elevator/Pos Feedback", pos);
		
		setVoltage(ff + vel + pos);
	}
}
