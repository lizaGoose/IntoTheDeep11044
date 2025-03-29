package org.firstinspires.ftc.teamcode.opmodes.auto;


import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.INITIAL;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.INITIAL_OFFSET;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.POINT_MOVE_INTAKE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.POINT_MOVE_PERKID_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.WAIT_START;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DELTA_2ND;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DELTA_3RD;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DELTA_4TH;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DELTA_5TH;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.DIST_SCOR_CLAW;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.FORCE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.POINT_PERKID_START_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.SLOWDOWN_FORCE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.SLOWDOWN_POINT_RUNG_1;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_AFTER_PICK_UP;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_FOR_CLAW_RELEASE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_FOR_HUMAN_ADJUST;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_HOLD_POS_AT_RUNG;
import static org.firstinspires.ftc.teamcode.opmodes.auto.LebronFiveOneConstants.WAIT_SPECIMEN_PICK_UP;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.ANGLE_AT_RIGHT_SAMPLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.DEBUG_WAIT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.DELTA_1ST;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.DELTA_6TH;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.DIFFERENTIAL_ROTATION;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.EXTEND_RIGHT_SAMPLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.EXTEND_TEMP_CLOSED;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.HAND_POSITION;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.LINKAGE_OFFSET;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.POINT_OPEN_CLAW_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.POINT_RETRACT_EXTEND;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.POINT_RIGHT_SIDE_DROP;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.SLOWDOWN_POINT_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.SLOWDOWN_POINT_HUMAN_1;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.SLOWDOWN_POINT_HUMAN_SIX;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.SLOWDOWN_POINT_RUNG_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.WAIT_AFTER_STOP_FOR_SUB_SAMPLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.WAIT_AT_RIGHT_SAMPLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.WAIT_FOR_DETECTION;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.WAIT_FOR_INTAKE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.WAIT_FOR_LINKAGE_EXTEND;
import static org.firstinspires.ftc.teamcode.opmodes.auto.TwoDriveClawSix.FloydWarshallConstants.WAIT_PICK_UP_SUM;

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
import org.firstinspires.ftc.teamcode.camera.AnalyzedSample;
import org.firstinspires.ftc.teamcode.camera.LimelightCamera;
import org.firstinspires.ftc.teamcode.camera.SampleColor;
import org.firstinspires.ftc.teamcode.math.ClawCalculation;
import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Differential;
import org.firstinspires.ftc.teamcode.modules.Linkage;
import org.firstinspires.ftc.teamcode.modules.Hand;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Perkid;
import org.firstinspires.ftc.teamcode.modules.ScorerClaw;
import org.firstinspires.ftc.teamcode.modules.ScorerDifferential;
import org.firstinspires.ftc.teamcode.modules.ScorerLinkage;
import org.firstinspires.ftc.teamcode.modules.ScorerPerkid;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Autonomous(name = "Floyd Warshall Red 6+0")
public class TwoDriveClawSix extends LinearOpMode {
    @Config
    public static class FloydWarshallConstants {
        public static double DELTA_1ST = 0;
        public static double DELTA_6TH = -1.1;

        public static double WAIT_AT_RIGHT_SAMPLE = 500 + 150 + 650;
        public static double ANGLE_AT_RIGHT_SAMPLE = -107.5;
        public static double WAIT_FOR_INTAKE = 500;

        public static double EXTEND_RIGHT_SAMPLE = 165;
        public static double HAND_POSITION = 0.25;
        public static double DIFFERENTIAL_ROTATION = 5;

        public static double EXTEND_TEMP_CLOSED = 100;

        public static double POINT_RIGHT_SIDE_DROP = 0.3;
        public static double POINT_OPEN_CLAW_RIGHT = 0.9;

        public static double SLOWDOWN_POINT_RUNG_0 = 0.72;
        public static double SLOWDOWN_POINT_HUMAN = 0.65;
        public static double SLOWDOWN_POINT_HUMAN_1 = 0.9;
        public static double SLOWDOWN_POINT_HUMAN_SIX = 0.9;
        public static double POINT_RETRACT_EXTEND = 0.8;

        public static double WAIT_PICK_UP_SUM = 2400;
        public static double WAIT_FOR_DETECTION = 200;
        public static double WAIT_AFTER_STOP_FOR_SUB_SAMPLE = 600;
        public static double WAIT_FOR_LINKAGE_EXTEND = 300;

        public static double LINKAGE_OFFSET = 0 ;
        public static double DEBUG_WAIT = 0;
    }

    private static final double X = 25.3 - 83.76641;
    private static final double X_PICK = X - 70;
    private static final double X_LIFT = X + 20;
    private static final double Y = 0;
    private static final double Z = 127.45;
    private static final double SAMPLE_X = -20;
    private static final double SAMPLE_Y = 0;

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
    private LimelightCamera camera;
    private DistanceSensor distInClaw;
    private DigitalChannel frontSensor;
    private FtcDashboard dashboard;
    /** @noinspection SuspiciousNameCombination*/
    private AnalyzedSample sample = new AnalyzedSample(Y, Z, 0, SampleColor.RED);

