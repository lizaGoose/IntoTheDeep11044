package org.firstinspires.ftc.teamcode.Modules;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.Camera.CameraTest;
import org.firstinspires.ftc.teamcode.Camera.SampleDetectionCameraClass;
import org.firstinspires.ftc.teamcode.drive.MecanumDriveRR;
import org.firstinspires.ftc.teamcode.util.Encoder;

public class Diff {
    LinearOpMode linearOpMode;
    Gamepad gamepad1;
    //SampleDetectionCameraClass cam;
    Servo right, left;
    AnalogInput leftEncoder, rightEncoder;
    public double angleDiff, angleDiff1, angleDiff2;

    public enum Mode {
        DRIVER_CONTROL,
        GET_UP,
        GET_DOWN,
        VISION_IN_USE,
        VISION_HUYNYA
    }

    public Mode currentMode = Mode.DRIVER_CONTROL;

    public Diff(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        gamepad1 = linearOpMode.gamepad1;
        left = linearOpMode.hardwareMap.get(Servo.class, "leftDif");
        right = linearOpMode.hardwareMap.get(Servo.class, "rightDif");
        left.setDirection(Servo.Direction.REVERSE);
        //cam = new SampleDetectionCameraClass(linearOpMode);

    }

    public void teleop(double angle) {
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
                left.setPosition(0.4);
                right.setPosition(0.4);
                break;
            case VISION_IN_USE:
                if (angleDiff1<=90) {
                    angleDiff = 180 * ( left.getPosition() - 0.5 );
                }
                else if(angleDiff1 > 90){
                    angleDiff = 0;
                }
                angleDiff1 = angleDiff + angle;

                left.setPosition(angleDiff1 / 180 + 0.5);
                right.setPosition(-angleDiff1 / 180 + 0.5);
                break;
            case VISION_HUYNYA:
                double angleHor = 0.0;
                double rotation = 45.0;
                left.setPosition(((angleHor + rotation / 2) / 90 + 1) / 2);
                right.setPosition(((angleHor - rotation / 2) / 90 + 1) / 2);
                break;
        }

    }
}
