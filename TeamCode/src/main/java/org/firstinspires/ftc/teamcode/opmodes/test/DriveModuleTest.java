package org.firstinspires.ftc.teamcode.opmodes.test;

import static org.firstinspires.ftc.teamcode.opmodes.test.DriveModuleTest.TeleOpAutomaticScoreConstants.POINT_PERKID_START_0;
import static org.firstinspires.ftc.teamcode.opmodes.test.DriveModuleTest.TeleOpAutomaticScoreConstants.POINT_PERKID_START_1;
import static org.firstinspires.ftc.teamcode.opmodes.test.DriveModuleTest.TeleOpAutomaticScoreConstants.POINT_SPECIMEN_START_SCORE_0;
import static org.firstinspires.ftc.teamcode.opmodes.test.DriveModuleTest.TeleOpAutomaticScoreConstants.SLOWDOWN_FORCE;
import static org.firstinspires.ftc.teamcode.opmodes.test.DriveModuleTest.TeleOpAutomaticScoreConstants.SLOWDOWN_POINT_1;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Differential;
import org.firstinspires.ftc.teamcode.modules.DriveModule;
import org.firstinspires.ftc.teamcode.modules.Linkage;
import org.firstinspires.ftc.teamcode.modules.Hand;
import org.firstinspires.ftc.teamcode.modules.Perkid;
import org.firstinspires.ftc.teamcode.modules.ScorerClaw;
import org.firstinspires.ftc.teamcode.modules.ScorerDifferential;
import org.firstinspires.ftc.teamcode.modules.ScorerPerkid;
import org.firstinspires.ftc.teamcode.auto.Trajectory;
import org.firstinspires.ftc.teamcode.auto.TrajectoryBuilder;

import java.io.IOException;

@Config
@TeleOp(group = "test")
public class DriveModuleTest extends LinearOpMode {
    public static String trajectory = "";
    public static int button = 0;

    private DriveModule driveModule;
    private ScorerDifferential scorerDifferential;
    private ScorerPerkid scorerPerkid;
    private ScorerClaw scorerClaw;
    private Perkid perkid;
    private Hand hand;
    private Claw claw;
    private Differential diff;

    public static class TeleOpAutomaticScoreConstants {
        public static double POINT_PERKID_START_0 = 0.2;
        public static double POINT_PERKID_START_1 = 0.5;
        public static double SLOWDOWN_POINT_1 = 0.65;
        public static double POINT_SPECIMEN_START_SCORE_0 = 0.91;

        public static double SLOWDOWN_FORCE = 0.15;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        DriveModule driveModule = new DriveModule(hardwareMap);
        scorerDifferential = new ScorerDifferential(this);
        Linkage linkage = new Linkage(this);
        scorerClaw = new ScorerClaw(this);
        perkid = new Perkid(this);
        TrajectoryBuilder builder;
        try {
            builder = driveModule.drive.buildTrajectory(trajectory);
            buildOneCycle(builder, 0);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Trajectory traj = builder.build();
        waitForStart();

        while (opModeIsActive()) {
            if (button > 0) {
                driveModule.followTrajectory(traj);
            }
            driveModule.update(gamepad1);
            linkage.teleop();
        }
    }

    public void buildOneCycle(TrajectoryBuilder builder, double t) {
        builder.addSpatialMarker(0, () -> {
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
                .addSpatialMarker(POINT_PERKID_START_1, () -> {
                    scorerDifferential.transferSpecimen();
                })
                .addSpatialMarker(SLOWDOWN_POINT_1, () -> {
                    driveModule.drive.setForce(SLOWDOWN_FORCE);
                })
                .addSpatialMarker(POINT_SPECIMEN_START_SCORE_0, () -> {
                    scorerPerkid.scoreSpecimen();
                    scorerDifferential.scoreSpecimen();
                })
                .waitForAt(1, 200, -90);
    }
}
