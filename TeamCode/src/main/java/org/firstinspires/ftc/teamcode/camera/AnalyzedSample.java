package org.firstinspires.ftc.teamcode.camera;

import androidx.annotation.NonNull;

public class AnalyzedSample {
    public double angle;
    SampleColor sampleColor;
    public double x;
    public double y;
    double area;

    AnalyzedSample() {
    }

    public AnalyzedSample(double x, double y, double angle, SampleColor sampleColor, double area) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.sampleColor = sampleColor;
        this.area = area;
    }

    @NonNull
    @Override
    public String toString() {
        return "AnalyzedSample{" +
                "angle=" + angle +
                ", color='" + sampleColor + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
