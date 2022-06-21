package com.stuypulse.robot.subsystems.elevator;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Motors.Elevator.Config;

public class LiftTest extends SubsystemBase {

	private WPI_TalonSRX sideFollower;
	private WPI_TalonSRX sideMaster;

	private WPI_VictorSPX left;
	private WPI_VictorSPX right;

	private DigitalInput topLimit;
	private DigitalInput bottomLimit;

	public LiftTest() {
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

	public void set(double voltage) {
		if (getBottomLimit() && voltage > 0) {
			DriverStation.reportError("Bottom Limit Switch reached", true);
			stop();
		} else if (getTopLimit() && voltage < 0) {
			DriverStation.reportError("Top Limit Switch reached", true);
			stop();
		} else {
			sideMaster.setVoltage(voltage);
			sideFollower.setVoltage(voltage);
			left.setVoltage(voltage);
			right.setVoltage(voltage);
		}
	}

	public void stop() {
		sideMaster.setVoltage(0);
		sideFollower.setVoltage(0);
		left.setVoltage(0);
		right.setVoltage(0);
	}

	public double getDistance() {
		return sideMaster.getSelectedSensorPosition();
	}

	public boolean getTopLimit() {
		return !topLimit.get();
	}

	public boolean getBottomLimit() {
		return !bottomLimit.get();
	}

	@Override
	public void periodic() {
		SmartDashboard.putNumber("Motor " + sideMaster.getDeviceID() + " Voltage", sideMaster.getMotorOutputVoltage());
		SmartDashboard.putNumber("Motor " + sideFollower.getDeviceID() + " Voltage", sideFollower.getMotorOutputVoltage());
		SmartDashboard.putNumber("Motor " + left.getDeviceID() + " Voltage", left.getMotorOutputVoltage());
		SmartDashboard.putNumber("Motor " + right.getDeviceID() + " Voltage", right.getMotorOutputVoltage());

		SmartDashboard.putNumber("Motor " + sideMaster.getDeviceID() + " Output Percent", sideMaster.getMotorOutputPercent());
		SmartDashboard.putNumber("Motor " + sideFollower.getDeviceID() + " Output Percent", sideFollower.getMotorOutputPercent());
		SmartDashboard.putNumber("Motor " + left.getDeviceID() + " Output Percent", left.getMotorOutputPercent());
		SmartDashboard.putNumber("Motor " + right.getDeviceID() + " Output Percent", right.getMotorOutputPercent());

		SmartDashboard.putNumber("Encoder Position", sideMaster.getSelectedSensorPosition());
		SmartDashboard.putNumber("Encoder Velocity", sideMaster.getSelectedSensorVelocity());

		SmartDashboard.putBoolean("Top Limit Switch", getTopLimit());
		SmartDashboard.putBoolean("Bottom Limit Switch", getBottomLimit());
	}
}
