package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class ClawTest extends LinearOpMode {
    Servo claw;
    @Override
    public void runOpMode() throws InterruptedException {
        claw = hardwareMap.get(Servo.class, "Claw");
        //right = hardwareMap.get(CRServoImplEx.class, "rightDif");

        waitForStart();

        while (opModeIsActive()){
            if(gamepad1.a){
                claw.setPosition(0.35);
               // right.setPower(1);
            }
            if(gamepad1.b){
                claw.setPosition(0);
               // right.setPower(1);
            }
        }
    }
}
