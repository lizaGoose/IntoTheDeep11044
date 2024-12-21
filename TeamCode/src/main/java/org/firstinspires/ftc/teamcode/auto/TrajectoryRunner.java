package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.dashboard.FieldDrawer;
import org.firstinspires.ftc.teamcode.math.BezierCurve;

import java.util.ArrayList;
import java.util.List;


@Config
public class TrajectoryRunner {
    private final ElapsedTime clock;
    private final FtcDashboard dashboard;

    private Trajectory currentTrajectory;
    private BezierCurve currentCurve;
    private double t;
    private int curveIndex;
    private List<SpatialMarker> markers;
    private List<StopPoint> stopPoints;
    private List<TurnMarker> turns;
    private ArrayList<Pose2d> poseHistory;
    private ArrayList<Vector2d> turnHistory;

    public static double kP = 0.25;
    public static double kCentripetal = 0.035;
    public static double force = 0.7; // TODO: REMOVE

    public boolean uselessFlag = false;

    double lastCurvature;

    double lastHeadingError = 0;

    public static PIDFCoefficients turn_coefficients = new PIDFCoefficients(0.15, 0, 0, 0);

    public static PIDFCoefficients x_coefficients = new PIDFCoefficients(0, 0, 0, 0);

    public static PIDFCoefficients y_coefficients = new PIDFCoefficients(0, 0, 0, 0);

    private double lastErrorX = 0;
    private double lastErrorY = 0;

    public TrajectoryRunner() {
        clock = new ElapsedTime();
        dashboard = FtcDashboard.getInstance();
        dashboard.setTelemetryTransmissionInterval(25);
    }

    public void followTrajectory(Trajectory trajectory) {
        currentTrajectory = trajectory;
        markers = trajectory.getMarkers();
        stopPoints = trajectory.getStopPoints();
        turns = trajectory.getTurns();
        t = 0;
        curveIndex = 0;
        currentCurve = currentTrajectory.get(0);
        lastCurvature = currentCurve.getCurvatureAt(t);
        poseHistory = new ArrayList<>();
        turnHistory = new ArrayList<>();
    }

