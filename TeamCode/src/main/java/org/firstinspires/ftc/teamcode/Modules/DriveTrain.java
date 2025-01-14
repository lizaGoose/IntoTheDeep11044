package org.firstinspires.ftc.teamcode.Modules;

import static org.firstinspires.ftc.teamcode.Modules.DriveTrainConstants.POWER_WHILE_MOVING_BACK;
import static org.firstinspires.ftc.teamcode.Modules.DriveTrainConstants.d;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.drive.MecanumDriveRR;

@Config
class DriveTrainConstants {
    public static double POWER_WHILE_MOVING_BACK = 0.5;
    public static double d = 2;
}

public class DriveTrain {
    public MecanumDriveRR drive;
    LinearOpMode linearOpMode;
    Gamepad gamepad1;
    public double x, y, heading;
    public double l, r;
    IMU imu;
    public double realHeading = 0;
    public double realControlHeading = 0;
    //public double d = 0.2;

    enum Mode {
        DRIVER_CONTROL,
        NON,
        MOVE_BACK_WHILE_SCORING_SPECIMEN,
        CONTROL_ANGLE
    }

    Mode currentMode = Mode.DRIVER_CONTROL;

    public DriveTrain(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        imu = linearOpMode.hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        drive = new MecanumDriveRR(linearOpMode.hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        gamepad1 = linearOpMode.gamepad1;
        imu.initialize(parameters);
    }

    public void teleOp() {
        l = drive.leftFront.getPower();
        r = drive.rightFront.getPower();
        switch (currentMode) {
            case DRIVER_CONTROL:
                x = gamepad1.left_stick_x;
                y = -gamepad1.left_stick_y;
                heading = -gamepad1.right_trigger + gamepad1.left_trigger;
                drive.setWeightedDrivePower(new Pose2d(heading, x, y));
                realHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

                break;
            case CONTROL_ANGLE:
                x = gamepad1.left_stick_x;
                y = -gamepad1.left_stick_y;
                heading = realHeading;
                realControlHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
                drive.setWeightedDrivePower(new Pose2d((-realControlHeading + heading)*d, x, y));

                break;
            case NON:
                drive.setMotorPowers(-gamepad1.right_stick_y, 0, gamepad1.right_stick_y, 0);
                break;
            case MOVE_BACK_WHILE_SCORING_SPECIMEN:
                drive.setWeightedDrivePower(new Pose2d(0, 0, POWER_WHILE_MOVING_BACK));
        }
        /*
        if (gamepad1.right_bumper) {
            x = -gamepad1.left_stick_x;
            y = gamepad1.left_stick_y;
            heading = -0.25;
        } else if (gamepad1.left_bumper) {
            x = -gamepad1.left_stick_x;
            y = gamepad1.left_stick_y;
            heading = 0.25;
        } else {
            x = -gamepad1.left_stick_x;
            y = gamepad1.left_stick_y;
            heading = -gamepad1.right_trigger + gamepad1.left_trigger;
        }
        */

    }
}