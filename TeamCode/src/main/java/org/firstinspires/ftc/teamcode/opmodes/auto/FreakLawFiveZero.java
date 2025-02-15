package org.firstinspires.ftc.teamcode.opmodes.auto;


import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.AIM_SPECIMEN_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.AIM_SPECIMEN_1;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.HOLD_POS;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.INITIAL;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.INITIAL_OFFSET;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.RELAX_CLAW;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.SPECIMEN_ADD_FORCE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.WAIT_START;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.ANGLE_LEFT_TO_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.ANGLE_MIDDLE_TO_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.ANGLE_TO_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.ANGLE_TO_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.ANGLE_TO_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.DELTA_1ST;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.DELTA_3RD;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.DELTA_4TH;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.DELTA_5TH;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.DIFF_ROTATION_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.DIFF_ROTATION_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.DIFF_ROTATION_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.DIST_SCOR_CLAW;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.EXTEND_CLOSED;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.EXTEND_LEFT_TO_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.EXTEND_MIDDLE_TO_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.EXTEND_TEMP_CLOSED;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.EXTEND_TO_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.EXTEND_TO_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.EXTEND_TO_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.HAND_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.HAND_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.HAND_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.MIDDLE_STOP_POINT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.POINT_PERKID_START_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.POINT_PERKID_START_1;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.POINT_SPECIMEN_START_SCORE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.POINT_SPECIMEN_START_SCORE_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.RIGHT_STOP_POINT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.SLOWDOWN_FORCE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.SLOWDOWN_POINT_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.SLOWDOWN_POINT_1;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.SLOWDOWN_POINT_2;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.WAIT_AT_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.WAIT_AT_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.WAIT_AT_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.WAIT_EXTEND_HALF_CLOSE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.WAIT_FOR_CLAW_RELEASE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.WAIT_FOR_HUMAN_ADJUST;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.WAIT_FOR_LEFT_ROTATION;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.WAIT_FOR_MIDDLE_ROTATION;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.WAIT_HOLD_POS_AT_RUNG;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.WAIT_PICK_UP;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.WAIT_RELAX_CLAW;
import static org.firstinspires.ftc.teamcode.opmodes.auto.FreakLawConstants.WAIT_SPECIMEN_PICK_UP;

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
import org.firstinspires.ftc.teamcode.Modules.Claw;
import org.firstinspires.ftc.teamcode.Modules.Differential;
import org.firstinspires.ftc.teamcode.Modules.Extend;
import org.firstinspires.ftc.teamcode.Modules.Hand;
import org.firstinspires.ftc.teamcode.Modules.Perkid;
import org.firstinspires.ftc.teamcode.Modules.ScorClaw;
import org.firstinspires.ftc.teamcode.Modules.ScoringDiff;
import org.firstinspires.ftc.teamcode.Modules.ScoringPerkid;
import org.firstinspires.ftc.teamcode.auto.Trajectory;
import org.firstinspires.ftc.teamcode.auto.TrajectoryBuilder;
import org.firstinspires.ftc.teamcode.drive.Drive;

import java.io.IOException;

@Autonomous(name = "FreakLaw_5+0")
public class FreakLawFiveZero extends LinearOpMode {
    private Drive drive;
    private ScoringPerkid scoringPerkid;
    private ScoringDiff scoringDiff;
    private ScorClaw scorClaw;

