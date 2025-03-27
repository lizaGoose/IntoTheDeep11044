package org.firstinspires.ftc.teamcode.opmodes.auto;

import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.INITIAL;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.INITIAL_OFFSET;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.POINT_MOVE_INTAKE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.POINT_MOVE_PERKID_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.WAIT_START;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DELTA_1ST;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DELTA_2ND;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DELTA_3RD;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DELTA_4TH;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DELTA_5TH;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.FORCE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.POINT_PERKID_START_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.SLOWDOWN_FORCE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.SLOWDOWN_POINT_RUNG_1;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_AFTER_PICK_UP;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_FOR_CLAW_RELEASE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_FOR_HUMAN_ADJUST;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_HOLD_POS_AT_RUNG;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_SPECIMEN_PICK_UP;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.ANGLE_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.ANGLE_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.DELTA_6TH;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.DIFF_ROTATION;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.EXTEND_LEFT_SAMPLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.EXTEND_MIDDLE_SAMPLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.EXTEND_RIGHT_SAMPLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.EXTEND_TEMP_CLOSED;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.HAND_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.HAND_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.HAND_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.LINKAGE_OFFSET;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.POINT_OPEN_CLAW_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.POINT_RETRACT_EXTEND;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.POINT_RIGHT_SIDE_DROP;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.SLOWDOWN_POINT_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.SLOWDOWN_POINT_HUMAN_1;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.SLOWDOWN_POINT_HUMAN_SIX;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.SLOWDOWN_POINT_RUNG_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.WAIT_AFTER_STOP_FOR_SUB_SAMPLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.WAIT_FOR_DETECTION;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.WAIT_FOR_INTAKE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.WAIT_FOR_LINKAGE_EXTEND;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.WAIT_LEFT_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.WAIT_LEFT_PICKUP;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.WAIT_MIDDLE_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.WAIT_MIDDLE_PICKUP;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.WAIT_RELEASE_SAMPLE_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.WAIT_RELEASE_SAMPLE_1;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.WAIT_RELEASE_SAMPLE_2;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.WAIT_RIGHT_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.WAIT_RIGHT_PICKUP;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.WAIT_START_RETRACT_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.WAIT_START_RETRACT_1;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawSeven.FloydThreeClawConstants.WAIT_START_RETRACT_2;

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

import org.firstinspires.ftc.teamcode.auto.Drive;
import org.firstinspires.ftc.teamcode.auto.Trajectory;
import org.firstinspires.ftc.teamcode.auto.TrajectoryBuilder;
import org.firstinspires.ftc.teamcode.camera.AnalyzedSample;
import org.firstinspires.ftc.teamcode.camera.SampleColor;
import org.firstinspires.ftc.teamcode.math.ClawCalculation;
import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Differential;
import org.firstinspires.ftc.teamcode.modules.Hand;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Linkage;
import org.firstinspires.ftc.teamcode.modules.Perkid;
import org.firstinspires.ftc.teamcode.modules.ScorerClaw;
import org.firstinspires.ftc.teamcode.modules.ScorerDifferential;
import org.firstinspires.ftc.teamcode.modules.ScorerLinkage;
import org.firstinspires.ftc.teamcode.modules.ScorerPerkid;

import java.io.IOException;
import java.util.List;

@Autonomous(name = "Floyd Warshall 7+0")
public class ThreeClawSeven extends LinearOpMode {
    @Config
    public static class FloydThreeClawConstants {
        public static double DELTA_6TH = -1.1;

        public static double WAIT_FOR_INTAKE = 500;

        public static double EXTEND_TEMP_CLOSED = 100;

        public static double POINT_RIGHT_SIDE_DROP = 0.3;
        public static double POINT_OPEN_CLAW_RIGHT = 0.9;

        public static double SLOWDOWN_POINT_RUNG_0 = 0.82;
        public static double SLOWDOWN_POINT_HUMAN = 0.65;
        public static double SLOWDOWN_POINT_HUMAN_1 = 0.9;
        public static double SLOWDOWN_POINT_HUMAN_SIX = 0.9;
        public static double POINT_RETRACT_EXTEND = 0.8;

        public static double WAIT_PICK_UP_SUM = 900;
        public static double WAIT_FOR_DETECTION = 200;
        public static double WAIT_AFTER_STOP_FOR_SUB_SAMPLE = 300;
        public static double WAIT_FOR_LINKAGE_EXTEND = 200;

        public static double LINKAGE_OFFSET = 60;

