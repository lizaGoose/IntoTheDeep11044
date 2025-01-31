package org.firstinspires.ftc.teamcode.Modules;

import static org.firstinspires.ftc.teamcode.Modules.SensorsConstants.EXTEND_AMPS;
import static org.firstinspires.ftc.teamcode.Modules.TimerConstants.OFFSET_CLAW_CLOSE;
import static org.firstinspires.ftc.teamcode.Modules.TimerConstants.PICKED_UNPICKED_PICK_BLOCK;
import static org.firstinspires.ftc.teamcode.Modules.TimerConstants.REGISTER_MOVEMENT_AFTER;
import static org.firstinspires.ftc.teamcode.Modules.TimerConstants.SET_POS_TO_ZERO_SAMPLE;
import static org.firstinspires.ftc.teamcode.Modules.TimerConstants.WAIT_UNTIL_SPECIMEN_PICKUP;
import static org.firstinspires.ftc.teamcode.Modules.TimerConstants.RELAX_SCORE_CLAW;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Camera.SampleDetectionCameraClass;

@Config
class SensorsConstants {
    public static double DIFFERENTIAL_CLAW_THRESHOLD = 10.2;
    public static double EXTEND_AMPS = 8;
}

@Config
class TimerConstants {
    public static double RELAX_SCORE_CLAW = 2000;
    public static double WAIT_UNTIL_SPECIMEN_PICKUP = 100;
    public static double REGISTER_MOVEMENT_AFTER = 450;
    public static double SET_POS_TO_ZERO_SAMPLE = 300;
    public static double PICKED_UNPICKED_PICK_BLOCK = 450;

    public static double OFFSET_CLAW_CLOSE = 0;
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

    ElapsedTime pickedUnpickedTimer, fromClawToClawTimer, dropSpecimenTimer, goToInitialFromScoreSample, humanDropPickSpecTimer, goFromScoreSpecTimer, fromTransferSpecimenToScore, forgetPosTimer, rightSideDropSampleTimer;
    private final ElapsedTime specimenPickedUp, specimenToTransfer;
    public DistanceSensor distanceSensor, distInClaw;
    LiftError lift;
    // TODO: too many booleans
    boolean sampleIsScored, startTimer, startTimerFromClawToClaw, startGoFromScorSpec, startFromClawToClaw, startForgetPosTimer, stopForgetPosTimer, startHumanDropPickSpec;
    boolean startPickUpBlock;
    public ColorSensor color;

    boolean startedMovingExtendBack = true, stoppedMovingExtendBack = true;

