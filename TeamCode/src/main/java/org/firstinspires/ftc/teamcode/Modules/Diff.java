package org.firstinspires.ftc.teamcode.Modules;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Camera.CameraTest;
import org.firstinspires.ftc.teamcode.Camera.SampleDetectionCameraClass;
import org.firstinspires.ftc.teamcode.drive.MecanumDriveRR;
import org.firstinspires.ftc.teamcode.math.ClawCalculation;
import org.firstinspires.ftc.teamcode.util.Encoder;

import java.util.Collections;
import java.util.List;

@Config
public class Diff {
    LinearOpMode linearOpMode;
    Gamepad gamepad1;
    //SampleDetectionCameraClass cam;
    ServoImplEx right, left;
    AnalogInput leftEncoder, rightEncoder;
    public double angleDiffLeft = 0.5, angleDiffRight = 0.5;
    ClawCalculation calc;

    public boolean buttonState = false;
public double lastServPos = 0, rightServPos = 0;
    public static double angle_config = 0.0;
    public static double rotation_config = 0.0;
public double rotation;
    private ElapsedTime timer;
    public static double wait = 0.5;
    private List<Double> diff = ClawCalculation.angleAndRotToDiffFix(angle_config, rotation_config);
    private double lastCamera = 0;

    public enum Mode {
        DRIVER_CONTROL,
        GET_UP,
        GET_DOWN,
        VISION_IN_USE,
        VISION_HUYNYA,
        CONFIG_TEST,
        VISION_MORE_THAN_HUYNYA
    }

    public Mode currentMode = Mode.GET_DOWN;

    public Diff(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        gamepad1 = linearOpMode.gamepad1;
        left = (ServoImplEx) linearOpMode.hardwareMap.get(Servo.class, "left_diff_servo");
        right = (ServoImplEx) linearOpMode.hardwareMap.get(Servo.class, "right_diff_servo");
        left.setDirection(Servo.Direction.REVERSE);
        // cam = new SampleDetectionCameraClass(linearOpMode);
        timer = new ElapsedTime();

    }