        public static double WAIT_START_RETRACT_0 = 400;
        public static double WAIT_RELEASE_SAMPLE_0 = 90;
        public static double WAIT_START_RETRACT_1 = 200;
        public static double WAIT_RELEASE_SAMPLE_1 = 90;
        public static double WAIT_START_RETRACT_2 = 200;
        public static double WAIT_RELEASE_SAMPLE_2 = 90;

        public static double EXTEND_MIDDLE_SAMPLE = 180;
        public static double WAIT_MIDDLE_PICKUP = 500 + 150 + 650;
        // ANGLE MIDDLE 0
        public static double WAIT_MIDDLE_HUMAN = 500;
        public static double HAND_MIDDLE = 0.5;

        public static double EXTEND_LEFT_SAMPLE = 200;
        public static double WAIT_LEFT_PICKUP = 500 + 150 + 650;
        public static double ANGLE_LEFT = -70;
        public static double WAIT_LEFT_HUMAN = 500;
        public static double HAND_LEFT = 0.5;

        public static double DIFF_ROTATION = 20;

        public static double EXTEND_RIGHT_SAMPLE = 240;
        public static double WAIT_RIGHT_PICKUP = 500 + 150 + 650;
        public static double ANGLE_RIGHT = -120;
        public static double WAIT_RIGHT_HUMAN = 500;
        public static double HAND_RIGHT = 0.25;
    }

    private static final double X = 25.3 - 83.76641;
    private static final double X_PICK = X - 60;
    private static final double X_LIFT = X + 30;
    private static final double Y = 0;
    private static final double Z = 127.45;

    private Drive drive;
    private ScorerPerkid scorerPerkid;
    private ScorerDifferential scorerDifferential;
    private ScorerClaw scorerClaw;

    private Perkid perkid;
    private Hand hand;
    private Differential diff;
    private Claw claw;
    private Linkage linkage;
    private Lift lift;
    private ScorerLinkage scorerLinkage;
//    private AutoCamera camera;
    /**
     * @noinspection FieldCanBeLocal
     */
    private DistanceSensor distInClaw;
    private DigitalChannel frontSensor;
    private FtcDashboard dashboard;
    /**
     * @noinspection SuspiciousNameCombination
     */
    private AnalyzedSample sample = new AnalyzedSample(Y, Z, 0, SampleColor.RED, 1);

    // Make this true somewhere to stop program
    private boolean DEBUG_END_FLAG = false;
    private boolean DETECTED = false;

    private ElapsedTime clock;

