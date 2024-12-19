package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Camera.SampleDetectionCameraClass;
import org.firstinspires.ftc.teamcode.Camera.SampleDetectionPipeline;
import org.firstinspires.ftc.teamcode.math.ClawCalculation;

public class Modules {
    LinearOpMode linearOpMode;
    SampleDetectionCameraClass cam;
    SampleDetectionPipeline pip;
    Claw claw;
    Diff diff;
    Extend extend;
    Hand hand;
    Perkid perkid;
    Gamepad gamepad1, gamepad2;
    CRServo serv;
    boolean smartButton;
    ScoringPerkid scorPer;
    ScoringDiff diffScor;
    ScorClaw clawScor;
    LiftError lift;
    double countPerkid = 50;
    ElapsedTime timer, timerScor, pickupTimer, dropHumanTimer, hangTimer;
    Hang hang;
    DriveTrain dt;

    enum Mode {
        VISION_CONTROL,
        OPERATOR_CONTROL
    }

    Mode modules = Mode.OPERATOR_CONTROL;

    public Modules(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        hang = new Hang(linearOpMode);
        cam = new SampleDetectionCameraClass(linearOpMode);
        claw = new Claw(linearOpMode);
        diff = new Diff(linearOpMode);
        extend = new Extend(linearOpMode);
        diffScor = new ScoringDiff(linearOpMode);
        hand = new Hand(linearOpMode);
        perkid = new Perkid(linearOpMode);
        pip = new SampleDetectionPipeline();
        clawScor = new ScorClaw(linearOpMode);
        scorPer = new ScoringPerkid(linearOpMode);
        lift = new LiftError(linearOpMode);
        gamepad1 = linearOpMode.gamepad1;
        gamepad2 = linearOpMode.gamepad2;
        timer = new ElapsedTime();
        timerScor = new ElapsedTime();
        pickupTimer = new ElapsedTime();
        dropHumanTimer = new ElapsedTime();
        dt = new DriveTrain(linearOpMode);
        hangTimer = new ElapsedTime();
    }

