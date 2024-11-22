package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Modules.Diff;

import java.io.DataInput;

@TeleOp
public class AnglesToServos extends LinearOpMode {
    DcMotor tes;
    @Override
    public void runOpMode() throws InterruptedException {
        tes = hardwareMap.get(DcMotor.class, "leftRear");

        waitForStart();

        while (opModeIsActive()){
            tes.setPower(1);
        }
    }
}
