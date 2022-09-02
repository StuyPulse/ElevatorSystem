package com.stuypulse.robot.subsystems;

import org.ejml.simple.SimpleMatrix;

import edu.wpi.first.math.MatBuilder;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N2;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.BatterySim;
import edu.wpi.first.wpilibj.simulation.ElevatorSim;
import edu.wpi.first.wpilibj.simulation.RoboRioSim;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;

public class SimElevator extends IElevator {

    private static final double kMinHeight = 0.5;
    private static final double kMaxHeight = 2.8;

    private ElevatorSim sim;

    private double voltage;
    private double height;
    private double velocity;

    // private Mechanism2d

    public SimElevator() {
        sim = new ElevatorSim(DCMotor.getCIM(4), 106.94, 2.5, 1.435, kMinHeight, kMaxHeight);

        voltage = 0.0;
        height = 0.0;
        velocity = 0.0;
    }

    @Override
    public void setVoltage(double volts) {
        if (sim.hasHitUpperLimit() && volts > 0.0) {
            volts = 0.0;
            sim.setState(VecBuilder.fill(kMaxHeight, 0.0));
        } else if (sim.hasHitLowerLimit() && volts < 0.0) {
            volts = 0.0;
            sim.setState(VecBuilder.fill(kMinHeight, 0.0));
        }

        double batteryVoltage = RobotController.getBatteryVoltage();
        voltage = MathUtil.clamp(volts, -batteryVoltage, +batteryVoltage);
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public double getVelocity() {
        return velocity;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);

        builder.addDoubleProperty("Height", this::getHeight, null);
        builder.addDoubleProperty("Velocity", this::getVelocity, null);
    }

    @Override
    public void simulationPeriodic() {
        sim.setInput(voltage);
        sim.update(0.02);

        height = sim.getPositionMeters();
        velocity = sim.getVelocityMetersPerSecond();

        System.out.println(height);
        System.out.println(velocity);

        RoboRioSim.setVInVoltage(BatterySim.calculateDefaultBatteryLoadedVoltage(sim.getCurrentDrawAmps()));
    }
    
}
