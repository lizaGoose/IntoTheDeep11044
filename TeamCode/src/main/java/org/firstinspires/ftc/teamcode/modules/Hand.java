package org.firstinspires.ftc.teamcode.modules;

import static org.firstinspires.ftc.teamcode.modules.Hand.HandConstants.FORWARD;
import static org.firstinspires.ftc.teamcode.modules.Hand.HandConstants.RANGE;
import static org.firstinspires.ftc.teamcode.modules.Hand.HandConstants.RANGE_END;
import static org.firstinspires.ftc.teamcode.modules.Hand.HandConstants.RANGE_START;
import static org.firstinspires.ftc.teamcode.modules.Hand.HandConstants.RIGHT_SIDE_DROP_SAMPLE;
import static org.firstinspires.ftc.teamcode.modules.Hand.HandConstants.ROTATION_SPEED;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.camera.CameraTest;
import org.firstinspires.ftc.teamcode.math.ClawCalculation;

public class Hand {
    OpMode opMode;
    Gamepad gamepad;
    CameraTest cam;
    ServoImplEx hand;
    AnalogInput axonEnc;
    ElapsedTime timer;
    double rotation = 0.5;

    enum Mode {
        DRIVER_CONTROL,
        FORWARD,
        VISION_CONTROL,
        RIGHT_SIDE_DROP_SAMPLE,
        RELAX
    }

    Mode currentMode = Mode.DRIVER_CONTROL;

    public Hand(OpMode opMode) {
        this.opMode = opMode;
        gamepad = opMode.gamepad1;
        hand = (ServoImplEx) opMode.hardwareMap.get(Servo.class, "horizontal_servo");
        hand.scaleRange(RANGE_START, RANGE_END);

        cam = new CameraTest();
        timer = new ElapsedTime();
        //   hand.setPosition(0.0);
    }

    public void teleop() {
        switch (currentMode) {
            case DRIVER_CONTROL:
//                rotation = rotation - Math.pow(gamepad.right_stick_x, 3) * timer.milliseconds() * ROTATION_SPEED;
//                rotation = Math.max(rotation, 0);
//                rotation = Math.min(rotation, 1);
                rotation = 0.5;
                hand.setPosition(rotation);
                timer.reset();
                break;
            case FORWARD:
                rotation = FORWARD;
                hand.setPosition(FORWARD);
                break;
            case RIGHT_SIDE_DROP_SAMPLE:
                //rotation = 0.5;
                hand.setPosition(RIGHT_SIDE_DROP_SAMPLE);
                break;
            case RELAX:
                hand.setPwmDisable();
                break;
        }
    }

    public void setPosition(double position) {
        hand.setPosition(position);
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getPosition() {
        return hand.getPosition();
    }

    public void dropOverRightSide() {
        hand.setPosition(RIGHT_SIDE_DROP_SAMPLE);
    }

    public void relax() {
        hand.setPwmDisable();
    }

    public double getAngle() {
        double position = hand.getPosition();

        return ClawCalculation.servoPosToAngle(position, RANGE);
    }

    public void setAngle(double angle) {
        double position = ClawCalculation.angleToServoPos(angle, RANGE);

        hand.setPosition(1 - position);
    }

    public void resetTimer() {
        timer.reset();
    }

    @Config
    static class HandConstants {
        public static double RANGE_CENTER = 0.24;
        public static double RANGE_SERVO = 0.24;
        public static double RANGE_START = RANGE_CENTER - RANGE_SERVO;
        public static double RANGE_END = RANGE_CENTER + RANGE_SERVO;
        public static double RANGE = 45;

        public static double ROTATION_SPEED = 0.002;
        public static double RIGHT_SIDE_DROP_SAMPLE = 0.1;
        public static double FORWARD = 0.5;
    }
}
