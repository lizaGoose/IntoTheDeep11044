package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.teamcode.camera.AutoCamera;
import org.firstinspires.ftc.teamcode.hardware.StickyKeys;
import org.firstinspires.ftc.teamcode.math.ClawCalculation;
import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Differential;
import org.firstinspires.ftc.teamcode.modules.Extend;
import org.firstinspires.ftc.teamcode.modules.Hand;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Perkid;
import org.firstinspires.ftc.teamcode.modules.ScorerPerkid;

import java.util.Arrays;
import java.util.List;

@Config
@TeleOp(group = "test")
public class InverseKinematicsCameraTest extends OpMode {
    public static double X = 25.3 - 83.76641;
    public static double Y = 0;
    public static double Z = 127.45;

    private Extend extend;
    private Differential differential;
    private Perkid perkid;
    private Hand hand;
    private Claw claw;
    private Lift lift;
    private ScorerPerkid scorerPerkid;
    private FtcDashboard dashboard;
    private AutoCamera camera;
    private StickyKeys stickyKeys;

    private Vector3D targetSamplePosition = new Vector3D(0.0, 0.0, 0.0);
    private double targetSampleRotation = 0;
    private List<Double> kinematicsResult;

    @Override
    public void init() {
        extend = new Extend(this);
        differential = new Differential(this);
        perkid = new Perkid(this);
        hand = new Hand(this);
        claw = new Claw(this);
        camera = new AutoCamera(this);
        camera.operatorControlRed();
        lift = new Lift(this);
        scorerPerkid = new ScorerPerkid(this);
        stickyKeys = new StickyKeys();

        lift.setSpecimen();
        scorerPerkid.scoreSpecimen();

        kinematicsResult = ClawCalculation.inverseKinematics(X, Y, Z);
        dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
    }

    @Override
    public void loop() {
        Vector3D samplePosition = camera.getSamplePosition(lift.getPosition());
        double sampleRotation = camera.getAngle();

        if (stickyKeys.triangle) {
            kinematicsResult = ClawCalculation.inverseKinematics(X, samplePosition.getX() * 25.4, samplePosition.getY() * 25.4 - 220.04);
            if (!kinematicsResult.contains(Double.NaN)) {
                extend.setPositionMillimeters(kinematicsResult.get(0) - 45);
                perkid.setAngle(Math.toDegrees(kinematicsResult.get(1)));
                hand.setAngle(Math.toDegrees(kinematicsResult.get(2)));
                differential.setAngleRotation(Math.toDegrees(kinematicsResult.get(3)), sampleRotation + Math.abs(kinematicsResult.get(2)));
                claw.open();
                targetSamplePosition = samplePosition;
                targetSampleRotation = sampleRotation + hand.getAngle();
            }
        }

        if (stickyKeys.circle) {
            kinematicsResult = ClawCalculation.inverseKinematics(X - 50, targetSamplePosition.getX() * 25.4, targetSamplePosition.getY() * 25.4 - 220.04);
            if (!kinematicsResult.contains(Double.NaN)) {
                extend.setPositionMillimeters(kinematicsResult.get(0) - 45);
                perkid.setAngle(Math.toDegrees(kinematicsResult.get(1)));
                hand.setAngle(Math.toDegrees(kinematicsResult.get(2)));
                differential.setAngleRotation(Math.toDegrees(kinematicsResult.get(3)), targetSampleRotation);
                claw.close();
            }
        }

        if (gamepad1.square) {
            kinematicsResult = ClawCalculation.inverseKinematics(X, Y, Z);
            if (!kinematicsResult.contains(Double.NaN)) {
                extend.setDirectPower(-0.5);
                if (extend.getPosition() < 0) {
                    extend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    extend.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
                extend.setPositionMillimeters(kinematicsResult.get(0));
                perkid.setAngle(Math.toDegrees(kinematicsResult.get(1)));
                hand.setAngle(Math.toDegrees(kinematicsResult.get(2)));
                differential.setAngleRotation(Math.toDegrees(kinematicsResult.get(3)), 0);
            }
        } else {
            extend.PIDControl();
        }

        lift.setSpecimen();
        lift.PIDControl();

        stickyKeys.update(gamepad1);

        telemetry.addData("lift", lift.getPosition());
        telemetry.addData("lift_reference", lift.getReference());
        telemetry.addData("extend_mm", extend.getPositionMillimeters());
        telemetry.addData("extend_pos", extend.getPosition());
        telemetry.addData("extend_target", extend.getReference());
        telemetry.addData("d0", kinematicsResult.get(0));
        telemetry.addData("diff_rot", targetSamplePosition);
        telemetry.addData("theta1", Math.toDegrees(kinematicsResult.get(1)));
        telemetry.addData("theta2", Math.toDegrees(kinematicsResult.get(2)));
        telemetry.addData("theta3", Math.toDegrees(kinematicsResult.get(1)));
        telemetry.addData("x_sample", samplePosition.getX());
        telemetry.addData("y_sample", samplePosition.getY());
        telemetry.addData("rot_sample", sampleRotation);
    }
}
