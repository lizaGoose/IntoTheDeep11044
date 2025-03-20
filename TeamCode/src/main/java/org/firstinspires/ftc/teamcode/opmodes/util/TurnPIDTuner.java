package org.firstinspires.ftc.teamcode.opmodes.util;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.Linkage;
import org.firstinspires.ftc.teamcode.auto.Drive;

@Config
@Autonomous(group = "util")
public class TurnPIDTuner extends LinearOpMode {
    public static double ANGLE = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        Drive drive = new Drive(hardwareMap);
        Linkage linkage = new Linkage(this);

        waitForStart();

        while (opModeIsActive()) {
            drive.setReference(new Pose2d(0, 0, Math.toRadians(ANGLE)));
            drive.updateLocalizer();
            drive.toPoint();
            linkage.PIDControl();
        }
    }
}