    DriveTrain dt;
    public DigitalChannel frontSensor;
    public static double upToChamber = -380, upToChamber2 = -430;
    public double zeroPos = 0;
    public FtcDashboard dashboard;
    //SampleDetectionCameraClass cam;


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
        //udalit esli cho pisal Ya
        PICK_UP_THE_BLOCK_SCORSPEC,
        LOOK_FOR_BLOCK_SCORSPEC,
        EMPTY,
        RIGHT_SIDE_DROP_SAMPLE,
        RIGT_SIDE_DROP_SAMPLE_PLUS_INIT_FUNCTIONS
    }

    public State state = State.INITIAL;

    public StateMachine(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;

        //cam = new SampleDetectionCameraClass(linearOpMode);
        extend = new Extend(linearOpMode);
        claw = new Claw(linearOpMode);
        perkid = new Perkid(linearOpMode);
        scorPerkid = new ScoringPerkid(linearOpMode);
        scorDiff = new ScoringDiff(linearOpMode);
        hand = new Hand(linearOpMode);
        scorClaw = new ScorClaw(linearOpMode);
        diff = new Differential(linearOpMode);
        lift = new LiftError(linearOpMode);
        hang = new Hang(linearOpMode);
        dt = new DriveTrain(linearOpMode);
        //cam = new SampleDetectionCameraClass(linearOpMode);

        gamepad1 = linearOpMode.gamepad1;
        gamepad2 = linearOpMode.gamepad2;

        pickedUnpickedTimer = new ElapsedTime();
        fromClawToClawTimer = new ElapsedTime();
        dropSpecimenTimer = new ElapsedTime();
        goToInitialFromScoreSample = new ElapsedTime();
        rightSideDropSampleTimer = new ElapsedTime();
        humanDropPickSpecTimer = new ElapsedTime();

        fromTransferSpecimenToScore = new ElapsedTime();
        specimenPickedUp = new ElapsedTime();
        specimenToTransfer = new ElapsedTime();
        forgetPosTimer = new ElapsedTime();

        dashboard = FtcDashboard.getInstance();
    }

    public void teleop() {
        distanceSensor = linearOpMode.hardwareMap.get(DistanceSensor.class, "Color");
        distInClaw = linearOpMode.hardwareMap.get(DistanceSensor.class, "ScorClawDist");
        frontSensor = linearOpMode.hardwareMap.get(DigitalChannel.class, "DistanceFront");
        //cam.visionInUse();
        extend.teleop();
        claw.teleop();
        perkid.teleop();
        scorPerkid.teleop();
        scorDiff.teleop();
        hand.teleop();
        scorClaw.teleop();
        lift.teleop();
        diff.teleop(0);
        dt.teleOp(extend.getPosition() / extend.getMaxPosition());
        hang.control();

        lift.currentMode = LiftError.Mode.DEFAULT;
        if (gamepad2.left_bumper || gamepad1.right_stick_button) {
            extend.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            extend.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            if (extend.getAmps() < 5) {
                extend.setPower(-1);
            } else {
                extend.setPower(0);
            }
        }
        if (state != State.PICK_UP_THE_BLOCK) {
            startForgetPosTimer = false;
            stopForgetPosTimer = false;
        }
        if (hang.hang.getCurrentPosition() >= 500) {
            scorPerkid.currentMode = ScoringPerkid.Mode.TRANSFER;
            scorDiff.currentMode = ScoringDiff.Mode.GET_FROM_CLAW;
            extend.setHang();
            state = State.EMPTY;
        }

        if (gamepad1.left_bumper) {
            dt.currentMode = DriveTrain.Mode.CONTROL_ANGLE;
        } else {
            dt.currentMode = DriveTrain.Mode.DRIVER_CONTROL;
        }
      /*  if(gamepad2.left_bumper && extend.currentMode == Extend.Mode.DRIVER_CONTROL){
            extend.setPower(-1);
        }*/
        // lift.currentMode = LiftError.Mode.NULL_POSE;

        switch (state) {
            case INITIAL:
                if (rightSideDropSampleTimer.milliseconds() > 850) {
                    sampleIsScored = false;
                    startTimer = false;
                    startTimerFromClawToClaw = false;
                    startGoFromScorSpec = false;
                    startHumanDropPickSpec = false;
                    extend.currentMode = Extend.Mode.DRIVER_CONTROL;


                    perkid.currentMode = Perkid.Mode.TRANSFER;
                    claw.currentMode = Claw.Mode.PICK;
                    scorPerkid.currentMode = ScoringPerkid.Mode.PICK_SPEC;
                    scorDiff.currentMode = ScoringDiff.Mode.PICK_SPEC;
                    hand.currentMode = Hand.Mode.FORWARD;
                    scorClaw.currentMode = ScorClaw.Mode.DROP;
                    diff.currentMode = Differential.Mode.TRANSFER;

                    //lift.pos = 0;


                    if (gamepad2.square || gamepad1.square) {
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
                        scorDiff.currentMode = ScoringDiff.Mode.AIM_POS;
                        state = State.PICK_SPEC;
                    }
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
                if (gamepad2.triangle || gamepad1.triangle) {
                    state = State.INITIAL;
                }
                break;

            case TRANSFER_SPEC:
                // lift.pos = upToChamber;
                claw.currentMode = Claw.Mode.DROP;
                scorPerkid.currentMode = ScoringPerkid.Mode.TRANSFER_SPEC;
                scorClaw.currentMode = ScorClaw.Mode.PICK;
                perkid.currentMode = Perkid.Mode.TRANSFER_FROM_SUBMERSABLE;
                diff.currentMode = Differential.Mode.STRAIGHT_DOWN;

                if (fromTransferSpecimenToScore.milliseconds() > 500) {
                    scorDiff.currentMode = ScoringDiff.Mode.TRANSFER_SPEC;
                } else {
                    scorDiff.currentMode = ScoringDiff.Mode.PICK_SPEC;
                }

                hand.currentMode = Hand.Mode.DRIVER_CONTROL;
                if (!gamepad2.circle || !gamepad1.options) {
                    if (!frontSensor.getState()) {
                        state = State.SCORE_SPEC;
                        fromTransferSpecimenToScore.reset();
                    }
                }
                if (gamepad2.triangle || gamepad1.triangle) {
                    state = State.INITIAL;
                }
                break;

            case SCORE_SPEC:
                scorPerkid.currentMode = ScoringPerkid.Mode.SCOR_SPEC;
                if (gamepad1.cross) {
                    scorDiff.currentMode = ScoringDiff.Mode.MOVE_SPECIMENS;
                    fromTransferSpecimenToScore.reset();
                } else {
                    scorDiff.currentMode = ScoringDiff.Mode.SCORE_SPEC;
                }
                hand.currentMode = Hand.Mode.DRIVER_CONTROL;
                //zalupa
                startGoFromScorSpec = false;
                perkid.currentMode = Perkid.Mode.LOOK;
                //diff.currentMode = Differential.Mode.VISION_HUYNYA;
                diff.currentMode = Differential.Mode.STRAIGHT_DOWN_DISCRETE;
                claw.currentMode = Claw.Mode.DROP;
                // lift.pos = 0;
                //zalupa

                if (gamepad2.right_bumper || gamepad1.circle) {
                    scorClaw.currentMode = ScorClaw.Mode.DROP;
                } else if (( fromTransferSpecimenToScore.milliseconds() > RELAX_SCORE_CLAW ) && ( !gamepad1.cross )) {
                    scorClaw.currentMode = ScorClaw.Mode.RELAX;
                }
                // lift.pos = upToChamber2;

                if (gamepad2.square) {

                    state = State.LOOK_FOR_BLOCK;
                }
                if (gamepad2.triangle || gamepad1.triangle) {
                    state = State.INITIAL;
                }
                if (gamepad2.circle || gamepad1.options) {
                    state = State.TRANSFER_SPEC;
                }

                if (gamepad1.square) {

                    state = State.PICK_UP_THE_BLOCK_SCORSPEC;
                    pickedUnpickedTimer.reset();
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
                if (gamepad2.triangle || gamepad1.triangle) {
                    state = State.INITIAL;
                }
                if (gamepad2.cross || gamepad1.square) {

                    state = State.PICK_UP_THE_BLOCK;
                    pickedUnpickedTimer.reset();
                }

                break;

            case LOOK_FOR_BLOCK_SCORSPEC:
                startGoFromScorSpec = false;
                perkid.currentMode = Perkid.Mode.LOOK;
                //diff.currentMode = Differential.Mode.VISION_HUYNYA;
                diff.currentMode = Differential.Mode.STRAIGHT_DOWN_DISCRETE;
                scorClaw.currentMode = ScorClaw.Mode.DROP;
                hand.currentMode = Hand.Mode.DRIVER_CONTROL;
                claw.currentMode = Claw.Mode.DROP;
                // lift.pos = 0;
                if (gamepad2.triangle || gamepad1.triangle) {
                    state = State.INITIAL;
                }
                if (gamepad2.cross || gamepad1.square) {

                    state = State.PICK_UP_THE_BLOCK_SCORSPEC;
                    pickedUnpickedTimer.reset();
                }

                break;

            case PICK_UP_THE_BLOCK_SCORSPEC:
                perkid.currentMode = Perkid.Mode.PICK_UP;
                diff.currentMode = Differential.Mode.PICK_UP;


                if (pickedUnpickedTimer.milliseconds() > 400) {
                    claw.currentMode = Claw.Mode.PICK;
                    if (pickedUnpickedTimer.milliseconds() >= PICKED_UNPICKED_PICK_BLOCK) {
                        perkid.currentMode = Perkid.Mode.TRANSFER_FROM_SUBMERSABLE;
                        if (gamepad2.right_stick_y != 0 && !startForgetPosTimer) {
                            startForgetPosTimer = true;
                        }
                        if (!startForgetPosTimer) {
                            forgetPosTimer.reset();
                        }
                        if (forgetPosTimer.milliseconds() >= SET_POS_TO_ZERO_SAMPLE) {
                            diff.rotation = 0;
                        }
                    }
                }

                if (gamepad2.right_bumper || gamepad1.circle) {
                    state = State.LOOK_FOR_BLOCK_SCORSPEC;
                }
                if (gamepad2.cross) {
                    startFromClawToClaw = true;
                }
                if (gamepad1.triangle) {
                    scorPerkid.currentMode = ScoringPerkid.Mode.PICK_SPEC;
                    scorDiff.currentMode = ScoringDiff.Mode.PICK_SPEC;
                    if (distInClaw.getDistance(DistanceUnit.MM) >= 17) {
                        specimenPickedUp.reset();
                    }
                    if (specimenPickedUp.milliseconds() > WAIT_UNTIL_SPECIMEN_PICKUP) {
                        state = State.PICK_SPEC;
                    }
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
                if (gamepad2.square) {
                    state = State.RIGHT_SIDE_DROP_SAMPLE;
                }
                break;

            case RIGHT_SIDE_DROP_SAMPLE:
                if (frontSensor.getState()) {
                    if (Math.abs(gamepad1.left_stick_y) >= 0.1 && !startHumanDropPickSpec) {
                        startHumanDropPickSpec = true;
                        humanDropPickSpecTimer.reset();
                    }
                }
                if (humanDropPickSpecTimer.milliseconds() >= 800 && humanDropPickSpecTimer.milliseconds() <= 1300) {
                    state = State.RIGT_SIDE_DROP_SAMPLE_PLUS_INIT_FUNCTIONS;
                }
                if (gamepad1.circle) {
                    rightSideDropSampleTimer.reset();
                }

                if (rightSideDropSampleTimer.milliseconds() >= 2000) {
                    perkid.currentMode = Perkid.Mode.RIGHT_SIDE_DROP_SAMPLE;
                    hand.currentMode = Hand.Mode.RIGHT_SIDE_DROP_SAMPLE;
                    diff.currentMode = Differential.Mode.RIGHT_SIDE_DROP_SAMPLE;
                    claw.currentMode = Claw.Mode.PICK;
                }

                if (rightSideDropSampleTimer.milliseconds() > 0 && rightSideDropSampleTimer.milliseconds() <= 700) {
                    claw.currentMode = Claw.Mode.DROP;
                }

                break;
            case RIGT_SIDE_DROP_SAMPLE_PLUS_INIT_FUNCTIONS:
                startHumanDropPickSpec = false;

                if (rightSideDropSampleTimer.milliseconds() > 0 && rightSideDropSampleTimer.milliseconds() <= 700) {
                    claw.currentMode = Claw.Mode.DROP;
                } else if (rightSideDropSampleTimer.milliseconds() >= 900 && rightSideDropSampleTimer.milliseconds() <= 1200) {
                    perkid.currentMode = Perkid.Mode.LOOK;
                    //diff.currentMode = Differential.Mode.VISION_HUYNYA;
                    diff.currentMode = Differential.Mode.STRAIGHT_DOWN_DISCRETE;
                    // scorClaw.currentMode = ScorClaw.Mode.DROP;
                    hand.currentMode = Hand.Mode.DRIVER_CONTROL;
                    // claw.currentMode = Claw.Mode.DROP;
                    // lift.pos = 0;
                    if (gamepad2.triangle || gamepad1.triangle) {
                        state = State.INITIAL;
                    }
                    if (gamepad2.cross || gamepad1.square) {

                        state = State.PICK_UP_THE_BLOCK_SCORSPEC;
                        pickedUnpickedTimer.reset();
                    }
                }
                scorPerkid.currentMode = ScoringPerkid.Mode.PICK_SPEC;
                scorDiff.currentMode = ScoringDiff.Mode.PICK_SPEC;
                scorClaw.currentMode = ScorClaw.Mode.DROP;

                if (gamepad1.circle) {
                    rightSideDropSampleTimer.reset();
                }

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
            case PICK_UP_THE_BLOCK:

                perkid.currentMode = Perkid.Mode.PICK_UP;
                diff.currentMode = Differential.Mode.PICK_UP;
                scorDiff.currentMode = ScoringDiff.Mode.GET_FROM_CLAW;


                if (pickedUnpickedTimer.milliseconds() > 400) {
                    claw.currentMode = Claw.Mode.PICK;
                    if (pickedUnpickedTimer.milliseconds() >= PICKED_UNPICKED_PICK_BLOCK) {
                        perkid.currentMode = Perkid.Mode.TRANSFER_FROM_SUBMERSABLE;
                        if (gamepad2.right_stick_y != 0 || gamepad1.cross && !startForgetPosTimer) {
                            startForgetPosTimer = true;
                        }
                        if (!startForgetPosTimer) {
                            forgetPosTimer.reset();
                        }
                        if (forgetPosTimer.milliseconds() >= SET_POS_TO_ZERO_SAMPLE) {
                            diff.rotation = 0;
                        }
                    }
                }

                if (gamepad2.right_bumper || gamepad1.circle) {
                    state = State.LOOK_FOR_BLOCK;
                }
                if (gamepad2.cross || gamepad1.touchpad) {
                    startFromClawToClaw = true;
                }
                if (startFromClawToClaw) {
                    claw.currentMode = Claw.Mode.PICK;
                    startTimerFromClawToClaw = true;
                }
                if (!startTimerFromClawToClaw) {
                    fromClawToClawTimer.reset();
                }
                if (startTimerFromClawToClaw && fromClawToClawTimer.milliseconds() >= 300) {
                    state = State.FROM_CLAW_TO_CLAW;
                    startedMovingExtendBack = false;
                }
                break;
            case FROM_CLAW_TO_CLAW:
                startFromClawToClaw = false;
                startTimerFromClawToClaw = false;
                if (fromClawToClawTimer.milliseconds() <= 800 && !startedMovingExtendBack) {
                    scorClaw.currentMode = ScorClaw.Mode.DROP;
                    claw.currentMode = Claw.Mode.LIGHT;
                    hand.currentMode = Hand.Mode.FORWARD;
                    scorDiff.currentMode = ScoringDiff.Mode.GET_FROM_CLAW;
                    scorPerkid.currentMode = ScoringPerkid.Mode.PICK_SPEC;
                    diff.currentMode = Differential.Mode.TRANSFER;
                    perkid.currentMode = Perkid.Mode.TRANSFER;
                } else if (fromClawToClawTimer.milliseconds() > 800 && fromClawToClawTimer.milliseconds() <= 1300 && !startedMovingExtendBack) {
                    claw.currentMode = Claw.Mode.PICK;
                    //extend.setTransfer();
                } else if (fromClawToClawTimer.milliseconds() > 1300 && fromClawToClawTimer.milliseconds() <= 1700 && !startedMovingExtendBack) {
                    extend.currentMode = Extend.Mode.POWER;
                    extend.setPower(-1);
                    fromClawToClawTimer.reset();
                    startedMovingExtendBack = true;
                }
                if (startedMovingExtendBack && extend.getAmps() < EXTEND_AMPS && !stoppedMovingExtendBack) {
                    fromClawToClawTimer.reset();
                } else {
                    extend.setReference(extend.getPosition());
                    stoppedMovingExtendBack = true;
                }
                if (fromClawToClawTimer.milliseconds() > 0 && fromClawToClawTimer.milliseconds() <= 600 + OFFSET_CLAW_CLOSE
                        && startedMovingExtendBack
                        && stoppedMovingExtendBack) {
                    scorPerkid.currentMode = ScoringPerkid.Mode.TRANSFER;
                } else if (fromClawToClawTimer.milliseconds() > 600 + OFFSET_CLAW_CLOSE && fromClawToClawTimer.milliseconds() <= 850 + OFFSET_CLAW_CLOSE
                        && startedMovingExtendBack
                        && stoppedMovingExtendBack) {
                    scorClaw.currentMode = ScorClaw.Mode.PICK;
                    extend.currentMode = Extend.Mode.PURE_PID;
                    extend.resetEncoder();
                    extend.setZero();
                } else if (startedMovingExtendBack
                        && stoppedMovingExtendBack) {
                    claw.currentMode = Claw.Mode.DROP;
                    if (gamepad2.left_stick_y != 0) {
                        extend.setReference(extend.motor.getCurrentPosition());
                    }
                }
                if (fromClawToClawTimer.milliseconds() > 1050 + OFFSET_CLAW_CLOSE
                        && startedMovingExtendBack
                        && extend.currentMode == Extend.Mode.PURE_PID) {
                    startPickUpBlock = false;
                    state = State.SCORE_SAMPLE;
                    extend.currentMode = Extend.Mode.DRIVER_CONTROL;
                }
                if (gamepad2.circle || gamepad1.options) {
                    dropSpecimenTimer.reset();
                    state = State.DROP_SPECIMEN_TO_HUMAN_ZONE;
                    extend.currentMode = Extend.Mode.DRIVER_CONTROL;
                }
                if (gamepad2.triangle || gamepad1.triangle) {
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
                if (gamepad2.triangle || gamepad1.triangle) {
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
                if (gamepad2.right_bumper || gamepad1.circle) {
                    sampleIsScored = true;
                }
                if (gamepad2.triangle || gamepad1.triangle) {
                    state = State.INITIAL;
                }
                if (sampleIsScored && Math.abs(gamepad1.left_stick_y) >= 0.1 && !startPickUpBlock) {
                    specimenToTransfer.reset();
                    startPickUpBlock = true;
                }
                if (startPickUpBlock && sampleIsScored && specimenToTransfer.milliseconds() > 200 || gamepad1.square || gamepad2.circle) {
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
        linearOpMode.telemetry.addData("Heading", dt.realHeading);
        linearOpMode.telemetry.addData("distance", distInClaw.getDistance(DistanceUnit.MM));
        linearOpMode.telemetry.addData("state", state);
        linearOpMode.telemetry.addData("perkid_state", perkid.currentMode);
        linearOpMode.telemetry.addData("diff_state", diff.currentMode);
        linearOpMode.telemetry.addData("hand_state", hand.currentMode);
        linearOpMode.telemetry.addData("scorDiff_state", scorDiff.currentMode);
        linearOpMode.telemetry.addData("scorPerkid_state", scorPerkid.currentMode);
        linearOpMode.telemetry.addData("transfer_timer", fromClawToClawTimer);
        linearOpMode.telemetry.addData("lift_left", lift.leftMotor.getCurrentPosition());
        linearOpMode.telemetry.addData("lift_right", lift.rightMotor.getCurrentPosition());
        linearOpMode.telemetry.addData("lift_ref", lift.getReference());
        linearOpMode.telemetry.addData("extend", extend.motor.getCurrentPosition());
        linearOpMode.telemetry.addData("StartTimer", startTimer);
        linearOpMode.telemetry.addData("hang", hang.hang.getCurrentPosition());
        linearOpMode.telemetry.addData("extend_voltage", extend.motor.getCurrent(CurrentUnit.AMPS));
        linearOpMode.telemetry.update();

        TelemetryPacket packet = new TelemetryPacket();
        packet.put("left_motor_pos", lift.leftMotor.getCurrentPosition());
        packet.put("right_motor_pos", lift.rightMotor.getCurrentPosition());
        packet.put("reference", lift.getReference());
        dashboard.sendTelemetryPacket(packet);
    }
}
