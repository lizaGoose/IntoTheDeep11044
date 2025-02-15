package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Modules.DriveModule;
import org.firstinspires.ftc.teamcode.auto.Trajectory;

import java.io.IOException;

@Config
@TeleOp(group = "test")
public class DriveModuleTest extends LinearOpMode {
    public static String trajectory = "";
    @Override
    public void runOpMode() throws InterruptedException {
        DriveModule driveModule = new DriveModule(hardwareMap);
        Trajectory traj;
        try {
            traj = driveModule.drive.buildTrajectory(trajectory)
                            .waitForAt(1, 100)
                                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                driveModule.followTrajectory(traj);
            }
            driveModule.update(gamepad1);
        }
    }
}
