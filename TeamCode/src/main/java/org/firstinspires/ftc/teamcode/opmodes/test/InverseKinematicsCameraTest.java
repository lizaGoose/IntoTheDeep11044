package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.teamcode.camera.AnalyzedSample;
import org.firstinspires.ftc.teamcode.camera.AutoCamera;
import org.firstinspires.ftc.teamcode.camera.SampleColor;
import org.firstinspires.ftc.teamcode.hardware.StickyKeys;
import org.firstinspires.ftc.teamcode.math.ClawCalculation;
import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Differential;
import org.firstinspires.ftc.teamcode.modules.Extend;
import org.firstinspires.ftc.teamcode.modules.Hand;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Perkid;
import org.firstinspires.ftc.teamcode.modules.ScorerPerkid;

import java.util.List;

@Config
@TeleOp(group = "test")
public class InverseKinematicsCameraTest extends OpMode {
    // 150 345
    // 345 - 240 = 105
    // 240 - 105 = 135
    // 135 345
    public static double OFFSET = 80;
    public static double PICK_UP_OFFSET = 60;
    public static double PICK_UP_CLOSE_AFTER = 100;
    public static double PICK_UP_LIFT_AFTER = 200;
    public static double EXTEND_MIN_ERROR = 20;
    public static double X = 25.3 - 83.76641;
    public static double Y = 0;
    public static double Z = 127.45;
    public static double PICK_UP_LIFT_OFFSET = 20;
    public static double HAND_COEFF = 0.01;

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
    private ElapsedTime globalTimer;
    private ElapsedTime pickUpTimer;

    private enum State {//kto prochital tot penis vzal v rotan
        IDLE,
        AIMING,
        AIM,
        PICKUP
    }
    private State state = State.IDLE;
    private double pickUpDelta = 0;

    private AnalyzedSample targetSample = new AnalyzedSample(Y, Z, 0, SampleColor.RED, 1);
    private Vector3D targetSamplePosition = new Vector3D(Y, Z, 0.0);
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

        globalTimer = new ElapsedTime();
        pickUpTimer = new ElapsedTime();

        lift.setSpecimen();
        scorerPerkid.scoreSpecimen();

        kinematicsResult = ClawCalculation.inverseKinematics(X, Y, Z);
        dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
    }

    @Override
    public void init_loop() {
       globalTimer.reset();
    }

    @Override
    public void loop() {
        AnalyzedSample sample = camera.getSamplePosition(lift.getPosition(), scorerPerkid.getPos())
                .orElse(targetSample);
        double sampleRotation = camera.getAngle()
                .orElse(targetSampleRotation);

        if (stickyKeys.triangle) {
            state = State.AIMING;
            kinematicsResult = ClawCalculation.inverseKinematics(X, sample.x, sample.y);
            if (!kinematicsResult.contains(Double.NaN)) {
                extend.setPositionMillimeters(kinematicsResult.get(0) - OFFSET);
                perkid.setAngle(Math.toDegrees(kinematicsResult.get(1)));
                hand.setAngle(Math.toDegrees(kinematicsResult.get(2)));
                differential.setAngleRotation(-91, sample.angle - Math.toDegrees(kinematicsResult.get(2)));
                claw.open();
                targetSample = sample;
            }
        }

        if (stickyKeys.circle) {
            kinematicsResult = ClawCalculation.inverseKinematics(X, sample.x, sample.y);
            if (!kinematicsResult.contains(Double.NaN)) {
                extend.setPositionMillimeters(kinematicsResult.get(0) - OFFSET);
                perkid.setAngle(Math.toDegrees(kinematicsResult.get(1)));
                hand.setAngle(Math.toDegrees(kinematicsResult.get(2)));
                differential.setAngleRotation(-91, sample.angle - Math.toDegrees(kinematicsResult.get(2)));
                claw.open();
            }
        }
        if (gamepad1.circle) {
            if (!kinematicsResult.contains(Double.NaN)) {
                extend.setPositionMillimeters(kinematicsResult.get(0) - OFFSET);
                perkid.setAngle(Math.toDegrees(kinematicsResult.get(1)));
                hand.setAngle(Math.toDegrees(kinematicsResult.get(2)));
                differential.setAngleRotation(-91, targetSample.angle - Math.toDegrees(kinematicsResult.get(2)));
            }
        }

        if (gamepad1.cross) {
            kinematicsResult = ClawCalculation.inverseKinematics(X, sample.x, sample.y);
            if (!kinematicsResult.contains(Double.NaN)) {
                extend.setPositionMillimeters(kinematicsResult.get(0) - OFFSET);
                perkid.setAngle(Math.toDegrees(kinematicsResult.get(1)));
                hand.setAngle(Math.toDegrees(kinematicsResult.get(2)));
                differential.setAngleRotation(-91, sample.angle - Math.toDegrees(kinematicsResult.get(2)));
                claw.open();
            }
        }

        if (state == State.AIMING && Math.abs(extend.getPosition() - extend.getReference()) < EXTEND_MIN_ERROR) {
            state = State.AIM;
        }

        if (state == State.AIM) {
            state = State.PICKUP;
            kinematicsResult = ClawCalculation.inverseKinematics(X - PICK_UP_OFFSET, targetSample.x, targetSample.y);
            if (!kinematicsResult.contains(Double.NaN)) {
                extend.setPositionMillimeters(kinematicsResult.get(0) - OFFSET);
                perkid.setAngle(Math.toDegrees(kinematicsResult.get(1)));
                hand.setAngle(Math.toDegrees(kinematicsResult.get(2)));
                differential.setAngleRotation(-91, targetSample.angle - Math.toDegrees(kinematicsResult.get(2)));
            }
        }

        if (state == State.PICKUP) {
            pickUpDelta += globalTimer.milliseconds();
            if (pickUpDelta < PICK_UP_CLOSE_AFTER) {
                claw.open();
            } else if (pickUpDelta < PICK_UP_LIFT_AFTER){
                claw.close();
            } else {
                kinematicsResult = ClawCalculation.inverseKinematics(X + PICK_UP_LIFT_OFFSET, targetSample.x, targetSample.y);
                if (!kinematicsResult.contains(Double.NaN)) {
                    extend.setPositionMillimeters(kinematicsResult.get(0));
                    perkid.setAngle(Math.toDegrees(kinematicsResult.get(1)));
                    hand.setAngle(Math.toDegrees(kinematicsResult.get(2)));
                    differential.setAngleRotation(-91, targetSample.angle - Math.toDegrees(kinematicsResult.get(2)));
                }
            }
        } else {
            pickUpDelta = 0;
        }
        //sosi penis

        if (gamepad1.square) {
            state = State.IDLE;
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
                claw.open();
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
        telemetry.addData("x_sample", sample.x);
        telemetry.addData("y_sample", sample.y);
        telemetry.addData("rot_sample", sampleRotation);
        telemetry.addData("loop_time_ns", globalTimer.nanoseconds());
        telemetry.addData("samples_list", camera.pip.getDetectedSamples());
        globalTimer.reset();
    }
}
