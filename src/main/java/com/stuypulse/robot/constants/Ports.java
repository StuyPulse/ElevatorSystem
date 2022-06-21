/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.constants;

/** This file contains the different ports of motors, solenoids and sensors */
public final class Ports {
    public interface Gamepad {
        int DRIVER = 0;
        int OPERATOR = 1;
        int DEBUGGER = 2;
    }

    public interface Elevator {
        int TALON_SIDE_MASTER = 7;
        int TALON_SIDE_FOLLOWER = 9;
        int VICTOR_LEFT = 8;
        int VICTOR_RIGHT = 10;

        int TOP_LIMIT_SWITCH = 2;
        int BOTTOM_LIMIT_SWITCH = 0;
    }
}
