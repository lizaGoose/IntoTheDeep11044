package org.firstinspires.ftc.teamcode.opmodes.auto;


import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.POINT_MOVE_PERKID_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.HOLD_POS;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.INITIAL;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.INITIAL_OFFSET;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.RELAX_CLAW;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.SPECIMEN_ADD_FORCE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.WAIT_START;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.SLOWDOWN_POINT_RUNG;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.ANGLE_LEFT_TO_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.ANGLE_MIDDLE_TO_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.ANGLE_TO_BASKET;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.ANGLE_TO_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.ANGLE_TO_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.ANGLE_TO_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DELTA_1ST;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DELTA_2ND;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DELTA_3RD;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DELTA_4TH;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DELTA_5TH;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DIFF_ROTATION_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DIFF_ROTATION_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DIFF_ROTATION_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DIST_SCOR_CLAW;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.EXTEND_CLOSED;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.EXTEND_LEFT_TO_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.EXTEND_MIDDLE_TO_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.EXTEND_TEMP_CLOSED;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.EXTEND_TO_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.EXTEND_TO_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.EXTEND_TO_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.FORCE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.HAND_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.HAND_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.HAND_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.LIFT_SCORE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.MIDDLE_STOP_POINT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.POINT_BASKET_SLOWDOWN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.POINT_PERKID_START_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.POINT_PERKID_START_1;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.POINT_PERKID_TO_SCORE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.POINT_SPECIMEN_START_SCORE_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.POINT_START_RAISING_LIFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.RIGHT_STOP_POINT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.SLOWDOWN_FORCE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.SLOWDOWN_POINT_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.SLOWDOWN_POINT_1;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.SLOWDOWN_POINT_2;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_AT_BASKET;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_AT_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_AT_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_AT_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_DROP_SAMPLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_EXTEND_HALF_CLOSE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_FOR_CLAW_RELEASE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_FOR_HUMAN_ADJUST;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_FOR_LEFT_ROTATION;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_FOR_MIDDLE_ROTATION;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_FOR_SLOWDOWN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_HOLD_POS_AT_RUNG;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_PICK_UP;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_RELAX_CLAW;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_SPECIMEN_PICK_UP;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Differential;
import org.firstinspires.ftc.teamcode.modules.Extend;
import org.firstinspires.ftc.teamcode.modules.Hand;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Perkid;
import org.firstinspires.ftc.teamcode.modules.ScorerClaw;
import org.firstinspires.ftc.teamcode.modules.ScorerLinkage;
import org.firstinspires.ftc.teamcode.modules.ScorerDifferential;
import org.firstinspires.ftc.teamcode.modules.ScorerPerkid;
import org.firstinspires.ftc.teamcode.auto.Trajectory;
import org.firstinspires.ftc.teamcode.auto.TrajectoryBuilder;
import org.firstinspires.ftc.teamcode.auto.Drive;

import java.io.IOException;

@Autonomous(name = "Lebron 5+1")
public class LebronFiveOne extends LinearOpMode {
    private Drive drive;
    private ScorerPerkid scorerPerkid;
    private ScorerDifferential scorerDifferential;
    private ScorerClaw scorerClaw;

    private Perkid perkid;
    private Hand hand;
    private Differential diff;
    private Claw claw;
    private Extend extend;
    private Lift lift;
    private ScorerLinkage scorerLinkage;
    private DistanceSensor distInClaw;
    private DigitalChannel frontSensor;
    private static boolean relaxFlag = false;
    private FtcDashboard dashboard;

    // Make this true somewhere to stop program
    private boolean DEBUG_END_FLAG = false;

    private ElapsedTime clock;

