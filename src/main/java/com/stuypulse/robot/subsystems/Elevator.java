package com.stuypulse.robot.subsystems;

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

import static com.stuypulse.robot.constants.Settings.Elevator.*;


public class Elevator extends SubsystemBase {
	
	// Hardware

	private final WPI_TalonSRX sideFollower;
	private final WPI_TalonSRX sideMaster;
 
	private final WPI_VictorSPX left;
	private final WPI_VictorSPX right;
 
	private final DigitalInput topLimit;
	private final DigitalInput bottomLimit;


	public Elevator() {
		// Motors

		sideMaster = Config.configTalonSRX(Ports.Elevator.TALON_SIDE_MASTER, false);
		sideFollower = Config.configTalonSRX(Ports.Elevator.TALON_SIDE_FOLLOWER, false);
		left = Config.configVictorSRX(Ports.Elevator.VICTOR_LEFT, false);
		right = Config.configVictorSRX(Ports.Elevator.VICTOR_RIGHT, false);

		// Encoder

		sideMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
		sideMaster.setSensorPhase(false);
		setEncoder(Settings.Elevator.MIN_HEIGHT);

		// Limit switches

		topLimit = new DigitalInput(Ports.Elevator.TOP_LIMIT_SWITCH);
		bottomLimit = new DigitalInput(Ports.Elevator.BOTTOM_LIMIT_SWITCH);
	}

	public double getVelocity() { 
		return sideMaster.getSelectedSensorVelocity() * -Settings.Elevator.Encoder.ENCODER_MULTIPLIER / 60.0;
	}

	public double getHeight() {
		return sideMaster.getSelectedSensorPosition() * -Settings.Elevator.Encoder.ENCODER_MULTIPLIER;
	}

	public boolean atTop() {
		return !topLimit.get();
	}

	public boolean atBottom() {
		return !bottomLimit.get();
	}

	private void setEncoder(double heightMeters) {
		sideMaster.setSelectedSensorPosition(heightMeters / -Settings.Elevator.Encoder.ENCODER_MULTIPLIER);
	}

	public void setVoltage(double voltage) {
		if (atBottom() && voltage < 0) {
			DriverStation.reportWarning("Bottom Limit Switch reached", false);

			setEncoder(MIN_HEIGHT);
			
			voltage = 0.0;
		} else if (atTop() && voltage > 0) {
			DriverStation.reportWarning("Top Limit Switch reached", false);
			
			setEncoder(MAX_HEIGHT);

			voltage = 0.0;
		} 

		sideMaster.setVoltage(voltage);
		sideFollower.setVoltage(voltage);
		left.setVoltage(voltage);
		right.setVoltage(voltage);
	}

	@Override
	public void periodic() {
		SmartDashboard.putNumber("Elevator/Height", getHeight());
		SmartDashboard.putNumber("Elevator/Velocity", getVelocity());
		SmartDashboard.putNumber("Elevator/Encoder Value", sideMaster.getSelectedSensorPosition());

		SmartDashboard.putBoolean("Elevator/At Top", atTop());
		SmartDashboard.putBoolean("Elevator/At Bottom", atBottom());
	}
}