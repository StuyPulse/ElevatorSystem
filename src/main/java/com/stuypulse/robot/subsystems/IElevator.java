package com.stuypulse.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class IElevator extends SubsystemBase {
    
    public IElevator() { 
        setSubsystem("Elevator");
    }

    public abstract void setVoltage(double volts);
    
    public abstract double getHeight();
    public abstract double getVelocity();

}
