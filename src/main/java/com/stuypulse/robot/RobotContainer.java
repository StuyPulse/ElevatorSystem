/************************ PROJECT DORCAS ************************/
/* Copyright (c) 2022 StuyPulse Robotics. All rights reserved.  */
/* This work is licensed under the terms of the MIT license.    */
/****************************************************************/

package com.stuypulse.robot;

import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.Elevator;
import com.stuypulse.robot.subsystems.IElevator;
import com.stuypulse.robot.subsystems.SimElevator;
import com.stuypulse.robot.util.BootlegXbox;
import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.stuylib.input.gamepads.AutoGamepad;
import com.stuypulse.stuylib.input.gamepads.keyboard.SimKeyGamepad;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class RobotContainer {

  // Subsystem
  public final IElevator elevator = new SimElevator();
  // Gamepads
  public final Gamepad driver = new SimKeyGamepad();
                                // new BootlegXbox(Ports.Gamepad.DRIVER);
  public final Gamepad operator = new AutoGamepad(Ports.Gamepad.OPERATOR);

  // Autons
  private static SendableChooser<Command> autonChooser = new SendableChooser<>();

  // Robot container

  public RobotContainer() {
    configureDefaultCommands();
    configureButtonBindings();
    configureAutons();
  }

  /****************/
  /*** DEFAULTS ***/
  /****************/

  private void configureDefaultCommands() {
  }

  /***************/
  /*** BUTTONS ***/
  /***************/

  private void configureButtonBindings() {
    driver.getBottomButton().whenPressed(new InstantCommand(() -> elevator.setTargetHeight(0.5), elevator));
    driver.getTopButton().whenPressed(new InstantCommand(() -> elevator.setTargetHeight(2.0), elevator));
  }

  /**************/
  /*** AUTONS ***/
  /**************/

  public void configureAutons() {
    // autonChooser.setDefaultOption("Halfway", new ToHeight(lift, 20));
    // autonChooser.setDefaultOption("ElevatorMove", new ElevatorMove(elevator, 5, 5, 10));
    // autonChooser.setDefaultOption("Do Nothing", new DoNothingAuton());
    // autonChooser.addOption("To Height Auton", new ToHeightAuton(this));

    SmartDashboard.putData("Autonomous", autonChooser);
  }

  public Command getAutonomousCommand() {
    return autonChooser.getSelected();
  }
}