    private Perkid perkid;
    private Hand hand;
    private Differential diff;
    private Claw claw;
    private Extend extend;
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
        Trajectory trajSpecimenToRung5th, trajRungToSpecimen5th;
        try {
            trajToRungs = drive.buildTrajectory("traj10transposed.json")
                    .addTurnMarker(0, -90)
                    .addSpatialMarker(AIM_SPECIMEN_0, () -> {
                        scoringPerkid.transferSpecimen();
                        scoringDiff.pickSpecimen();
                        perkid.liftSample();
                        hand.setPosition(0.5);
                        diff.aim();
                        claw.close();
                    })
                    .addSpatialMarker(AIM_SPECIMEN_1, () -> {
                        scoringDiff.transferSpecimen();
                    })
                    .addSpatialMarker(SLOWDOWN_POINT_1, () -> {
                        drive.setForce(SLOWDOWN_FORCE);
                    })
                    .addSpatialMarker(POINT_SPECIMEN_START_SCORE, () -> {
                        scoringPerkid.scoreSpecimen();
                        scoringDiff.scoreSpecimen();
                    })
                    .waitForAt(1, HOLD_POS, -90)
                    .plusPoint(2, new Vector2d(0, DELTA_1ST))
                    .plusPoint(3, new Vector2d(0, DELTA_1ST))
                    .build();
            trajToThree = drive.buildTrajectory("traj15transposed.json")
                    .addSpatialMarker(0, () -> {
                        drive.resetForce();
                    })
                    .addTurnMarker(0, -90)
                    .plusPoint(0, new Vector2d(0, DELTA_1ST))
                    .plusPoint(1, new Vector2d(0, DELTA_1ST))
                    .addSpatialMarker(0, () -> {
                        drive.setForce(0.65);

                        relaxFlag = true;
                        clock.reset();
                    })
                    .addSpatialMarker(INITIAL, () -> {
                        scoringPerkid.pickSpecimen();
                        scoringDiff.pickSpecimen();
                        scorClaw.open();

                        // hand.setPosition(0.5);
                        // perkid.transfer();
                        // diff.transfer();
                        // claw.open();
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
                        perkid.aim();
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
                        perkid.aim();

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
//                         hand.setPosition(0.5);
//                         perkid.transfer();
//                         diff.transfer();
//                         claw.open();
                        extend.setReference(EXTEND_TO_MIDDLE);
                        double rotation = ANGLE_TO_MIDDLE + hand.getAngle() + 90 + DIFF_ROTATION_MIDDLE;
                        diff.aimAngle(rotation);
                    })
                    .addTimeMarker(WAIT_AT_MIDDLE, () -> {
                        hand.setPosition(HAND_MIDDLE);
                        double rotation = ANGLE_TO_MIDDLE + hand.getAngle() + 90 + DIFF_ROTATION_MIDDLE;
                        diff.aimAngle(rotation);
                        perkid.aim();
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
                        perkid.aim();

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
//                         hand.setPosition(0.5);
//                         perkid.transfer();
//                         diff.transfer();
//                         claw.open();
                        double rotation = ANGLE_TO_RIGHT + hand.getAngle() + 90 + DIFF_ROTATION_RIGHT;
                        diff.aimAngle(rotation);
                    })
                    .addTimeMarker(WAIT_AT_RIGHT, () -> {
                        hand.setPosition(HAND_RIGHT);
                        double rotation = ANGLE_TO_RIGHT + hand.getAngle() + 90 + DIFF_ROTATION_RIGHT;
                        extend.setReference(EXTEND_TO_RIGHT);
                        diff.aimAngle(rotation);
                        perkid.aim();
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
                        perkid.aim();

                    })
                    // RIGHT TO HUMAN
                    .addTurnMarker(2, -90)
                    .addSpatialMarker(2.01, () -> {
                        scorClaw.open();
                    })
                    .addSpatialMarker(2.1, () -> {
                        extend.setReference(EXTEND_TEMP_CLOSED);
                        hand.dropOverRightSide();
                        perkid.dropOverRightSide();
                        diff.dropOverRightSide();
                        scoringPerkid.aimAtSpecimen();
                        scoringDiff.aimSpecimen();
                        scorClaw.open();
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
                    .addTimeMarker(WAIT_FOR_HUMAN_ADJUST, () -> {
                        scoringPerkid.pickSpecimen();
                        scoringDiff.pickSpecimen();
                    })
                    .build();
            buildSpecimenToRung = drive.buildTrajectory("traj13transposed.json")
                    .addSpatialMarker(0, () -> {
                        scoringDiff.liftSpecimenFromWall();
                    })
                    .addTurnMarker(0, -90)
                    .addSpatialMarker(POINT_PERKID_START_0, () -> {
                        scoringPerkid.transferSpecimen();
                        scoringDiff.pickSpecimen();
                        scorClaw.close();

                        perkid.liftSample();
                        hand.setPosition(0.5);
                        claw.close();
                        diff.aim();
                    })
                    .addSpatialMarker(SLOWDOWN_POINT_1, () -> {
                        drive.setForce(SLOWDOWN_FORCE);
                    })
                    .addSpatialMarker(POINT_PERKID_START_1, () -> {
                        scoringDiff.transferSpecimen();
                    })
                    .addSpatialMarker(POINT_SPECIMEN_START_SCORE_0, () -> {
                        scoringPerkid.scoreSpecimen();
                        scoringDiff.scoreSpecimen();
                    })
                    .waitForAt(1, WAIT_HOLD_POS_AT_RUNG, -90);
            trajSpecimenToRung = buildSpecimenToRung.build();
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
                        scoringPerkid.aimAtSpecimen();
                        scoringDiff.aimSpecimen();
                        scorClaw.open();

                        // hand.setPosition(0.5);
                        // perkid.transfer();
                        // diff.transfer();
                        // claw.open();
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
                    .waitForAt(1, WAIT_FOR_HUMAN_ADJUST + 100, -90)
                    .addTimeMarker(WAIT_FOR_HUMAN_ADJUST, () -> {
                        scoringPerkid.pickSpecimen();
                        scoringDiff.pickSpecimen();
                    });
            trajRungToSpecimen = buildRungToSpecimen.build();
            trajRungToSpecimen3rd = buildRungToSpecimen
                    .plusPoint(0, new Vector2d(DELTA_3RD, 0))
                    .plusPoint(1, new Vector2d(DELTA_3RD, 0))
                    .build();
            trajRungToSpecimen4th = buildRungToSpecimen
                    .plusPoint(0, new Vector2d(DELTA_4TH, 0))
                    .plusPoint(1, new Vector2d(DELTA_4TH, 0))
                    .build();
            trajRungToSpecimen5th = buildRungToSpecimen
                    .plusPoint(0, new Vector2d(DELTA_5TH, 0))
                    .plusPoint(1, new Vector2d(DELTA_5TH, 0))
                    .addSpatialMarker(INITIAL + 0.02, () -> {
                        drive.setForce(1);
                    })
                    .addSpatialMarker(SLOWDOWN_POINT_2 + 0.02, () -> {
                        drive.setForce(1);
                    })
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        drive.setPoseEstimate(new Pose2d(trajToRungs.get(0).point0, Math.toRadians(270)));
        drive.resetForce();

        scorClaw.close();

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
                scorClaw.relax();
                drive.resetForce();
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

        scorClaw.close();

        drive.setForce(0.6);

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
                scorClaw.relax();
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

        scorClaw.close();

        drive.setForce(0.6);

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
                scorClaw.relax();
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

        scorClaw.close();

        drive.setForce(0.6);

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
                scorClaw.relax();
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

        scorClaw.close();

        drive.setForce(0.6);

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

        drive.followTrajectory(trajRungToSpecimen5th);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            if (relaxFlag && clock.milliseconds() > WAIT_RELAX_CLAW) {
                scorClaw.relax();
                relaxFlag = false;
                drive.setForce(1);
            }

            drive.update();
            extend.PIDControl();
        }
    }

    private void initRobot() {
        drive = new Drive(hardwareMap);

        scoringPerkid = new ScoringPerkid(this);
        scoringDiff = new ScoringDiff(this);
        scorClaw = new ScorClaw(this);

        perkid = new Perkid(this);
        hand = new Hand(this);
        diff = new Differential(this);
        claw = new Claw(this);

        extend = new Extend(this);
        distInClaw = hardwareMap.get(DistanceSensor.class, "ScorClawDist");
        frontSensor = hardwareMap.get(DigitalChannel.class, "DistanceFront");

        clock = new ElapsedTime();
        dashboard = FtcDashboard.getInstance();
    }
}
