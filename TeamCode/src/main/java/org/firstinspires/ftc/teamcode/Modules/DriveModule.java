package org.firstinspires.ftc.teamcode.Modules;

import static org.firstinspires.ftc.teamcode.Modules.DriveTrainConstants.EXTEND_MOVE_PERCENTAGE;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.auto.Trajectory;
import org.firstinspires.ftc.teamcode.auto.Drive;

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
       drive = new Drive(hardwareMap);
    }

    public DriveModule(HardwareMap hardwareMap, Pose2d startPose) {
        drive = new Drive(hardwareMap, startPose);
    }

    public void update(Gamepad gamepad1) {
        update(gamepad1, 0);
    }

    public void update(Gamepad gamepad1, double extendPercentage) {
        boolean dryRun = false;
        switch (currentMode) {
            case DRIVER_CONTROL:
                double heading = gamepad1.right_trigger - gamepad1.left_trigger;
                heading = heading * (1 - (1 - EXTEND_MOVE_PERCENTAGE) * extendPercentage);
                drive.setPowerByPose(new Pose2d(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x,
                        heading
                ));
                dryRun = true;
            case FOLLOWING_TRAJECTORY:
                drive.update(dryRun);
                if (!drive.isBusy())
                    currentMode = Mode.DRIVER_CONTROL;
                break;
        }
    }

    public void followTrajectory(Trajectory trajectory) {
        if (currentMode == Mode.FOLLOWING_TRAJECTORY) {
            return;
        }
        drive.followTrajectory(trajectory);
        currentMode = Mode.FOLLOWING_TRAJECTORY;
    }
}