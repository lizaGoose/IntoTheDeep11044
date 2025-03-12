package org.firstinspires.ftc.teamcode.opmodes.auto;


import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.INITIAL;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.INITIAL_OFFSET;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.POINT_MOVE_INTAKE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.POINT_MOVE_PERKID_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.WAIT_START;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.ANGLE_TO_BASKET;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.ANGLE_TO_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DELTA_1ST;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DELTA_2ND;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DELTA_3RD;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DELTA_4TH;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DELTA_5TH;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DIFF_ROTATION_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DIST_SCOR_CLAW;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.FORCE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.LIFT_SCORE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.POINT_BASKET_SLOWDOWN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.POINT_PERKID_START_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.POINT_PERKID_TO_SCORE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.POINT_START_RAISING_LIFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.SLOWDOWN_FORCE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.SLOWDOWN_POINT_HUMAN_1;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.SLOWDOWN_POINT_RUNG_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.SLOWDOWN_POINT_RUNG_1;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_AFTER_PICK_UP;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_AT_BASKET;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_DROP_SAMPLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_FOR_CLAW_RELEASE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_FOR_HUMAN_ADJUST;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_FOR_SLOWDOWN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_HOLD_POS_AT_RUNG;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_SPECIMEN_PICK_UP;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.ANGLE_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.ANGLE_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.EXTEND_LEFT_SAMPLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.EXTEND_MIDDLE_SAMPLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.EXTEND_RIGHT_SAMPLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.EXTEND_TEMP_CLOSED;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.HAND_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.HAND_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.HAND_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.POINT_RETRACT_EXTEND;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.POINT_RIGHT_SIDE_DROP;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.SLOWDOWN_POINT_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.WAIT_AFTER_STOP_FOR_SUB_SAMPLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.WAIT_LEFT_PICKUP;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.WAIT_MIDDLE_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.WAIT_MIDDLE_PICKUP;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.WAIT_FOR_INTAKE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.WAIT_RELEASE_SAMPLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.WAIT_RIGHT_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.WAIT_RIGHT_PICKUP;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.WAIT_START_RETRACT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.SubThreeClaw.SubThreeClawConstants.WAIT_LEFT_HUMAN;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.auto.Drive;
import org.firstinspires.ftc.teamcode.auto.Trajectory;
import org.firstinspires.ftc.teamcode.auto.TrajectoryBuilder;
import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Differential;
import org.firstinspires.ftc.teamcode.modules.Extend;
import org.firstinspires.ftc.teamcode.modules.Hand;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Perkid;
import org.firstinspires.ftc.teamcode.modules.ScorerClaw;
import org.firstinspires.ftc.teamcode.modules.ScorerDifferential;
import org.firstinspires.ftc.teamcode.modules.ScorerLinkage;
import org.firstinspires.ftc.teamcode.modules.ScorerPerkid;

import java.io.IOException;

@Autonomous(name = "1 Sub 3 Claw 6+0")
public class SubThreeClaw extends LinearOpMode {
    @Config
    public static class SubThreeClawConstants {
        public static double WAIT_FOR_INTAKE = 500;

        public static double WAIT_AFTER_STOP_FOR_SUB_SAMPLE = 300;

        public static double WAIT_START_RETRACT = 200;
        public static double WAIT_RELEASE_SAMPLE = 300;

        public static double EXTEND_MIDDLE_SAMPLE = 160;
        public static double WAIT_MIDDLE_PICKUP = 500 + 150 + 650;
        // ANGLE MIDDLE 0
        public static double WAIT_MIDDLE_HUMAN = 500;
        public static double HAND_MIDDLE = 0.5;

        public static double EXTEND_LEFT_SAMPLE = 160;
        public static double WAIT_LEFT_PICKUP = 500 + 150 + 650;
        public static double ANGLE_LEFT = -70;
        public static double WAIT_LEFT_HUMAN = 500;
        public static double HAND_LEFT = 0.5;

