/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.constants;

import com.stuypulse.stuylib.network.SmartNumber;

import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N2;
import edu.wpi.first.math.system.LinearSystem;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.simulation.ElevatorSim;

/*-
 * File containing tunable settings for every subsystem on the robot.
 *
 * We use StuyLib's SmartNumber / SmartBoolean in order to have tunable
 * values that we can edit on Shuffleboard.
 */
public final class Settings {
	public static double DT = 0.02;

	public interface Elevator {
		public interface PID {
			SmartNumber kP = new SmartNumber("kP", 0.0001);
			SmartNumber kI = new SmartNumber("kI", 0.0000001);
			SmartNumber kD = new SmartNumber("kD", 0.0001);
		}

		public interface FF {
			SmartNumber kG = new SmartNumber("kG", 0.001);
			SmartNumber kS = new SmartNumber("kS", 0.0001);
			SmartNumber kV = new SmartNumber("kV", 0.0001);
			SmartNumber kA = new SmartNumber("kA", 0.0001);
		}
	}
}
