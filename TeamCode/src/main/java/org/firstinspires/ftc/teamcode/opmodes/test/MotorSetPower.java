package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@Config
@TeleOp(group = "test")
public class MotorSetPower extends LinearOpMode {
    public static String motorConfig = "";
    public static double pos = 0.0;

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx servo = hardwareMap.get(DcMotorEx.class, motorConfig);

        waitForStart();

        while (opModeIsActive()) {

            if (servo.getCurrentPosition() >= 200) {
                servo.setPower(Math.min(0, -gamepad1.left_stick_y));
            } else if (servo.getCurrentPosition() <= 0) {
                servo.setPower(Math.max(0, -gamepad1.left_stick_y));
            } else {
                servo.setPower(-gamepad1.left_stick_y);
            }
            telemetry.addData("encoder_pos", servo.getCurrentPosition());
            telemetry.addData("motor_power", servo.getPower());
            telemetry.update();
        }
    }
}
