package org.firstinspires.ftc.teamcode.modules;

import static org.firstinspires.ftc.teamcode.modules.SensorsConstants.EXTEND_AMPS;
import static org.firstinspires.ftc.teamcode.modules.StateMachine.TeleOpAutomaticScoreConstants.AMOUNT_TO_SCORE;
import static org.firstinspires.ftc.teamcode.modules.StateMachine.TeleOpAutomaticScoreConstants.DELTA;
import static org.firstinspires.ftc.teamcode.modules.StateMachine.TeleOpAutomaticScoreConstants.POINT_MOVE_PERKID_TO_PICK;
import static org.firstinspires.ftc.teamcode.modules.StateMachine.TeleOpAutomaticScoreConstants.POINT_PERKID_START;
import static org.firstinspires.ftc.teamcode.modules.StateMachine.TeleOpAutomaticScoreConstants.POINT_SPECIMEN_START_SCORE;
import static org.firstinspires.ftc.teamcode.modules.StateMachine.TeleOpAutomaticScoreConstants.POINT_TO_INITIAL;
import static org.firstinspires.ftc.teamcode.modules.StateMachine.TeleOpAutomaticScoreConstants.SLOWDOWN_FORCE;
import static org.firstinspires.ftc.teamcode.modules.StateMachine.TeleOpAutomaticScoreConstants.SLOWDOWN_POINT_1;
import static org.firstinspires.ftc.teamcode.modules.StateMachine.TeleOpAutomaticScoreConstants.SLOWDOWN_POINT_2;
import static org.firstinspires.ftc.teamcode.modules.StateMachine.TeleOpAutomaticScoreConstants.WAIT_AT_RUNG;
import static org.firstinspires.ftc.teamcode.modules.StateMachine.TeleOpAutomaticScoreConstants.WAIT_END;
import static org.firstinspires.ftc.teamcode.modules.TeleOpConstants.DISTANCE;
import static org.firstinspires.ftc.teamcode.modules.TeleOpConstants.OFFSET_CLAW_CLOSE;
import static org.firstinspires.ftc.teamcode.modules.TeleOpConstants.PICKED_UNPICKED_PICK_BLOCK;
import static org.firstinspires.ftc.teamcode.modules.TeleOpConstants.POSE_X;
import static org.firstinspires.ftc.teamcode.modules.TeleOpConstants.POSE_Y;
import static org.firstinspires.ftc.teamcode.modules.TeleOpConstants.REGISTER_MOVEMENT_AFTER;
import static org.firstinspires.ftc.teamcode.modules.TeleOpConstants.SET_POS_TO_ZERO_SAMPLE;
import static org.firstinspires.ftc.teamcode.modules.TeleOpConstants.WAIT_FOR_INITIAL_STATE;
import static org.firstinspires.ftc.teamcode.modules.TeleOpConstants.WAIT_FOR_LINKAGE;
import static org.firstinspires.ftc.teamcode.modules.TeleOpConstants.WAIT_FOR_PERKID;
import static org.firstinspires.ftc.teamcode.modules.TeleOpConstants.WAIT_SAMPLE_SCORER_FLIP;
import static org.firstinspires.ftc.teamcode.modules.TeleOpConstants.WAIT_START_LINKAGE_RETRACT;
import static org.firstinspires.ftc.teamcode.modules.TeleOpConstants.WAIT_UNTIL_SPECIMEN_PICKUP;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.auto.Trajectory;
import org.firstinspires.ftc.teamcode.auto.TrajectoryBuilder;
import org.firstinspires.ftc.teamcode.camera.AutoCamera;
import org.firstinspires.ftc.teamcode.hardware.StickyKeys;

import java.io.IOException;

@Config
class SensorsConstants {
    public static double EXTEND_AMPS = 8;
}

@Config
class TeleOpConstants {
    public static double WAIT_UNTIL_SPECIMEN_PICKUP = 1;
    public static double REGISTER_MOVEMENT_AFTER = 450;
    public static double SET_POS_TO_ZERO_SAMPLE = 300;
    public static double PICKED_UNPICKED_PICK_BLOCK = 300;

    public static double OFFSET_CLAW_CLOSE = 0;
    public static double WAIT_FOR_PERKID = 200;
    public static double WAIT_FOR_LINKAGE = 100;
    public static double WAIT_FOR_INITIAL_STATE = 500;

