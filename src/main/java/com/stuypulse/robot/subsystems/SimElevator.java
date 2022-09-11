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

import static com.stuypulse.robot.constants.Settings.Elevator.PID.*;
import static com.stuypulse.robot.constants.Settings.Elevator.FF.*;
import static com.stuypulse.robot.constants.Settings.Elevator.MotionProfile.*;
import static com.stuypulse.robot.constants.Settings.Elevator.*;

public class SimElevator extends IElevator {

    /** Elevator */

    private ElevatorSim sim;
    private double height;
    private double velocity;

    private Controller controller;
    private double targetHeight;

    // private Mechanism2d

    public SimElevator() {
        sim = new ElevatorSim(DCMotor.getCIM(4), 106.94, 2.5, 1.435, MIN_HEIGHT, MAX_HEIGHT);
        height = 0.0;
        velocity = 0.0;

        controller = new Feedforward.Elevator(kG, kS, kV, kA).position()
            .add(new PIDController(kP, kI, kD))
            .setSetpointFilter(new MotionProfile(VEL_LIMIT, ACCEL_LIMIT));
        targetHeight = MIN_HEIGHT;
    }

    @Override
    public void setTargetHeight(double heightMeters) {
        targetHeight = heightMeters;
    }

    @Override
    public double getTargetHeight() {
        return targetHeight;
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
            sim.setState(VecBuilder.fill(MAX_HEIGHT, 0.0));
        } else if (sim.hasHitLowerLimit() && volts < 0.0) {
            volts = 0.0;
            sim.setState(VecBuilder.fill(MIN_HEIGHT, 0.0));
        }

        double batteryVoltage = RobotController.getBatteryVoltage();
        sim.setInput(MathUtil.clamp(volts, -batteryVoltage, +batteryVoltage));
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);

        builder.addDoubleProperty("Height", this::getHeight, null);
        builder.addDoubleProperty("Velocity", this::getVelocity, null);
        builder.addDoubleProperty("Target Height", this::getTargetHeight, this::setTargetHeight);
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
