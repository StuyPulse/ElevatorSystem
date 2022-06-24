package com.stuypulse.robot.commands.elevator;

import com.stuypulse.robot.subsystems.Elevator;

// TODO: maybe override isFinished to check if limit switches are enabled

public class ToHome extends ToHeight {

    public ToHome(Elevator lift) {
        super(lift, 0);
    }

}
