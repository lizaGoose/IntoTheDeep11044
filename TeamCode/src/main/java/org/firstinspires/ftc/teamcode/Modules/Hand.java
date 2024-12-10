package org.firstinspires.ftc.teamcode.Modules;

import static org.firstinspires.ftc.teamcode.Modules.HandConstants.ROTATION_SPEED;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Camera.CameraTest;

@Config
class HandConstants {
    public static double ROTATION_SPEED = 0.01;
}

public class Hand {
    LinearOpMode linearOpMode;
    Gamepad gamepad;
    CameraTest cam;
    Servo hand;
    AnalogInput axonEnc;
    ElapsedTime timer;
    double rotation = 0.5;
    // 0.83
    //

    enum Mode {
        DRIVER_CONTROL,
        FORWARD,
        VISION_CONTROL
    }

    Mode currentMode = Mode.DRIVER_CONTROL;

    public Hand(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        gamepad = linearOpMode.gamepad2;
        hand = linearOpMode.hardwareMap.get(Servo.class, "horizontal_servo");
        hand.scaleRange(1 - (0.83 + 0.15), 1 - (0.83 - 0.15));

        cam = new CameraTest();
        timer = new ElapsedTime();
        //   hand.setPosition(0.0);
    }

    public void teleop() {
        switch (currentMode) {
            case DRIVER_CONTROL:
                rotation = rotation - Math.pow(gamepad.left_stick_x, 3) * timer.milliseconds() * ROTATION_SPEED;
                rotation = Math.max(rotation, 0);
                rotation = Math.min(rotation, 1);
                hand.setPosition(rotation);
                timer.reset();
                break;
            case FORWARD:
                rotation = 0.5;
                hand.setPosition(0.5);
                break;
        }
    }

    public void resetTimer() {
        timer.reset();
    }
}