    // Make this true somewhere to stop program
    private boolean DEBUG_END_FLAG = false;
    private boolean DETECTED = false;

    private ElapsedTime clock;

    /** @noinspection SuspiciousNameCombination*/
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
                    //.waitForAt(1, WAIT_PICK_UP_SUM, -90)
                    .waitForAt(1, WAIT_FOR_DETECTION + WAIT_AFTER_STOP_FOR_SUB_SAMPLE + WAIT_FOR_LINKAGE_EXTEND + 300, -90)
                    .addTimeMarker(WAIT_FOR_DETECTION, true, () -> {
                        camera.getSamplePose(lift.getPosition(), scorerPerkid.getAngle())
                                .ifPresent(analyzedSample -> {
                                    DETECTED = true;
                                    sample = analyzedSample;
                                });
                        camera.takeSnapshot();
                    })
                    .addTimeMarker(WAIT_AFTER_STOP_FOR_SUB_SAMPLE, true, () -> {
                        List<Double> result = ClawCalculation.inverseKinematics(X, sample.x + SAMPLE_X, sample.y + SAMPLE_Y, sample.angle);
                        if (!result.contains(Double.NaN)) {
                            linkage.setPositionMillimeters(result.get(0) - LINKAGE_OFFSET);
                            perkid.setAngle(result.get(1));
                            hand.setAngle(result.get(2));
                            diff.setAngleRotation(-91, result.get(4));
                        }
                    })
                    .addTimeMarker(WAIT_FOR_LINKAGE_EXTEND, true, () -> {
                        List<Double> result = ClawCalculation.inverseKinematics(X_PICK, sample.x + SAMPLE_X, sample.y + SAMPLE_Y, sample.angle);
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
                            //perkid.setAngle(result.get(1));
                            perkid.liftSample();
                            hand.setAngle(result.get(2));
                            diff.setAngleRotation(-91, result.get(4));
                        }
                    })
                    .plusPoint(2, new Vector2d(DELTA_1ST, 0))
                    .plusPoint(3, new Vector2d(DELTA_1ST, 0))
                    .build();
            trajRungToHuman = drive.buildTrajectory("rung_to_observ.json")
                    .addTurnMarker(0, -90)
                    .plusPoint(0, new Vector2d(DELTA_1ST, 0))
                    .plusPoint(1, new Vector2d(DELTA_1ST, 0))
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
            trajRungToThreeToThird = drive.buildTrajectory("auto_2_samp_drive.json")
                    .addTurnMarker(0, -90)
                    .plusPoint(0, new Vector2d(DELTA_2ND, 0))
                    .plusPoint(1, new Vector2d(DELTA_2ND, 0))
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
                    })
                    // GO TO LEFT
                    .addTurnMarker(1, -90)
                    // PUSH LEFT
                    .addTurnMarker(2, -90)
                    // GO TO MIDDLE
                    .addTurnMarker(3, -90)
                    // PUSH MIDDLE
                    .addTurnMarker(4, -90)
                    // GO TO RIGHT
                    .waitForAt(5, WAIT_AT_RIGHT_SAMPLE + 4 * DEBUG_WAIT, ANGLE_AT_RIGHT_SAMPLE)
                    .addTimeMarker(0, () -> {
                        linkage.setReference(EXTEND_RIGHT_SAMPLE);
                        hand.setPosition(HAND_POSITION);
                        double rotation = ANGLE_AT_RIGHT_SAMPLE + hand.getAngle() + 90 + DIFFERENTIAL_ROTATION;
                        diff.aimAngle(rotation);
                        perkid.aimAtSpike();
                        claw.close();
                    })
                    .addTimeMarker(200 + DEBUG_WAIT, true, () -> {
                        claw.open();
                    })
                    .addTimeMarker(WAIT_FOR_INTAKE + DEBUG_WAIT, true, () -> {
                        perkid.pickUp();
                    })
                    .addTimeMarker(100 + DEBUG_WAIT, true, () -> {
                        double rotation = ANGLE_AT_RIGHT_SAMPLE + hand.getAngle() + 90 + DIFFERENTIAL_ROTATION;
                        diff.pickUpAngle(rotation);
                    })
                    .addTimeMarker(150 + DEBUG_WAIT, true, () -> {
                        claw.close();
                    })
                    .addTimeMarker(150 + DEBUG_WAIT, true, () -> {
                        diff.aim();
                        perkid.liftSample();
                    })
                    // GO TO HUMAN
                    .addTurnMarker(5, -90)
                    .addSpatialMarker(5 + POINT_RIGHT_SIDE_DROP, () -> {
                        linkage.setReference(EXTEND_TEMP_CLOSED);
                        hand.dropOverRightSide();
                        perkid.dropOverRightSide();
                        diff.dropOverRightSide();
                        scorerPerkid.pushIntoSpecimen();
                        scorerDifferential.pushIntoSpecimen();
                        scorerClaw.open();
                    })
                    .addSpatialMarker(5 + SLOWDOWN_POINT_HUMAN, () -> {
                        drive.setForce(SLOWDOWN_FORCE);
                    })
                    .addSpatialMarker(5 + POINT_RETRACT_EXTEND, () -> {
                        linkage.setReference(0);
                    })
                    .waitForAt(6, WAIT_FOR_HUMAN_ADJUST + 100, -90)
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

        camera.start();

        while (opModeInInit()) {
            camera.getResult().ifPresent(llResult -> {
                telemetry.addData("pipeline_index", llResult.getPipelineIndex());
                telemetry.addData("latency", llResult.getTargetingLatency());
                telemetry.addData("type", llResult.getPipelineType());
                telemetry.addData("pythonOutput", Arrays.toString(llResult.getPythonOutput()));
            });
        }

        waitForStart();

        camera.setPipeline(LimelightCamera.Pipeline.PYTHON_RED);


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

            camera.getResult().ifPresent(llResult -> {
                telemetry.addData("pipeline_index", llResult.getPipelineIndex());
                telemetry.addData("latency", llResult.getTargetingLatency());
                telemetry.addData("type", llResult.getPipelineType());
                telemetry.addData("pythonOutput", Arrays.toString(Arrays.stream(llResult.getPythonOutput()).limit(8).toArray()));
            });

            camera.getSamplePose(lift.getPosition(), scorerPerkid.getPosition()).ifPresent(sample -> {
                telemetry.addData("sample_X", sample.x);
                telemetry.addData("sample_Y", sample.y);
                telemetry.addData("sample_angle", sample.angle);
            });

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
        boolean pickedUp = false;
        while (clock.milliseconds() < WAIT_SPECIMEN_PICK_UP && !DEBUG_END_FLAG) {
            if (distInClaw.getDistance(DistanceUnit.MM) <= DIST_SCOR_CLAW) {
                pickedUp = true;
            }
        }

        scorerClaw.close();

        drive.setForce(FORCE);

        drive.followTrajectory(trajThirdToRung);

        boolean resetX = true;

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
        pickedUp = false;
        while (clock.milliseconds() < WAIT_SPECIMEN_PICK_UP && !DEBUG_END_FLAG) {
            if (distInClaw.getDistance(DistanceUnit.MM) <= DIST_SCOR_CLAW) {
                pickedUp = true;
            }
        }

        scorerClaw.close();

        drive.setForce(FORCE);

        drive.followTrajectory(trajFourthToRung);

        resetX = true;

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            if (resetX && !frontSensor.getState()) {
                Pose2d poseEstimate = drive.getPoseEstimate();
                //drive.setPoseEstimate(new Pose2d(-31.362, poseEstimate.getY(), poseEstimate.getHeading()));
                resetX = false;
            }
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
        pickedUp = false;
        while (clock.milliseconds() < WAIT_SPECIMEN_PICK_UP && !DEBUG_END_FLAG) {
            if (distInClaw.getDistance(DistanceUnit.MM) <= DIST_SCOR_CLAW) {
                pickedUp = true;
            }
        }

        scorerClaw.close();

        drive.setForce(FORCE);

        drive.followTrajectory(trajFifthToRung);

        resetX = true;

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            if (resetX && !frontSensor.getState()) {
                Pose2d poseEstimate = drive.getPoseEstimate();
                //drive.setPoseEstimate(new Pose2d(-31.362, poseEstimate.getY(), poseEstimate.getHeading()));
                resetX = false;
            }
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
        pickedUp = false;
        while (clock.milliseconds() < WAIT_SPECIMEN_PICK_UP && !DEBUG_END_FLAG) {
            if (distInClaw.getDistance(DistanceUnit.MM) <= DIST_SCOR_CLAW) {
                pickedUp = true;
            }
        }

        scorerClaw.close();

        drive.setForce(FORCE);

        drive.followTrajectory(trajSixthToRung);

        resetX = true;

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            if (resetX && !frontSensor.getState()) {
                Pose2d poseEstimate = drive.getPoseEstimate();
                //drive.setPoseEstimate(new Pose2d(-31.362, poseEstimate.getY(), poseEstimate.getHeading()));
                resetX = false;
            }
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

        camera.stop();
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

        camera = new LimelightCamera(this);

        telemetry.setMsTransmissionInterval(11);

        camera.setPipeline(LimelightCamera.Pipeline.THRESHOLD_BLUE);

        linkage = new Linkage(this);
        distInClaw = hardwareMap.get(DistanceSensor.class, "ScorClawDist");
        frontSensor = hardwareMap.get(DigitalChannel.class, "DistanceFront");

        clock = new ElapsedTime();
        dashboard = FtcDashboard.getInstance();
    }
}
