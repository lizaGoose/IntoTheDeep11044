package org.firstinspires.ftc.teamcode.math;

import com.acmerobotics.dashboard.config.Config;

import java.util.Arrays;
import java.util.List;

@Config
public class ClawCalculation {
    public static final double d1 = 0.0243;
    public static final double d2 = 0.12745;
    public static final double d3 = 0.05;

    @Deprecated
    public static List<Double> DEPRECATED_inverseKinematics(double x, double y, double z) {
        double d0 = z - (d2 / 2);
        double theta2 = Math.asin(y / d2);
        double fi1 = Math.atan((x + d3) / (z - d0));
        double fi = Math.atan(d1 / (d2 * Math.cos(theta2)));
        double theta1 = fi1 - fi;
        double theta3 = -Math.PI / 2 + theta1;
        return Arrays.asList(d0, theta1, theta2, theta3);
    }

    public static List<Double> inverseKinematics(double x, double y, double z) {
        double theta2 = Math.asin(y / d2);
        double fi1 = Math.asin((x + d3) / Math.sqrt(Math.pow(d1, 2) + Math.pow(d2 * Math.cos(theta2), 2)));
        double fi = Math.atan(d1 / (d2 + Math.cos(theta2)));
        double theta1 = fi1 - fi;
        double d0 = z - (d2 * Math.cos(theta2) * Math.cos(theta1)) + d1 * Math.sin(theta1);
        double theta3 = -Math.PI / 2 + theta1;
        return Arrays.asList(d0, theta1, theta2, theta3);
    }

    public static List<Double> angleAndRotToDiff(double angle, double rotation) {
        double leftDiff = (angle / 90 + 1) / 2 + (rotation / 180 + 1) / 2;
        double rightDiff = (angle / 90 + 1) / 2 - (rotation / 180 + 1) / 2;

        return Arrays.asList(leftDiff, rightDiff);
    }

    // NOTE: PASS ONLY left.getPosition() as servoPos
    public static double rotationFromAngle(double angle, double servoPos) {
        double servoAngle = angleToServoPos(angle, 180);
        return ((servoPos - servoAngle) * 2 - 1) * 180;
    }

    public static double angleToServoPos(double angle, double range) {
        return (angle / range / 2 + 1) / 2;
    }
}
