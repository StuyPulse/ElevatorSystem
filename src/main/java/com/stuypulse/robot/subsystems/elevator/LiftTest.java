package com.stuypulse.robot.subsystems.elevator;

import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.stuypulse.robot.constants.Settings.Elevator.Feedback;
import com.stuypulse.robot.constants.Settings.Elevator.Feedforward;

public class LiftTest extends SubsystemBase {
	private WPI_TalonSRX[] motors;

	// INCHES
	private DutyCycleEncoder encoder;

	public LiftTest() {
		motors = new WPI_TalonSRX[4];
		motors[0] = new WPI_TalonSRX(7);
		motors[1] = new WPI_TalonSRX(8);
		motors[2] = new WPI_TalonSRX(9);
		motors[3] = new WPI_TalonSRX(10);

		encoder = new DutyCycleEncoder(0);
		encoder.setDistancePerRotation(0.5);
	}

	public void set(double voltage) {
		for (WPI_TalonSRX motor : motors) {
			motor.set(voltage);
		}
	}

	public double getDistance() {
		return encoder.getDistance();
	}
}
