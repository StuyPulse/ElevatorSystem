package com.stuypulse.robot.subsystems.elevator;

import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Motors.Elevator.Config;

public class Wildcard extends Elevator {

	private final WPI_TalonSRX sideFollower;
	private final WPI_TalonSRX sideMaster;
 
	private final WPI_VictorSPX left;
	private final WPI_VictorSPX right;
 
	private final DigitalInput topLimit;
	private final DigitalInput bottomLimit;

	public Wildcard() {
		super(
			Settings.Elevator.Feedforward.getFeedforward(),
			Settings.Elevator.Feedback.getVelFeedback(),
			Settings.Elevator.Feedback.getFeedback()
		);

		sideMaster = Config.configTalonSRX(Ports.Elevator.TALON_SIDE_MASTER, true);
		sideFollower = Config.configTalonSRX(Ports.Elevator.TALON_SIDE_FOLLOWER, true);
		left = Config.configVictorSRX(Ports.Elevator.VICTOR_LEFT, true);
		right = Config.configVictorSRX(Ports.Elevator.VICTOR_RIGHT, true);

		// encoder

		sideMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
		sideMaster.setSelectedSensorPosition(0);
		sideMaster.setSensorPhase(false);

		// limit switches

		topLimit = new DigitalInput(Ports.Elevator.TOP_LIMIT_SWITCH);
		bottomLimit = new DigitalInput(Ports.Elevator.BOTTOM_LIMIT_SWITCH);
	}

	public void set(double output) {
		setVoltage(output * RobotController.getBatteryVoltage());
	}

	public void stop() {
		volts = 0;

		sideMaster.setVoltage(0);
		sideFollower.setVoltage(0);
		left.setVoltage(0);
		right.setVoltage(0);
	}

	public double getDistance() {
		return sideMaster.getSelectedSensorPosition() * Settings.Elevator.System.ENCODER_MULTIPLIER;
	}

	public boolean isTopLimitTriggered() {
		return !topLimit.get();
	}

	public boolean isBottomLimitTriggered() {
		return !bottomLimit.get();
	}

	@Override
	public State getState() {
		return new State(getDistance(), getVelocity());
	}

	public double getVelocity() { 
		return sideMaster.getSelectedSensorVelocity() * Settings.Elevator.System.ENCODER_MULTIPLIER / 60.0;
	}

	@Override
	protected void setVoltage(double voltage) {
		if (isBottomLimitTriggered() && voltage > 0) {
			DriverStation.reportWarning("Bottom Limit Switch reached", false);
			sideMaster.setSelectedSensorPosition(0);
			stop();
		} else if (isTopLimitTriggered() && voltage < 0) {
			DriverStation.reportWarning("Top Limit Switch reached", false);
			stop();
			// sideMaster.setSelectedSensorPosition(MAX_HEIGHT); 
		} else {
			volts = voltage;

			sideMaster.setVoltage(voltage);
			sideFollower.setVoltage(voltage);
			left.setVoltage(voltage);
			right.setVoltage(voltage);
		}
	}

	@Override
	protected void setDistance(double distance) {
		sideMaster.setSelectedSensorPosition(distance / Settings.Elevator.System.ENCODER_MULTIPLIER);
	}

	@Override
	public void periodic() {
		SmartDashboard.putNumber("Elevator/Motor " + sideMaster.getDeviceID() + " Voltage", sideMaster.getMotorOutputVoltage());
		SmartDashboard.putNumber("Elevator/Motor " + sideFollower.getDeviceID() + " Voltage", sideFollower.getMotorOutputVoltage());
		SmartDashboard.putNumber("Elevator/Motor " + left.getDeviceID() + " Voltage", left.getMotorOutputVoltage());
		SmartDashboard.putNumber("Elevator/Motor " + right.getDeviceID() + " Voltage", right.getMotorOutputVoltage());

		SmartDashboard.putNumber("Elevator/Motor " + sideMaster.getDeviceID() + " Output Percent", sideMaster.getMotorOutputPercent());
		SmartDashboard.putNumber("Elevator/Motor " + sideFollower.getDeviceID() + " Output Percent", sideFollower.getMotorOutputPercent());
		SmartDashboard.putNumber("Elevator/Motor " + left.getDeviceID() + " Output Percent", left.getMotorOutputPercent());
		SmartDashboard.putNumber("Elevator/Motor " + right.getDeviceID() + " Output Percent", right.getMotorOutputPercent());

		SmartDashboard.putNumber("Elevator/Encoder Position", getDistance());

		SmartDashboard.putBoolean("Elevator/Top Limit Switch", isTopLimitTriggered());
		SmartDashboard.putBoolean("Elevator/Bottom Limit Switch", isBottomLimitTriggered());
	}
}