    @Override
    public void runOpMode() throws InterruptedException {
        initRobot();

        relaxFlag = false;
        TrajectoryBuilder buildSpecimenToRung, buildRungToSpecimen;
        Trajectory trajToRungs, trajToThree, trajSpecimenToRung, trajRungToSpecimen;
        Trajectory trajSpecimenToRung3rd, trajRungToSpecimen3rd;
        Trajectory trajSpecimenToRung4th, trajRungToSpecimen4th;
        Trajectory trajSpecimenToRung5th, trajRungToYellowSample;
        Trajectory trajSampleToBasket;
        try {
            trajToRungs = drive.buildTrajectory("traj10transposed.json")
                    .addTurnMarker(0, -90)
                    .addSpatialMarker(POINT_MOVE_PERKID_0, () -> {
                        scorerPerkid.scoreSpecimen();
                        scorerDifferential.scoreSpecimen();
                        lift.setSpecimen();
                        scorerLinkage.extend();
                    })
                    .addSpatialMarker(SLOWDOWN_POINT_RUNG, () -> {
                        drive.setForce(FreakLawConstants.SLOWDOWN_FORCE);
                    })
                    .waitForAt(1, HOLD_POS, -90)
                    .plusPoint(2, new Vector2d(0, DELTA_1ST))
                    .plusPoint(3, new Vector2d(0, DELTA_1ST))
                    .build();
            trajToThree = drive.buildTrajectory("traj15transposed.json")
                    .addSpatialMarker(0, () -> {
                        drive.setForce(FORCE);
                    })
                    .addTurnMarker(0, -90)
                    .plusPoint(0, new Vector2d(0, DELTA_1ST))
                    .plusPoint(1, new Vector2d(0, DELTA_1ST))
                    .addSpatialMarker(0, () -> {
                        drive.setForce(FORCE);

                        relaxFlag = true;
                        clock.reset();
                    })
                    .addSpatialMarker(INITIAL, () -> {
                        scorerPerkid.pickSpecimen();
                        scorerDifferential.pickSpecimen();
                        scorerClaw.open();
                    })
                    .addSpatialMarker(INITIAL + INITIAL_OFFSET, () -> {
                        hand.setPosition(0.5);
                        perkid.transfer();
                        diff.transfer();
                        claw.close();
                    })
                    // PICK UP LEFT
                    .waitForAt(1, WAIT_AT_LEFT + WAIT_PICK_UP + 650, ANGLE_TO_LEFT)
                    .addTimeMarker(WAIT_AT_LEFT, () -> {
                        extend.setReference(EXTEND_TO_LEFT);
                        hand.setPosition(HAND_LEFT);
                        double rotation = ANGLE_TO_LEFT + hand.getAngle() + 90 + DIFF_ROTATION_LEFT;
                        diff.aimAngle(rotation);
                        perkid.aimAtSpike();
                        claw.transfer();
                    })
                    .addTimeMarker(WAIT_AT_LEFT + 200, () -> {
                        claw.open();
                    })
                    .addTimeMarker(WAIT_AT_LEFT + 400, () -> {
                        perkid.pickUp();
                    })
                    .addTimeMarker(WAIT_AT_LEFT + 500, () -> {
                        double rotation = ANGLE_TO_LEFT + hand.getAngle() + 90 + DIFF_ROTATION_LEFT;
                        diff.pickUpAngle(rotation);
                    })
                    .addTimeMarker(WAIT_AT_LEFT + 600, () -> {
                        claw.close();
                    })
                    .addTimeMarker(WAIT_AT_LEFT + WAIT_PICK_UP + 600, () -> {
                        diff.aim();
                        perkid.liftSample();
                    })
                    .addTurnMarker(1, (t) -> {
                        if (t <= 0.5) {
                            return Math.toRadians(ANGLE_LEFT_TO_HUMAN);
                        } else {
                            return Math.toRadians(ANGLE_MIDDLE_TO_HUMAN);
                        }
                    })
                    // LEFT TO HUMAN
                    .waitForAt(MIDDLE_STOP_POINT, WAIT_FOR_LEFT_ROTATION + 100, ANGLE_LEFT_TO_HUMAN)
                    .addTimeMarker(0, () -> {
                        extend.setReference(EXTEND_LEFT_TO_HUMAN);
                    })
                    .addTimeMarker(WAIT_FOR_LEFT_ROTATION, () -> {
                        claw.open();
                    })
                    // PICK UP MIDDLE
                    .waitForAt(MIDDLE_STOP_POINT, WAIT_AT_MIDDLE + WAIT_PICK_UP + 650, ANGLE_TO_MIDDLE)
                    .addTimeMarker(0, () -> {
                        extend.setReference(EXTEND_TO_MIDDLE);
                        double rotation = ANGLE_TO_MIDDLE + hand.getAngle() + 90 + DIFF_ROTATION_MIDDLE;
                        diff.aimAngle(rotation);
                    })
                    .addTimeMarker(WAIT_AT_MIDDLE, () -> {
                        hand.setPosition(HAND_MIDDLE);
                        double rotation = ANGLE_TO_MIDDLE + hand.getAngle() + 90 + DIFF_ROTATION_MIDDLE;
                        diff.aimAngle(rotation);
                        perkid.aimAtSpike();
                        claw.open();
                    })
                    .addTimeMarker(WAIT_AT_MIDDLE + 400, () -> {
                        perkid.pickUp();
                    })
                    .addTimeMarker(WAIT_AT_MIDDLE + 500, () -> {
                        double rotation = ANGLE_TO_MIDDLE + hand.getAngle() + 90 + DIFF_ROTATION_MIDDLE;
                        diff.pickUpAngle(rotation);
                    })
                    .addTimeMarker(WAIT_AT_MIDDLE + 600, () -> {
                        claw.close();
                    })
                    .addTimeMarker(WAIT_AT_MIDDLE + WAIT_PICK_UP + 600, () -> {
                        diff.aim();
                        perkid.liftSample();
                    })
                    // MIDDLE TO HUMAN
                    .waitForAt(RIGHT_STOP_POINT, WAIT_FOR_MIDDLE_ROTATION + 200, ANGLE_MIDDLE_TO_HUMAN)
                    .addTimeMarker(WAIT_FOR_MIDDLE_ROTATION / 2, () -> {
                        extend.setReference(EXTEND_MIDDLE_TO_HUMAN);
                    })
                    .addTimeMarker(WAIT_FOR_MIDDLE_ROTATION, () -> {
                        claw.open();
                    })
                    // PICK UP RIGHT
                    .waitForAt(RIGHT_STOP_POINT, WAIT_EXTEND_HALF_CLOSE, ANGLE_MIDDLE_TO_HUMAN)
                    .addTimeMarker(0, () -> {
                        claw.open();
                        extend.setReference(EXTEND_CLOSED);
                    })
                    .waitForAt(RIGHT_STOP_POINT, WAIT_AT_RIGHT + WAIT_PICK_UP + 650, ANGLE_TO_RIGHT)
                    .addTimeMarker(0, () -> {
                        double rotation = ANGLE_TO_RIGHT + hand.getAngle() + 90 + DIFF_ROTATION_RIGHT;
                        diff.aimAngle(rotation);
                    })
                    .addTimeMarker(WAIT_AT_RIGHT, () -> {
                        hand.setPosition(HAND_RIGHT);
                        double rotation = ANGLE_TO_RIGHT + hand.getAngle() + 90 + DIFF_ROTATION_RIGHT;
                        extend.setReference(EXTEND_TO_RIGHT);
                        diff.aimAngle(rotation);
                        perkid.aimAtSpike();
                        claw.open();
                    })
                    .addTimeMarker(WAIT_AT_RIGHT + 400, () -> {
                        perkid.pickUp();
                    })
                    .addTimeMarker(WAIT_AT_RIGHT + 500, () -> {
                        double rotation = ANGLE_TO_RIGHT + hand.getAngle() + 90 + DIFF_ROTATION_RIGHT;
                        diff.pickUpAngle(rotation);
                    })
                    .addTimeMarker(WAIT_AT_RIGHT  + 600, () -> {
                        claw.close();
                    })
                    .addTimeMarker(WAIT_AT_RIGHT + WAIT_PICK_UP + 600, () -> {
                        diff.aim();
                        perkid.liftSample();

                    })
                    // RIGHT TO HUMAN
                    .addTurnMarker(2, -90)
                    .addSpatialMarker(2.01, () -> {
                        scorerClaw.open();
                    })
                    .addSpatialMarker(2.1, () -> {
                        extend.setReference(EXTEND_TEMP_CLOSED);
                        hand.dropOverRightSide();
                        perkid.dropOverRightSide();
                        diff.dropOverRightSide();
                        scorerPerkid.pickSpecimen();
                        scorerDifferential.pickSpecimen();
                        scorerClaw.open();
                    })
                    .addSpatialMarker(2 + SLOWDOWN_POINT_0, () -> {
                        drive.setForce(SLOWDOWN_FORCE);
                    })
                    .addSpatialMarker(2.8, () -> {
                        extend.setReference(0);
                    })
                    /*
                    .addSpatialMarker(POINT_THIRD_RELEASE, () -> {
                        claw.open();
                    })
                    */
                    .waitForAt(3, WAIT_FOR_HUMAN_ADJUST + 100, -90)
                    .addTimeMarker(WAIT_FOR_CLAW_RELEASE, () -> {
                        claw.open();
                    })
                    .build();
            buildSpecimenToRung = drive.buildTrajectory("traj13transposed.json")
                    .addSpatialMarker(0, () -> {
                        scorerDifferential.liftSpecimenFromWall();
                    })
                    .addTurnMarker(0, -90)
                    .addSpatialMarker(POINT_PERKID_START_0, () -> {
                        scorerPerkid.transferSpecimen();
                        scorerDifferential.pickSpecimen();
                        scorerClaw.close();

                        perkid.liftSample();
                        hand.setPosition(0.5);
                        claw.close();
                        diff.aim();
                    })
                    .addSpatialMarker(SLOWDOWN_POINT_1, () -> {
                        drive.setForce(SLOWDOWN_FORCE);
                    })
                    .addSpatialMarker(POINT_PERKID_START_1, () -> {
                        scorerDifferential.transferSpecimen();
                    })
                    .addSpatialMarker(POINT_SPECIMEN_START_SCORE_0, () -> {
                        scorerPerkid.scoreSpecimen();
                        scorerDifferential.scoreSpecimen();
                    })
                    .waitForAt(1, WAIT_HOLD_POS_AT_RUNG, -90);
            trajSpecimenToRung = buildSpecimenToRung
                    .plusPoint(3, new Vector2d(DELTA_2ND, 0))
                    .plusPoint(2, new Vector2d(DELTA_2ND, 0))
                    .build();
            trajSpecimenToRung3rd = buildSpecimenToRung
                    .plusPoint(3, new Vector2d(DELTA_3RD, 0))
                    .plusPoint(2, new Vector2d(DELTA_3RD, 0))
                    .build();
            trajSpecimenToRung4th = buildSpecimenToRung
                    .plusPoint(3, new Vector2d(DELTA_4TH, 0))
                    .plusPoint(2, new Vector2d(DELTA_4TH, 0))
                    .build();
            trajSpecimenToRung5th = buildSpecimenToRung
                    .plusPoint(3, new Vector2d(DELTA_5TH, 0))
                    .plusPoint(2, new Vector2d(DELTA_5TH, 0))
                    .build();
            buildRungToSpecimen = drive.buildTrajectory("traj11transposed.json")
                    .addTurnMarker(0, -90)
                    .addSpatialMarker(0, () -> {
                        drive.setForce(SPECIMEN_ADD_FORCE);

                        relaxFlag = true;
                        clock.reset();
                    })
                    .addSpatialMarker(INITIAL, () -> {
                        scorerPerkid.pickSpecimen();
                        scorerDifferential.pickSpecimen();
                        scorerClaw.open();
                    })
                    .addSpatialMarker(INITIAL + INITIAL_OFFSET, () -> {
                        hand.setPosition(0.5);
                        perkid.transfer();
                        diff.transfer();
                        claw.close();
                    })
                    .addSpatialMarker(SLOWDOWN_POINT_2, () -> {
                        drive.setForce(SLOWDOWN_FORCE);
                    })
                    .waitForAt(1, WAIT_FOR_HUMAN_ADJUST + 100, -90);
            trajRungToSpecimen = buildRungToSpecimen
                    .plusPoint(0, new Vector2d(DELTA_2ND, 0))
                    .plusPoint(1, new Vector2d(DELTA_2ND, 0))
                    .build();
            trajRungToSpecimen3rd = buildRungToSpecimen
                    .plusPoint(0, new Vector2d(DELTA_3RD, 0))
                    .plusPoint(1, new Vector2d(DELTA_3RD, 0))
                    .build();
            trajRungToSpecimen4th = buildRungToSpecimen
                    .plusPoint(0, new Vector2d(DELTA_4TH, 0))
                    .plusPoint(1, new Vector2d(DELTA_4TH, 0))
                    .build();
            trajRungToYellowSample = buildRungToSpecimen
                    .plusPoint(0, new Vector2d(DELTA_5TH, 0))
                    .plusPoint(1, new Vector2d(DELTA_5TH, 0))
                    .build();
            trajSampleToBasket = drive.buildTrajectory("traj19.json")
                    .addTurnMarker(0, -180)
                    .addSpatialMarker(POINT_PERKID_TO_SCORE, () -> {
                        scorerPerkid.scoreSample();
                        scorerDifferential.scoreSample();
                    })
                    .addSpatialMarker(POINT_START_RAISING_LIFT, () -> {
                        lift.setReference(LIFT_SCORE);
                    })
                    .addSpatialMarker(POINT_BASKET_SLOWDOWN, () -> {
//                        drive.setForce(SLOWDOWN_FORCE);
                    })
                    .waitForAt(1, WAIT_AT_BASKET, -180)
                    .addTimeMarker(WAIT_DROP_SAMPLE - 100, () -> {
                        scorerDifferential.slamDunkSample();
                    })
                    .addTimeMarker(WAIT_DROP_SAMPLE, () -> {
                        scorerClaw.open();
                    })
                    .addTimeMarker(WAIT_FOR_SLOWDOWN, () -> {
                        drive.setForce(SLOWDOWN_FORCE);
                    })
                    .waitForAt(2, 400, ANGLE_TO_BASKET)
                    .addTimeMarker(0, () -> {
                        scorerPerkid.pickSpecimen();
                        lift.setReference(0);
                    })
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        drive.setPoseEstimate(new Pose2d(trajToRungs.get(0).point0, Math.toRadians(270)));
        drive.setForce(FORCE);

        scorerClaw.close();

        TelemetryPacket packet = new TelemetryPacket();
        packet.put("extend_reference", extend.getReference());
        packet.put("extend_position", extend.getPosition());
        dashboard.sendTelemetryPacket(packet);

        waitForStart();

        /* ************************************************************************************* */

        /* *** WAIT *** */
        clock.reset();

        while (clock.seconds() < WAIT_START) {
            telemetry.update();
        }

        /* *** GO TO RUNGS *** */
        drive.followTrajectory(trajToRungs);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {

            packet = new TelemetryPacket();
            packet.put("extend_reference", extend.getReference());
            packet.put("extend_position", extend.getPosition());
            dashboard.sendTelemetryPacket(packet);

            drive.update();
            extend.PIDControl();
        }
        // -31.362

        clock.reset();

        /* *** GO TO HUMAN AND DO EVERYTHING *** */
        drive.followTrajectory(trajToThree);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            if (relaxFlag && clock.milliseconds() > RELAX_CLAW) {
                scorerClaw.relax();
                drive.setForce(FORCE);
                relaxFlag = false;
            }

            packet = new TelemetryPacket();
            packet.put("extend_reference", extend.getReference());
            packet.put("extend_position", extend.getPosition());
            dashboard.sendTelemetryPacket(packet);

            drive.update();
            extend.PIDControl();
        }

        /* ***************** START 2ND SPECIMEN CYCLE ***************** */

        clock.reset();
        boolean pickedUp = false;
        while (clock.milliseconds() < WAIT_SPECIMEN_PICK_UP && !DEBUG_END_FLAG) {
            if (distInClaw.getDistance(DistanceUnit.MM) <= DIST_SCOR_CLAW) {
                pickedUp = true;
            }
        }

        scorerClaw.close();

        drive.setForce(FORCE);

        drive.followTrajectory(trajSpecimenToRung);

        boolean resetX = true;

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            if (resetX && !frontSensor.getState()) {
                Pose2d poseEstimate = drive.getPoseEstimate();
                //drive.setPoseEstimate(new Pose2d(-31.362, poseEstimate.getY(), poseEstimate.getHeading()));
                resetX = false;
            }
            drive.update();
            extend.PIDControl();
        }