    /**
     * @noinspection SuspiciousNameCombination, CodeBlock2Expr
     */
    @Override
    public void runOpMode() throws InterruptedException {
        initRobot();

        TrajectoryBuilder buildSpecimenToRung, buildRungToSpecimen;
        Trajectory trajFirstToRung, trajRungToHuman;
        Trajectory trajSecondToRung, trajRungToThreeToThird;
        Trajectory trajThirdToRung, trajRungToFourth;
        Trajectory trajFourthToRung, trajRungToFifth;
        Trajectory trajFifthToRung, trajRungToSixth;
        Trajectory trajSixthToRung, trajRungToPark;
        try {
            trajFirstToRung = drive.buildTrajectory("first_to_rung6.json")
                    .addTurnMarker(0, -90)
                    .addSpatialMarker(POINT_MOVE_PERKID_0, () -> {
                        scorerPerkid.scoreSpecimen();
                        scorerDifferential.scoreSpecimen();
                        lift.setSpecimen();
                        scorerLinkage.extend();
//                        camera.startCamera();
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
                    .waitForAt(1, WAIT_FOR_DETECTION + WAIT_AFTER_STOP_FOR_SUB_SAMPLE +
                            WAIT_FOR_LINKAGE_EXTEND + 100 + 150 + 100, -90)
                    .addTimeMarker(WAIT_FOR_DETECTION, true, () -> {
//                        camera.getOptimalSamplePosition(lift.getPosition(), scorerPerkid.getPos())
//                                .ifPresent(analyzedSample -> {
//                                    DETECTED = true;
//                                    sample = analyzedSample;
//                                });
                    })
                    .addTimeMarker(WAIT_AFTER_STOP_FOR_SUB_SAMPLE, true, () -> {
                        List<Double> result = ClawCalculation.inverseKinematics(X, sample.x, sample.y, sample.angle);
                        if (!result.contains(Double.NaN)) {
                            linkage.setPositionMillimeters(result.get(0) - LINKAGE_OFFSET);
                            perkid.setAngle(result.get(1));
                            hand.setAngle(result.get(2));
                            diff.setAngleRotation(-91, result.get(4));
                        }
                    })
                    .addTimeMarker(WAIT_FOR_LINKAGE_EXTEND, true, () -> {
                        List<Double> result = ClawCalculation.inverseKinematics(X_PICK, sample.x, sample.y, sample.angle);
                        if (!result.contains(Double.NaN)) {
                            linkage.setPositionMillimeters(result.get(0) - LINKAGE_OFFSET);
                            perkid.setAngle(result.get(1));
                            hand.setAngle(result.get(2));
                            diff.setAngleRotation(-88, result.get(4));
                        }
                    })
                    .addTimeMarker(100, true, () -> {
                        claw.close();
                    })
                    .addTimeMarker(150, true, () -> {
                        List<Double> result = ClawCalculation.inverseKinematics(X_LIFT, Y, Z, 0);
                        if (!result.contains(Double.NaN)) {
                            linkage.setPositionMillimeters(result.get(0));
                            perkid.setAngle(result.get(1));
                            hand.setAngle(result.get(2));
                            diff.setAngleRotation(-91, result.get(4));
                        }
//                        camera.stopCamera();
                    })
                    .plusPoint(2, new Vector2d(0, DELTA_1ST))
                    .plusPoint(3, new Vector2d(0, DELTA_1ST))
                    .build();
            trajRungToHuman = drive.buildTrajectory("rung_to_observ.json")
                    .addTurnMarker(0, -90)
                    .plusPoint(0, new Vector2d(0, DELTA_1ST))
                    .plusPoint(1, new Vector2d(0, DELTA_1ST))
                    .addSpatialMarker(0, () -> {
                        drive.setForce(FORCE);
                        scorerClaw.open();
                        scorerDifferential.liftAfterScoreSpecimen();
                    })
                    .addSpatialMarker(INITIAL, () -> {
                        scorerPerkid.pushIntoSpecimen();
                        scorerDifferential.pushIntoSpecimen();
                        scorerLinkage.retract();
                    })
                    .addSpatialMarker(INITIAL + INITIAL_OFFSET, () -> {
                        lift.setReference(0);
                        hand.dropOverRightSide();
                        diff.dropOverRightSide();
                        perkid.dropOverRightSide();
                    })
                    .addSpatialMarker(POINT_OPEN_CLAW_RIGHT, () -> {
                        claw.open();
                    })
                    .addSpatialMarker(SLOWDOWN_POINT_HUMAN_1, () -> {
                        drive.setForce(SLOWDOWN_FORCE);
                    })
                    .waitForAt(1, WAIT_FOR_HUMAN_ADJUST + 100, -90)
                    .build();
            trajRungToThreeToThird = drive.buildTrajectory("auto_3_samp_claw.json")
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
                        linkage.setReference(EXTEND_MIDDLE_SAMPLE);
                        hand.setPosition(HAND_MIDDLE);
                        double rotation = hand.getAngle();
                        diff.aimAngle(rotation);
                        perkid.aimAtSpike();
                        claw.close();
                    })
                    .addTimeMarker(200, true, () -> {
                        claw.open();

                        scorerPerkid.scoreSample();
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
                        perkid.transfer();
                        hand.setPosition(0.5);
                        diff.transfer();
                    })
                    .addTimeMarker(WAIT_START_RETRACT_0, true, () -> {
                        linkage.setReference(0);
                    })
                    .addTimeMarker(WAIT_RELEASE_SAMPLE_0, true, () -> {
                        claw.open();
                    })
                    // LEFT SAMPLE -------------------------------
                    .waitForAt(1, WAIT_LEFT_PICKUP, ANGLE_LEFT)
                    .addTimeMarker(0, true, () -> {
                        linkage.setReference(EXTEND_LEFT_SAMPLE);
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
                    .addTimeMarker(WAIT_START_RETRACT_1, true, () -> {
                        linkage.setReference(0);
                    })
                    .addTimeMarker(WAIT_RELEASE_SAMPLE_1, true, () -> {
                        claw.open();
                    })
                    // RIGHT SAMPLE --------------------------------
                    .waitForAt(1, WAIT_RIGHT_PICKUP, ANGLE_RIGHT)
                    .addTimeMarker(0, true, () -> {
                        linkage.setReference(EXTEND_RIGHT_SAMPLE);
                        hand.setPosition(HAND_RIGHT);
                        double rotation = ANGLE_RIGHT + 90 + hand.getAngle() + DIFF_ROTATION;
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
                        double rotation = ANGLE_RIGHT + 90 + hand.getAngle() + DIFF_ROTATION;
                        diff.pickUpAngle(rotation);
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
                    .addTimeMarker(WAIT_START_RETRACT_2, true, () -> {
                        linkage.setReference(0);
                    })
                    .addTimeMarker(WAIT_RELEASE_SAMPLE_2, true, () -> {
                        claw.open();
                    })
                    // GO TO HUMAN
                    .addTurnMarker(1, -90)
                    .addSpatialMarker(1 + POINT_RIGHT_SIDE_DROP, () -> {
                        linkage.setReference(EXTEND_TEMP_CLOSED);
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
                        linkage.setReference(0);
                    })
                    .waitForAt(2, WAIT_FOR_HUMAN_ADJUST + 100, -90)
                    .addTimeMarker(WAIT_FOR_CLAW_RELEASE, () -> {
                        claw.open();
                        perkid.relax();
                        hand.relax();
                    })
                    .build();
            buildSpecimenToRung = drive.buildTrajectory("observ_to_rung.json")
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
            trajSecondToRung = buildSpecimenToRung
                    .plusPoint(3, new Vector2d(DELTA_2ND, 0))
                    .plusPoint(2, new Vector2d(DELTA_2ND, 0))
                    .build();
            trajThirdToRung = buildSpecimenToRung
                    .plusPoint(3, new Vector2d(DELTA_3RD, 0))
                    .plusPoint(2, new Vector2d(DELTA_3RD, 0))
                    .build();
            trajFourthToRung = buildSpecimenToRung
                    .plusPoint(3, new Vector2d(DELTA_4TH, 0))
                    .plusPoint(2, new Vector2d(DELTA_4TH, 0))
                    .build();
            trajFifthToRung = buildSpecimenToRung
                    .plusPoint(3, new Vector2d(DELTA_5TH, 0))
                    .plusPoint(2, new Vector2d(DELTA_5TH, 0))
                    .build();
            trajSixthToRung = buildSpecimenToRung
                    .plusPoint(3, new Vector2d(DELTA_6TH, 0))
                    .plusPoint(2, new Vector2d(DELTA_6TH, 0))
                    .build();
            buildRungToSpecimen = drive.buildTrajectory("rung_to_observ.json")
                    .addTurnMarker(0, -90)
                    .addSpatialMarker(0, () -> {
                        drive.setForce(FORCE);
                        scorerClaw.open();
                        scorerDifferential.liftAfterScoreSpecimen();
                    })
                    .addSpatialMarker(INITIAL, () -> {
                        scorerPerkid.pushIntoSpecimen();
                        scorerDifferential.pushIntoSpecimen();
                        scorerClaw.open();
                        scorerLinkage.retract();
                    })
                    .addSpatialMarker(INITIAL + INITIAL_OFFSET, () -> {
                        lift.setReference(0);
                    })
                    .addSpatialMarker(SLOWDOWN_POINT_HUMAN_SIX, () -> {
                        drive.setForce(SLOWDOWN_FORCE);
                    })
                    .waitForAt(1, WAIT_FOR_HUMAN_ADJUST + 100, -90);
            trajRungToFourth = buildRungToSpecimen
                    .plusPoint(0, new Vector2d(DELTA_3RD, 0))
                    .plusPoint(1, new Vector2d(DELTA_3RD, 0))
                    .build();
            trajRungToFifth = buildRungToSpecimen
                    .plusPoint(0, new Vector2d(DELTA_4TH, 0))
                    .plusPoint(1, new Vector2d(DELTA_4TH, 0))
                    .build();
            trajRungToSixth = buildRungToSpecimen
                    .plusPoint(0, new Vector2d(DELTA_5TH, 0))
                    .plusPoint(1, new Vector2d(DELTA_5TH, 0))
                    .build();
            trajRungToPark = buildRungToSpecimen
                    .plusPoint(0, new Vector2d(DELTA_6TH, 0))
                    .plusPoint(1, new Vector2d(DELTA_6TH, 0))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        drive.setPoseEstimate(new Pose2d(trajFirstToRung.get(0).point0, Math.toRadians(270)));
        drive.setForce(FORCE);

        scorerClaw.close();
        scorerDifferential.aimSpecimen();
        scorerPerkid.aimAtSpecimen();
        scorerLinkage.retract();

        TelemetryPacket packet = new TelemetryPacket();
        packet.put("extend_reference", linkage.getReference());
        packet.put("extend_position", linkage.getPosition());
        dashboard.sendTelemetryPacket(packet);

        waitForStart();


        // ********************* WAIT **************************************************
        clock.reset();

        while (clock.seconds() < WAIT_START) {
            telemetry.update();
        }

        // ********************* START 1TH SPECIMEN CYCLE **************************************************

        drive.followTrajectory(trajFirstToRung);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {

            packet = new TelemetryPacket();
            packet.put("extend_reference", linkage.getReference());
            packet.put("extend_position", linkage.getPosition());
            packet.put("sample", sample);
            dashboard.sendTelemetryPacket(packet);

            telemetry.addData("DETECTED", DETECTED);
            telemetry.addData("sample", sample);
            telemetry.update();

            lift.PIDControl();
            drive.update();
            linkage.PIDControl();
        }

        clock.reset();

        drive.followTrajectory(trajRungToHuman);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            lift.PIDControl();
            drive.update();
            linkage.PIDControl();
        }

        // ********************* START 2TH SPECIMEN CYCLE AND THREE SAMPLES **************************************************

        scorerClaw.close();

        drive.setForce(FORCE);

        drive.followTrajectory(trajSecondToRung);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            lift.PIDControl();
            drive.update();
            linkage.PIDControl();
        }

        /* *** GO TO HUMAN AND DO EVERYTHING *** */
        drive.followTrajectory(trajRungToThreeToThird);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            packet = new TelemetryPacket();
            packet.put("extend_reference", linkage.getReference());
            packet.put("extend_position", linkage.getPosition());
            dashboard.sendTelemetryPacket(packet);

            lift.PIDControl();
            drive.update();
            linkage.PIDControl();
        }

        /* ***************** START 3ND SPECIMEN CYCLE ***************** */

        clock.reset();
        while (clock.milliseconds() < WAIT_SPECIMEN_PICK_UP && !DEBUG_END_FLAG) {
            telemetry.addLine("Waiting!");
            telemetry.update();
        }

        scorerClaw.close();

        drive.setForce(FORCE);

        drive.followTrajectory(trajThirdToRung);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            lift.PIDControl();
            drive.update();
            linkage.PIDControl();
        }

        clock.reset();

        drive.followTrajectory(trajRungToFourth);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            lift.PIDControl();
            drive.update();
            linkage.PIDControl();
        }

        // ********************* START 4ND SPECIMEN CYCLE **************************************************

        clock.reset();
        while (clock.milliseconds() < WAIT_SPECIMEN_PICK_UP && !DEBUG_END_FLAG) {
            telemetry.addLine("Waiting!");
            telemetry.update();
        }

        scorerClaw.close();

        drive.setForce(FORCE);

        drive.followTrajectory(trajFourthToRung);


        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            lift.PIDControl();
            drive.update();
            linkage.PIDControl();
        }

