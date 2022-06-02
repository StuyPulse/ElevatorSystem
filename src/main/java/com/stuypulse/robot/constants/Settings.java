/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.constants;

import com.stuypulse.stuylib.control.PIDController;

import edu.wpi.first.math.controller.ElevatorFeedforward;

/*-
 * File containing tunable settings for every subsystem on the robot.
 *
 * We use StuyLib's SmartNumber / SmartBoolean in order to have tunable
 * values that we can edit on Shuffleboard.
 */
public final class Settings {
	public interface Elevator {
		public interface Feedforward {
			double kG = 0.001;
			double kS = -1;
			double kV = -1;
			double kA = -1;

			public static ElevatorFeedforward getFeedforward() {
				return new ElevatorFeedforward(kS, kG, kV, kA);
			}
		}

		public interface Feedback {
			double kP = -1;
			double kI = -1;
			double kD = -1;

			public static PIDController getFeedback() {
				return new PIDController(kP, kI, kD);
			}
		}
	}
}
