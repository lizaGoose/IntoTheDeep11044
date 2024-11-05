package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Camera.CameraTest;

public class Perkid {
    LinearOpMode linearOpMode;
    Gamepad gamepad1;
    DcMotor extend;
    CameraTest cam;
    Servo left, right;

    enum Mode {
        CLOSE_POSITION,
        SCORE_POSITION,
        PICK_POSITION
    }
    Mode currentMode = Mode.CLOSE_POSITION;

    public Perkid(LinearOpMode linearOpMode){
        this.linearOpMode = linearOpMode;
        gamepad1 = linearOpMode.gamepad1;
        left = linearOpMode.hardwareMap.get(Servo.class, "leftPerkid");
        right = linearOpMode.hardwareMap.get(Servo.class, "rightPerkid");
        cam = new CameraTest();
    }
    public void teleop(){
        switch (currentMode) {
            case CLOSE_POSITION:
                left.setPosition(1);
                right.setPosition(0);
                break;
            case SCORE_POSITION:
                left.setPosition(0);
                right.setPosition(1);
                break;
            case PICK_POSITION:
                left.setPosition(0.5);
                right.setPosition(0.5);
                break;
        }

    }
}
