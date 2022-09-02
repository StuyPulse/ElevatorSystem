/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.constants;

import com.stuypulse.stuylib.control.PIDController;
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
		
		public interface Controls { 
			SmartNumber VOLTAGE = new SmartNumber("Controls/Voltage", 8.0);
			SmartNumber MAX_VELOCITY = new SmartNumber("Controls/Max Velocity", 3.0);
			SmartNumber MAX_ACCELERATION = new SmartNumber("Controls/Max Acceleration", 3.0);
		}

		public interface Encoder {
			double GEARING = 0.01;
	
			double ENCODER_MULTIPLIER = 6.175038019510E-5 * 0.0254;
		}

		public interface Feedforward {
			double kG = 0.5;
			double kS = 0.01;
			double kV = 0.5;
			double kA = 0.1;

			public static ElevatorFeedforward getFeedforward() {
				return new ElevatorFeedforward(kS, kG, kV, kA);
			}
		}

		public interface Feedback {
			double kP = 0.0;
			double kI = 0;
			double kD = 0.0;

			public static PIDController getFeedback() {
				return new PIDController(kP, kI, kD);
			}
		}
	}
}
