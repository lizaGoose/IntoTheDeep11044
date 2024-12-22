package org.firstinspires.ftc.teamcode.opmodes.auto;


import static org.firstinspires.ftc.teamcode.opmodes.auto.PreloadSpecimenConstants.AIM_SPECIMEN_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.PreloadSpecimenConstants.AIM_SPECIMEN_1;
import static org.firstinspires.ftc.teamcode.opmodes.auto.PreloadSpecimenConstants.HOLD_POS;
import static org.firstinspires.ftc.teamcode.opmodes.auto.PreloadSpecimenConstants.INITIAL;
import static org.firstinspires.ftc.teamcode.opmodes.auto.PreloadSpecimenConstants.RELAX_CLAW;
import static org.firstinspires.ftc.teamcode.opmodes.auto.PreloadSpecimenConstants.WAIT_SCORE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.PreloadTwoSpecimenConstants.PICK_UP_CLAW;
import static org.firstinspires.ftc.teamcode.opmodes.auto.PreloadTwoSpecimenConstants.PICK_UP_SLOWDOWN;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
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
import org.firstinspires.ftc.teamcode.auto.TrajectoryRunner;
import org.firstinspires.ftc.teamcode.drive.Drive;

import java.io.IOException;

@Config
class PreloadTwoSpecimenConstants {
    public static double AIM_SPECIMEN_0 = 0.05;
    public static double AIM_SPECIMEN_1 = 0.7;

    public static int HOLD_POS = 1000;

    public static double WAIT_SCORE = 200;

    public static double RELAX_CLAW = 200;
    public static double INITIAL = 0.4;


    public static double SPECIMEN_ADD_FORCE = 0.6;
    public static double PICK_UP_SLOWDOWN = 0.3;

    public static double PICK_UP_CLAW = 200;

}

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

        Trajectory trajectoryToRungs, trajectoryPark, trajectoryToSpecimen;
        try {
             trajectoryToRungs = drive.buildTrajectory("traj10.json")
                     .addSpatialMarker(AIM_SPECIMEN_0, () -> {
                         scoringPerkid.transferSpecimen();
                         scoringDiff.pickSpecimen();
                         perkid.aim();
                         hand.setPosition(0.5);
                         diff.aim();
                     })
                     .addSpatialMarker(AIM_SPECIMEN_1, () -> {
                         scoringDiff.transferSpecimen();
                     })
                    .waitForAt(1, HOLD_POS)
                    .build();
             trajectoryPark = drive.buildTrajectory("traj11.json")
                     .addSpatialMarker(0, () -> {
                         TrajectoryRunner.force = PreloadTwoSpecimenConstants.SPECIMEN_ADD_FORCE;

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
            trajectoryToSpecimen = drive.buildTrajectory("traj12.json")
                    .addSpatialMarker(0, () -> {
                        TrajectoryRunner.force = PICK_UP_SLOWDOWN;
                    })
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        drive.setPoseEstimate(new Pose2d(trajectoryToRungs.get(0).point0, 0));

        scorClaw.close();

        waitForStart();


        drive.followTrajectory(trajectoryToRungs);

        while (drive.isBusy() && opModeIsActive()) {
            drive.update();
            extend.PIDControl();
        }

        hangSpecimen();

        drive.followTrajectory(trajectoryPark);

        while (drive.isBusy() && opModeIsActive()) {
            if (relaxFlag && clock.milliseconds() > RELAX_CLAW) {
                scorClaw.relax();
                TrajectoryRunner.force = 0.5;
            }

            drive.update();
            extend.PIDControl();
        }

        drive.followTrajectory(trajectoryToSpecimen);

        boolean pickedUp = false;

        while (drive.isBusy() && opModeIsActive()) {
            if (distInClaw.getDistance(DistanceUnit.MM) <= 17) {
                drive.discardTrajectory();
                pickedUp = true;

            }
            drive.update();
            extend.PIDControl();
        }

        if (!pickedUp) {
            return;
        }

        TrajectoryRunner.force = 0.5;
        clock.reset();
        while (clock.milliseconds() < PICK_UP_CLAW) {
            scorClaw.close();
        }
        relaxFlag = false;

        drive.followTrajectory(trajectoryToRungs);

        while (drive.isBusy() && opModeIsActive()) {
            drive.update();
            extend.PIDControl();
        }

        hangSpecimen();

        drive.followTrajectory(trajectoryPark);

        while (drive.isBusy() && opModeIsActive()) {
            if (relaxFlag && clock.milliseconds() > RELAX_CLAW) {
                scorClaw.relax();
                TrajectoryRunner.force = 0.5;
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
