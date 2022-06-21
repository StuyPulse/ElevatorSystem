package com.stuypulse.robot.commands.elevator;

import com.stuypulse.robot.subsystems.elevator.Wildcard;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ToHome extends CommandBase {

    private final Wildcard lift; 

    public ToHome(Wildcard lift) {
        this.lift = lift;
        addRequirements(lift);
    }

    @Override
    public void execute() {
        lift.set(-0.5);
    }
    
	@Override
	public boolean isFinished() {
		return lift.isBottomLimitTriggered();
	}

	@Override
	public void end(boolean interrupted) {
		lift.set(0);
	}
}