        public static double EXTEND_RIGHT_SAMPLE = 200;
        public static double WAIT_RIGHT_PICKUP = 500 + 150 + 650;
        public static double ANGLE_RIGHT = -120;
        public static double WAIT_RIGHT_HUMAN = 500;
        public static double HAND_RIGHT = 0.25;

        public static double EXTEND_TEMP_CLOSED = 100;

        public static double POINT_RIGHT_SIDE_DROP = 0.3;

        public static double SLOWDOWN_POINT_HUMAN = 0.65;
        public static double POINT_RETRACT_EXTEND = 0.8;
    }

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
    private FtcDashboard dashboard;

    // Make this true somewhere to stop program
    private boolean DEBUG_END_FLAG = false;

    private ElapsedTime clock;

    @Override
    public void runOpMode() throws InterruptedException {
        initRobot();

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
                    .addSpatialMarker(POINT_MOVE_INTAKE, () -> {
                        hand.setPosition(0.5);
                        perkid.aimAtSpike();
                        diff.aim();
                        claw.open();
                    })
                    .addSpatialMarker(SLOWDOWN_POINT_RUNG_0, () -> {
                        drive.setForce(SLOWDOWN_FORCE);
                    })
                    .waitForAt(1, 700, -90)
                    .addTimeMarker(WAIT_AFTER_STOP_FOR_SUB_SAMPLE, true, () -> {
                        perkid.pickUp();
                    })
                    .addTimeMarker(100, true, () -> {
                        diff.pickUp();
                    })
                    .addTimeMarker(100, true, () -> {
                        claw.close();
                    })
                    .addTimeMarker(150, true, () -> {
                        diff.aim();
                        perkid.liftSample();
                    })
                    .plusPoint(2, new Vector2d(0, DELTA_1ST))
                    .plusPoint(3, new Vector2d(0, DELTA_1ST))
                    .build();
            trajToThree = drive.buildTrajectory("auto_3_samp_claw.json")
                    .addTurnMarker(0, -90)
                    .plusPoint(0, new Vector2d(0, DELTA_1ST))
                    .plusPoint(1, new Vector2d(0, DELTA_1ST))
                    .addSpatialMarker(0, () -> {
                        drive.setForce(FORCE);
                        scorerDifferential.liftAfterScoreSpecimen();
                        scorerClaw.open();
                    })
                    .addSpatialMarker(INITIAL, () -> {
                        scorerPerkid.pickSpecimen();
                        scorerDifferential.pickSpecimen();
                        scorerLinkage.retract();
                    })
                    .addSpatialMarker(INITIAL + INITIAL_OFFSET, () -> {
                        lift.setReference(0);
                        hand.dropOverRightSide();
                        diff.dropOverRightSide();
                        perkid.dropOverRightSide();
                    })
                    .addSpatialMarker(0.9, () -> {
                        claw.open();
                    })
                    // AT OBSERVATION ZONE
                    // MIDDLE SAMPLE -----------------------------
                    .waitForAt(1, WAIT_MIDDLE_PICKUP, -90)
                    .addTimeMarker(0, true, () -> {
                        extend.setReference(EXTEND_MIDDLE_SAMPLE);
                        hand.setPosition(HAND_MIDDLE);
                        double rotation = hand.getAngle();
                        diff.aimAngle(rotation);
                        perkid.aimAtSpike();
                        claw.close();
                    })
                    .addTimeMarker(200, true, () -> {
                        claw.open();
                    })
                    .addTimeMarker(WAIT_FOR_INTAKE, true, () -> {
                        perkid.pickUp();
                    })
                    .addTimeMarker(100, true, () -> {
                        double rotation = hand.getAngle();
                        diff.pickUpAngle(rotation);
                    })
                    .addTimeMarker(100, true, () -> {
                        claw.close();
                    })
                    .addTimeMarker(150, true, () -> {
                        diff.aim();
                        perkid.liftSample();
                    })
                    .waitForAt(1, WAIT_MIDDLE_HUMAN)
                    .addTimeMarker(0, true, () -> {
                        perkid.dropOverRightSide();
                        hand.dropOverRightSide();
                        diff.dropOverRightSide();
                    })
                    .addTimeMarker(WAIT_START_RETRACT, true, () -> {
                        extend.setReference(0);
                    })
                    .addTimeMarker(WAIT_RELEASE_SAMPLE, true, () -> {
                        claw.open();
                    })
                    // LEFT SAMPLE -------------------------------
                    .waitForAt(1, WAIT_LEFT_PICKUP, ANGLE_LEFT)
                    .addTimeMarker(0, true, () -> {
                        extend.setReference(EXTEND_LEFT_SAMPLE);
                        hand.setPosition(HAND_LEFT);
                        double rotation = ANGLE_LEFT + 90 + hand.getAngle();
                        diff.aimAngle(rotation);
                        perkid.aimAtSpike();
                        claw.close();
                    })
                    .addTimeMarker(200, true, () -> {
                        claw.open();
                    })
                    .addTimeMarker(WAIT_FOR_INTAKE, true, () -> {
                        perkid.pickUp();
                    })
                    .addTimeMarker(100, true, () -> {
                        double rotation = ANGLE_LEFT + 90 + hand.getAngle();
                        diff.pickUpAngle(rotation);
                    })
                    .addTimeMarker(100, true, () -> {
                        claw.close();
                    })
                    .addTimeMarker(150, true, () -> {
                        diff.aim();
                        perkid.liftSample();
                    })
                    .waitForAt(1, WAIT_LEFT_HUMAN)
                    .addTimeMarker(0, true, () -> {
                        perkid.dropOverRightSide();
                        hand.dropOverRightSide();
                        diff.dropOverRightSide();
                    })
                    .addTimeMarker(WAIT_START_RETRACT, true, () -> {
                        extend.setReference(0);
                    })
                    .addTimeMarker(WAIT_RELEASE_SAMPLE, true, () -> {
                        claw.open();
                    })
                    // RIGHT SAMPLE --------------------------------
                    .waitForAt(1, WAIT_RIGHT_PICKUP, ANGLE_RIGHT)
                    .addTimeMarker(0, true, () -> {
                        extend.setReference(EXTEND_RIGHT_SAMPLE);
                        hand.setPosition(HAND_RIGHT);
                        double rotation = ANGLE_RIGHT + 90 + hand.getAngle();
                        diff.aimAngle(rotation);
                        perkid.aimAtSpike();
                        claw.close();
                    })
                    .addTimeMarker(200, true, () -> {
                        claw.open();
                    })
                    .addTimeMarker(WAIT_FOR_INTAKE, true, () -> {
                        perkid.pickUp();
                    })
                    .addTimeMarker(100, true, () -> {
                        double rotation = ANGLE_RIGHT + 90 + hand.getAngle();
                        diff.pickUp(rotation);
                    })
                    .addTimeMarker(100, true, () -> {
                        claw.close();
                    })
                    .addTimeMarker(150, true, () -> {
                        diff.aim();
                        perkid.liftSample();
                    })
                    .waitForAt(1, WAIT_RIGHT_HUMAN)
                    .addTimeMarker(0, true, () -> {
                        perkid.dropOverRightSide();
                        hand.dropOverRightSide();
                        diff.dropOverRightSide();
                    })
                    .addTimeMarker(WAIT_START_RETRACT, true, () -> {
                        extend.setReference(0);
                    })
                    .addTimeMarker(WAIT_RELEASE_SAMPLE, true, () -> {
                        claw.open();
                        DEBUG_END_FLAG = true;
                    })
                    // GO TO HUMAN
                    .addTurnMarker(1, -90)
                    .addSpatialMarker(1 + POINT_RIGHT_SIDE_DROP, () -> {
                        extend.setReference(EXTEND_TEMP_CLOSED);
                        hand.dropOverRightSide();
                        perkid.dropOverRightSide();
                        diff.dropOverRightSide();
                        scorerPerkid.pushIntoSpecimen();
                        scorerDifferential.pickSpecimen();
                        scorerClaw.open();
                    })
                    .addSpatialMarker(1 + SLOWDOWN_POINT_HUMAN, () -> {
                        drive.setForce(SLOWDOWN_FORCE);
                    })
                    .addSpatialMarker(1 + POINT_RETRACT_EXTEND, () -> {
                        extend.setReference(0);
                    })
                    .waitForAt(2, WAIT_FOR_HUMAN_ADJUST + 100, -90)
                    .addTimeMarker(WAIT_FOR_CLAW_RELEASE, () -> {
                        claw.open();
                        perkid.relax();
                        hand.relax();
                    })
                    .build();
            buildSpecimenToRung = drive.buildTrajectory("traj13transposed.json")
                    .waitForAt(0, WAIT_AFTER_PICK_UP, -90)
                    .addSpatialMarker(0, () -> {
                        scorerDifferential.scoreSpecimen();
                    })
                    .addTurnMarker(0, -90)
                    .addSpatialMarker(POINT_PERKID_START_0, () -> {
                        scorerPerkid.scoreSpecimen();
                        lift.setSpecimen();
                        scorerLinkage.extend();
                    })
                    .addSpatialMarker(SLOWDOWN_POINT_RUNG_1, () -> {
                        drive.setForce(SLOWDOWN_FORCE);
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
                        drive.setForce(FORCE);
                        scorerClaw.open();
                        scorerDifferential.liftAfterScoreSpecimen();
                    })
                    .addSpatialMarker(INITIAL, () -> {
                        scorerPerkid.pushIntoSpecimen();
                        scorerDifferential.pickSpecimen();
                        scorerClaw.open();
                        scorerLinkage.retract();
                    })
                    .addSpatialMarker(INITIAL + INITIAL_OFFSET, () -> {
                        lift.setReference(0);
                    })
                    .addSpatialMarker(SLOWDOWN_POINT_HUMAN_1, () -> {
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
                        drive.setForce(SLOWDOWN_FORCE);
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
        scorerDifferential.aimSpecimen();
        scorerPerkid.aimAtSpecimen();
        scorerLinkage.retract();

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

            lift.PIDControl();
            drive.update();
            extend.PIDControl();
        }
        // -31.362

        clock.reset();

        /* *** GO TO HUMAN AND DO EVERYTHING *** */
        drive.followTrajectory(trajToThree);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            packet = new TelemetryPacket();
            packet.put("extend_reference", extend.getReference());
            packet.put("extend_position", extend.getPosition());
            dashboard.sendTelemetryPacket(packet);

            lift.PIDControl();
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
            lift.PIDControl();
            drive.update();
            extend.PIDControl();
        }

        clock.reset();

        drive.followTrajectory(trajRungToSpecimen);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            lift.PIDControl();
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
            lift.PIDControl();
            drive.update();
            extend.PIDControl();
        }

        clock.reset();

        drive.followTrajectory(trajRungToSpecimen3rd);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            lift.PIDControl();
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
            lift.PIDControl();
            drive.update();
            extend.PIDControl();
        }

        clock.reset();

        drive.followTrajectory(trajRungToSpecimen4th);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            lift.PIDControl();
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
            lift.PIDControl();
            drive.update();
            extend.PIDControl();
        }

        clock.reset();

        drive.followTrajectory(trajRungToYellowSample);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            lift.PIDControl();
            drive.update();
            extend.PIDControl();
        }

        // ********************* START YELLOW STUFF **************************************************

        DEBUG_END_FLAG = true;

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
        scorerLinkage = new ScorerLinkage(hardwareMap);
        lift = new Lift(this);

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
