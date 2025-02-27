package org.firstinspires.ftc.teamcode.opmodes.auto;


import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.POINT_MOVE_PERKID_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.POINT_MOVE_PERKID_1;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.HOLD_POS;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.INITIAL;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.RELAX_CLAW;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.WAIT_SCORE;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.camera.AutoCamera;
import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Differential;
import org.firstinspires.ftc.teamcode.modules.Extend;
import org.firstinspires.ftc.teamcode.modules.Hand;
import org.firstinspires.ftc.teamcode.modules.Perkid;
import org.firstinspires.ftc.teamcode.modules.ScorerClaw;
import org.firstinspires.ftc.teamcode.modules.ScorerDifferential;
import org.firstinspires.ftc.teamcode.modules.ScorerPerkid;
import org.firstinspires.ftc.teamcode.auto.Trajectory;
import org.firstinspires.ftc.teamcode.auto.Drive;

import java.io.IOException;

@Config
class WithCameraConstants {
    public static double AIM_SPECIMEN_0 = 0.05;
    public static double AIM_SPECIMEN_1 = 0.7;

    public static int HOLD_POS = 1000;

    public static double WAIT_SCORE = 200;

    public static double RELAX_CLAW = 200;
    public static double INITIAL = 0.4;

}

@Autonomous
public class WithCamera extends LinearOpMode {
    private Drive drive;
    private ScorerPerkid scorerPerkid;
    private ScorerDifferential scorerDifferential;
    private ScorerClaw scorerClaw;

    private Perkid perkid;
    private Hand hand;
    private Differential diff;
    private Claw claw;
    private Extend extend;
    private static boolean relaxFlag = false;
    private AutoCamera cam;

    private ElapsedTime clock;

    @Override
    public void runOpMode() throws InterruptedException {
        initRobot();

        relaxFlag = false;

        Trajectory trajectoryToRungs, trajectoryPark;
        try {
            trajectoryToRungs = drive.buildTrajectory("traj10.json")
                    .addSpatialMarker(POINT_MOVE_PERKID_0, () -> {
                        scorerPerkid.transferSpecimen();
                        scorerDifferential.pickSpecimen();
                        perkid.aimAtSpike();
                        hand.setPosition(0.5);
                        diff.aim();
                    })
                    .addSpatialMarker(POINT_MOVE_PERKID_1, () -> {
                        scorerDifferential.transferSpecimen();
                    })
                    .waitForAt(1, HOLD_POS)
                    .build();
            trajectoryPark = drive.buildTrajectory("traj14.json")
                    .addSpatialMarker(0, () -> {
                        drive.setForce(0.6);

                        relaxFlag = true;
                        clock.reset();
                    })
                    .addSpatialMarker(INITIAL, () -> {
                        scorerPerkid.pickSpecimen();
                        scorerDifferential.pickSpecimen();
                        scorerClaw.open();

                        hand.setPosition(0.5);
                        perkid.transfer();
                        diff.transfer();
                        claw.open();
                    })
                    .waitForAt(2, 2000)
                    .addSpatialMarker(2, () -> {
                        drive.setForce(0.4);
                    })
                    .waitForAt(3, 2000)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        drive.setPoseEstimate(new Pose2d(trajectoryToRungs.get(0).point0, 0));

        scorerClaw.close();

        waitForStart();

        /* *** WAIT *** */
        clock.reset();

        while (clock.seconds() < 1) {
            telemetry.update();
        }

        /* *** GO TO RUNGS *** */
        drive.followTrajectory(trajectoryToRungs);

        while (drive.isBusy() && opModeIsActive()) {
         //   drive.update();
           // extend.PIDControl();
        }

        hangSpecimen();

        /* *** GO TO HUMAN *** */
        drive.followTrajectory(trajectoryPark);

        while (drive.isBusy() && opModeIsActive()) {
            if (relaxFlag && clock.milliseconds() > RELAX_CLAW) {
                scorerClaw.relax();
                drive.setForce(0.5);
            }
            telemetry.addData("cam", cam.Angle);
            telemetry.update();
          //  drive.update();
          //  extend.PIDControl();
        }
    }

    private void hangSpecimen() {
        scorerPerkid.scoreSpecimen();
        scorerDifferential.scoreSpecimen();

        clock.reset();

        while (clock.milliseconds() < WAIT_SCORE) {
            telemetry.update();
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

        clock = new ElapsedTime();
    }
}
