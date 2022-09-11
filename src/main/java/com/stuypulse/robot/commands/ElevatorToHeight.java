package com.stuypulse.robot.commands;

import com.stuypulse.robot.subsystems.IElevator;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ElevatorToHeight extends CommandBase {

    private static final double EPSILON = Units.inchesToMeters(1);
    
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
            elevator.isReady(EPSILON);

        return true;
    }

}
