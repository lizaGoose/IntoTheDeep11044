package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.Modules.Diff;

@Config
@TeleOp(group = "test")
public class TwoServosSetToZero extends LinearOpMode {
    public static String servoConfig1 = "";
    public static String servoConfig2 = "";
    public static double pos = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {
        Servo servo1 = hardwareMap.get(Servo.class, servoConfig1);
        Servo servo2 = hardwareMap.get(Servo.class, servoConfig2);
        servo2.setDirection(Servo.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {
            servo1.setPosition(pos);
            servo2.setPosition(pos);
            telemetry.addData("servo_pos1", servo1.getPosition());
            telemetry.addData("servo_pos2", servo2.getPosition());
            telemetry.update();
        }
    }
}