        clock.reset();

        drive.followTrajectory(trajRungToSpecimen);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            if (relaxFlag && clock.milliseconds() > WAIT_RELAX_CLAW) {
                scorerClaw.relax();
                relaxFlag = false;
            }

            drive.update();
            extend.PIDControl();
        }

        // ********************* START 3ND SPECIMEN CYCLE **************************************************

        clock.reset();
        pickedUp = false;
        while (clock.milliseconds() < WAIT_SPECIMEN_PICK_UP && !DEBUG_END_FLAG) {
            if (distInClaw.getDistance(DistanceUnit.MM) <= DIST_SCOR_CLAW) {
                pickedUp = true;
            }
        }

        scorerClaw.close();

        drive.setForce(FORCE);

        drive.followTrajectory(trajSpecimenToRung3rd);

        resetX = true;

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            if (resetX && !frontSensor.getState()) {
                Pose2d poseEstimate = drive.getPoseEstimate();
                //drive.setPoseEstimate(new Pose2d(-31.362, poseEstimate.getY(), poseEstimate.getHeading()));
                resetX = false;
            }
            drive.update();
            extend.PIDControl();
        }

        clock.reset();

        drive.followTrajectory(trajRungToSpecimen3rd);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            if (relaxFlag && clock.milliseconds() > WAIT_RELAX_CLAW) {
                scorerClaw.relax();
                relaxFlag = false;
            }

            drive.update();
            extend.PIDControl();
        }

        // ********************* START 4TH SPECIMEN CYCLE **************************************************

        clock.reset();
        pickedUp = false;
        while (clock.milliseconds() < WAIT_SPECIMEN_PICK_UP && !DEBUG_END_FLAG) {
            if (distInClaw.getDistance(DistanceUnit.MM) <= DIST_SCOR_CLAW) {
                pickedUp = true;
            }
        }

        scorerClaw.close();

        drive.setForce(FORCE);

        drive.followTrajectory(trajSpecimenToRung4th);

        resetX = true;

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            if (resetX && !frontSensor.getState()) {
                Pose2d poseEstimate = drive.getPoseEstimate();
                //drive.setPoseEstimate(new Pose2d(-31.362, poseEstimate.getY(), poseEstimate.getHeading()));
                resetX = false;
            }
            drive.update();
            extend.PIDControl();
        }

        clock.reset();

        drive.followTrajectory(trajRungToSpecimen4th);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            if (relaxFlag && clock.milliseconds() > WAIT_RELAX_CLAW) {
                scorerClaw.relax();
                relaxFlag = false;
            }

            drive.update();
            extend.PIDControl();
        }

        // ********************* START 5TH SPECIMEN CYCLE **************************************************

        clock.reset();
        pickedUp = false;
        while (clock.milliseconds() < WAIT_SPECIMEN_PICK_UP && !DEBUG_END_FLAG) {
            if (distInClaw.getDistance(DistanceUnit.MM) <= DIST_SCOR_CLAW) {
                pickedUp = true;
            }
        }

        scorerClaw.close();

        drive.setForce(FORCE);

        drive.followTrajectory(trajSpecimenToRung5th);

        resetX = true;

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            if (resetX && !frontSensor.getState()) {
                Pose2d poseEstimate = drive.getPoseEstimate();
                //drive.setPoseEstimate(new Pose2d(-31.362, poseEstimate.getY(), poseEstimate.getHeading()));
                resetX = false;
            }
            drive.update();
            extend.PIDControl();
        }

        clock.reset();

        drive.followTrajectory(trajRungToYellowSample);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            if (relaxFlag && clock.milliseconds() > WAIT_RELAX_CLAW) {
                scorerClaw.relax();
                relaxFlag = false;
                drive.setForce(FORCE);
            }

            drive.update();
            extend.PIDControl();
        }

        // ********************* START YELLOW STUFF **************************************************

        clock.reset();
        pickedUp = false;
        while (clock.milliseconds() < WAIT_SPECIMEN_PICK_UP && !DEBUG_END_FLAG) {
            if (distInClaw.getDistance(DistanceUnit.MM) <= DIST_SCOR_CLAW) {
                pickedUp = true;
            }
        }

        scorerClaw.close();

        drive.setForce(1);

        drive.followTrajectory(trajSampleToBasket);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            drive.update();
            extend.PIDControl();
            lift.PIDControl();
        }
    }

    private void initRobot() {
        drive = new Drive(hardwareMap);

        scorerPerkid = new ScorerPerkid(this);
        scorerDifferential = new ScorerDifferential(this);
        scorerClaw = new ScorerClaw(this);

        perkid = new Perkid(this);
        hand = new Hand(this);
        diff = new Differential(this);
        claw = new Claw(this);

        extend = new Extend(this);
        lift = new Lift(this);
        distInClaw = hardwareMap.get(DistanceSensor.class, "ScorClawDist");
        frontSensor = hardwareMap.get(DigitalChannel.class, "DistanceFront");

        clock = new ElapsedTime();
        dashboard = FtcDashboard.getInstance();
    }
}
