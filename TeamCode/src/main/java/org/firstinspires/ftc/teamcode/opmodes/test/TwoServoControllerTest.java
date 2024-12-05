package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp(group = "test")
public class TwoServoControllerTest extends LinearOpMode {
    public static String servoConfig1 = "";
    public static String servoConfig2 = "";

    @Override
    public void runOpMode() throws InterruptedException {
        Servo servo1 = hardwareMap.get(Servo.class, servoConfig1);
        Servo servo2 = hardwareMap.get(Servo.class, servoConfig2);
        servo2.setDirection(Servo.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {
            servo1.setPosition((gamepad1.left_stick_y + 1) / 2 + gamepad1.right_stick_x / 2);
            servo2.setPosition((gamepad1.left_stick_y + 1) / 2 - gamepad1.right_stick_x / 2);
            telemetry.addData("servo_pos1", servo1.getPosition());
            telemetry.addData("servo_pos2", servo2.getPosition());
            telemetry.update();
        }
    }
}
