package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Modules.Extend;
import org.firstinspires.ftc.teamcode.drive.Drive;

@Config
@Autonomous
public class ThreeClawBot extends LinearOpMode {
    private Drive drive;
    private Extend extend;

    public static double x = -48, y = -60, heading = 0;

    public static double xThreshold = 0.1, yThreshold = 0.1, headingThreshold = 0.025;
    public static double parralelVelThreshold = 0.1, perpendicularVelThreshold = 0.1;

    @Override
    public void runOpMode() throws InterruptedException {
        initRobot();

        extend.setReference(0);

        waitForStart();


        while (true) {
            drive.setReference(new Pose2d(x, y, Math.toRadians(heading)));
            Pose2d error = drive.toPoint();
            if (Math.abs(error.getX()) <= xThreshold && Math.abs(error.getY()) <= yThreshold && Math.abs(error.getHeading()) <= headingThreshold &&
                Math.abs(drive.getParralelVelocity()) <= parralelVelThreshold && Math.abs(drive.getPerpendicularVelocity()) <= perpendicularVelThreshold) {
                break;
            }
            extend.PIDControl();
            drive.updateLocalizer();

            telemetry.addData("parVel", drive.getParralelVelocity());
            telemetry.addData("perVel", drive.getPerpendicularVelocity());

            telemetry.addData("x_error", error.getX());
            telemetry.addData("y_error", error.getY());
            telemetry.addData("heading_error", error.getHeading());
            telemetry.update();
        }

        extend.setReference(300);

        while (opModeIsActive()) {
            extend.PIDControl();
        }
    }

    public void initRobot() {
        drive = new Drive(hardwareMap, new Pose2d(-64.5, -29.2));
        extend = new Extend(this);
    }
}
