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
import java.util.stream.Collectors;


@Config
public class TrajectoryRunner {
    private final ElapsedTime clock;
    private final FtcDashboard dashboard;

    private Trajectory currentTrajectory = null;
    private BezierCurve currentCurve;
    private double t;
    private int curveIndex;
    private List<SpatialMarker> markers;
    private List<StopPoint> stopPoints;
    private double stopPointTime;
    private List<TurnMarker> turns;
    private List<TimeMarker> timeMarkers;
    private int lastStopPoint;
    private ElapsedTime timeMarkerTimer;
    private ArrayList<Pose2d> poseHistory;
    private ArrayList<Vector2d> turnHistory;

    public static double kP = 0.25;
    public static double kCentripetal = 0.035;
    public static double FORCE = 0.65;
    private double force = FORCE; // TODO: REMOVE
    public static double VELOCITY_THRESHOLD = 0.001;
    public static double NEXT_POINT_THRESHOLD = 0.1;
    private double fake_t = 0;

    public boolean uselessFlag = false;

    double lastCurvature;

    private double lastHeadingError = 0;
    private double integralSumHeading = 0;
    public static double headingUpper = 0.2, headingLower = 0.02;

    public static PIDFCoefficients turnCoeffs = new PIDFCoefficients(0.7, 2, 0.04, 0);
    // 0.15 0.004
    // 0.12 0.018
    public static PIDFCoefficients xCoeffs = new PIDFCoefficients(0.12, 0, 0.018, 0);

    public static PIDFCoefficients yCoeffs = new PIDFCoefficients(0.12, 0, 0.018, 0);

    public static double stopThreshold = 0.05;

    private double lastErrorX = 0;
    private double integralSumX = 0;
    private double lastErrorY = 0;
    private double integralSumY = 0;

    public TrajectoryRunner() {
        clock = new ElapsedTime();
        dashboard = FtcDashboard.getInstance();
        dashboard.setTelemetryTransmissionInterval(25);
    }

    public void followTrajectory(Trajectory trajectory) {
        currentTrajectory = trajectory;
        markers = trajectory.getMarkers().stream()
                .map(SpatialMarker::clone)
                .collect(Collectors.toList());
        stopPoints = trajectory.getStopPoints().stream()
                .map(StopPoint::clone)
                .collect(Collectors.toList());
        lastStopPoint = 0;
        stopPointTime = 0;
        turns = trajectory.getTurns().stream()
                .map(TurnMarker::clone)
                .collect(Collectors.toList());
        timeMarkers = trajectory.getTimeMarkers().stream()
                .map(TimeMarker::clone)
                .collect(Collectors.toList());
        t = 0;
        curveIndex = 0;
        currentCurve = currentTrajectory.get(0);
        lastCurvature = currentCurve.getCurvatureAt(t);
        poseHistory = new ArrayList<>();
        turnHistory = new ArrayList<>();
        timeMarkerTimer = new ElapsedTime();
    }

