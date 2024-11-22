package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp
public class ServoControllerTest extends LinearOpMode {
    public static String servoConfig = "";
    private Servo servo;

    @Override
    public void runOpMode() throws InterruptedException {
        servo = hardwareMap.get(Servo.class, servoConfig);

        waitForStart();

        while (opModeIsActive()) {
            servo.setPosition((gamepad1.left_stick_x + 1) / 2);
            telemetry.addData("servo_pos", servo.getPosition());
            telemetry.update();
        }
    }
}