    public void teleop(double cameraAngle) {
        switch (currentMode) {
            case DRIVER_CONTROL:
              /*  if (gamepad1.dpad_up) {
                    right.setPower(-1);
                    left.setPower(1);
                }
                else if (gamepad1.dpad_down){
                    right.setPower(1);
                    left.setPower(-1);
                }
                else {
                    right.setPower(0);
                    left.setPower(0);
                }*/
                right.setPosition(( gamepad1.left_stick_y + 1 ) / 2 + ( gamepad1.left_stick_x ) / 8);
                left.setPosition(( gamepad1.left_stick_y + 1 ) / 2 - ( gamepad1.left_stick_x ) / 8);
                break;

            case GET_UP:
                left.setPosition(0.5);
                right.setPosition(0.5);
                break;
            case GET_DOWN:
                left.setPosition(0);
                right.setPosition(0);
                break;
            case VISION_IN_USE:

                if (gamepad1.dpad_right) {
                    boolean buttonState2 = gamepad1.dpad_right;
                    if (buttonState2 != buttonState) {
                        angleDiffLeft = left.getPosition() + ClawCalculation.angleAndRotToDiff(0, cameraAngle/* + (left.getPosition()-0.5)*180*/).get(0)/*ClawCalculation.angleToServoPos(left.getPosition(), 180)*/;
                        angleDiffRight = right.getPosition() + ClawCalculation.angleAndRotToDiff(0, cameraAngle/*+ (right.getPosition()-0.5)*180*/).get(1);
                        /*- ClawCalculation.angleToServoPos(right.getPosition(), 180)*/
                        ;
                   /*  angleDiffLeft = ClawCalculation.angleAndRotToDiff(0, ClawCalculation.rotationFromAngle(cameraAngle, left.getPosition())).get(0);
                    angleDiffRight = ClawCalculation.angleAndRotToDiff(0, ClawCalculation.rotationFromAngle(cameraAngle, right.getPosition())).get(1);*/
                    }
                }
           /*   if(gamepad1.dpad_left) {
                    angleDiffLeft =  left.getPosition() + ClawCalculation.angleAndRotToDiff(0, 45).get(0) /*ClawCalculation.angleToServoPos(left.getPosition(), 180)*/
                ;
                // angleDiffRight = right.getPosition() + ClawCalculation.angleAndRotToDiff(0, 45).get(1);
                /*- ClawCalculation.angleToServoPos(right.getPosition(), 180)*/
                ;
                //   }
                buttonState = gamepad1.dpad_right;
                left.setPosition(angleDiffLeft);
                right.setPosition(angleDiffRight);
                if (gamepad1.dpad_left) {
                    angleDiffLeft = 0.5;
                    angleDiffRight = 0.5;
                }
              /*  if (angleDiff1<=90) {
                    angleDiff = 180 * ( left.getPosition() - 0.5 );
                }
                else if(angleDiff1 > 90){
                    angleDiff = 0;
                }
                angleDiff1 = angleDiff + angle;

                left.setPosition(angleDiff1 / 180 + 0.5);
                right.setPosition(-angleDiff1 / 180 + 0.5);*/
                break;
            case VISION_HUYNYA:

                   /* if((lastCamera <= 90) && (cameraAngle < 0) && (cameraAngle > -10) && (lastCamera > 0)){
                        rotation = 0;
                    }
                    else if((lastCamera >= -90) && (cameraAngle > 0)&& (cameraAngle < 10) && (lastCamera < 0)){
                        rotation = 0;
                    }*/

              //  if(!(cameraAngle > -10 && cameraAngle < 10)){
                if (cameraAngle > 10 && left.getPosition() > 0.7){
                rotation = -90;
 }
                else if(left.getPosition() == 1 && cameraAngle > -10){
                    rotation = -90;
                }
                else if (cameraAngle < -10 && left.getPosition() < 0.3){
                    rotation = 90;
                }
                else if(left.getPosition() == 0 && cameraAngle > 10){
                    rotation = 90;
                }

                else {
            if (timer.seconds() > wait) {
                if (cameraAngle == -95) {
                    cameraAngle = lastCamera;
                }
                rotation = ClawCalculation.rotationFromAngle(angle_config, left.getPosition()) + cameraAngle;
                if (rotation > 90) {
                    rotation = -90 + rotation - 90;
                }
                if (rotation < -90) {
                    rotation = 90 + rotation + 90;
                }
                if (gamepad1.dpad_left){
                    angle_config = 45;
                }
                diff = ClawCalculation.angleAndRotToDiffFix(angle_config, rotation);
                lastCamera = cameraAngle;
                timer.reset();
            }
            //
                }


              /*  else if(left.getPosition() >= 0.6 || left.getPosition() <= 0.4) {
                    diff = ClawCalculation.angleAndRotToDiffFix(angle_config, 90);
                }*/

                left.setPosition(diff.get(0));
                right.setPosition(diff.get(1));
                break;

            case VISION_MORE_THAN_HUYNYA:
                if (!(cameraAngle > -10 && cameraAngle < 10) && !(timer.seconds() < 0.05)) {
                    double rotation = ClawCalculation.rotationFromAngle(angle_config, left.getPosition()) + cameraAngle;
                    if (rotation > 90) {
                        rotation = -90 + rotation - 90;
                    }
                    if (rotation < -90) {
                        rotation = 90 + rotation + 90;
                    }
                    if (gamepad1.dpad_left){
                        angle_config = 45;
                    }
                    diff = ClawCalculation.angleAndRotToDiffFix(angle_config, rotation);

                    timer.reset();
                }

                left.setPosition(diff.get(0));
                right.setPosition(diff.get(1));
                break;
            case CONFIG_TEST:
                diff = ClawCalculation.angleAndRotToDiffFix(angle_config, rotation_config);
                left.setPosition(diff.get(0));
                right.setPosition(diff.get(1));
                break;
        }

    }
}
