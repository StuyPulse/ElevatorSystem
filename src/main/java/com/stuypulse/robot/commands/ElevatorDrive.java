package com.stuypulse.robot.commands;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.IElevator;
import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.stuylib.streams.IStream;
import com.stuypulse.stuylib.util.StopWatch;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ElevatorDrive extends CommandBase {
    
    private final IElevator elevator;
    private final IStream velocity;

    private final StopWatch timer;

    public ElevatorDrive(IElevator elevator, Gamepad gamepad) {
        this.elevator = elevator;

        velocity = IStream.create(gamepad::getLeftY)
            .filtered(x -> x * Settings.Elevator.DRIVE_SPEED);
        
        timer = new StopWatch();

        addRequirements(elevator);
    }

    @Override
    public void execute() {
        elevator.addTargetHeight(velocity.get() * timer.reset());
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}
