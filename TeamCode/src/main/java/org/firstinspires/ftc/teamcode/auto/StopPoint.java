package org.firstinspires.ftc.teamcode.auto;

import androidx.annotation.NonNull;

import org.opencv.core.Mat;

public class StopPoint {
    double t;
    int milliseconds;
    double angle;

    StopPoint(double t, int milliseconds) {
        this.t = t;
        this.milliseconds = milliseconds;
        this.angle = 0;
    }

    StopPoint(double t, int milliseconds, double angle) {
        this.t = t;
        this.milliseconds = milliseconds;
        this.angle = Math.toRadians(angle);
    }

    @NonNull
    @Override
    public StopPoint clone() {
        return new StopPoint(this.t, this.milliseconds, this.angle);
    }
}