    public Pose2d update(Pose2d poseEstimate, Pose2d velocityEstimate) {
        Pose2d result = new Pose2d(0, 0, 0);
        t = currentCurve.getClosestTtoPoint(poseEstimate.vec());

        TelemetryPacket packet = new TelemetryPacket();
        Canvas fieldOverlay = packet.fieldOverlay();

        if (!stopPoints.isEmpty() && ( t + curveIndex >= stopPoints.get(0).t ||
                ( Math.abs(( t + curveIndex ) - stopPoints.get(0).t) <= 1e-6 ) )) {
            if (stopPoints.get(0).milliseconds >= 0) {
                stopPoints.get(0).milliseconds -= (int) clock.milliseconds();
            } else {
                stopPoints.remove(0);
            }

            Vector2d reference = currentCurve.getPointAt(t);
            Vector2d error = reference.minus(poseEstimate.vec());
            double errorX = error.getX(), errorY = error.getY();
            double derivativeX = (errorX - lastErrorX) / clock.seconds(), derivativeY = (errorY - lastErrorY) / clock.seconds();
            double powerX = (x_coefficients.p * errorX) + (x_coefficients.d * derivativeX);
            double powerY = (y_coefficients.p * errorY) + (x_coefficients.d * derivativeY);


            double headingReference = stopPoints.get(0).angle;
            double headingError = correctHeading(poseEstimate.getHeading(), headingReference);
            double headingDerivative = ( headingError - lastHeadingError ) / clock.seconds();
            double headingPower = ( turn_coefficients.p * headingError ) + ( turn_coefficients.i * 0 ) + ( turn_coefficients.d * headingDerivative );

            lastHeadingError = headingError;
            lastErrorX = errorX;
            lastErrorY = errorY;

            result = new Pose2d(powerX, powerY, headingPower);

        } else if (Math.abs(t - 1) <= 1e-6) {
            Vector2d reference = currentCurve.getPointAt(t);
            Vector2d error = reference.minus(poseEstimate.vec());

            curveIndex += 1;
            if (curveIndex == currentTrajectory.size()) {
                curveIndex -= 1;
                result = new Pose2d(error.times(kP), 0);
            } else {
                currentCurve = currentTrajectory.get(curveIndex);
            }
        } else {
            Vector2d reference = currentCurve.getPointAt(t);
            Vector2d error = reference.minus(poseEstimate.vec());
            //.div(reference.minus(poseEstimate.vec()).norm())

            Vector2d derivative = currentCurve.getFirstDerivativeAt(t);
            Vector2d normDeriv = derivative.div(derivative.norm());

            double curvature = currentCurve.getCurvatureAt(t);
            //double curvatureDeriv = (Math.abs(curvature) - Math.abs(lastCurvature)) / clock.seconds();
            double accel = curvatureToForce(curvature);
            /*
            if (curvatureDeriv > curvatureDerivLimit && Math.abs(curvature) > curvatureLimit) {
                turnHistory.add(poseEstimate.vec());
                accel = kTurnAccel;
            }
             */

            Vector2d centripetalForce = normDeriv.rotated(Math.toRadians(90)).times(
                    kCentripetal * Math.pow(velocityEstimate.vec().norm(), 2) * curvature
            );

            // double angle = Math.asin(error.norm());

            // Vector2d pathing_power = normDer.rotated(angle);
            Vector2d pathing_power = normDeriv.plus(error.times(kP));

            double headingPower = 0;

            double headingReference = 0;

            if (!turns.isEmpty() && curveIndex == turns.get(0).curve) {
                headingReference = Math.toRadians(turns.get(0).getProducer().produce(0));

                double headingError = correctHeading(poseEstimate.getHeading(), headingReference);

                double headingDerivative = ( headingError - lastHeadingError ) / clock.seconds();

                headingPower = ( turn_coefficients.p * headingError ) + ( turn_coefficients.i * 0 ) + ( turn_coefficients.d * headingDerivative );

                lastHeadingError = headingError;
            } else if (!turns.isEmpty() && curveIndex > turns.get(0).curve) {
                turns.remove(0);
            }

            result = new Pose2d(pathing_power.plus(centripetalForce).times(accel), headingPower);

            draw(fieldOverlay, poseEstimate, reference, centripetalForce, pathing_power);

            packet.put("error_norm", error.norm());
            packet.put("pathing_power_norm", pathing_power.norm());


            packet.put("heading_reference", headingReference);
            packet.put("heading_power", headingPower);
            packet.put("corrected_heading", lastHeadingError);
            packet.put("curvature", curvature);
        }



        if (!markers.isEmpty() && t + curveIndex >= markers.get(0).t) {
            markers.get(0).getRunnable().onMarkerReached();
            markers.remove(0);
        }

        poseHistory.add(poseEstimate);


        packet.put("x", poseEstimate.getX());
        packet.put("y", poseEstimate.getY());
        packet.put("heading", poseEstimate.getHeading());

        packet.put("velocity", velocityEstimate.vec().norm());


        dashboard.sendTelemetryPacket(packet);
        clock.reset();

        return result;
    }

    private void draw(Canvas fieldOverlay, Pose2d poseEstimate, Vector2d reference,
                      Vector2d centripetalForce, Vector2d pathing_power) {
        FieldDrawer.drawPoint(fieldOverlay, reference, 1, "blue");

        FieldDrawer.drawRobot(fieldOverlay, poseEstimate);
        FieldDrawer.drawPoseHistory(fieldOverlay, poseHistory);
        for (Vector2d vector : turnHistory) {
            FieldDrawer.drawPoint(fieldOverlay, vector, 1, "yellow");
        }
        FieldDrawer.drawVectorFromRobot(fieldOverlay, poseEstimate.vec(), centripetalForce);
        FieldDrawer.drawVectorFromRobot(fieldOverlay, poseEstimate.vec(), pathing_power);

        for (int i = 0; i < currentTrajectory.size(); i++) {
            FieldDrawer.drawBezierCurve(
                    fieldOverlay,
                    currentTrajectory.get(i),
                    1,
                    "green"
            );
        }
    }

    private double correctHeading(double heading, double reference) {
        double H = heading - reference;
        double x = 1 / ( 2 * Math.PI ) * H + 0.5;
        return 2 * Math.PI * ( x - Math.floor(x) ) - Math.PI;
    }

    private double curvatureToForce(double curvature) {
        return force;
    }
}
