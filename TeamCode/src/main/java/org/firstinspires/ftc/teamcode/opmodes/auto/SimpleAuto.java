package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.drive.MecanumDriveRR;

@Autonomous
public class SimpleAuto extends LinearOpMode {
    MecanumDriveRR drive;
    LinearOpMode linearOpMode;

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new MecanumDriveRR(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        while (opModeIsActive()){
            drive.setWeightedDrivePower(new Pose2d(0, 0.5, 0));
        }
    }
}
