package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.auto.Trajectory;
import org.firstinspires.ftc.teamcode.auto.TrajectoryBuilder;
import org.firstinspires.ftc.teamcode.auto.TrajectoryRunner;
import org.firstinspires.ftc.teamcode.file.FilesystemUtil;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Vector;


@Config
public class Drive {
    private final DcMotorEx leftFrontDrive, leftBackDrive, rightFrontDrive, rightBackDrive;
    private final IMU imu;

    private final TwoWheelTrackingLocalizer localizer;

    private final TrajectoryRunner runner;

    public static PIDFCoefficients xCoeffs = new PIDFCoefficients(0.15, 0, 0.004, 0), yCoeffs = new PIDFCoefficients(0.15, 0, 0.004, 0);
    public static PIDFCoefficients turn_coefficients = new PIDFCoefficients(0.7, 2, 0.04, 0);

    private double lastErrorX = 0;
    private double integralSumX = 0;
    private double lastErrorY = 0;
    private double integralSumY = 0;
    private double lastHeadingError = 0;
    private double integralSumHeading = 0;

    public static double headingUpper = 0.2, headingLower = 0.02;

    private Pose2d reference;

    private final FtcDashboard dashboard;

    private ElapsedTime clock;

    public Drive(HardwareMap hardwareMap, Pose2d startPose) {
        // Motors
        leftFrontDrive = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftBackDrive = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightFrontDrive = hardwareMap.get(DcMotorEx.class, "rightFront");
        rightBackDrive = hardwareMap.get(DcMotorEx.class, "rightRear");

        leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        leftFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        imu.initialize(parameters);
        imu.resetYaw();

        // Localizer
        localizer = new TwoWheelTrackingLocalizer(hardwareMap, this);
        localizer.setPoseEstimate(startPose);

        clock = new ElapsedTime();

        dashboard = FtcDashboard.getInstance();
        dashboard.setTelemetryTransmissionInterval(25);
        reference = startPose;

        runner = new TrajectoryRunner();
    }

    public Drive(HardwareMap hardwareMap) {
        // Motors
        leftFrontDrive = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftBackDrive = hardwareMap.get(DcMotorEx.class, "leftRear");
        rightFrontDrive = hardwareMap.get(DcMotorEx.class, "rightFront");
        rightBackDrive = hardwareMap.get(DcMotorEx.class, "rightRear");

        leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        imu.initialize(parameters);
        imu.resetYaw();

        // Localizer
        localizer = new TwoWheelTrackingLocalizer(hardwareMap, this);
        localizer.setPoseEstimate(new Pose2d(0, 0, 0));

        clock = new ElapsedTime();

        dashboard = FtcDashboard.getInstance();
        dashboard.setTelemetryTransmissionInterval(25);
        reference = new Pose2d(0, 0, 0);

        runner = new TrajectoryRunner();
    }

    public void setMotorPowers(double lf, double lb, double rb, double rf) {
        leftFrontDrive.setPower(lf);
        leftBackDrive.setPower(lb);
        rightBackDrive.setPower(rb);
        rightFrontDrive.setPower(rf);
    }

    public void setMotorPowers(double[] motorPowers) {
        leftFrontDrive.setPower(motorPowers[0]);
        leftBackDrive.setPower(motorPowers[1]);
        rightBackDrive.setPower(motorPowers[2]);
        rightFrontDrive.setPower(motorPowers[3]);
    }

    public double[] setPowerByPose(Pose2d pose) {
        double axial = pose.getX();
        double lateral = -pose.getY();
        double yaw = pose.getHeading();

        lateral = lateral * 1.1;

        double leftFrontPower = axial + lateral + yaw;
        double rightFrontPower = axial - lateral - yaw;
        double leftBackPower = axial - lateral + yaw;
        double rightBackPower = axial + lateral - yaw;

        double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        setMotorPowers(leftFrontPower,
                leftBackPower,
                rightFrontPower,
                rightBackPower);
        return new double[]{leftFrontPower, leftBackPower,
                rightFrontPower, rightBackPower};
    }

    public double[] setPowerByPoseField(Pose2d pose) {
        double axial = pose.getX();
        double lateral = -pose.getY();
        double yaw = pose.getHeading();

        double heading = localizer.getPoseEstimate().getHeading();

        double rotLateral = lateral * Math.cos(-heading) - axial * Math.sin(-heading);
        double rotAxial = lateral * Math.sin(-heading) + axial * Math.cos(-heading);

        rotLateral = rotLateral * 1.1;

        double leftFrontPower = rotAxial + rotLateral + yaw;
        double rightFrontPower = rotAxial - rotLateral - yaw;
        double leftBackPower = rotAxial - rotLateral + yaw;
        double rightBackPower = rotAxial + rotLateral - yaw;

        double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        setMotorPowers(leftFrontPower,
                leftBackPower,
                rightFrontPower,
                rightBackPower);
        return new double[]{leftFrontPower, leftBackPower,
                rightFrontPower, rightBackPower};
    }

    public void updateLocalizer() {
        localizer.update();
    }