    public Pose2d[] update(Pose2d poseEstimate, Pose2d velocityEstimate) {
        TelemetryPacket packet = new TelemetryPacket();
        Canvas fieldOverlay = packet.fieldOverlay();

        Pose2d newPose = null;

        if (currentTrajectory == null) {
            draw(fieldOverlay, poseEstimate, poseEstimate.vec(), new Vector2d(), new Vector2d());

            dashboard.sendTelemetryPacket(packet);
            clock.reset();

            return new Pose2d[]{new Pose2d(0, 0, 0), null};
        }

        Pose2d result = new Pose2d(0, 0, 0);
        t = currentCurve.getClosestTtoPoint(poseEstimate.vec());

        if (!stopPoints.isEmpty() && ( t + curveIndex >= stopPoints.get(0).t ||
                ( Math.abs(( t + curveIndex ) - stopPoints.get(0).t) <= stopThreshold ) )) {

            if (!timeMarkers.isEmpty() && ( timeMarkerTimer.milliseconds() >= timeMarkers.get(0).milliseconds
                && timeMarkers.get(0).waitIndex == lastStopPoint)) {
                timeMarkers.get(0).getRunnable().onMarkerReached();
                timeMarkers.remove(0);
            }

            if (!timeMarkers.isEmpty() && timeMarkers.get(0).waitIndex < lastStopPoint) {
                timeMarkers.get(0).getRunnable().onMarkerReached();
                timeMarkers.remove(0);
            }

            Vector2d reference = currentCurve.getPointAt(stopPoints.get(0).t - curveIndex);
            Vector2d error = reference.minus(poseEstimate.vec());
            double errorX = error.getX(), errorY = error.getY();
            double derivativeX = (errorX - lastErrorX) / clock.seconds(), derivativeY = (errorY - lastErrorY) / clock.seconds();
            integralSumX += errorX * clock.seconds();
            integralSumY += errorY * clock.seconds();
            double powerX = ( xCoeffs.p * errorX ) + ( xCoeffs.i * integralSumX ) + ( xCoeffs.d * derivativeX );
            double powerY = ( yCoeffs.p * errorY ) + ( yCoeffs.i * integralSumY ) + ( yCoeffs.d * derivativeY );

            powerX = Math.min(Math.max(powerX, -force), force);
            powerY = Math.min(Math.max(powerY, -force), force);

            double headingReference = stopPoints.get(0).angle;
            double headingError = correctHeading(poseEstimate.getHeading(), headingReference);
            double headingDerivative = ( headingError - lastHeadingError ) / clock.seconds();
            if (Math.abs(headingError) > headingUpper) {
                integralSumHeading = 0;
            } else if (Math.abs(headingError) < headingLower) {
                integralSumHeading = integralSumHeading;
            } else {
                integralSumHeading += headingError * clock.seconds();
            }

            double headingPower = ( turnCoeffs.p * headingError ) + ( turnCoeffs.i * integralSumHeading ) + ( turnCoeffs.d * headingDerivative );

            lastHeadingError = headingError;
            lastErrorX = errorX;
            lastErrorY = errorY;

            packet.put("PID", true);
            packet.put("wait_point_index", lastStopPoint);
            packet.put("curve", false);
            packet.put("power_x", powerX);
            packet.put("power_y", powerY);

            packet.put("reference_x", reference.getX());
            packet.put("reference_y", reference.getY());
            packet.put("reference_heading", headingReference);
            packet.put("x_derivative", derivativeX);
            packet.put("y_derivative", derivativeY);
            packet.put("heading_derivative", headingDerivative);

//            draw(fieldOverlay, poseEstimate, reference, new Vector2d(), reference);

            result = new Pose2d(powerX, powerY, headingPower);

//             draw(fieldOverlay, poseEstimate, reference, new Vector2d(), result.vec());

            if (stopPoints.get(0).milliseconds >= stopPointTime) {
                stopPointTime += clock.milliseconds();
            } else {
                stopPoints.remove(0);
                lastStopPoint += 1;
                timeMarkerTimer.reset();
                stopPointTime = 0;
            }
        } else {
//            if (velocityEstimate.vec().norm() < VELOCITY_THRESHOLD && 1 - t <= NEXT_POINT_THRESHOLD) {
//                if (curveIndex != currentTrajectory.size() - 1) {
//                    curveIndex += 1;
//                    t = 0;
//                    newPose = new Pose2d(currentTrajectory.get(curveIndex).getPointAt(t), poseEstimate.getHeading());
//                } else {
//                    t = 1.0;
//                    newPose = new Pose2d(currentTrajectory.get(curveIndex).getPointAt(t), poseEstimate.getHeading());
//                }
//            }

            timeMarkerTimer.reset();
            integralSumHeading = 0;

            Vector2d reference = currentCurve.getPointAt(t);
            Vector2d error = reference.minus(poseEstimate.vec());

            Vector2d derivative = currentCurve.getFirstDerivativeAt(t);
            Vector2d normDeriv = derivative.div(derivative.norm());

            double curvature = currentCurve.getCurvatureAt(t);
            double accel = curvatureToForce(curvature);

            Vector2d centripetalForce = normDeriv.rotated(Math.toRadians(90)).times(
                    kCentripetal * Math.pow(velocityEstimate.vec().norm(), 2) * curvature
            );

            Vector2d pathing_power = normDeriv.plus(error.times(kP));

            double headingPower = 0;

            double headingReference = 0;

            if (!turns.isEmpty() && curveIndex == turns.get(0).curve) {
                headingReference = turns.get(0).getProducer().produce(t);

                double headingError = correctHeading(poseEstimate.getHeading(), headingReference);

                double headingDerivative = ( headingError - lastHeadingError ) / clock.seconds();

                headingPower = ( turnCoeffs.p * headingError ) + ( turnCoeffs.i * 0 ) + ( turnCoeffs.d * headingDerivative );

                lastHeadingError = headingError;
            } else if (!turns.isEmpty() && curveIndex > turns.get(0).curve) {
                turns.remove(0);
            }

            result = new Pose2d(pathing_power.plus(centripetalForce).times(accel), headingPower);

            draw(fieldOverlay, poseEstimate, reference, centripetalForce, pathing_power);

            packet.put("error_norm", error.norm());
            packet.put("pathing_power_norm", pathing_power.norm());

            packet.put("reference_heading", headingReference);
            packet.put("heading_power", headingPower);
            packet.put("corrected_heading", lastHeadingError);
            packet.put("curvature", curvature);

            packet.put("PID", false);
            packet.put("curve", true);
        }

        if (!markers.isEmpty() && t + curveIndex >= markers.get(0).t) {
            markers.get(0).getRunnable().onMarkerReached();
            markers.remove(0);
        }

        poseHistory.add(poseEstimate);

        packet.put("x", poseEstimate.getX());
        packet.put("y", poseEstimate.getY());
        packet.put("heading", poseEstimate.getHeading());

        packet.put("x_vel", velocityEstimate.getX());
        packet.put("y_vel", velocityEstimate.getY());
        packet.put("heading_vel", velocityEstimate.getHeading());

        packet.put("velocity", velocityEstimate.vec().norm());

        packet.put("t", t + curveIndex);
        packet.put("curve_index", curveIndex);

        packet.put("curves", currentTrajectory.size());

        if (newPose == null) {
            if (1 - t <= 1e-4 && curveIndex != currentTrajectory.size() - 1) {
                if ((markers.isEmpty() || (int) Math.floor(markers.get(0).t) > curveIndex) &&
                        (stopPoints.isEmpty() || (int) Math.floor(stopPoints.get(0).t) > curveIndex)) {
                    curveIndex += 1;
                    currentCurve = currentTrajectory.get(curveIndex);
                    t = 0;
                }
            }

            if ((Math.abs(t + curveIndex - currentTrajectory.size()) <= 1e-2 && stopPoints.isEmpty()) ||
                    stopPoints.isEmpty()) {
                packet.put("END_OF_TRAJ", true);
                currentTrajectory = null;
            }
        }

        dashboard.sendTelemetryPacket(packet);
        clock.reset();

        return new Pose2d[]{result, newPose};
    }

