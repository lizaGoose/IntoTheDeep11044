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
        double d0 = z - ( d2 / 2 );
        double theta2 = Math.asin(y / d2);
        double fi1 = Math.atan(( x + d3 ) / ( z - d0 ));
        double fi = Math.atan(d1 / ( d2 * Math.cos(theta2) ));
        double theta1 = fi1 - fi;
        double theta3 = -Math.PI / 2 + theta1;
        return Arrays.asList(d0, theta1, theta2, theta3);
    }

    public static List<Double> inverseKinematics(double x, double y, double z) {
        double theta2 = Math.asin(y / d2);
        double fi1 = Math.asin(( x + d3 ) / Math.sqrt(Math.pow(d1, 2) + Math.pow(d2 * Math.cos(theta2), 2)));
        double fi = Math.atan(d1 / ( d2 + Math.cos(theta2) ));
        double theta1 = fi1 - fi;
        double d0 = z - ( d2 * Math.cos(theta2) * Math.cos(theta1) ) + d1 * Math.sin(theta1);
        double theta3 = -Math.PI / 2 + theta1;
        return Arrays.asList(d0, theta1, theta2, theta3);
    }

    public static List<Double> angleAndRotToDiff(double angle, double rotation) {
        double leftDiff = ( angle / 90 ) / 2 + ( rotation / 180 ) / 1.25;
        double rightDiff = ( angle / 90 ) / 2 - ( rotation / 180 ) / 1.25;

        return Arrays.asList(leftDiff, rightDiff);
    }

    public static List<Double> angleAndRotToDiffFix(double angle, double rotation) {
        double leftDiff = ( ( angle + rotation / 2 ) / 90 + 1 ) / 2;
        double rightDiff = ( ( angle - rotation / 2 ) / 90 + 1 ) / 2;

        return Arrays.asList(leftDiff, rightDiff);
    }

    // NOTE: PASS ONLY left.getPosition() as servoPos
    public static double rotationFromAngle(double angle, double servoPos) {
        return ( ( servoPos * 2 - 1 ) * 90 - angle ) * 2;
    }

    public static double angleToServoPos(double angle, double range) {
        return ( angle / range / 2 + 1 ) / 2;
    }

    public static List<Double> camToRealWorldCoordinates(double rotAngle, double xMov, double yMov, double xActual, double yActual, double pixelsPerInch) {

        double[][] rotMat0C = {
                {1, 0, 0},
                {0, Math.cos(rotAngle), -Math.sin(rotAngle)},
                {0, Math.sin(rotAngle), Math.cos(rotAngle)}
        };


        double[][] dispVec0C = {
                {xMov},
                {yMov},
                {0.0}
        };


        double[][] extraRowHomgen = {
                {0, 0, 0, 1}
        };


        double[][] homgen0C = new double[3][4];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(rotMat0C[i], 0, homgen0C[i], 0, 3);
            homgen0C[i][3] = dispVec0C[i][0];
        }

        double[][] finalHomgen0C = new double[4][4];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(homgen0C[i], 0, finalHomgen0C[i], 0, 4);
        }
        System.arraycopy(extraRowHomgen[0], 0, finalHomgen0C[3], 0, 4);


        double x2Cm = xActual * pixelsPerInch/640;
        double y2Cm = yActual * pixelsPerInch/640;

        double[][] camRefCoord = {
                {x2Cm},
                {y2Cm},
                {0.0},
                {1}
        };


        double[][] coordBaseFrame = new double[4][1];
        for (int i = 0; i < 4; i++) {
            coordBaseFrame[i][0] = finalHomgen0C[i][0] * camRefCoord[0][0] +
                    finalHomgen0C[i][1] * camRefCoord[1][0] +
                    finalHomgen0C[i][2] * camRefCoord[2][0] +
                    finalHomgen0C[i][3] * camRefCoord[3][0];
        }

        return Arrays.asList(coordBaseFrame[0][0], coordBaseFrame[1][0]);
    }
}
