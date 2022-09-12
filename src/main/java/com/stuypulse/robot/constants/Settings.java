/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.constants;

import com.stuypulse.stuylib.network.SmartNumber;

import edu.wpi.first.math.util.Units;

/*-
 * File containing tunable settings for every subsystem on the robot.
 *
 * We use StuyLib's SmartNumber / SmartBoolean in order to have tunable
 * values that we can edit on Shuffleboard.
 */
public final class Settings {
	public static double DT = 0.02;

	public interface Elevator {

		double MIN_HEIGHT = 0.5;
		double MAX_HEIGHT = 2.8;
		
		double DRIVE_SPEED = Units.feetToMeters(1);

		SmartNumber MAX_HEIGHT_ERROR = new SmartNumber("Max Height Error", 0.05);

		public interface Encoder {
			double ENCODER_MULTIPLIER = 6.175038019510467E-5 / (1 / 106.94) * Units.inchesToMeters(1);
		}

		public interface PID {
			SmartNumber kP = new SmartNumber("kP", 2.0);
			SmartNumber kI = new SmartNumber("kI", 0.1);
			SmartNumber kD = new SmartNumber("kD", 0.0);
		}

		public interface FF {
			SmartNumber kG = new SmartNumber("kG", 0.50);
			SmartNumber kS = new SmartNumber("kS", 0.10);
			SmartNumber kV = new SmartNumber("kV", 0.30);
			SmartNumber kA = new SmartNumber("kA", 0.06);
		}

		public interface MotionProfile {
			SmartNumber VEL_LIMIT = new SmartNumber("VelLimit", 3);
			SmartNumber ACCEL_LIMIT = new SmartNumber("AccelLimit", 3);
		}
	}
}
