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
import org.firstinspires.ftc.teamcode.drive.LocalizerKolhoz;
import org.firstinspires.ftc.teamcode.util.Encoder;

public class Diff {
    LinearOpMode linearOpMode;
    Gamepad gamepad1;
    CameraTest cam;
    CRServo right, left;
    AnalogInput leftEncoder, rightEncoder;

    enum Mode {
        DRIVER_CONTROL,
        VISION_CONTROL,
        SET_ZERO_POWER,
      SET_ZERO_POS,

    }
    Mode currentMode = Mode.SET_ZERO_POWER;

    public Diff(LinearOpMode linearOpMode){
        this.linearOpMode = linearOpMode;
        gamepad1 = linearOpMode.gamepad1;
        left = linearOpMode.hardwareMap.get(CRServo.class, "leftDif");
        right = linearOpMode.hardwareMap.get(CRServo.class, "rightDif");
        leftEncoder = linearOpMode.hardwareMap.get(AnalogInput.class, "leftDifEnc");
        rightEncoder = linearOpMode.hardwareMap.get(AnalogInput.class, "rightDifEnc");
        cam = new CameraTest();
    }
    public void teleop(){
        switch (currentMode) {
            case DRIVER_CONTROL:
                if (gamepad1.dpad_up) {
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
                }
                break;
            case VISION_CONTROL:
                right.setPower(-cam.clawError);
                left.setPower(cam.clawError);

                break;
            case SET_ZERO_POWER:
                left.setPower(0);
                right.setPower(0);
                break;
            case SET_ZERO_POS:
                if (leftEncoder.getVoltage() !=0){
                    left.setPower(1);
                }
                if (rightEncoder.getVoltage()!=0){
                    right.setPower(1);
                }
                break;

        }

    }
}
