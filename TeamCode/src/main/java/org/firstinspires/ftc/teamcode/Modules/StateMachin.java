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
    Differential diff;
   // SampleDetectionCameraClass cam;
    ElapsedTime pickedUnpickedTimer, fromClawToClawTimer, dropSpecimenTimer, goToInitialFromScoreSample;
    public DistanceSensor distanceSensor;
    liftError lift;
    boolean sampleIsScored, startTimer;
    DriveTrain dt;


    public enum State {
        INITIAL,
        LOOK_FOR_BLOCK,
        PICK_UP_THE_BLOCK,
        FROM_CLAW_TO_CLAW,
        SCORE_SAMPLE,
        DROP_SPECIMEN_TO_HUMAN_ZONE,
        TRANSFER_SPEC,
        SCORE_SPEC
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
        diff = new Differential(linearOpMode);
        lift = new liftError(linearOpMode);
        dt = new DriveTrain(linearOpMode);
        //cam = new SampleDetectionCameraClass(linearOpMode);

        gamepad1 = linearOpMode.gamepad1;
        gamepad2 = linearOpMode.gamepad2;

        pickedUnpickedTimer = new ElapsedTime();
        fromClawToClawTimer = new ElapsedTime();
        dropSpecimenTimer = new ElapsedTime();
        goToInitialFromScoreSample = new ElapsedTime();

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
        lift.teleop();
        diff.teleop();
        dt.teleOp();
        dt.currentMode = DriveTrain.Mode.DRIVER_CONTROL;

        switch (state) {
            case INITIAL:
                sampleIsScored = false;
                startTimer = false;
                extend.currentMode = Extend.Mode.DRIVER_CONTROL;
                perkid.currentMode = Perkid.Mode.TRANSFER;
                claw.currentMode = Claw.Mode.DROP;
                scorPerkid.currentMode = ScoringPerkid.Mode.PICK_SPEC;
                scorDiff.currentMode = ScoringDiff.Mode.PICK_SPEC;
                hand.currentMode = Hand.Mode.FORWARD;
                scorClaw.currentMode = ScorClaw.Mode.DROP;
                diff.currentMode = Differential.Mode.TRANSFER;
                lift.currentMode = liftError.Mode.NULL_POSE;


                if (gamepad2.square) {
                    state = State.LOOK_FOR_BLOCK;
                }
                if(gamepad2.dpad_up){
                  state = State.TRANSFER_SPEC;
                }
                break;
            case TRANSFER_SPEC:
                lift.pos = -450;
                scorPerkid.currentMode = ScoringPerkid.Mode.TRANSFER_SPEC;
                scorClaw.currentMode = ScorClaw.Mode.PICK;
                perkid.currentMode = Perkid.Mode.LOOK;
                diff.currentMode = Differential.Mode.STRAIGHT_DOWN;
                if(gamepad2.dpad_left){
               state = State.SCORE_SPEC;
                }
                break;
            case SCORE_SPEC:
                scorPerkid.currentMode = ScoringPerkid.Mode.SCOR_SPEC;
                scorDiff.currentMode = ScoringDiff.Mode.SCORE_SPEC;
                break;
            case LOOK_FOR_BLOCK:
                perkid.currentMode = Perkid.Mode.LOOK;
                diff.currentMode = Differential.Mode.STRAIGHT_DOWN;
                scorPerkid.currentMode = ScoringPerkid.Mode.TRANSFER;
                scorClaw.currentMode = ScorClaw.Mode.DROP;
                scorDiff.currentMode = ScoringDiff.Mode.GET_FROM_CLAW;
                hand.currentMode = Hand.Mode.DRIVER_CONTROL;

                if (gamepad2.cross) {
                    state = State.PICK_UP_THE_BLOCK;
                    pickedUnpickedTimer.reset();
                }
                break;
            case PICK_UP_THE_BLOCK:
                perkid.currentMode = Perkid.Mode.PICK_UP;
                diff.currentMode = Differential.Mode.PICK_UP;

                if (pickedUnpickedTimer.milliseconds() > 1500 && distanceSensor.getDistance(DistanceUnit.MM) > 9.5) {
                    state = State.LOOK_FOR_BLOCK;
                } else if (pickedUnpickedTimer.milliseconds() > 1500 && distanceSensor.getDistance(DistanceUnit.MM) <= 9.5) {
                    fromClawToClawTimer.reset();
                    claw.currentMode = Claw.Mode.PICK;
                    extend.setReference(-150);
                    state = State.FROM_CLAW_TO_CLAW;
                }
                break;
            case FROM_CLAW_TO_CLAW:

                claw.currentMode = Claw.Mode.PICK;
                perkid.currentMode = Perkid.Mode.TRANSFER;
                diff.currentMode = Differential.Mode.TRANSFER;
                scorDiff.currentMode = ScoringDiff.Mode.GET_FROM_CLAW;
                hand.currentMode = Hand.Mode.FORWARD;
                if (fromClawToClawTimer.milliseconds() <= 1000) {
                    scorClaw.currentMode = ScorClaw.Mode.DROP;
                } else {
                    scorClaw.currentMode = ScorClaw.Mode.PICK;
                    claw.currentMode = Claw.Mode.DROP;
                }
                if(gamepad2.triangle){
                    state = State.SCORE_SAMPLE;
                }
                if(gamepad2.circle){
                    dropSpecimenTimer.reset();
                    state = State.DROP_SPECIMEN_TO_HUMAN_ZONE;
                }
                break;
            case DROP_SPECIMEN_TO_HUMAN_ZONE:
                scorClaw.currentMode = ScorClaw.Mode.DROP;
                diff.currentMode = Differential.Mode.STRAIGHT_DOWN;
                perkid.currentMode = Perkid.Mode.LOOK;
                hand.currentMode = Hand.Mode.FORWARD;

                if(dropSpecimenTimer.milliseconds() <= 1000){
                    claw.currentMode = Claw.Mode.PICK;
                }
                else {
                    claw.currentMode = Claw.Mode.DROP;
                    state = State.INITIAL;
                }
                break;
            case SCORE_SAMPLE:
                scorPerkid.currentMode = ScoringPerkid.Mode.SCORE_SAMP;
                scorDiff.currentMode = ScoringDiff.Mode.SCORE_SAMP;
                lift.pos = -1000;
                if(gamepad2.right_bumper){
                    sampleIsScored = true;
                }

                if (sampleIsScored == true){
                    scorClaw.currentMode = ScorClaw.Mode.DROP;
                    if (Math.abs(gamepad1.left_stick_y) >= 0.4){
                        goToInitialFromScoreSample.reset();
                        startTimer = true;
                    }

                    if(goToInitialFromScoreSample.milliseconds() >= 350 && startTimer == true){
                        lift.pos = 0;
                        state = State.INITIAL;
                    }
                }
                break;

        }
        linearOpMode.telemetry.addData("distance", distanceSensor.getDistance(DistanceUnit.MM));
        linearOpMode.telemetry.addData("state", state);
        linearOpMode.telemetry.addData("lift", lift.leftMotor.getCurrentPosition());
        linearOpMode.telemetry.addData("StartTimer", startTimer);
        linearOpMode.telemetry.update();
    }
}
