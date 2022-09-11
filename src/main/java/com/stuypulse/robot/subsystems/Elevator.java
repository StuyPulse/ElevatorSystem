package com.stuypulse.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Motors.Config;
import com.stuypulse.stuylib.control.Controller;
import com.stuypulse.stuylib.control.feedback.PIDController;
import com.stuypulse.stuylib.control.feedforward.Feedforward;
import com.stuypulse.stuylib.network.SmartNumber;

import static com.stuypulse.robot.constants.Settings.Elevator.PID.*;
import static com.stuypulse.robot.constants.Settings.Elevator.FF.*;


public class Elevator extends IElevator {
	
	// Hardware

	private final WPI_TalonSRX sideFollower;
	private final WPI_TalonSRX sideMaster;
 
	private final WPI_VictorSPX left;
	private final WPI_VictorSPX right;
 
	private final DigitalInput topLimit;
	private final DigitalInput bottomLimit;

	// Control

	private final Controller position;

	private final SmartNumber targetHeight;


	public Elevator() {
		// Motors

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

		// Control
		position = new PIDController(kP, kI, kD).add(new Feedforward.Elevator(kG, kS, kV, kA).position());
		targetHeight = new SmartNumber("Elevator Target Height", 0);
	}

	@Override
	public double getVelocity() { 
		return sideMaster.getSelectedSensorVelocity(); // * -Settings.Elevator.Encoder.ENCODER_MULTIPLIER / 60.0;
	}

	@Override
	public double getHeight() {
		return sideMaster.getSelectedSensorPosition(); // * -Settings.Elevator.Encoder.ENCODER_MULTIPLIER;
	}

	@Override
	public double getTargetHeight() {
		return targetHeight.get();
	}

	public boolean atTop() {
		return !topLimit.get();
	}

	public boolean atBottom() {
		return !bottomLimit.get();
	}

	// public void setTargetState(State state) {
	// 	targetState = state;
	// }

	private void setVoltage(double voltage) {
		if (atBottom() && voltage < 0) {
			DriverStation.reportWarning("Bottom Limit Switch reached", false);
			sideMaster.setSelectedSensorPosition(0);
			
			voltage = 0.0;
		} else if (atTop() && voltage > 0) {
			DriverStation.reportWarning("Top Limit Switch reached", false);

			voltage = 0.0;
		} 

		sideMaster.setVoltage(voltage);
		sideFollower.setVoltage(voltage);
		left.setVoltage(voltage);
		right.setVoltage(voltage);
	}

	@Override
    public void setTargetHeight(double height) {
		targetHeight.set(height);
    }

	@Override
	public void periodic() {
		setVoltage(position.update(getHeight(), getTargetHeight()));

		SmartDashboard.putNumber("Elevator/Height", getHeight());
		SmartDashboard.putNumber("Elevator/Velocity", getVelocity());
		SmartDashboard.putNumber("Elevator/Encoder Units", sideMaster.getSelectedSensorPosition());

		SmartDashboard.putBoolean("Elevator/At Top", atTop());
		SmartDashboard.putBoolean("Elevator/At Bottom", atBottom());
	}
}