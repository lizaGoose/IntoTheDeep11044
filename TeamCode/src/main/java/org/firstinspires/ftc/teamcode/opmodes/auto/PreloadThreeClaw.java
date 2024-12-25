package org.firstinspires.ftc.teamcode.opmodes.auto;


import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.AIM_SPECIMEN_0;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.AIM_SPECIMEN_1;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.HOLD_POS;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.INITIAL;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.RELAX_CLAW;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.WAIT_SCORE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoCommonConstants.WAIT_START;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.ANGLE_MIDDLE_TO_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.ANGLE_RIGHT_TO_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.ANGLE_TO_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.ANGLE_LEFT_TO_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.ANGLE_TO_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.ANGLE_TO_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.DIFF_ROTATION_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.DIFF_ROTATION_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.DIFF_ROTATION_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.EXTEND_HALF_CLOSE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.EXTEND_MIDDLE_TO_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.EXTEND_RIGHT_TO_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.EXTEND_TO_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.EXTEND_LEFT_TO_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.EXTEND_TO_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.EXTEND_TO_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.MIDDLE_STOP_POINT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.RIGHT_STOP_POINT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.WAIT_AT_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.WAIT_AT_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.WAIT_AT_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.WAIT_FOR_LEFT_ROTATION;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.WAIT_FOR_MIDDLE_ROTATION;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.WAIT_FOR_RIGHT_ROTATION;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

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

@Autonomous
public class PreloadThreeClaw extends LinearOpMode {
    private Drive drive;
    private ScoringPerkid scoringPerkid;
    private ScoringDiff scoringDiff;
    private ScorClaw scorClaw;

    private Perkid perkid;
    private Hand hand;
    private Differential diff;
    private Claw claw;
    private Extend extend;
    private static boolean relaxFlag = false;

    // Make this true somewhere to stop program
    private boolean DEBUG_END_FLAG = false;

    private ElapsedTime clock;