        clock.reset();

        drive.followTrajectory(trajRungToFifth);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            lift.PIDControl();
            drive.update();
            linkage.PIDControl();
        }

        // ********************* START 5TH SPECIMEN CYCLE **************************************************

        clock.reset();
        while (clock.milliseconds() < WAIT_SPECIMEN_PICK_UP && !DEBUG_END_FLAG) {
            telemetry.addLine("Waiting!");
            telemetry.update();
        }

        scorerClaw.close();

        drive.setForce(FORCE);

        drive.followTrajectory(trajFifthToRung);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            lift.PIDControl();
            drive.update();
            linkage.PIDControl();
        }

        clock.reset();

        drive.followTrajectory(trajRungToSixth);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            lift.PIDControl();
            drive.update();
            linkage.PIDControl();
        }

        // ********************* START 6TH SPECIMEN CYCLE **************************************************

        clock.reset();
        while (clock.milliseconds() < WAIT_SPECIMEN_PICK_UP && !DEBUG_END_FLAG) {
            telemetry.addLine("wait");
            telemetry.update();
        }

        scorerClaw.close();

        drive.setForce(FORCE);

        drive.followTrajectory(trajSixthToRung);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            lift.PIDControl();
            drive.update();
            linkage.PIDControl();
        }

        clock.reset();

        drive.followTrajectory(trajRungToPark);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            lift.PIDControl();
            drive.update();
            linkage.PIDControl();
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

//        camera = new AutoCamera(this);
//
//        camera.operatorControlRed();

        linkage = new Linkage(this);
        distInClaw = hardwareMap.get(DistanceSensor.class, "ScorClawDist");
        frontSensor = hardwareMap.get(DigitalChannel.class, "DistanceFront");

        clock = new ElapsedTime();
        dashboard = FtcDashboard.getInstance();
    }
}
