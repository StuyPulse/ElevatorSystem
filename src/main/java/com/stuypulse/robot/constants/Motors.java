/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.constants;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

/*-
 * File containing all of the configurations that different motors require.
 *
 * Such configurations include:
 *  - If it is Inverted
 *  - The Idle Mode of the Motor
 *  - The Current Limit
 *  - The Open Loop Ramp Rate
 */
public final class Motors {
	public interface Config {
		public static WPI_TalonSRX configTalonSRX(int port, boolean inverted) {
			WPI_TalonSRX talon = new WPI_TalonSRX(port);

			talon.setNeutralMode(NeutralMode.Brake);
			
			if (inverted) {
				talon.setInverted(InvertType.InvertMotorOutput);
			} else {
				talon.setInverted(InvertType.None);
			}

			return talon;
		}

		public static WPI_VictorSPX configVictorSRX(int port, boolean inverted) {
			WPI_VictorSPX victor = new WPI_VictorSPX(port);

			victor.setNeutralMode(NeutralMode.Brake);
			
			if (inverted) {
				victor.setInverted(InvertType.InvertMotorOutput);
			} else {
				victor.setInverted(InvertType.None);
			}

			return victor;
		}
	}
}
