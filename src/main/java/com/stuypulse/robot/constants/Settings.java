/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.constants;

import com.stuypulse.stuylib.control.PIDController;

import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;

/*-
 * File containing tunable settings for every subsystem on the robot.
 *
 * We use StuyLib's SmartNumber / SmartBoolean in order to have tunable
 * values that we can edit on Shuffleboard.
 */
public final class Settings {
	public static double DT = 0.02;

	public interface Elevator {
		public interface Feedforward {
			double kG = 1.3;
			double kS = 0;
			double kV = 0.5;
			double kA = 0.1;

			public static ElevatorFeedforward getFeedforward() {
				return new ElevatorFeedforward(kS, kG, kV, kA);
			}
		}

		public interface Feedback {
			double VEL_kP = 1;
			double VEL_kI = 0;
			double VEL_kD = 0.2;

			double kP = 1;
			double kI = 0;
			double kD = 0.2;

			public static PIDController getVelFeedback() {
				return new PIDController(VEL_kP, VEL_kI, VEL_kD);
			}

			public static PIDController getFeedback() {
				return new PIDController(kP, kI, kD);
			}
		}

		public interface System {
			double GEARING = 1;

			double ENCODER_MULTIPLIER = 6.175038019510E-5 * 0.0254;

			double MAX_ACCELERATION = 1;
			double MAX_VELOCITY = 1.5;

			public static FlywheelSim getSystem() {
				return new FlywheelSim(
					LinearSystemId.identifyVelocitySystem(Feedforward.kV, Feedforward.kA),
					DCMotor.getNeo550(1),
					GEARING
				);
			}
		}
	}
}