    public static double POSE_X = -17.939825444151644;
    public static double POSE_Y = 64.98661527894448;

    public static double WAIT_SAMPLE_SCORER_FLIP = 1000;
    public static double WAIT_START_LINKAGE_RETRACT = 1600;

    public static double DISTANCE = 22;
}

@Config
public class StateMachine {
    LinearOpMode linearOpMode;
    Linkage linkage;
    Claw claw;
    Perkid perkid;
    ScorerPerkid scorerPerkid;
    ScorerDifferential scorerDifferential;
    Hand hand;
    ScorerClaw scorerClaw;
    Gamepad gamepad1, gamepad2;
    Differential diff;
    Hang hang;
    ScorerLinkage scorerLinkage;
    Lift lift;

    private final ElapsedTime pickedUnpickedTimer, fromClawToClawTimer, dropSpecimenTimer, goToInitialFromScoreSample, humanDropPickSpecTimer, fromTransferSpecimenToScore, forgetPosTimer, rightSideDropSampleTimer;
    private final ElapsedTime specimenPickedUp, specimenToTransfer;
    private final ElapsedTime perekidTimer;
    private final ElapsedTime initialTimer;
    public DistanceSensor distanceSensor, distInClaw;
    // TODO: too many booleans
    boolean sampleIsScored, startTimer, startTimerFromClawToClaw, startFromClawToClaw, startForgetPosTimer, stopForgetPosTimer, startHumanDropPickSpec;
    boolean startPickUpBlock;
    public ColorSensor color;

    boolean startedMovingExtendBack = true, stoppedMovingExtendBack = true;

    DriveModule driveModule;
    public DigitalChannel frontSensor;
    public FtcDashboard dashboard;
    private final StickyKeys stickyGamepad1;
//    AutoCamera cam;

    private Trajectory[] fullCycle = new Trajectory[8];
    private int amountAutoscored = 0;

    public enum AutoScoreState {
        SCORING,
        INACTIVE
    }

