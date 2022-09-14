package com.stuypulse.robot.commands;

import com.stuypulse.robot.subsystems.Elevator;
import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.stuylib.streams.IStream;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ElevatorDrive extends CommandBase {
    
    private final Elevator elevator;
    private final IStream voltage;

    public ElevatorDrive(Elevator elevator, Gamepad gamepad) {
        this.elevator = elevator;

        voltage = IStream.create(gamepad::getLeftY)
            .filtered(x -> x * RobotController.getBatteryVoltage());

        addRequirements(elevator);
    }

    @Override
    public void execute() {
        elevator.setVoltage(voltage.get());
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
