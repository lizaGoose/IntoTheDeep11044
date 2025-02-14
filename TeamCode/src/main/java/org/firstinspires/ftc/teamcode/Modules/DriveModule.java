package org.firstinspires.ftc.teamcode.Modules;

import static org.firstinspires.ftc.teamcode.Modules.DriveTrainConstants.EXTEND_MOVE_PERCENTAGE;
import static org.firstinspires.ftc.teamcode.Modules.DriveTrainConstants.POWER_WHILE_MOVING_BACK;
import static org.firstinspires.ftc.teamcode.Modules.DriveTrainConstants.d;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.auto.Trajectory;
import org.firstinspires.ftc.teamcode.drive.Drive;
import org.firstinspires.ftc.teamcode.drive.MecanumDriveRR;

@Config
class DriveModuleConstants {
    public static double POWER_WHILE_MOVING_BACK = 0.5;
    public static double EXTEND_MOVE_PERCENTAGE = 0.5;
    public static double d = 2;
}

public class DriveModule {
    public Drive drive;
    enum Mode {
        DRIVER_CONTROL,
        FOLLOWING_TRAJECTORY
    }

    Mode currentMode = Mode.DRIVER_CONTROL;

    public DriveModule(HardwareMap hardwareMap) {
       drive = Drive.getInstance(hardwareMap, new Pose2d());
    }

    public void update(Gamepad gamepad1) {
        boolean dryRun = false;
        switch (currentMode) {
            case DRIVER_CONTROL:
                drive.setPowerByPose(new Pose2d(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x,
                        gamepad1.right_trigger - gamepad1.left_trigger
                ));
                dryRun = true;
            case FOLLOWING_TRAJECTORY:
                drive.update(dryRun);
                if (!drive.isBusy())
                    currentMode = Mode.DRIVER_CONTROL;
                break;
        }
    }

    void followTrajectory(Trajectory trajectory) {
        drive.followTrajectory(trajectory);
        currentMode = Mode.FOLLOWING_TRAJECTORY;
    }
}