package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.Modules.Diff;

@Config
@TeleOp
public class ServoSetToZero extends LinearOpMode {
    public static String servoConfig = "";
    public static double pos = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {
        Servo servo = hardwareMap.get(Servo.class, servoConfig);

        waitForStart();

        while (opModeIsActive()) {
            servo.setPosition(pos);
        }
    }
}
