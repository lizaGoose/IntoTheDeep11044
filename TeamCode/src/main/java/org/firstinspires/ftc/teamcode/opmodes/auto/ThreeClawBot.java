package org.firstinspires.ftc.teamcode.opmodes.auto;

import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.ANGLE_TO_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.ANGLE_TO_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.DIFF_ROTATION_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.DIFF_ROTATION_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.DIFF_ROTATION_RIGHT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.EXTEND_POSITION_THRESHOLD;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.EXTEND_TO_LEFT;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.EXTEND_TO_MIDDLE;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.EXTEND_VELOCITY_THRESHOLD;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.X;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.X_HUMAN;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.Y;
import static org.firstinspires.ftc.teamcode.opmodes.auto.ThreeClawConstants.Y_HUMAN;

import com.acmerobotics.dashboard.config.Config;
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
import org.firstinspires.ftc.teamcode.drive.Drive;

@Config
class ThreeClawConstants {

    public static double X = -48;
    public static double Y = -50;

    public static double X_HUMAN = -60;
    public static double Y_HUMAN = -48;

    public static int EXTEND_TO_MIDDLE = 100;
    public static double ANGLE_TO_MIDDLE = 0;
    public static double DIFF_ROTATION_MIDDLE = 0;


    public static int EXTEND_TO_LEFT = 100;
    public static double ANGLE_TO_LEFT = -30;
    public static double DIFF_ROTATION_LEFT = 0;

    public static int EXTEND_TO_RIGHT = 100;
    public static double ANGLE_TO_RIGHT = 30;
    public static double DIFF_ROTATION_RIGHT = 0;

    public static double EXTEND_VELOCITY_THRESHOLD = 5;
    public static double EXTEND_POSITION_THRESHOLD = 5;
}

@Config
@Autonomous
public class ThreeClawBot extends LinearOpMode {
    private Drive drive;
    private Extend extend;

    private Perkid perkid;
    private Differential diff;
    private Hand hand;
    private Claw claw;
    private ScorClaw scorClaw;
    private DistanceSensor distInClaw;

    private ScoringDiff scoringDiff;
    private ScoringPerkid scoringPerkid;

    private ElapsedTime clock;


    public static double x = -48, y = -60, heading = 0;

    public static double xThreshold = 0.1, yThreshold = 0.1, headingThreshold = 0.025;
    public static double parralelVelThreshold = 0.1, perpendicularVelThreshold = 0.1;

    @Override
    public void runOpMode() throws InterruptedException {
        initRobot();

        extend.setReference(0);

        waitForStart();

        clock.reset();

        while (true) {
            drive.setReference(new Pose2d(X, Y, Math.toRadians(ANGLE_TO_MIDDLE)));
            Pose2d error = drive.toPoint();
            if (Math.abs(error.getHeading()) <= headingThreshold &&
                Math.abs(drive.getParralelVelocity()) <= parralelVelThreshold && Math.abs(drive.getPerpendicularVelocity()) <= perpendicularVelThreshold) {
                break;
            }
            extend.PIDControl();
            drive.updateLocalizer();

            telemetry.addData("par_vel", drive.getParralelVelocity());
            telemetry.addData("per_vel", drive.getPerpendicularVelocity());

            telemetry.addData("x_error", error.getX());
            telemetry.addData("y_error", error.getY());
            telemetry.addData("heading_error", error.getHeading());
            telemetry.update();
        }

        pickUpSample(EXTEND_TO_MIDDLE, DIFF_ROTATION_MIDDLE);

        while (true) {
            drive.setReference(new Pose2d(X, Y, Math.toRadians(ANGLE_TO_LEFT)));
            Pose2d error = drive.toPoint();
            if (Math.abs(error.getHeading()) <= headingThreshold &&
                    Math.abs(drive.getParralelVelocity()) <= parralelVelThreshold && Math.abs(drive.getPerpendicularVelocity()) <= perpendicularVelThreshold) {
                break;
            }
            extend.PIDControl();
            drive.updateLocalizer();

            telemetry.addData("par_vel", drive.getParralelVelocity());
            telemetry.addData("per_vel", drive.getPerpendicularVelocity());

            telemetry.addData("x_error", error.getX());
            telemetry.addData("y_error", error.getY());
            telemetry.addData("heading_error", error.getHeading());
            telemetry.update();
        }

        pickUpSample(EXTEND_TO_LEFT, DIFF_ROTATION_LEFT);
    }

