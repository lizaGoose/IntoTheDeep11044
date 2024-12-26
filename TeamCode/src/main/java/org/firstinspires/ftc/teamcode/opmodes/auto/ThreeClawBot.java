package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Modules.Extend;
import org.firstinspires.ftc.teamcode.drive.Drive;


@Config
@Autonomous
public class ThreeClawBot extends LinearOpMode {
    private Drive drive;
    private Extend extend;

    private ElapsedTime clock;


    public static double x = 0, y = 0, heading = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        initRobot();

        extend.setReference(0);

        waitForStart();

        clock.reset();

        while (opModeIsActive()) {
            drive.setReference(new Pose2d(x, y, heading));
            Pose2d error = drive.toPoint();
            extend.PIDControl();
            drive.updateLocalizer();

            telemetry.addData("par_vel", drive.getParralelVelocity());
            telemetry.addData("per_vel", drive.getPerpendicularVelocity());

            //telemetry.addData("x_error", error.getX());
            //telemetry.addData("y_error", error.getY());
            //telemetry.addData("heading_error", error.getHeading());
            telemetry.update();
        }
    }


    public void initRobot() {
        drive = new Drive(hardwareMap, new Pose2d(0, 0));
        extend = new Extend(this);

        clock = new ElapsedTime();
    }
}
