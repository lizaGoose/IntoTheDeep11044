package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class ClawTest extends LinearOpMode {
    CRServo left, right;
    @Override
    public void runOpMode() throws InterruptedException {
        left = hardwareMap.get(CRServo.class, "leftDif");
        //right = hardwareMap.get(CRServoImplEx.class, "rightDif");

        waitForStart();

        while (opModeIsActive()){
            if(gamepad1.a){
                left.setPower(1);
               // right.setPower(1);
            }
            if(gamepad1.b){
                left.setPower(1);
               // right.setPower(1);
            }
        }
    }
}
