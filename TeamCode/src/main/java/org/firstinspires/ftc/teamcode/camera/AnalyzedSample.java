package org.firstinspires.ftc.teamcode.camera;

import androidx.annotation.NonNull;

public class AnalyzedSample implements Cloneable {
    public double angle;
    SampleColor sampleColor;
    public double x;
    public double y;

    AnalyzedSample() {
    }

    public AnalyzedSample(double x, double y, double angle, SampleColor sampleColor) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.sampleColor = sampleColor;
    }

    @NonNull
    @Override
    public String toString() {
        return "AnalyzedSample{" +
                "angle=" + angle +
                ", sampleColor=" + sampleColor +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    /** @noinspection UnnecessaryLocalVariable*/
    @NonNull
    @Override
    public AnalyzedSample clone() {
        try {
            AnalyzedSample clone = (AnalyzedSample) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
