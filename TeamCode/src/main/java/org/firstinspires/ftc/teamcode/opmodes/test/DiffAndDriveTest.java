package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Modules.Claw;
import org.firstinspires.ftc.teamcode.Modules.Differential;
import org.firstinspires.ftc.teamcode.Modules.Hand;
import org.firstinspires.ftc.teamcode.drive.Drive;

@Config
@TeleOp
public class DiffAndDriveTest extends LinearOpMode {
    public static double HAND_ANGLE = 0;
    public static double DIFF_ANGLE = -90;
    @Override
    public void runOpMode() throws InterruptedException {
        Drive drive = new Drive(hardwareMap);
        Differential diff = new Differential(this);
        Hand hand = new Hand(this);
        Claw claw = new Claw(this);

        waitForStart();
        claw.currentMode = Claw.Mode.DROP;

        while (opModeIsActive()) {
            double heading = drive.getCorrectedHeading();
            Pose2d pose = drive.getPoseEstimate();

            hand.setAngle(HAND_ANGLE);

            diff.setAngleRotation(DIFF_ANGLE, Math.toDegrees(heading) + hand.getAngle());

            telemetry.addData("x", pose.getX());
            telemetry.addData("y", pose.getY());
            telemetry.addData("heading", pose.getHeading());
            telemetry.addData("hand_angle", hand.getAngle());
            telemetry.addData("hand_pos", hand.getPosition());
            telemetry.addData("left_diff", diff.getPositions().get(0));
            telemetry.addData("right_diff", diff.getPositions().get(1));
            telemetry.addData("rotation", Math.toDegrees(heading) + hand.getAngle());
            telemetry.update();

            drive.updateLocalizer();

        }
    }
}
