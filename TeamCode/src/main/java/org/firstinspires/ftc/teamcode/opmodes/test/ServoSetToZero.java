package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.Modules.Diff;

@Config
@TeleOp(group = "test")
public class ServoSetToZero extends LinearOpMode {
    public static String servoConfig = "";
    public static double pos = 0.5;
    public static boolean power = false;

    @Override
    public void runOpMode() throws InterruptedException {
        ServoImplEx servo = (ServoImplEx) hardwareMap.get(Servo.class, servoConfig);
        servo.setDirection(Servo.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {
            if (!power) {
                servo.setPwmDisable();
            }
            servo.setPosition(pos);
            telemetry.addData("servo_pos", servo.getPosition());
            telemetry.update();
        }
    }
}
