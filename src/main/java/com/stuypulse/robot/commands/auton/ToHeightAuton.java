/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot.commands.auton;

import com.stuypulse.robot.RobotContainer;
import com.stuypulse.robot.commands.elevator.MoveDistance;
import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/*-
 * This auton does nothing... it is used as a placeholder
 *
 * @author Sam Belliveau
 */
public class ToHeightAuton extends SequentialCommandGroup {

    public ToHeightAuton(RobotContainer robot) {
        addCommands(
            new MoveDistance(
                robot.lift, 
                Settings.Elevator.System.MAX_ACCELERATION, 
                Settings.Elevator.System.MAX_VELOCITY, 
                1
            )
        );
    }
}