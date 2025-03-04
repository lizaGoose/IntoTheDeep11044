package org.firstinspires.ftc.teamcode.Tests;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.Camera.AutoCamera;
import org.firstinspires.ftc.teamcode.Modules.Extend;
import org.firstinspires.ftc.teamcode.Modules.Perkid;

@Config
@TeleOp
public class TestKinematics extends LinearOpMode {
    public double pos, posHand;

    public boolean isPressed;
    Servo leftDiff, rightDiff;
    public static double leftPos = 0.65, rightPos = 0.55, leftPos1 = 0.75, rightPos1 = 0.45, centerPos = 0.65, centerPos1 = 0.9, k = 1;
    public static double minRange = 0.25, maxRange = 0.75, minRange1 = 0.45, maxRange1 = 0.55;

    @Override
    public void runOpMode() throws InterruptedException {
        AutoCamera cam = new AutoCamera(this);
        Extend extend = new Extend(this);
        Perkid perkid = new Perkid(this);
        leftDiff = hardwareMap.get(Servo.class, "left_diff_servo");
        rightDiff = hardwareMap.get(Servo.class, "right_diff_servo");
        leftDiff.scaleRange(minRange, maxRange);
        rightDiff.scaleRange(minRange1, maxRange1);

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.triangle) {
                if (!isPressed) {
                    pos = cam.AutonomousRecognition().get(0) - k;
                    posHand = 1 - cam.AutonomousRecognition().get(1);
                    //  if (cam.hand.getPosition() <0.5) {
                    leftDiff.setPosition(Math.abs(cam.getAngle() / 90 - cam.hand.getPosition())/* - 0.25*/);
                    rightDiff.setPosition(Math.abs(cam.getAngle() / 90 - cam.hand.getPosition())/* - 0.25*/);
                    //}
                    /*else {
                        leftDiff.setPosition(Math.abs(cam.getAngle() / 180 - cam.hand.getPosition()));
                        rightDiff.setPosition(Math.abs(cam.getAngle() / 180 - cam.hand.getPosition()));
                    }*/

                    //   leftDiff.setPosition(cam.getAngle()/360-posHand/k);
                    // rightDiff.setPosition(cam.getAngle()/360-posHand/k);
                   /* if(cam.handPos<0.6){
                        if(cam.getAngle() <70){
                            leftDiff.setPosition(leftPos);
                            rightDiff.setPosition(leftPos);
                        }
                        else if(cam.getAngle() > 100){
                            leftDiff.setPosition(rightPos);
                            rightDiff.setPosition(rightPos);
                        }

                        else{
                            leftDiff.setPosition(centerPos);
                            rightDiff.setPosition(centerPos);
                        }
                    }
                    else {
                        if(cam.getAngle() <70){
                            leftDiff.setPosition(rightPos1);
                            rightDiff.setPosition(rightPos1);

                        }
                        else if(cam.getAngle() > 100){
                            leftDiff.setPosition(leftPos1);
                            rightDiff.setPosition(leftPos1);
                        }

                        else{
                            leftDiff.setPosition(centerPos1);
                            rightDiff.setPosition(centerPos1);
                        }
                    }*/

                }
                isPressed = true;
            } else {
                isPressed = false;
            }
            if (gamepad1.square) {
                perkid.pickUp();
            } else {
                perkid.look();

            }
            if (gamepad1.cross) {
                extend.motor.setPower(gamepad1.right_stick_y);
            } else {
                cam.operatorControlRed();
                // extend.setPower(gamepad1.right_stick_y);
                cam.goToElementPosExtend(pos);
                cam.goToElementPosHand(posHand);
            }
            telemetry.addData("DiffPos", leftDiff.getPosition());
            telemetry.addData("yCoord", cam.handPos);
            telemetry.addData("testAngle", Math.cos(cam.handPos * 120));
            telemetry.addData("xCoord", cam.distanceX);
            telemetry.addData("sampCoordInches", cam.pip.xSample);
            telemetry.addData("encoder", extend.motor.getCurrentPosition());
            telemetry.update();
        }
    }
}
