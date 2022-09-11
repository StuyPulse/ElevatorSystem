package com.stuypulse.robot.commands;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.IElevator;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ElevatorToHeight extends CommandBase {

    private final IElevator elevator;
    private final double height;

    private boolean instant;

    public ElevatorToHeight(IElevator elevator, double height) {
        this.elevator = elevator;
        this.height = height;

        instant = true;

        addRequirements(elevator);
    }

    public final ElevatorToHeight untilReady() {
        instant = false;
        return this;
    }

    @Override
    public void initialize() {
        elevator.setTargetHeight(height);
    }

    @Override
    public boolean isFinished() {
        if (!instant)
            elevator.isReady(Settings.Elevator.MAX_HEIGHT_ERROR.get());

        return true;
    }

}
