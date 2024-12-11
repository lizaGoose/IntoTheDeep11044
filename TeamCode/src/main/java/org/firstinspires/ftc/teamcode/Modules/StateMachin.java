package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Camera.SampleDetectionCameraClass;

public class StateMachin {
    LinearOpMode linearOpMode;
    Extend extend;
    Claw claw;
    Perkid perkid;
    ScoringPerkid scorPerkid;
    ScoringDiff scorDiff;
    Hand hand;
    ScorClaw scorClaw;
    Gamepad gamepad1, gamepad2;
    Diff diff;
    SampleDetectionCameraClass cam;
    ElapsedTime pickedUnpickedTimer, fromClawToClawTimer;
    public DistanceSensor distanceSensor;


    public enum State {
        INITIAL,
        LOOK_FOR_BLOCK,
        PICK_UP_THE_BLOCK,
        FROM_CLAW_TO_CLAW
    }

    public State state = State.INITIAL;

    public StateMachin(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;

        extend = new Extend(linearOpMode);
        claw = new Claw(linearOpMode);
        perkid = new Perkid(linearOpMode);
        scorPerkid = new ScoringPerkid(linearOpMode);
        scorDiff = new ScoringDiff(linearOpMode);
        hand = new Hand(linearOpMode);
        scorClaw = new ScorClaw(linearOpMode);
        diff = new Diff(linearOpMode);
        cam = new SampleDetectionCameraClass(linearOpMode);

        gamepad1 = linearOpMode.gamepad1;
        gamepad2 = linearOpMode.gamepad2;

        pickedUnpickedTimer = new ElapsedTime();
        fromClawToClawTimer = new ElapsedTime();

    }

    public void teleop() {
        distanceSensor = linearOpMode.hardwareMap.get(DistanceSensor.class, "Color");
        extend.teleop();
        claw.teleop();
        perkid.teleop();
        scorPerkid.teleop();
        scorDiff.teleop();
        hand.teleop();
        scorClaw.teleop();
        diff.teleop(Math.round(cam.Angle));

        switch (state) {
            case INITIAL:
                extend.currentMode = Extend.Mode.DRIVER_CONTROL;
                perkid.currentMode = Perkid.Mode.TRANSFER;
                claw.currentMode = Claw.Mode.DROP;
                scorPerkid.currentMode = ScoringPerkid.Mode.PICK_SPEC;
                scorDiff.currentMode = ScoringDiff.Mode.PICK_SPEC;
                hand.currentMode = Hand.Mode.FORWARD;
                scorClaw.currentMode = ScorClaw.Mode.DROP;
                diff.currentMode = Diff.Mode.TRANSFER;

                if (gamepad2.square) {
                    state = State.LOOK_FOR_BLOCK;
                }
                break;
            case LOOK_FOR_BLOCK:
                perkid.currentMode = Perkid.Mode.LOOK;
                diff.currentMode = Diff.Mode.STRAIGHT_DOWN;
                scorPerkid.currentMode = ScoringPerkid.Mode.TRANSFER;
                scorClaw.currentMode = ScorClaw.Mode.DROP;
                scorDiff.currentMode = ScoringDiff.Mode.GET_FROM_CLAW;

                if (gamepad2.cross) {
                    state = State.PICK_UP_THE_BLOCK;
                    pickedUnpickedTimer.reset();
                }
                break;
            case PICK_UP_THE_BLOCK:
                perkid.currentMode = Perkid.Mode.PICK_UP;
                diff.currentMode = Diff.Mode.PICK_UP;

                if (pickedUnpickedTimer.milliseconds() > 700 && distanceSensor.getDistance(DistanceUnit.MM) > 8.5) {
                    state = State.LOOK_FOR_BLOCK;
                } else if (pickedUnpickedTimer.milliseconds() > 700 && distanceSensor.getDistance(DistanceUnit.MM) <= 8.5) {
                    state = State.FROM_CLAW_TO_CLAW;
                }
                break;
            case FROM_CLAW_TO_CLAW:
                fromClawToClawTimer.reset();
                //claw.currentMode = Claw.Mode.PICK;
                perkid.currentMode = Perkid.Mode.TRANSFER;
                diff.currentMode = Diff.Mode.TRANSFER;
                if (fromClawToClawTimer.milliseconds() <= 700) {
                    scorClaw.currentMode = ScorClaw.Mode.DROP;
                } else {
                    scorClaw.currentMode = ScorClaw.Mode.PICK;
                }
                break;

        }
        linearOpMode.telemetry.addData("distance", distanceSensor.getDistance(DistanceUnit.MM));
        linearOpMode.telemetry.addData("state", state);
        linearOpMode.telemetry.update();
    }
}
