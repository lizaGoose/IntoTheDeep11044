package org.firstinspires.ftc.teamcode.Modules;

import static org.firstinspires.ftc.teamcode.Modules.HandConstants.RANGE;
import static org.firstinspires.ftc.teamcode.Modules.HandConstants.RANGE_END;
import static org.firstinspires.ftc.teamcode.Modules.HandConstants.RANGE_START;
import static org.firstinspires.ftc.teamcode.Modules.HandConstants.RIGHT_SIDE_DROP_SAMPLE;
import static org.firstinspires.ftc.teamcode.Modules.HandConstants.ROTATION_SPEED;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Camera.CameraTest;
import org.firstinspires.ftc.teamcode.math.ClawCalculation;

@Config
class HandConstants {
    public static double RANGE_START = 0.01;
    public static double RANGE_END = 0.38;
    public static double RANGE = 35;

    public static double ROTATION_SPEED = 0.00;
    public static double RIGHT_SIDE_DROP_SAMPLE = 0;
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
        VISION_CONTROL,
        RIGHT_SIDE_DROP_SAMPLE
    }

    Mode currentMode = Mode.DRIVER_CONTROL;

    public Hand(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        gamepad = linearOpMode.gamepad2;
        hand = linearOpMode.hardwareMap.get(Servo.class, "horizontal_servo");
        hand.scaleRange(RANGE_START, RANGE_END);

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
            case RIGHT_SIDE_DROP_SAMPLE:
                //rotation = 0.5;
                hand.setPosition(RIGHT_SIDE_DROP_SAMPLE);
                break;

        }
    }

    public void setPosition(double position) {
        hand.setPosition(position);
    }

    public double getPosition() {
        return hand.getPosition();
    }

    public void dropOverRightSide() {
        hand.setPosition(RIGHT_SIDE_DROP_SAMPLE);
    }

    public double getAngle() {
        double position = hand.getPosition();

        return ClawCalculation.servoPosToAngle(position, RANGE);
    }

    public void setAngle(double angle) {
        double position = ClawCalculation.angleToServoPos(angle, RANGE);

        hand.setPosition(position);
    }

    public void resetTimer() {
        timer.reset();
    }
}
