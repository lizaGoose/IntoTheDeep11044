package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Camera.CameraTest;

public class Hand {
    LinearOpMode linearOpMode;
    Gamepad gamepad;
    CameraTest cam;
    Servo hand;
    AnalogInput axonEnc;
    ElapsedTime timer;
    // 0.83
    //

    enum Mode {
        DRIVER_CONTROL,
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
                hand.setPosition((gamepad.left_stick_x + 1) / 2);
                break;

        }
    }

    public void resetTimer() {
        timer.reset();
    }
}
