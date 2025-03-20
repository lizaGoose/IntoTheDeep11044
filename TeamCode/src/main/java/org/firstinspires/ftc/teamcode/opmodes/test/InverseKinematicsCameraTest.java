package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.camera.AnalyzedSample;
import org.firstinspires.ftc.teamcode.camera.AutoCamera;
import org.firstinspires.ftc.teamcode.camera.SampleColor;
import org.firstinspires.ftc.teamcode.hardware.StickyKeys;
import org.firstinspires.ftc.teamcode.math.ClawCalculation;
import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Differential;
import org.firstinspires.ftc.teamcode.modules.Linkage;
import org.firstinspires.ftc.teamcode.modules.Hand;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Perkid;
import org.firstinspires.ftc.teamcode.modules.ScorerPerkid;

import java.util.List;
import java.util.Optional;

@Config
@TeleOp(group = "test")
public class InverseKinematicsCameraTest extends OpMode {
    // 150 345
    // 345 - 240 = 105
    // 240 - 105 = 135
    // 135 345
    public static double OFFSET = 60;
    public static double PICK_UP_OFFSET = 60;
    public static double PICK_UP_CLOSE_AFTER = 100;
    public static double PICK_UP_LIFT_AFTER = 200;
    public static double EXTEND_MIN_ERROR = 20;
    private static final double X = 25.3 - 83.76641;
    private static final double Y = 0;
    private static final double Z = 127.45;
    public static double PICK_UP_LIFT_OFFSET = 20;
    public static double HAND_COEFF = 0.01;

    private Linkage linkage;
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
    private List<Double> kinematicsResult;

    @Override
    public void init() {
        linkage = new Linkage(this);
        differential = new Differential(this);
        perkid = new Perkid(this);
        hand = new Hand(this);
        claw = new Claw(this);
        camera = new AutoCamera(this);
        camera.operatorControlRed();
        camera.startCamera();
        lift = new Lift(this);
        scorerPerkid = new ScorerPerkid(this);
        stickyKeys = new StickyKeys();

        globalTimer = new ElapsedTime();
        pickUpTimer = new ElapsedTime();

        lift.setSpecimen();
        scorerPerkid.scoreSpecimen();

        kinematicsResult = ClawCalculation.inverseKinematics_noAngle(X, Y, Z);
        dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
    }

    @Override
    public void init_loop() {
       globalTimer.reset();
    }

    @Override
    public void loop() {
        AnalyzedSample sample = camera.getOptimalSamplePosition(lift.getPosition(), scorerPerkid.getPos())
                .orElse(targetSample);

        if (stickyKeys.triangle) {
            state = State.AIMING;
            kinematicsResult = ClawCalculation.inverseKinematics_noAngle(X, sample.x, sample.y);
            if (!kinematicsResult.contains(Double.NaN)) {
                linkage.setPositionMillimeters(kinematicsResult.get(0) - OFFSET);
                perkid.setAngle(Math.toDegrees(kinematicsResult.get(1)));
                hand.setAngle(Math.toDegrees(kinematicsResult.get(2)));
                differential.setAngleRotation(-91, sample.angle - Math.toDegrees(kinematicsResult.get(2)));
                claw.open();
                targetSample = sample;
            }
        }

        if (stickyKeys.circle) {
            kinematicsResult = ClawCalculation.inverseKinematics_noAngle(X, sample.x, sample.y);
            if (!kinematicsResult.contains(Double.NaN)) {
                linkage.setPositionMillimeters(kinematicsResult.get(0) - OFFSET);
                perkid.setAngle(Math.toDegrees(kinematicsResult.get(1)));
                hand.setAngle(Math.toDegrees(kinematicsResult.get(2)));
                differential.setAngleRotation(-91, sample.angle - Math.toDegrees(kinematicsResult.get(2)));
                claw.open();
            }
        }
        if (gamepad1.circle) {
            if (!kinematicsResult.contains(Double.NaN)) {
                linkage.setPositionMillimeters(kinematicsResult.get(0) - OFFSET);
                perkid.setAngle(Math.toDegrees(kinematicsResult.get(1)));
                hand.setAngle(Math.toDegrees(kinematicsResult.get(2)));
                differential.setAngleRotation(-91, targetSample.angle - Math.toDegrees(kinematicsResult.get(2)));
            }
        }

        if (gamepad1.cross) {
            kinematicsResult = ClawCalculation.inverseKinematics_noAngle(X, sample.x, sample.y);
            if (!kinematicsResult.contains(Double.NaN)) {
                linkage.setPositionMillimeters(kinematicsResult.get(0) - OFFSET);
                perkid.setAngle(Math.toDegrees(kinematicsResult.get(1)));
                hand.setAngle(Math.toDegrees(kinematicsResult.get(2)));
                differential.setAngleRotation(-91, sample.angle - Math.toDegrees(kinematicsResult.get(2)));
                claw.open();
            }
        }

        if (state == State.AIMING && Math.abs(linkage.getPosition() - linkage.getReference()) < EXTEND_MIN_ERROR) {
            state = State.AIM;
        }

        if (state == State.AIM) {
            state = State.PICKUP;
            kinematicsResult = ClawCalculation.inverseKinematics_noAngle(X - PICK_UP_OFFSET, targetSample.x, targetSample.y);
            if (!kinematicsResult.contains(Double.NaN)) {
                linkage.setPositionMillimeters(kinematicsResult.get(0) - OFFSET);
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
                kinematicsResult = ClawCalculation.inverseKinematics_noAngle(X + PICK_UP_LIFT_OFFSET, targetSample.x, targetSample.y);
                if (!kinematicsResult.contains(Double.NaN)) {
                    linkage.setPositionMillimeters(kinematicsResult.get(0));
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
            kinematicsResult = ClawCalculation.inverseKinematics_noAngle(X, Y, Z);
            if (!kinematicsResult.contains(Double.NaN)) {
                linkage.setDirectPower(-0.5);
                if (linkage.getPosition() < 0) {
                    linkage.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    linkage.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                }
                linkage.setPositionMillimeters(kinematicsResult.get(0));
                perkid.setAngle(Math.toDegrees(kinematicsResult.get(1)));
                hand.setAngle(Math.toDegrees(kinematicsResult.get(2)));
                differential.setAngleRotation(Math.toDegrees(kinematicsResult.get(3)), 0);
                claw.open();
            }
        } else {
            linkage.PIDControl();
        }

        lift.setSpecimen();
        lift.PIDControl();

        stickyKeys.update(gamepad1);

        telemetry.addData("lift", lift.getPosition());
        telemetry.addData("lift_reference", lift.getReference());
        telemetry.addData("extend_mm", linkage.getPositionMillimeters());
        telemetry.addData("extend_pos", linkage.getPosition());
        telemetry.addData("extend_target", linkage.getReference());
        telemetry.addData("d0", kinematicsResult.get(0));
        telemetry.addData("diff_rot", targetSample.angle - Math.toDegrees(kinematicsResult.get(2)));
        telemetry.addData("theta1", Math.toDegrees(kinematicsResult.get(1)));
        telemetry.addData("theta2", Math.toDegrees(kinematicsResult.get(2)));
        telemetry.addData("theta3", Math.toDegrees(kinematicsResult.get(1)));
        telemetry.addData("pure_sample", camera.getSample().flatMap(x -> Optional.of(x.toString())).orElse("none"));
        telemetry.addData("sample", sample);
        telemetry.addData("targetSample", targetSample);
        telemetry.addData("loop_time_ns", globalTimer.nanoseconds());
        telemetry.addData("samples_list", camera.pip.getDetectedSamples());
        globalTimer.reset();
    }
}
