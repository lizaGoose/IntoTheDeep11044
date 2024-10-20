package org.firstinspires.ftc.teamcode.Modules;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Camera.CameraTest;
import org.firstinspires.ftc.teamcode.drive.LocalizerKolhoz;
import org.firstinspires.ftc.teamcode.util.Encoder;

public class Hand {
    LinearOpMode linearOpMode;
    Gamepad gamepad1;
    CameraTest cam;
    CRServoImplEx hand;
    AnalogInput axonEnc;

    enum Mode {
        DRIVER_CONTROL,
        VISION_CONTROL,
        NULL_POSE,
        GIVE_POSE
    }
    Mode currentMode = Mode.DRIVER_CONTROL;

    public Hand(LinearOpMode linearOpMode){
        this.linearOpMode = linearOpMode;
        gamepad1 = linearOpMode.gamepad1;
       hand = linearOpMode.hardwareMap.get(CRServoImplEx.class, "hand");
       axonEnc = linearOpMode.hardwareMap.get(AnalogInput.class, "handEnc");
        cam = new CameraTest();
    }
    public void teleop(){
        switch (currentMode) {
            case DRIVER_CONTROL:
                if (gamepad1.dpad_right){
                    hand.setPower(-1);
                }
                if (gamepad1.dpad_left){
                    hand.setPower(1);
                }
                else {
                    hand.setPower(0);
                }

                break;
            case VISION_CONTROL:
                hand.setPower(cam.handError);
                break;
            case NULL_POSE:
                if (axonEnc.getVoltage() != 0){
                    hand.setPower(-0.2);
                }
                else {
                    hand.setPower(0);
                }
                break;
            case GIVE_POSE:
                if (axonEnc.getVoltage() != 100){
                    hand.setPower(0.5);
                }
                else {
                    hand.setPower(0);
                }
                break;
        }
    }
}
