package com.stuypulse.robot.subsystems;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.stuylib.control.Controller;
import com.stuypulse.stuylib.control.feedback.PIDController;
import com.stuypulse.stuylib.control.feedforward.Feedforward;
import com.stuypulse.stuylib.streams.filters.MotionProfile;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.BatterySim;
import edu.wpi.first.wpilibj.simulation.ElevatorSim;
import edu.wpi.first.wpilibj.simulation.RoboRioSim;

public class SimElevator extends IElevator {

    /** Constants */

    private static final double kMinHeight = 0.5;
    private static final double kMaxHeight = 2.8;

    private static final double kG = 0.50;
    private static final double kS = 0.10;
    private static final double kV = 0.30;
    private static final double kA = 0.06;

    private static final double kP = 2.0;
    private static final double kI = 0.1;
    private static final double kD = 0.0;

    private static final double kMaxVelocity = 3.0;
    private static final double kMaxAcceleration = 3.0;

    /** Elevator */

    private ElevatorSim sim;
    private double height;
    private double velocity;

    private Controller controller;
    private double targetHeight;

    // private Mechanism2d

    public SimElevator() {
        sim = new ElevatorSim(DCMotor.getCIM(4), 106.94, 2.5, 1.435, kMinHeight, kMaxHeight);
        height = 0.0;
        velocity = 0.0;

        controller = new Feedforward.Elevator(kG, kS, kV, kA).position()
            .add(new PIDController(kP, kI, kD))
            .setSetpointFilter(new MotionProfile(kMaxVelocity, kMaxAcceleration));
        targetHeight = kMinHeight;
    }

    @Override
    public void setHeight(double heightMeters) {
        targetHeight = heightMeters;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public double getVelocity() {
        return velocity;
    }

    private void setVoltage(double volts) {
        if (sim.hasHitUpperLimit() && volts > 0.0) {
            volts = 0.0;
            sim.setState(VecBuilder.fill(kMaxHeight, 0.0));
        } else if (sim.hasHitLowerLimit() && volts < 0.0) {
            volts = 0.0;
            sim.setState(VecBuilder.fill(kMinHeight, 0.0));
        }

        double batteryVoltage = RobotController.getBatteryVoltage();
        sim.setInput(MathUtil.clamp(volts, -batteryVoltage, +batteryVoltage));
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);

        builder.addDoubleProperty("Height", this::getHeight, null);
        builder.addDoubleProperty("Velocity", this::getVelocity, null);
        builder.addDoubleProperty("Target Height", () -> targetHeight, this::setHeight);
    }

    @Override
    public void periodic() {
        setVoltage(controller.update(targetHeight, height));
    }

    @Override
    public void simulationPeriodic() {
        sim.update(Settings.DT);

        height = sim.getPositionMeters();
        velocity = sim.getVelocityMetersPerSecond();

        RoboRioSim.setVInVoltage(BatterySim.calculateDefaultBatteryLoadedVoltage(sim.getCurrentDrawAmps()));
    }
    
}
