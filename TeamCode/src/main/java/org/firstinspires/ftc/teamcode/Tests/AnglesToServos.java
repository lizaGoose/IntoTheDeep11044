package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Modules.Diff;

import java.io.DataInput;

@TeleOp
public class AnglesToServos extends LinearOpMode {
    DcMotorEx tes;
    double encoder;
    boolean buttonState;
    public int pos;
    private ElapsedTime timer;
    Servo test;

    @Override
    public void runOpMode() throws InterruptedException {
        tes = hardwareMap.get(DcMotorEx.class, "rightRear");
        tes.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        test = hardwareMap.get(Servo.class, "leftPerkid");
        //tes.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);


        timer = new ElapsedTime();

        waitForStart();
        timer.reset();
        double reference = 0;
        while (opModeIsActive()){

            if (gamepad1.dpad_left){
                test.setPosition(0.5);
            }

            reference += (gamepad1.right_stick_y) * timer.seconds() * 2000000;

            reference = Math.max(0, Math.min(reference, 400));

            double error = reference - tes.getCurrentPosition();

            tes.setPower(error * 0.01);



            telemetry.addData("enc", tes.getCurrentPosition());
            telemetry.addData("ref", reference);
            telemetry.addData("enc", gamepad1.right_stick_y);
            telemetry.update();
            timer.reset();
        }
    }
}