    public Pose2d getPoseEstimate() {
        return localizer.getPoseEstimate();
    }

    public Pose2d getVelocityEstimate() {
        return localizer.getPoseVelocity();
    }

    public double[] getMotorPowers() {
        return new double[]{leftFrontDrive.getPower(), leftBackDrive.getPower(),
                rightFrontDrive.getPower(), rightBackDrive.getPower()};
    }

    public List<Double> getWheelVelocities() {
        return localizer.getWheelVelocities();
    }

    public List<Double> getWheelPositions() {
        return localizer.getWheelPositions();
    }

    public double getParralelVelocity() {
        return Objects.requireNonNull(localizer.getWheelVelocities()).get(0);
    }

    public double getPerpendicularVelocity() {
        return Objects.requireNonNull(localizer.getWheelVelocities()).get(1);
    }

    public double getVelocityInTicks() {
        List<Double> wheelVelocities = localizer.getWheelVelocities();
        return Math.sqrt(wheelVelocities.get(1) * wheelVelocities.get(1) +
                wheelVelocities.get(3) * wheelVelocities.get(3));
    }

    public double getRawExternalHeading() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    public double getCorrectedHeading(double angle) {
        double H = getPoseEstimate().getHeading() - angle;
        double x = 1 / ( 2 * Math.PI ) * H + 0.5;
        return 2 * Math.PI * ( x - Math.floor(x) ) - Math.PI;
    }

    public Double getExternalHeadingVelocity() {
        return (double) imu.getRobotAngularVelocity(AngleUnit.RADIANS).zRotationRate;
    }

    public TrajectoryBuilder buildTrajectory(String file) throws IOException {
        return new TrajectoryBuilder(FilesystemUtil.loadFile(file));
    }

    public TrajectoryBuilder buildTrajectory(String file, double startHeading) throws IOException {
        localizer.setPoseEstimate(new Pose2d(0, 0, startHeading));
        return new TrajectoryBuilder(FilesystemUtil.loadFile(file));
    }

    public void followTrajectory(Trajectory trajectory) {
        localizer.setPoseEstimate(new Pose2d(trajectory.get(0).point0, Math.toRadians(localizer.getHeading())));
        runner.followTrajectory(trajectory);
    }

    public Pose2d toPoint() {
        Pose2d poseEstimate = getPoseEstimate();

        Vector2d error = reference.vec().minus(getPoseEstimate().vec());
        double errorX = error.getX(), errorY = error.getY();
        double derivativeX = ( errorX - lastErrorX ) / clock.seconds(), derivativeY = ( errorY - lastErrorY ) / clock.seconds();
        integralSumX += errorX * clock.seconds();
        integralSumY += errorY * clock.seconds();
        double powerX = ( xCoeffs.p * errorX ) + ( xCoeffs.i * integralSumX ) + ( xCoeffs.d * derivativeX );
        double powerY = ( yCoeffs.p * errorY ) + ( yCoeffs.i * integralSumY ) + ( yCoeffs.d * derivativeY );

        powerX = Math.min(Math.max(powerX, -0.7), 0.7);
        powerY = Math.min(Math.max(powerY, -0.7), 0.7);

        double headingReference = reference.getHeading();
        double headingError = getCorrectedHeading(headingReference);
        double headingDerivative = ( headingError - lastHeadingError ) / clock.seconds();
        if (Math.abs(headingError) > headingUpper) {
            integralSumHeading = 0;
        } else if (Math.abs(headingError) < headingLower) {
            integralSumHeading = integralSumHeading;
        } else {
            integralSumHeading += headingError * clock.seconds();
        }

        double headingPower = ( turn_coefficients.p * headingError ) + ( turn_coefficients.i * integralSumHeading ) + ( turn_coefficients.d * headingDerivative );

        TelemetryPacket packet = new TelemetryPacket();

        packet.put("referenceX", reference.getX());
        packet.put("x", poseEstimate.getX());

        packet.put("referenceY", reference.getY());
        packet.put("y", poseEstimate.getY());

        packet.put("referenceHeading", reference.getHeading());
        packet.put("heading", getCorrectedHeading(0));

        dashboard.sendTelemetryPacket(packet);

        lastHeadingError = headingError;
        lastErrorX = errorX;
        lastErrorY = errorY;

        clock.reset();

        setPowerByPoseField(new Pose2d(powerX, powerY, headingPower));
        return new Pose2d(errorX, errorY, headingError);
    }

    public void update() {
        updateLocalizer();
        Pose2d res = runner.update(getPoseEstimate(), getVelocityEstimate());
        setPowerByPoseField(res);
    }

    public Pose2d getReference() {
        return reference;
    }

    public void setReference(Pose2d reference) {
        if (reference.getHeading() != this.reference.getHeading()) {
            integralSumHeading = 0;
        }
        if (reference.getX() != this.reference.getX()) {
            integralSumX = 0;
        }
        if (reference.getY() != this.reference.getY()) {
            integralSumY = 0;
        }
        this.reference = reference;
    }
}