    private void draw(Canvas fieldOverlay, Pose2d poseEstimate, Vector2d reference,
                      Vector2d centripetalForce, Vector2d pathing_power) {
        FieldDrawer.drawPoint(fieldOverlay, reference, 1, "blue");

        FieldDrawer.drawRobot(fieldOverlay, poseEstimate);
        FieldDrawer.drawPoseHistory(fieldOverlay, poseHistory);
        if (turnHistory != null) {
            for (Vector2d vector : turnHistory) {
                FieldDrawer.drawPoint(fieldOverlay, vector, 1, "yellow");
            }
        }
        FieldDrawer.drawVectorFromRobot(fieldOverlay, poseEstimate.vec(), centripetalForce);
        FieldDrawer.drawVectorFromRobot(fieldOverlay, poseEstimate.vec(), pathing_power);
        if (currentTrajectory != null) {
            for (int i = 0; i < currentTrajectory.size(); i++) {
                FieldDrawer.drawBezierCurve(
                        fieldOverlay,
                        currentTrajectory.get(i),
                        1,
                        "green"
                );
            }
        }
    }

    public void discardTrajectory() {
        currentTrajectory = null;
    }

    private double correctHeading(double heading, double reference) {
        double H = heading - reference;
        double x = 1 / ( 2 * Math.PI ) * H + 0.5;
        return 2 * Math.PI * ( x - Math.floor(x) ) - Math.PI;
    }

    private double curvatureToForce(double curvature) {
        return force;
    }

    public boolean isBusy() {
        return currentTrajectory != null;
    }

    public void setForce(double force) {
        this.force = force;
    }

    public void resetForce() {
        this.force = FORCE;
    }
}
