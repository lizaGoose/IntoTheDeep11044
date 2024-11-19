package org.firstinspires.ftc.teamcode.math;

import com.acmerobotics.dashboard.config.Config;

import java.util.Arrays;
import java.util.List;

@Config
public class ClawInverseKinematics {
    private final double d1;
    private final double d2;
    private final double d3;

    public ClawInverseKinematics(double d1, double d2, double d3) {
        this.d1 = d1;
        this.d2 = d2;
        this.d3 = d3;
    }

    public List<Double> inverseKinematics(double x, double y, double z) {
        double d0 = z - (d2 / 2);
        double alpha2 = Math.atan((x + d3) / Math.sqrt(Math.pow(z - d0, 2) + Math.pow(y, 2)));
        double alpha = Math.atan(d1 / d2);
        double theta1 = alpha2 - alpha;
        double theta2 = Math.asin(y / d2);
        double theta3 = - Math.PI / 2 + theta1;
        return Arrays.asList(d0, theta1, theta2, theta3);
    }
}
