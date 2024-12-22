package org.firstinspires.ftc.teamcode.Modules;

import static org.firstinspires.ftc.teamcode.Modules.TimerConstants.REGISTER_MOVEMENT_AFTER;
import static org.firstinspires.ftc.teamcode.Modules.TimerConstants.SET_POS_TO_ZERO_SAMPLE;
import static org.firstinspires.ftc.teamcode.Modules.TimerConstants.WAIT_UNTIL_SPECIMEN_PICKUP;
import static org.firstinspires.ftc.teamcode.Modules.TimerConstants.RELAX_SCORE_CLAW;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Camera.SampleDetectionCameraClass;

@Config
class SensorsConstants {
    public static double DIFFERENTIAL_CLAW_THRESHOLD = 10.2;
}

@Config
class TimerConstants {
    public static double RELAX_SCORE_CLAW = 2000;
    public static double WAIT_UNTIL_SPECIMEN_PICKUP = 100;
    public static double REGISTER_MOVEMENT_AFTER = 450;
    public static double SET_POS_TO_ZERO_SAMPLE = 300;
}

@Config
public class StateMachine {
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
    Hang hang;
    ElapsedTime pickedUnpickedTimer, fromClawToClawTimer, dropSpecimenTimer, goToInitialFromScoreSample, goFromScoreSpecTimer, fromTransferSpecimenToScore, forgetPosTimer;
    private final ElapsedTime specimenPickedUp, specimenToTransfer, movingBack;
    public DistanceSensor distanceSensor, distInClaw;
    LiftError lift;
    boolean sampleIsScored, startTimer, startTimerFromClawToClaw, startGoFromScorSpec, startFromClawToClaw, startForgetPosTimer, stopForgetPosTimer;


    DriveTrain dt;
    public DigitalChannel frontSensor;
    public static double upToChamber = -380, upToChamber2 = -430;
    public double zeroPos = 0;
    SampleDetectionCameraClass cam;


    public enum State {
        INITIAL,
        LOOK_FOR_BLOCK,
        PICK_UP_THE_BLOCK,
        FROM_CLAW_TO_CLAW,
        SCORE_SAMPLE,
        DROP_SPECIMEN_TO_HUMAN_ZONE,
        TRANSFER_SPEC,
        SCORE_SPEC,
        PICK_SPEC,
        EMPTY
    }

    public State state = State.INITIAL;

    public StateMachine(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;

        cam = new SampleDetectionCameraClass(linearOpMode);
        extend = new Extend(linearOpMode);
        claw = new Claw(linearOpMode);
        perkid = new Perkid(linearOpMode);
        scorPerkid = new ScoringPerkid(linearOpMode);
        scorDiff = new ScoringDiff(linearOpMode);
        hand = new Hand(linearOpMode);
        scorClaw = new ScorClaw(linearOpMode);
        diff = new Differential(linearOpMode);
        lift = new LiftError(linearOpMode);
        dt = new DriveTrain(linearOpMode);
        hang = new Hang(linearOpMode);
        //cam = new SampleDetectionCameraClass(linearOpMode);

        gamepad1 = linearOpMode.gamepad1;
        gamepad2 = linearOpMode.gamepad2;

        pickedUnpickedTimer = new ElapsedTime();
        fromClawToClawTimer = new ElapsedTime();
        dropSpecimenTimer = new ElapsedTime();
        goToInitialFromScoreSample = new ElapsedTime();

        fromTransferSpecimenToScore = new ElapsedTime();
        specimenPickedUp = new ElapsedTime();
        specimenToTransfer = new ElapsedTime();
        movingBack = new ElapsedTime();
        forgetPosTimer = new ElapsedTime();
    }

