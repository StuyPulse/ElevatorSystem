package com.stuypulse.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class IElevator extends SubsystemBase {
    
    public IElevator() { 
        setSubsystem("Elevator");
    }

    public abstract void setTargetHeight(double heightMeters);
    public final void addTargetHeight(double delta) {
        setTargetHeight(getTargetHeight() + delta);
    }

    public final boolean isReady(double error) {
        return Math.abs(getTargetHeight() - getHeight()) < error;
    }
    
    public abstract double getTargetHeight();
    public abstract double getHeight();
    public abstract double getVelocity();

}