    public AutoScoreState autoScoreState = AutoScoreState.INACTIVE;

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
        PICK_UP_THE_BLOCK_SCORSPEC,
        LOOK_FOR_BLOCK_SCORSPEC,
        EMPTY,
        RIGHT_SIDE_DROP_SAMPLE,
        RIGHT_SIDE_DROP_SAMPLE_PLUS_INIT_FUNCTIONS,
        SPECIMEN_TO_INITIAL,
        START,
        PICK_UP_THE_BLOCK_WITH_APPENDIX
    }

    public State state = State.START;

    public StateMachine(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;

        linkage = new Linkage(linearOpMode);
        claw = new Claw(linearOpMode);
        perkid = new Perkid(linearOpMode);
        scorerPerkid = new ScorerPerkid(linearOpMode);
        scorerDifferential = new ScorerDifferential(linearOpMode);
        hand = new Hand(linearOpMode);
        scorerClaw = new ScorerClaw(linearOpMode);
        diff = new Differential(linearOpMode);
        lift = new Lift(linearOpMode);
        hang = new Hang(linearOpMode);
        driveModule = new DriveModule(linearOpMode.hardwareMap, new Pose2d(POSE_X, POSE_Y, Math.toRadians(270)));
        driveModule.drive.setForce(1);
//        cam = new AutoCamera(linearOpMode);
        scorerLinkage = new ScorerLinkage(linearOpMode.hardwareMap);

        gamepad1 = linearOpMode.gamepad1;
        gamepad2 = linearOpMode.gamepad2;

        stickyGamepad1 = new StickyKeys();

        TrajectoryBuilder builder;

        try {
            builder = driveModule.drive.buildTrajectory("teleop_full_cycle.json");
            buildOneCycle(builder, 0);
            for (int i = 0; i < 8; ++i) {
                fullCycle[i] = builder
                        .plusPoint(3, new Vector2d(DELTA, 0))
                        .plusPoint(4, new Vector2d(DELTA, 0))
                        .build();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
        perekidTimer = new ElapsedTime();
        initialTimer = new ElapsedTime();

        dashboard = FtcDashboard.getInstance();
    }

    public void teleop() {
//        distanceSensor = linearOpMode.hardwareMap.get(DistanceSensor.class, "Color");
        distInClaw = linearOpMode.hardwareMap.get(DistanceSensor.class, "ScorClawDist");
        frontSensor = linearOpMode.hardwareMap.get(DigitalChannel.class, "DistanceFront");
        linkage.teleop();
        claw.teleop();
        perkid.teleop();
        scorerPerkid.teleop();
        scorerDifferential.teleop();
        hand.teleop();
        scorerClaw.teleop();
        lift.teleop();
        diff.teleop(0);
        driveModule.update(gamepad1, linkage.getPosition() / linkage.getMaxPosition());
        stickyGamepad1.update(gamepad1);
        hang.discreteControl();

        lift.currentMode = Lift.Mode.DEFAULT;
        if (gamepad2.left_bumper || gamepad1.right_stick_button) {
            linkage.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            linkage.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            if (linkage.getAmps() < 5) {
                linkage.setPower(-1);
            } else {
                linkage.setPower(0);
            }
        }
        if (gamepad2.right_stick_button) {
            lift.currentMode = Lift.Mode.PUSH_DOWN;
        } else {
            lift.currentMode = Lift.Mode.DEFAULT;
        }
        if (state != State.PICK_UP_THE_BLOCK) {
            startForgetPosTimer = false;
            stopForgetPosTimer = false;
        }
        if (hang.hang.getCurrentPosition() >= 500) {
            scorerPerkid.currentMode = ScorerPerkid.Mode.TRANSFER;
            scorerDifferential.currentMode = ScorerDifferential.Mode.GET_FROM_CLAW;
            linkage.setHang();
            state = State.EMPTY;
        }

        switch (state) {
            case START:
                initialTimer.reset();
                state = State.INITIAL;
                break;
            case INITIAL:
                if (rightSideDropSampleTimer.milliseconds() > 850) {
                    sampleIsScored = false;
                    startTimer = false;
                    startTimerFromClawToClaw = false;
                    startHumanDropPickSpec = false;
                    linkage.currentMode = Linkage.Mode.DRIVER_CONTROL;

                    if (initialTimer.milliseconds() > WAIT_FOR_INITIAL_STATE) {
                        perkid.currentMode = Perkid.Mode.RELAX;
                        hand.currentMode = Hand.Mode.RELAX;
                    } else {
                        perkid.currentMode = Perkid.Mode.RIGHT_SIDE_DROP_SAMPLE;
                        claw.currentMode = Claw.Mode.PICK;
                        hand.currentMode = Hand.Mode.RIGHT_SIDE_DROP_SAMPLE;
                        diff.currentMode = Differential.Mode.TRANSFER;
                        lift.setReferenceZero();
                    }
                    scorerPerkid.currentMode = ScorerPerkid.Mode.PICK_SPEC;
                    scorerDifferential.currentMode = ScorerDifferential.Mode.PICK_SPEC;
                    scorerClaw.currentMode = ScorerClaw.Mode.DROP;
                    scorerLinkage.retract();

                    if (stickyGamepad1.dpad_up) {
                        Lift.LiftConstants.MIN_POSITION += 10;
                        lift.setReferenceZero();
                    }
                    if (stickyGamepad1.dpad_down) {
                        Lift.LiftConstants.MIN_POSITION -= 10;
                        lift.setReferenceZero();
                    }

                    if (autoScoreState == AutoScoreState.SCORING && driveModule.currentMode == DriveModule.Mode.DRIVER_CONTROL) {
                        amountAutoscored += 1;
                        if (amountAutoscored < AMOUNT_TO_SCORE) {
                            state = State.PICK_SPEC;
                            driveModule.followTrajectory(fullCycle[amountAutoscored]);
                        } else {
                            autoScoreState = AutoScoreState.INACTIVE;
                            amountAutoscored = 0;
                        }
                    }

                    if (stickyGamepad1.cross && driveModule.currentMode == DriveModule.Mode.FOLLOWING_TRAJECTORY) {
                        driveModule.drive.discardTrajectory();
                        amountAutoscored = 0;
                        autoScoreState = AutoScoreState.INACTIVE;
                    }

                    if (gamepad2.square || stickyGamepad1.square) {
                        state = State.LOOK_FOR_BLOCK;
                    }
                    if (gamepad2.cross) {
                        state = State.TRANSFER_SPEC;
                        fromTransferSpecimenToScore.reset();
                    }

                    if (distInClaw.getDistance(DistanceUnit.MM) <= DISTANCE && !gamepad1.triangle) {
                        claw.currentMode = Claw.Mode.DROP;
                        specimenPickedUp.reset();
                        scorerDifferential.currentMode = ScorerDifferential.Mode.AIM_POS;
                        state = State.PICK_SPEC;
                    }
                }
                break;

            case PICK_SPEC:
                scorerClaw.currentMode = ScorerClaw.Mode.PICK;
                scorerDifferential.currentMode = ScorerDifferential.Mode.AIM_POS;
                scorerLinkage.retract();

                if (autoScoreState == AutoScoreState.SCORING && driveModule.currentMode == DriveModule.Mode.DRIVER_CONTROL) {
                    amountAutoscored += 1;
                    if (amountAutoscored < AMOUNT_TO_SCORE) {
                        state = State.PICK_SPEC;
                        driveModule.followTrajectory(fullCycle[amountAutoscored]);
                    } else {
                        autoScoreState = AutoScoreState.INACTIVE;
                        amountAutoscored = 0;
                    }
                }

                if (stickyGamepad1.cross && driveModule.currentMode == DriveModule.Mode.DRIVER_CONTROL
                        && autoScoreState == AutoScoreState.INACTIVE) {
                    driveModule.drive.setPoseEstimate(new Pose2d(fullCycle[0].get(0).point0, Math.toRadians(270)));
                    driveModule.followTrajectory(fullCycle[amountAutoscored]);
                    autoScoreState = AutoScoreState.SCORING;
                }

                if (!( gamepad1.left_stick_y <= -0.1 )) {
                    specimenToTransfer.reset();
                }

                if (specimenToTransfer.milliseconds() > REGISTER_MOVEMENT_AFTER || gamepad2.square) {
                    state = State.TRANSFER_SPEC;
                    fromTransferSpecimenToScore.reset();
                }

                if (( gamepad2.triangle || gamepad1.triangle ) && driveModule.currentMode == DriveModule.Mode.DRIVER_CONTROL) {
                    state = State.INITIAL;
                    initialTimer.reset();
                }
                break;

            case TRANSFER_SPEC:
                scorerPerkid.currentMode = ScorerPerkid.Mode.TRANSFER_SPEC;
                claw.currentMode = Claw.Mode.PICK;
                lift.setSpecimenTele();

                if (fromTransferSpecimenToScore.milliseconds() > WAIT_FOR_LINKAGE) {
                    scorerDifferential.currentMode = ScorerDifferential.Mode.TRANSFER_SPEC;
                    scorerLinkage.extend();
                    scorerClaw.currentMode = ScorerClaw.Mode.PICK;
                } else {
                    scorerDifferential.currentMode = ScorerDifferential.Mode.PICK_SPEC;
                    scorerLinkage.retract();
                    scorerClaw.currentMode = ScorerClaw.Mode.PICK;
                }

                if (stickyGamepad1.cross && driveModule.currentMode == DriveModule.Mode.FOLLOWING_TRAJECTORY) {
                    driveModule.drive.discardTrajectory();
                    amountAutoscored = 0;
                    autoScoreState = AutoScoreState.INACTIVE;
                }

                if (stickyGamepad1.dpad_up) {
                    Lift.LiftConstants.SPECIMEN_POS += 10;
                }
                if (stickyGamepad1.dpad_down) {
                    Lift.LiftConstants.SPECIMEN_POS -= 10;
                }


                if (!gamepad2.circle || !gamepad1.options) {
                    if (!frontSensor.getState()) {
                        state = State.SCORE_SPEC;
                        claw.currentMode = Claw.Mode.RELAX;
                        fromTransferSpecimenToScore.reset();
                    }
                }
                if (gamepad2.triangle || gamepad1.triangle) {
                    perekidTimer.reset();
                    state = State.SPECIMEN_TO_INITIAL;
                }
                break;

            case SCORE_SPEC:
                scorerPerkid.currentMode = ScorerPerkid.Mode.SCOR_SPEC;
                if (scorerDifferential.currentMode != ScorerDifferential.Mode.AFTER_SCORE_SPEC) {
                    scorerDifferential.currentMode = ScorerDifferential.Mode.PICK_SPEC;
                }

                scorerLinkage.extend();
                lift.setSpecimenTele();

                if (gamepad2.right_bumper || gamepad1.circle) {
                    scorerClaw.currentMode = ScorerClaw.Mode.DROP;
                    scorerDifferential.currentMode = ScorerDifferential.Mode.AFTER_SCORE_SPEC;
                } else if (gamepad1.cross) {
                    scorerClaw.currentMode = ScorerClaw.Mode.PICK;
                } else if (driveModule.currentMode == DriveModule.Mode.DRIVER_CONTROL) {
                    scorerClaw.currentMode = ScorerClaw.Mode.RELAX;
                }

                if (stickyGamepad1.dpad_up) {
                    Lift.LiftConstants.SPECIMEN_POS += 10;
                }
                if (stickyGamepad1.dpad_down) {
                    Lift.LiftConstants.SPECIMEN_POS -= 10;
                }

                if (stickyGamepad1.cross && driveModule.currentMode == DriveModule.Mode.FOLLOWING_TRAJECTORY) {
                    driveModule.drive.discardTrajectory();
                    amountAutoscored = 0;
                    autoScoreState = AutoScoreState.INACTIVE;
                }

                if (gamepad2.triangle || gamepad1.triangle) {
                    perekidTimer.reset();
                    state = State.SPECIMEN_TO_INITIAL;
                }
                if (gamepad2.circle || gamepad1.options) {
                    state = State.TRANSFER_SPEC;
                }
                if (gamepad1.square) {
                    diff.currentMode = Differential.Mode.STRAIGHT_DOWN_DISCRETE;
                    state = State.LOOK_FOR_BLOCK_SCORSPEC;
                    hand.setRotation(0.5);
                    pickedUnpickedTimer.reset();
                }
                break;
            case SPECIMEN_TO_INITIAL:
                scorerLinkage.retract();
                scorerPerkid.currentMode = ScorerPerkid.Mode.PICK_SPEC;

                if (perekidTimer.milliseconds() > WAIT_FOR_PERKID) {
                    state = State.INITIAL;
                    initialTimer.reset();
                }
                break;
            case LOOK_FOR_BLOCK:
                perkid.currentMode = Perkid.Mode.LOOK;
                diff.currentMode = Differential.Mode.STRAIGHT_DOWN_DISCRETE;
                scorerPerkid.currentMode = ScorerPerkid.Mode.TRANSFER;
                scorerClaw.currentMode = ScorerClaw.Mode.DROP;
                scorerDifferential.currentMode = ScorerDifferential.Mode.GET_FROM_CLAW;
                hand.currentMode = Hand.Mode.DRIVER_CONTROL;
                claw.currentMode = Claw.Mode.DROP;
                scorerLinkage.retract();

                if (gamepad2.triangle || gamepad1.triangle) {
                    state = State.INITIAL;
                    initialTimer.reset();
                }

                if (gamepad2.cross || stickyGamepad1.square) {
                    state = State.PICK_UP_THE_BLOCK;
                    pickedUnpickedTimer.reset();
                }
                if (gamepad1.touchpad) {

                    state = State.PICK_UP_THE_BLOCK_WITH_APPENDIX;
                    pickedUnpickedTimer.reset();
                }

                break;

            case LOOK_FOR_BLOCK_SCORSPEC:
                perkid.currentMode = Perkid.Mode.LOOK;
                diff.currentMode = Differential.Mode.STRAIGHT_DOWN_DISCRETE;
                scorerClaw.currentMode = ScorerClaw.Mode.DROP;
                hand.currentMode = Hand.Mode.DRIVER_CONTROL;
                claw.currentMode = Claw.Mode.DROP;

                if (gamepad2.triangle || gamepad1.triangle) {
                    state = State.INITIAL;
                    initialTimer.reset();
                }
                if (gamepad2.cross || stickyGamepad1.square) {
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
                if (gamepad2.triangle || gamepad1.triangle) {
                    perekidTimer.reset();
                    state = State.SPECIMEN_TO_INITIAL;
                }
                /*if (gamepad1.triangle) {
                    scorerPerkid.currentMode = ScorerPerkid.Mode.PICK_SPEC;
                    scorerDifferential.currentMode = ScorerDifferential.Mode.PICK_SPEC;
                    if (distInClaw.getDistance(DistanceUnit.MM) >= 17) {
                        specimenPickedUp.reset();
                    }
                    if (specimenPickedUp.milliseconds() > WAIT_UNTIL_SPECIMEN_PICKUP) {
                        state = State.PICK_SPEC;
                    }
                }*/
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
                    state = State.RIGHT_SIDE_DROP_SAMPLE_PLUS_INIT_FUNCTIONS;
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
            case RIGHT_SIDE_DROP_SAMPLE_PLUS_INIT_FUNCTIONS:
                startHumanDropPickSpec = false;

                if (rightSideDropSampleTimer.milliseconds() > 0 && rightSideDropSampleTimer.milliseconds() <= 700) {
                    claw.currentMode = Claw.Mode.DROP;
                } else if (rightSideDropSampleTimer.milliseconds() >= 900 && rightSideDropSampleTimer.milliseconds() <= 1200) {
                    perkid.currentMode = Perkid.Mode.LOOK;
                    diff.currentMode = Differential.Mode.STRAIGHT_DOWN_DISCRETE;
                    hand.currentMode = Hand.Mode.DRIVER_CONTROL;
                    if (gamepad2.triangle || gamepad1.triangle) {
                        state = State.INITIAL;
                        initialTimer.reset();
                    }
                    if (gamepad2.cross || gamepad1.square) {

                        state = State.PICK_UP_THE_BLOCK_SCORSPEC;
                        pickedUnpickedTimer.reset();
                    }
                }
                scorerPerkid.currentMode = ScorerPerkid.Mode.PICK_SPEC;
                scorerDifferential.currentMode = ScorerDifferential.Mode.PICK_SPEC;
                scorerClaw.currentMode = ScorerClaw.Mode.DROP;

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
                scorerDifferential.currentMode = ScorerDifferential.Mode.GET_FROM_CLAW;

                if (pickedUnpickedTimer.milliseconds() > 200) {
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
            case PICK_UP_THE_BLOCK_WITH_APPENDIX:

                perkid.currentMode = Perkid.Mode.PICK_UP;
                diff.currentMode = Differential.Mode.PICK_UP;
                scorerDifferential.currentMode = ScorerDifferential.Mode.GET_FROM_CLAW;

                if (pickedUnpickedTimer.milliseconds() > 200) {
                    claw.currentMode = Claw.Mode.TRANSFER;
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
                // FLIP THE SAMPLE CLAW
                if (fromClawToClawTimer.milliseconds() <= WAIT_SAMPLE_SCORER_FLIP && !startedMovingExtendBack) {
                    claw.currentMode = Claw.Mode.TRANSFER;
                    scorerClaw.currentMode = ScorerClaw.Mode.DROP;
                    hand.currentMode = Hand.Mode.FORWARD;
                    scorerDifferential.currentMode = ScorerDifferential.Mode.GET_FROM_CLAW;
                    scorerPerkid.currentMode = ScorerPerkid.Mode.PICK_SPEC;
                    diff.currentMode = Differential.Mode.TRANSFER;
                    perkid.currentMode = Perkid.Mode.TRANSFER;
                }
                // START RECTRACTING THE LINKAGE
                else if (fromClawToClawTimer.milliseconds() > WAIT_SAMPLE_SCORER_FLIP && fromClawToClawTimer.milliseconds() <= WAIT_START_LINKAGE_RETRACT && !startedMovingExtendBack) {
                    linkage.currentMode = Linkage.Mode.POWER;
                    linkage.setPower(-1);
                    fromClawToClawTimer.reset();
                    startedMovingExtendBack = true;
                }
                if (startedMovingExtendBack && linkage.getAmps() < EXTEND_AMPS && !stoppedMovingExtendBack) {
                    fromClawToClawTimer.reset();
                } else {
                    linkage.setReference(linkage.getPosition());
                    stoppedMovingExtendBack = true;
                }
                // MOVE THE SPECIMEN SCORER
                if (fromClawToClawTimer.milliseconds() > 0 && fromClawToClawTimer.milliseconds() <= 300
                        && startedMovingExtendBack
                        && stoppedMovingExtendBack) {
                    scorerPerkid.currentMode = ScorerPerkid.Mode.TRANSFER;
                }
                // CLOSE THE SPECIMEN CLAW
                else if (fromClawToClawTimer.milliseconds() > 300 && fromClawToClawTimer.milliseconds() <= 550
                        && startedMovingExtendBack
                        && stoppedMovingExtendBack) {
                    scorerClaw.currentMode = ScorerClaw.Mode.PICK;
                    linkage.currentMode = Linkage.Mode.PURE_PID;
                    linkage.resetEncoder();
                    linkage.setZero();
                }
                // OPEN THE SAMPLE CLAW
                else if (startedMovingExtendBack
                        && stoppedMovingExtendBack) {
                    claw.currentMode = Claw.Mode.DROP;
                    if (gamepad2.left_stick_y != 0) {
                        linkage.setReference(linkage.motor.getCurrentPosition());
                    }
                }

                if (fromClawToClawTimer.milliseconds() > 1050 + OFFSET_CLAW_CLOSE
                        && startedMovingExtendBack
                        && linkage.currentMode == Linkage.Mode.PURE_PID) {
                    startPickUpBlock = false;
                    state = State.SCORE_SAMPLE;
                    linkage.currentMode = Linkage.Mode.DRIVER_CONTROL;
                }

                if (gamepad2.circle || gamepad1.options) {
                    dropSpecimenTimer.reset();
                    state = State.DROP_SPECIMEN_TO_HUMAN_ZONE;
                    linkage.currentMode = Linkage.Mode.DRIVER_CONTROL;
                }
                if (gamepad2.triangle || gamepad1.triangle) {
                    state = State.INITIAL;
                    initialTimer.reset();
                    linkage.currentMode = Linkage.Mode.DRIVER_CONTROL;
                }
                break;
            case DROP_SPECIMEN_TO_HUMAN_ZONE:
                linkage.currentMode = Linkage.Mode.DRIVER_CONTROL;
                perkid.currentMode = Perkid.Mode.TRANSFER;
                claw.currentMode = Claw.Mode.TRANSFER;
                scorerPerkid.currentMode = ScorerPerkid.Mode.PICK_SPEC;
                scorerDifferential.currentMode = ScorerDifferential.Mode.PICK_SPEC;
                hand.currentMode = Hand.Mode.FORWARD;
                diff.currentMode = Differential.Mode.TRANSFER;

                if (dropSpecimenTimer.milliseconds() <= 400) {
                    scorerClaw.currentMode = ScorerClaw.Mode.PICK;
                } else {
                    scorerClaw.currentMode = ScorerClaw.Mode.DROP;
                }
                if (gamepad2.triangle || gamepad1.triangle) {
                    state = State.INITIAL;
                    initialTimer.reset();
                }
                if (gamepad2.square) {
                    pickedUnpickedTimer.reset();
                    sampleIsScored = false;
                    startTimer = false;
                    startTimerFromClawToClaw = false;
                    state = State.LOOK_FOR_BLOCK;
                }
                break;
            case SCORE_SAMPLE:
                scorerPerkid.currentMode = ScorerPerkid.Mode.SCORE_SAMP;
                scorerDifferential.currentMode = ScorerDifferential.Mode.SCORE_SAMP;
                if (gamepad2.right_bumper || gamepad1.circle) {
                    sampleIsScored = true;
                }
                if (gamepad2.triangle || gamepad1.triangle) {
                    state = State.INITIAL;
                    initialTimer.reset();
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
                    scorerClaw.currentMode = ScorerClaw.Mode.DROP;
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
        }

        linearOpMode.telemetry.addData("Heading", driveModule.drive.getRawExternalHeading());
        linearOpMode.telemetry.addData("distance", distInClaw.getDistance(DistanceUnit.MM));
        linearOpMode.telemetry.addData("state", state);
        linearOpMode.telemetry.addData("perkid_state", perkid.currentMode);
        linearOpMode.telemetry.addData("diff_state", diff.currentMode);
        linearOpMode.telemetry.addData("hand_state", hand.currentMode);
        linearOpMode.telemetry.addData("hand_rotation", hand.rotation);
        linearOpMode.telemetry.addData("scorDiff_state", scorerDifferential.currentMode);
        linearOpMode.telemetry.addData("scorPerkid_state", scorerPerkid.currentMode);
        linearOpMode.telemetry.addData("transfer_timer", fromClawToClawTimer);
        linearOpMode.telemetry.addData("lift_left", lift.getPosition());
        linearOpMode.telemetry.addData("lift_right", lift.getPosition());
        linearOpMode.telemetry.addData("lift_ref", lift.getReference());
        linearOpMode.telemetry.addData("extend", linkage.motor.getCurrentPosition());
        linearOpMode.telemetry.addData("extend_reference", linkage.getReference());
        linearOpMode.telemetry.addData("StartTimer", startTimer);
        linearOpMode.telemetry.addData("hang", hang.hang.getCurrentPosition());
        linearOpMode.telemetry.addData("extend_voltage", linkage.motor.getCurrent(CurrentUnit.AMPS));
        linearOpMode.telemetry.addData("drive_state", driveModule.currentMode);
        linearOpMode.telemetry.update();

        TelemetryPacket packet = new TelemetryPacket();
        packet.put("left_motor_pos", lift.getPosition());
        packet.put("right_motor_pos", lift.getPosition());
        packet.put("reference", lift.getReference());
        packet.put("extend", linkage.motor.getCurrentPosition());
        packet.put("extend_reference", linkage.getReference());
        dashboard.sendTelemetryPacket(packet);
    }

    @Config
    public static class TeleOpAutomaticScoreConstants {
        public static double POINT_PERKID_START = 0.2;
        public static double SLOWDOWN_POINT_1 = 0.92;
        public static double POINT_SPECIMEN_START_SCORE = 0.91;
        public static double POINT_MOVE_PERKID_TO_PICK = 0.2;
        public static double POINT_TO_INITIAL = 0.3;

        public static double SLOWDOWN_POINT_2 = 0.8;

        public static double WAIT_AT_RUNG = 50;

        public static double SLOWDOWN_FORCE = 0.15;

        public static double DELTA = -1;

        public static double WAIT_END = 200;
        public static int AMOUNT_TO_SCORE = 8;
    }

    public void buildOneCycle(TrajectoryBuilder builder, double t) {
        builder.addSpatialMarker(t, () -> {
                    driveModule.drive.setForce(1);
                    scorerDifferential.transferSpecimen();
                })
                .addTurnMarker((int) t, -90)
                .addSpatialMarker(t + POINT_PERKID_START, () -> {
                    state = State.TRANSFER_SPEC;
                    fromTransferSpecimenToScore.reset();

                    scorerPerkid.transferSpecimen();
                    scorerDifferential.transferSpecimen();
                    scorerClaw.close();
                    lift.setSpecimenTele();
                    scorerLinkage.extend();
                })
                .addSpatialMarker(t + SLOWDOWN_POINT_1, () -> {
                    driveModule.drive.setForce(SLOWDOWN_FORCE);
                })
                .addSpatialMarker(t + POINT_SPECIMEN_START_SCORE, () -> {
                    state = State.SCORE_SPEC;
                    scorerClaw.currentMode = ScorerClaw.Mode.PICK;
                    fromTransferSpecimenToScore.reset();

                    scorerPerkid.scoreSpecimen();
                    scorerDifferential.scoreSpecimen();
                })
                .waitForAt(1, WAIT_AT_RUNG, -90)
                .addTimeMarker(0, () -> {
                    scorerClaw.currentMode = ScorerClaw.Mode.DROP;
                })
                .addTurnMarker((int) t + 1, -90)
                .addSpatialMarker(t + 1, () -> {
                    scorerDifferential.currentMode = ScorerDifferential.Mode.AFTER_SCORE_SPEC;
                    driveModule.drive.setForce(1);
                })
                .addSpatialMarker(t + 1 + POINT_MOVE_PERKID_TO_PICK, () -> {
                    state = State.SPECIMEN_TO_INITIAL;
                    perekidTimer.reset();

                    scorerLinkage.retract();
                })
                .addSpatialMarker(t + 1 + POINT_TO_INITIAL, () -> {
                    state = State.INITIAL;
                    initialTimer.reset();
                })
                .addSpatialMarker(t + 1 + SLOWDOWN_POINT_2, () -> {
                    driveModule.drive.setForce(SLOWDOWN_FORCE);
                })
                .waitForAt(t + 2, WAIT_END, -90)
                .addTimeMarker(WAIT_END - 100, () -> {
                    state = State.PICK_SPEC;
                });
    }
}