    public void pickUpSample(int extendPos, double diffRotation) {
        /* ***** START AIMING **** */
        extend.setReference(extendPos);

        perkid.aim();
        diff.aim(diffRotation);
        hand.setPosition(0.5);
        claw.open();

        scoringPerkid.transferSample();
        scoringDiff.transferSample();
        scorClaw.open();



        while (true) {
            double error = extend.PIDControl();
            if (Math.abs(error) <= EXTEND_POSITION_THRESHOLD && Math.abs(extend.motor.getVelocity()) < EXTEND_VELOCITY_THRESHOLD) {
                break;
            }
            telemetry.addData("extend_pos", extend.getPosition());
        }

        clock.reset();

        while (clock.milliseconds() < 800) {
            perkid.pickUp();
            diff.pickUp(diffRotation);

            extend.PIDControl();
        }

        clock.reset();

        while (clock.milliseconds() < 600) {
            claw.close();
            perkid.liftSample();

            extend.PIDControl();
        }

        clock.reset();

        /* ***** START TRANSFER **** */
        while (clock.milliseconds() < 1600) {
            diff.transfer();
            hand.setPosition(0.5);
            perkid.transfer();
            claw.light();

            scoringPerkid.transferSample();
            scoringDiff.transferSample();
            scorClaw.open();

            scorClaw.open();

            extend.PIDControl();
        }

        while (clock.milliseconds() < 2600) {
            claw.close();

            extend.PIDControl();
        }

        while (distInClaw.getDistance(DistanceUnit.MM) >= 17 || clock.milliseconds() < 4000) {
            extend.setTransfer();
            extend.PIDControl();
        }

        clock.reset();

        while (clock.milliseconds() < 500) {
            scorClaw.close();
            extend.setCloser();

            extend.PIDControl();
        }

        claw.open();

        /* ***** SAMPLE TO HUMAN ***** */
        scoringPerkid.dropSampleToHuman();
        scoringDiff.pickSpecimen();

        while (true) {
            drive.setReference(new Pose2d(X_HUMAN, Y_HUMAN, Math.toRadians(0)));
            Pose2d error = drive.toPoint();
            if (Math.abs(error.getHeading()) <= headingThreshold &&
                    Math.abs(drive.getParralelVelocity()) <= parralelVelThreshold && Math.abs(drive.getPerpendicularVelocity()) <= perpendicularVelThreshold) {
                break;
            }

            extend.PIDControl();
            drive.updateLocalizer();

            telemetry.addData("par_vel", drive.getParralelVelocity());
            telemetry.addData("per_vel", drive.getPerpendicularVelocity());

            telemetry.addData("x_error", error.getX());
            telemetry.addData("y_error", error.getY());
            telemetry.addData("heading_error", error.getHeading());
            telemetry.update();
        }

        clock.reset();

        while (clock.milliseconds() < 600) {
            scorClaw.open();
        }
    }

    public void initRobot() {
        drive = new Drive(hardwareMap, new Pose2d(-64.5, -29.2));
        extend = new Extend(this);

        hand = new Hand(this);
        perkid = new Perkid(this);
        diff = new Differential(this);
        claw = new Claw(this);

        scoringDiff = new ScoringDiff(this);
        scoringPerkid = new ScoringPerkid(this);
        scorClaw = new ScorClaw(this);
        distInClaw = hardwareMap.get(DistanceSensor.class, "ScorClawDist");

        clock = new ElapsedTime();
    }
}
