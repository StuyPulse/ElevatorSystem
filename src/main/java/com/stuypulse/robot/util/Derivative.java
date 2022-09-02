package com.stuypulse.robot.util;

import com.stuypulse.stuylib.streams.filters.IFilter;
import com.stuypulse.stuylib.util.StopWatch;

public class Derivative implements IFilter {

    private final StopWatch timer;
    private double prev;

    public Derivative() {
        timer = new StopWatch();
        prev = 0.0;
    }

    @Override
    public double get(double next) {
        double deriv = (next - prev) / timer.reset();
        prev = next;
        return deriv;
    }


}