    @Override
    public void runOpMode() throws InterruptedException {
        initRobot();

        relaxFlag = false;

        Trajectory trajToRungs, trajToThree;
        try {
             trajToRungs = drive.buildTrajectory("traj10.json")
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
             trajToThree = drive.buildTrajectory("traj15.json")
                     .addSpatialMarker(0, () -> {
                         TrajectoryRunner.force = 0.6;

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
                     // PICK UP LEFT
                     .waitForAt(1, WAIT_AT_LEFT + 1200, ANGLE_TO_LEFT)
                     .addTimeMarker(WAIT_AT_LEFT, () -> {
                         extend.setReference(EXTEND_TO_LEFT);
                         diff.aim(DIFF_ROTATION_LEFT);
                         perkid.aim();
                         hand.setPosition(0.5);
                         claw.open();
                     })
                     .addTimeMarker(WAIT_AT_LEFT + 400, () -> {
                         diff.pickUp(DIFF_ROTATION_LEFT);
                         perkid.pickUp();
                     })
                     .addTimeMarker(WAIT_AT_LEFT + 600, () -> {
                         claw.close();
                     })
                     .addTimeMarker(WAIT_AT_LEFT + 1150, () -> {
                         diff.aim();
                         perkid.aim();
                     })
                     .addTurnMarker(1, (t) -> {
                         if (t <= 0.5) {
                             return ANGLE_LEFT_TO_HUMAN;
                         } else {
                             return ANGLE_MIDDLE_TO_HUMAN;
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
                     .waitForAt(MIDDLE_STOP_POINT, WAIT_AT_MIDDLE + 1250, ANGLE_TO_MIDDLE)
                     .addTimeMarker(0, () -> {
//                         hand.setPosition(0.5);
//                         perkid.transfer();
//                         diff.transfer();
//                         claw.open();
                         extend.setReference(EXTEND_TO_MIDDLE);
                         diff.aim(DIFF_ROTATION_MIDDLE);
                     })
                     .addTimeMarker(WAIT_AT_MIDDLE, () -> {
                         diff.aim(DIFF_ROTATION_MIDDLE);
                         perkid.aim();
                         hand.setPosition(0.5);
                         claw.open();
                     })
                     .addTimeMarker(WAIT_AT_MIDDLE + 400, () -> {
                         diff.pickUp(DIFF_ROTATION_MIDDLE);
                         perkid.pickUp();
                     })
                     .addTimeMarker(WAIT_AT_MIDDLE + 600, () -> {
                         claw.close();
                     })
                     .addTimeMarker(WAIT_AT_MIDDLE + 1150, () -> {
                         diff.aim();
                         perkid.aim();
                     })
                     .addTimeMarker(WAIT_AT_MIDDLE + 1200, () -> {
                         extend.setReference(0);
                     })
                     // MIDDLE TO HUMAN
                     .waitForAt(RIGHT_STOP_POINT, WAIT_FOR_MIDDLE_ROTATION + 200, ANGLE_MIDDLE_TO_HUMAN)
                     .addTimeMarker(0, () -> {
                         extend.setReference(0);
                     })
                     .addTimeMarker(WAIT_FOR_MIDDLE_ROTATION / 2, () -> {
                         extend.setReference(EXTEND_MIDDLE_TO_HUMAN);
                     })
                     .addTimeMarker(WAIT_FOR_MIDDLE_ROTATION, () -> {
                         claw.open();
                         DEBUG_END_FLAG = true;
                     })
                     // PICK UP RIGHT
                     .waitForAt(RIGHT_STOP_POINT, EXTEND_HALF_CLOSE, ANGLE_MIDDLE_TO_HUMAN)
                     .addTimeMarker(0, () -> {
                         claw.open();
                         extend.setReference(0);
                     })
                     .waitForAt(RIGHT_STOP_POINT, WAIT_AT_RIGHT + 1200, ANGLE_TO_RIGHT)
                     .addTimeMarker(0, () -> {
//                         hand.setPosition(0.5);
//                         perkid.transfer();
//                         diff.transfer();
//                         claw.open();
                         diff.aim(DIFF_ROTATION_RIGHT);
                     })
                     .addTimeMarker(WAIT_AT_RIGHT, () -> {
                         extend.setReference(EXTEND_TO_RIGHT);
                         diff.aim(DIFF_ROTATION_RIGHT);
                         perkid.aim();
                         hand.setPosition(0.5);
                         claw.open();
                     })
                     .addTimeMarker(WAIT_AT_RIGHT + 400, () -> {
                         diff.pickUp(DIFF_ROTATION_RIGHT);
                         perkid.pickUp();
                     })
                     .addTimeMarker(WAIT_AT_RIGHT + 600, () -> {
                         claw.close();
                     })
                     .addTimeMarker(WAIT_AT_RIGHT + 1150, () -> {
                         diff.aim();
                         perkid.aim();
                     })
                     // RIGHT TO HUMAN
                     .waitForAt(RIGHT_STOP_POINT, WAIT_FOR_RIGHT_ROTATION + 100, ANGLE_RIGHT_TO_HUMAN)
                     .addTimeMarker(0, () -> {
                         extend.setReference(0);
                     })
                     .addTimeMarker(WAIT_FOR_RIGHT_ROTATION / 2, () -> {
                         extend.setReference(EXTEND_RIGHT_TO_HUMAN);
                     })
                     .addTimeMarker(WAIT_FOR_RIGHT_ROTATION, () -> {
                         claw.open();
                     })
                     .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        drive.setPoseEstimate(new Pose2d(trajToRungs.get(0).point0, 0));

        scorClaw.close();

        waitForStart();

        /* *** WAIT *** */
        clock.reset();

        while (clock.seconds() < WAIT_START) {
            telemetry.update();
        }

        /* *** GO TO RUNGS *** */
        drive.followTrajectory(trajToRungs);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
            drive.update();
            extend.PIDControl();
        }

        hangSpecimen();

        /* *** GO TO HUMAN AND DO EVERYTHING *** */
        drive.followTrajectory(trajToThree);

        while (drive.isBusy() && opModeIsActive() && !DEBUG_END_FLAG) {
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

        clock = new ElapsedTime();
    }
}