    public void teleop() {
        claw.teleop();
        diff.teleop(Math.round(cam.Angle));
        extend.teleop();
        hand.teleop();
        perkid.teleop();
        clawScor.teleop();
        scorPer.teleop();
        diffScor.teleop();
        lift.teleop();
        hang.teleop();
        dt.teleOp();


        if (gamepad1.x) {
            modules = Mode.VISION_CONTROL;


        }
        if (gamepad1.a) {
            modules = Mode.OPERATOR_CONTROL;
        }


        switch (modules) {

            case VISION_CONTROL:
                cam.visionInUse();
                cam.operatorControlBlue();

                if (gamepad1.dpad_left) {
                    diff.currentMode = Diff.Mode.VISION_HUYNYA;
                }

                break;

            case OPERATOR_CONTROL:
                cam.operatorControlBlue();
                extend.currentMode = Extend.Mode.DRIVER_CONTROL;
                lift.currentMode = LiftError.Mode.NULL_POSE;


                if (gamepad2.dpad_up) {

                    claw.currentMode = Claw.Mode.PICK;
                    timer.reset();

                }
                if (gamepad2.dpad_down) {
                    perkid.currentMode = Perkid.Mode.PICK_POSITION;
                    diff.currentMode = Diff.Mode.PICK_DOWN;
                }

                if (timer.milliseconds() <= 200) {
                    clawScor.currentMode = ScorClaw.Mode.DROP;
                    claw.currentMode = Claw.Mode.PICK;
                } else if (timer.milliseconds() > 200 && timer.milliseconds() <= 300) {
                    clawScor.currentMode = ScorClaw.Mode.DROP;
                    claw.currentMode = Claw.Mode.PICK;
                    perkid.currentMode = Perkid.Mode.SCORE_POSITION;
                    scorPer.currentMode = ScoringPerkid.Mode.PICK;
                    diff.currentMode = Diff.Mode.TRANSFER;
                    //extend.setReference(0);
                }
                if (gamepad2.left_bumper) {
                    timerScor.reset();
                }
                if (timerScor.milliseconds() <= 150) {
                    clawScor.currentMode = ScorClaw.Mode.PICK;
                    claw.currentMode = Claw.Mode.PICK;
                } else if (timerScor.milliseconds() > 150 && timerScor.milliseconds() <= 250) {
                    claw.currentMode = Claw.Mode.DROP;
                }

                if (gamepad2.dpad_left) {
                    perkid.currentMode = Perkid.Mode.AIM_POSITION;
                    diff.currentMode = Diff.Mode.STRAIGHT_DOWN;
                    claw.currentMode = Claw.Mode.PICK;
                    dropHumanTimer.reset();
                }
                if (dropHumanTimer.milliseconds() >= 500 && dropHumanTimer.milliseconds() <= 600) {
                    claw.currentMode = Claw.Mode.DROP;
                }
                if (gamepad2.a) { // cross
                    diffScor.currentMode = ScoringDiff.Mode.PICK_POSITION;
                    clawScor.currentMode = ScorClaw.Mode.PICK;
                    scorPer.currentMode = ScoringPerkid.Mode.PICK;
                    perkid.currentMode = Perkid.Mode.SCORE_POSITION;
                    diff.currentMode = Diff.Mode.TRANSFER;
                    hand.currentMode = Hand.Mode.FORWARD;
                    timerScor.reset();

                }
                if (gamepad2.y) { // triangle
                    diffScor.currentMode = ScoringDiff.Mode.SCORE_SAMP;
                    clawScor.currentMode = ScorClaw.Mode.PICK;
                    scorPer.currentMode = ScoringPerkid.Mode.SAMPLE;
                    perkid.currentMode = Perkid.Mode.SCORE_POSITION;
                    hand.currentMode = Hand.Mode.FORWARD;
                    timerScor.reset();
                }
                if (gamepad2.b) { // circle
                    clawScor.currentMode = ScorClaw.Mode.DROP;
                    scorPer.currentMode = ScoringPerkid.Mode.PICK_SPEC;
                    diffScor.currentMode = ScoringDiff.Mode.PICK_SPEC;
                    perkid.currentMode = Perkid.Mode.SCORE_POSITION;
                    hand.currentMode = Hand.Mode.FORWARD;
                    timerScor.reset();
                }
                if (gamepad2.x) { // square
                    scorPer.currentMode = ScoringPerkid.Mode.SPECIMEN;
                    clawScor.currentMode = ScorClaw.Mode.PICK;
                    diffScor.currentMode = ScoringDiff.Mode.PICK_SPEC;
                    perkid.currentMode = Perkid.Mode.UP_POSITION;
                    hand.currentMode = Hand.Mode.FORWARD;
                    timerScor.reset();
                    pickupTimer.reset();
                }
                if (300 >= pickupTimer.milliseconds() && pickupTimer.milliseconds() >= 200) {
                    diffScor.currentMode = ScoringDiff.Mode.SCORE_SPEC;
                }
                if (gamepad2.right_bumper) {
                    clawScor.currentMode = ScorClaw.Mode.DROP;
                }
                if (gamepad1.dpad_up) {
                    hang.currentMode = Hang.Mode.ENGAGED;
                 //   dt.currentMode = DriveTrain.Mode.NON;
                    hangTimer.reset();
                }
                if (gamepad1.dpad_down) {
                    hang.currentMode = Hang.Mode.NOT_ENGAGED;

                }
                if (hang.currentMode == Hang.Mode.ENGAGED) {

                    dt.currentMode = DriveTrain.Mode.NON;
                }
            else{
                    dt.currentMode = DriveTrain.Mode.DRIVER_CONTROL;
                }
                if(gamepad1.dpad_right){
                    perkid.currentMode = Perkid.Mode.LOOK;
                    diff.currentMode = Diff.Mode.GET_DOWN;
                }


                break;
        }
        linearOpMode.telemetry.addData("handPos", hand.hand.getPosition());
        linearOpMode.telemetry.addData("handPos", gamepad2.left_bumper);
        linearOpMode.telemetry.addData("inverseKinematics", ClawCalculation.inverseKinematics(10,cam.y, cam.x));
        linearOpMode.telemetry.addData("handPos", gamepad2.left_bumper);

        linearOpMode.telemetry.addData("xActual", cam.xActual);
        linearOpMode.telemetry.addData("yActual", cam.yActual);
        linearOpMode.telemetry.addData("x", cam.x);
        linearOpMode.telemetry.addData("y", cam.y);
        linearOpMode.telemetry.addData("Distance", cam.distance);
        linearOpMode.telemetry.addData("reference", extend.getReference());
        linearOpMode.telemetry.addData("encoder", extend.motor.getCurrentPosition());

        linearOpMode.telemetry.addData("left_lift_wheel", dt.l);
        linearOpMode.telemetry.addData("right_lift_wheel", dt.r);
        linearOpMode.telemetry.addData("hang_motor", hang.hang.getPower());


        linearOpMode.telemetry.update();
        resetTimers();
    }

    public void resetTimers() {
        hand.resetTimer();
    }
}
