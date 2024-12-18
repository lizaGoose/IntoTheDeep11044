package org.firstinspires.ftc.teamcode.Modules;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.drive.MecanumDriveRR;

public class DriveTrain {
    MecanumDriveRR drive;
    LinearOpMode linearOpMode;
    Gamepad gamepad1;
    public double x, y, heading;
    public double l, r;

    enum Mode {
        DRIVER_CONTROL,
        NON
    }

    Mode currentMode = Mode.DRIVER_CONTROL;

    public DriveTrain(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        drive = new MecanumDriveRR(linearOpMode.hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        gamepad1 = linearOpMode.gamepad1;
    }

    public void teleOp() {
        l = drive.leftFront.getPower();
        r = drive.rightFront.getPower();
        switch (currentMode) {
            case DRIVER_CONTROL:
                drive.setWeightedDrivePower(new Pose2d(heading, x, y));
                break;
            case NON:
                drive.setMotorPowers(-gamepad1.right_stick_y, 0, gamepad1.right_stick_y, 0);
                break;
        }
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


    }
}