    public void teleop() {
        distanceSensor = linearOpMode.hardwareMap.get(DistanceSensor.class, "Color");
        distInClaw = linearOpMode.hardwareMap.get(DistanceSensor.class, "ScorClawDist");
        frontSensor = linearOpMode.hardwareMap.get(DigitalChannel.class, "DistanceFront");
        cam.visionInUse();
        extend.teleop();
        claw.teleop();
        perkid.teleop();
        scorPerkid.teleop();
        scorDiff.teleop();
        hand.teleop();
        scorClaw.teleop();
        lift.teleop();
        diff.teleop(cam.Angle);
        dt.teleOp();
        hang.teleop();
        dt.currentMode = DriveTrain.Mode.DRIVER_CONTROL;
        lift.currentMode = LiftError.Mode.NULL_POSE;
        if (gamepad2.left_bumper) {
            extend.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            extend.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        if (state != State.PICK_UP_THE_BLOCK) {
            startForgetPosTimer = false;
            stopForgetPosTimer = false;
        }
        if (hang.hang.getCurrentPosition() >= 500){
            scorPerkid.currentMode = ScoringPerkid.Mode.TRANSFER;
            scorDiff.currentMode = ScoringDiff.Mode.GET_FROM_CLAW;
            extend.setHang();
            state = State.EMPTY;

        }

        switch (state) {
            case INITIAL:
                sampleIsScored = false;
                startTimer = false;
                startTimerFromClawToClaw = false;
                startGoFromScorSpec = false;

                extend.currentMode = Extend.Mode.DRIVER_CONTROL;
                perkid.currentMode = Perkid.Mode.TRANSFER;
                claw.currentMode = Claw.Mode.TRANSFER;
                scorPerkid.currentMode = ScoringPerkid.Mode.PICK_SPEC;
                scorDiff.currentMode = ScoringDiff.Mode.PICK_SPEC;
                hand.currentMode = Hand.Mode.FORWARD;
                scorClaw.currentMode = ScorClaw.Mode.DROP;
                diff.currentMode = Differential.Mode.TRANSFER;
                //  lift.currentMode = LiftError.Mode.NULL_POSE;
                //lift.pos = 0;


                if (gamepad2.square) {
                    state = State.LOOK_FOR_BLOCK;
                }
                if (gamepad2.cross) {
                    state = State.TRANSFER_SPEC;
                    fromTransferSpecimenToScore.reset();
                }

                if (distInClaw.getDistance(DistanceUnit.MM) >= 17) {
                    specimenPickedUp.reset();
                }
                if (specimenPickedUp.milliseconds() > WAIT_UNTIL_SPECIMEN_PICKUP) {
                    state = State.PICK_SPEC;
                }
                break;
            case PICK_SPEC:
                scorClaw.currentMode = ScorClaw.Mode.PICK;
                // lift.pos = -200;

                if (!( gamepad1.left_stick_y <= -0.1 )) {
                    specimenToTransfer.reset();
                }

                if (specimenToTransfer.milliseconds() > REGISTER_MOVEMENT_AFTER || gamepad2.square) {
                    state = State.TRANSFER_SPEC;
                    fromTransferSpecimenToScore.reset();
                }
                if (gamepad2.triangle) {
                    state = State.INITIAL;
                }
                break;

            case TRANSFER_SPEC:
                // lift.pos = upToChamber;
                scorPerkid.currentMode = ScoringPerkid.Mode.TRANSFER_SPEC;
                scorClaw.currentMode = ScorClaw.Mode.PICK;
                perkid.currentMode = Perkid.Mode.LOOK;
                diff.currentMode = Differential.Mode.STRAIGHT_DOWN;
                if (fromTransferSpecimenToScore.milliseconds() > 500) {
                    scorDiff.currentMode = ScoringDiff.Mode.TRANSFER_SPEC;
                } else {
                    scorDiff.currentMode = ScoringDiff.Mode.PICK_SPEC;
                }

                hand.currentMode = Hand.Mode.DRIVER_CONTROL;

                if (!frontSensor.getState()) {
                    state = State.SCORE_SPEC;
                    fromTransferSpecimenToScore.reset();
                }
                if (gamepad2.triangle) {
                    state = State.INITIAL;
                }
                break;
            case SCORE_SPEC:
                scorPerkid.currentMode = ScoringPerkid.Mode.SCOR_SPEC;
                scorDiff.currentMode = ScoringDiff.Mode.SCORE_SPEC;
                hand.currentMode = Hand.Mode.DRIVER_CONTROL;
                if (gamepad2.right_bumper || gamepad1.circle) {
                    scorClaw.currentMode = ScorClaw.Mode.DROP;
                } else if (( fromTransferSpecimenToScore.milliseconds() > RELAX_SCORE_CLAW ) && ( !gamepad1.cross )) {
                    scorClaw.currentMode = ScorClaw.Mode.RELAX;
                }
                // lift.pos = upToChamber2;

                if (gamepad2.square) {

                    state = State.LOOK_FOR_BLOCK;
                }
                if (gamepad2.triangle || gamepad1.square) {
                    state = State.INITIAL;
                }
                if (gamepad2.circle) {
                    state = State.TRANSFER_SPEC;
                }

                break;
            case LOOK_FOR_BLOCK:
                startGoFromScorSpec = false;
                perkid.currentMode = Perkid.Mode.LOOK;
                //diff.currentMode = Differential.Mode.VISION_HUYNYA;
                diff.currentMode = Differential.Mode.STRAIGHT_DOWN_DISCRETE;
                scorPerkid.currentMode = ScoringPerkid.Mode.TRANSFER;
                scorClaw.currentMode = ScorClaw.Mode.DROP;
                scorDiff.currentMode = ScoringDiff.Mode.GET_FROM_CLAW;
                hand.currentMode = Hand.Mode.DRIVER_CONTROL;
                claw.currentMode = Claw.Mode.DROP;
                // lift.pos = 0;
                if (gamepad2.triangle) {
                    state = State.INITIAL;
                }
                if (gamepad2.cross || gamepad1.square) {

                    state = State.PICK_UP_THE_BLOCK;
                    pickedUnpickedTimer.reset();
                }

                break;
            case PICK_UP_THE_BLOCK:

                perkid.currentMode = Perkid.Mode.PICK_UP;
                diff.currentMode = Differential.Mode.PICK_UP;
                scorDiff.currentMode = ScoringDiff.Mode.GET_FROM_CLAW;

                if (pickedUnpickedTimer.milliseconds() > 400) {
                    claw.currentMode = Claw.Mode.PICK;
                    perkid.currentMode = Perkid.Mode.TRANSFER_FROM_SUBMERSABLE;
                    if (gamepad2.right_stick_y != 0 || gamepad1.cross || gamepad1.triangle && !startForgetPosTimer) {
                        startForgetPosTimer = true;
                    }
                    if (!startForgetPosTimer) {
                        forgetPosTimer.reset();
                    }
                    if (forgetPosTimer.milliseconds() >= SET_POS_TO_ZERO_SAMPLE) {
                        diff.rotation = 0;
                    }
                }

                if (gamepad2.right_bumper || gamepad1.circle) {
                    state = State.LOOK_FOR_BLOCK;
                }
                if (gamepad2.cross) {
                    startFromClawToClaw = true;
                }
                if (startFromClawToClaw) {
                    claw.currentMode = Claw.Mode.PICK;
                    perkid.currentMode = Perkid.Mode.LOOK;
                    if (Math.abs(gamepad1.left_stick_y) >= 0.1 && !startTimerFromClawToClaw) {
                        fromClawToClawTimer.reset();
                        startTimerFromClawToClaw = true;

                    }
                }
                if (startTimerFromClawToClaw && fromClawToClawTimer.milliseconds() >= 300) {
                    state = State.FROM_CLAW_TO_CLAW;
                }
                //   }

             /*   if (pickedUnpickedTimer.milliseconds() > 800 && distanceSensor.getDistance(DistanceUnit.MM) > DIFFERENTIAL_CLAW_THRESHOLD) {
                    state = State.LOOK_FOR_BLOCK;
                } else if (pickedUnpickedTimer.milliseconds() > 800 && distanceSensor.getDistance(DistanceUnit.MM) <= DIFFERENTIAL_CLAW_THRESHOLD) {
                    claw.currentMode = Claw.Mode.PICK;
                    perkid.currentMode = Perkid.Mode.LOOK;
                    if (Math.abs(gamepad1.left_stick_y) >= 0.1 && !startTimerFromClawToClaw) {
                        fromClawToClawTimer.reset();
                        startTimerFromClawToClaw = true;
                    }
                }
                if (startTimerFromClawToClaw && fromClawToClawTimer.milliseconds() >= 300) {
                    state = State.FROM_CLAW_TO_CLAW;
                }*/
              /*  if(fromClawToClawTimer.milliseconds() <= 500){
                    claw.currentMode = Claw.Mode.PICK;
                    perkid.currentMode = Perkid.Mode.LOOK;
                }
                   /* if (Math.abs(gamepad1.left_stick_y) >= 0.1 && startTimerFromClawToClaw == false) {

                        fromClawToClawTimer.reset();
                        startTimerFromClawToClaw = true;
                    }

                    if (startTimerFromClawToClaw == true) {

                        if (fromClawToClawTimer.milliseconds() > 900 && fromClawToClawTimer.milliseconds() <= 1400) {



                        } else if (fromClawToClawTimer.milliseconds() > 1400) {
                            extend.pos = 0;
                            perkid.currentMode = Perkid.Mode.TRANSFER;

                            state = State.FROM_CLAW_TO_CLAW;
                        }
                    }
                    if (gamepad2.triangle) {
                        state = State.INITIAL;
                    }
                }*/
                break;
            case FROM_CLAW_TO_CLAW:
                startFromClawToClaw = false;
                startTimerFromClawToClaw = false;
                if (fromClawToClawTimer.milliseconds() <= 800) {
                    scorClaw.currentMode = ScorClaw.Mode.DROP;
                    claw.currentMode = Claw.Mode.LIGHT;
                    hand.currentMode = Hand.Mode.FORWARD;
                    scorDiff.currentMode = ScoringDiff.Mode.GET_FROM_CLAW;
                    scorPerkid.currentMode = ScoringPerkid.Mode.TRANSFER;
                    diff.currentMode = Differential.Mode.TRANSFER;
                    perkid.currentMode = Perkid.Mode.TRANSFER;
                    extend.currentMode = Extend.Mode.PURE_PID;
                } else if (fromClawToClawTimer.milliseconds() > 800 && fromClawToClawTimer.milliseconds() <= 1000) {
                    claw.currentMode = Claw.Mode.PICK;
                  //  extend.setTransfer();
                }
                else if(fromClawToClawTimer.milliseconds() > 1000 && fromClawToClawTimer.milliseconds() <= 1300){
                    extend.setTransfer();
                }

                    else if (fromClawToClawTimer.milliseconds() > 1300 && fromClawToClawTimer.milliseconds() <= 2000) {
                    extend.setTransfer();
                } else {
                    scorClaw.currentMode = ScorClaw.Mode.PICK;
                    claw.currentMode = Claw.Mode.DROP;
                    extend.setTransfer();
                    if (gamepad2.left_stick_y != 0) {
                        extend.setReference(extend.motor.getCurrentPosition());
                    }
                }
                if (gamepad2.square) {
                    state = State.SCORE_SAMPLE;
                    extend.currentMode = Extend.Mode.DRIVER_CONTROL;
                }
                if (gamepad2.circle) {
                    dropSpecimenTimer.reset();
                    state = State.DROP_SPECIMEN_TO_HUMAN_ZONE;
                    extend.currentMode = Extend.Mode.DRIVER_CONTROL;
                }
                if (gamepad2.triangle) {
                    state = State.INITIAL;
                    extend.currentMode = Extend.Mode.DRIVER_CONTROL;
                }
                break;
            case DROP_SPECIMEN_TO_HUMAN_ZONE:
                extend.currentMode = Extend.Mode.DRIVER_CONTROL;
                perkid.currentMode = Perkid.Mode.TRANSFER;
                claw.currentMode = Claw.Mode.TRANSFER;
                scorPerkid.currentMode = ScoringPerkid.Mode.PICK_SPEC;
                scorDiff.currentMode = ScoringDiff.Mode.PICK_SPEC;
                hand.currentMode = Hand.Mode.FORWARD;
                diff.currentMode = Differential.Mode.TRANSFER;
                //    lift.currentMode = LiftError.Mode.NULL_POSE;

                if (dropSpecimenTimer.milliseconds() <= 400) {
                    scorClaw.currentMode = ScorClaw.Mode.PICK;
                } else {
                    scorClaw.currentMode = ScorClaw.Mode.DROP;
                }
                if (gamepad2.triangle) {
                    state = State.INITIAL;
                }
                if (gamepad2.square) {
                    pickedUnpickedTimer.reset();
                    sampleIsScored = false;
                    startTimer = false;
                    startTimerFromClawToClaw = false;
                    state = State.LOOK_FOR_BLOCK;
                }
               /* claw.currentMode = Claw.Mode.PICK;

                scorPerkid.currentMode = ScoringPerkid.Mode.PICK_SPEC;

                if(dropSpecimenTimer.milliseconds() > 400 && dropSpecimenTimer.milliseconds() <= 1200){
                    scorClaw.currentMode = ScorClaw.Mode.DROP;
                    diff.currentMode = Differential.Mode.STRAIGHT_DOWN;
                    perkid.currentMode = Perkid.Mode.LOOK;
                    hand.currentMode = Hand.Mode.FORWARD;

                }
                else if(dropSpecimenTimer.milliseconds() > 1200) {
                    claw.currentMode = Claw.Mode.DROP;
                    state = State.INITIAL;
                }*/

                break;
            case SCORE_SAMPLE:
                scorPerkid.currentMode = ScoringPerkid.Mode.SCORE_SAMP;
                scorDiff.currentMode = ScoringDiff.Mode.SCORE_SAMP;
                // lift.pos = 1330;
                if (gamepad2.right_bumper) {
                    sampleIsScored = true;
                }
                if (gamepad2.triangle) {
                    state = State.INITIAL;
                }
                if (gamepad2.circle) {
                    pickedUnpickedTimer.reset();
                    sampleIsScored = false;
                    startTimer = false;
                    startTimerFromClawToClaw = false;
                    state = State.LOOK_FOR_BLOCK;
                }
                if (sampleIsScored) {
                    scorClaw.currentMode = ScorClaw.Mode.DROP;
                    diff.rotation = 0;
                    if (Math.abs(gamepad1.left_stick_y) >= 0.4) {
                        goToInitialFromScoreSample.reset();
                        startTimer = true;
                    }

                    if (goToInitialFromScoreSample.milliseconds() >= 550 && startTimer) {
                        //  lift.pos = 0;
                        pickedUnpickedTimer.reset();
                        sampleIsScored = false;
                        startTimer = false;
                        startTimerFromClawToClaw = false;
                        state = State.LOOK_FOR_BLOCK;
                    }
                }
                break;
            case EMPTY:
                break;

        }
        linearOpMode.telemetry.addData("distance", distInClaw.getDistance(DistanceUnit.MM));
        linearOpMode.telemetry.addData("state", state);
        linearOpMode.telemetry.addData("lift", lift.leftMotor.getCurrentPosition());
        linearOpMode.telemetry.addData("lift", lift.pos);
        linearOpMode.telemetry.addData("extend", extend.motor.getCurrentPosition());
        linearOpMode.telemetry.addData("StartTimer", startTimer);
        linearOpMode.telemetry.addData("hang", hang.hang.getCurrentPosition());
        linearOpMode.telemetry.update();
    }
}
