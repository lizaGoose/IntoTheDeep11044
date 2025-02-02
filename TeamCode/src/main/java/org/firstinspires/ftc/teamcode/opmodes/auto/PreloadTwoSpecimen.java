package org.firstinspires.ftc.teamcode.opmodes.auto;


import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.AIM_SPECIMEN_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.AIM_SPECIMEN_1;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.HOLD_POS;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.INITIAL;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.RELAX_CLAW;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.SPECIMEN_ADD_FORCE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.WAIT_SCORE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.PICK_UP_CLAW;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.PICK_UP_SLOWDOWN;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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
import org.firstinspires.ftc.teamcode.drive.Drive;

import java.io.IOException;

@Autonomous
public class PreloadTwoSpecimen extends LinearOpMode {
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

    private static boolean relaxFlag = false;

    private ElapsedTime clock;

    @Override
    public void runOpMode() throws InterruptedException {
        initRobot();

        relaxFlag = false;

        Trajectory trajToRungs, trajPark, trajToSpecimen, trajSpecimenToRung;
        try {
             trajToRungs = drive.buildTrajectory("traj10.json")
                     .addSpatialMarker(AIM_SPECIMEN_0, () -> {
                         scoringPerkid.transferSpecimen();
                         scoringDiff.pickSpecimen();
                         perkid.aimAtSpike();
                         hand.setPosition(0.5);
                         diff.aim();
                     })
                     .addSpatialMarker(AIM_SPECIMEN_1, () -> {
                         scoringDiff.transferSpecimen();
                     })
                    .waitForAt(1, HOLD_POS)
                    .build();
             trajPark = drive.buildTrajectory("traj11.json")
                     .addSpatialMarker(0, () -> {
                         drive.setForce(SPECIMEN_ADD_FORCE);

                         relaxFlag = true;
                         clock.reset();
                     })
                     .addSpatialMarker(INITIAL, () -> {
                        scoringPerkid.pickSpecimen();
                        scoringDiff.pickSpecimen();
                        scorClaw.open();

                        hand.setPosition(0.5);
                        perkid.transfer();
                        diff.transfer();
                        claw.open();
                     })
                     .waitForAt(1, HOLD_POS)
                     .build();
            trajToSpecimen = drive.buildTrajectory("traj12.json")
                    .addSpatialMarker(0, () -> {
                        drive.setForce(PICK_UP_SLOWDOWN);
                    })
                    .build();
            trajSpecimenToRung = drive.buildTrajectory("traj13.json")
                    .addSpatialMarker(AIM_SPECIMEN_0, () -> {
                        scoringPerkid.transferSpecimen();
                        scoringDiff.pickSpecimen();
                        scorClaw.close();

                        perkid.aimAtSpike();
                        hand.setPosition(0.5);
                        diff.aim();
                    })
                    .addSpatialMarker(AIM_SPECIMEN_1, () -> {
                        scoringDiff.transferSpecimen();
                    })
                    .waitForAt(1, HOLD_POS + 100)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        drive.setPoseEstimate(new Pose2d(trajToRungs.get(0).point0, 0));

        scorClaw.close();

        waitForStart();


        drive.followTrajectory(trajToRungs);

        while (drive.isBusy() && opModeIsActive()) {
            drive.update();
            extend.PIDControl();
        }

        hangSpecimen();

        drive.followTrajectory(trajPark);

        while (drive.isBusy() && opModeIsActive()) {
            if (relaxFlag && clock.milliseconds() > RELAX_CLAW) {
                scorClaw.relax();
                drive.resetForce();
            }

            drive.update();
            extend.PIDControl();
        }

        drive.setPoseEstimate(new Pose2d(trajToSpecimen.get(0).point0, 0));

        boolean pickedUp = false;

        while (opModeIsActive()) {
            if (distInClaw.getDistance(DistanceUnit.MM) <= 17) {
                pickedUp = true;
                break;
            }
            drive.setPowerByPoseField(new Pose2d(-PICK_UP_SLOWDOWN, 0, 0));
            drive.update();
            extend.PIDControl();
        }

        drive.setPowerByPoseField(new Pose2d(0, 0, 0));

        if (!pickedUp) {
            return;
        }

        clock.reset();
        while (clock.milliseconds() < PICK_UP_CLAW) {
            scorClaw.close();
        }
        relaxFlag = false;

        drive.followTrajectory(trajSpecimenToRung);

        while (drive.isBusy() && opModeIsActive()) {
            drive.update();
            extend.PIDControl();
        }

        hangSpecimen();

        drive.followTrajectory(trajPark);

        while (drive.isBusy() && opModeIsActive()) {
            if (relaxFlag && clock.milliseconds() > RELAX_CLAW) {
                scorClaw.relax();
                drive.resetForce();
            }

            drive.update();
            extend.PIDControl();
        }

    }

    private void hangSpecimen() {
        scoringPerkid.scoreSpecimen();
        scoringDiff.scoreSpecimen();

        clock.reset();

        while (clock.milliseconds() < WAIT_SCORE) {
            telemetry.update();
        }
    }

    private void initRobot() {
        drive = new Drive(hardwareMap);
        drive.resetForce();

        scoringPerkid = new ScoringPerkid(this);
        scoringDiff = new ScoringDiff(this);
        scorClaw = new ScorClaw(this);

        perkid = new Perkid(this);
        hand = new Hand(this);
        diff = new Differential(this);
        claw = new Claw(this);

        extend = new Extend(this);
        distInClaw = hardwareMap.get(DistanceSensor.class, "ScorClawDist");

        clock = new ElapsedTime();
    }
}
