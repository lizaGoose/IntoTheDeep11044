package org.firstinspires.ftc.teamcode.modules;


import static org.firstinspires.ftc.teamcode.modules.Perkid.PerkidConstants.AIM_AT_SPIKE;
import static org.firstinspires.ftc.teamcode.modules.Perkid.PerkidConstants.LOOK_POS;
import static org.firstinspires.ftc.teamcode.modules.Perkid.PerkidConstants.PICK_UP;
import static org.firstinspires.ftc.teamcode.modules.Perkid.PerkidConstants.RIGHT_SIDE_DROP_SAMPLE;
import static org.firstinspires.ftc.teamcode.modules.Perkid.PerkidConstants.STRAIGHT_POS;
import static org.firstinspires.ftc.teamcode.modules.Perkid.PerkidConstants.TRANSFER_FROM_SUBMERSABLE;
import static org.firstinspires.ftc.teamcode.modules.Perkid.PerkidConstants.TRANSFER_POS;
import static org.firstinspires.ftc.teamcode.modules.Perkid.PerkidConstants.VERTICAL_POS;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.camera.CameraTest;

public class Perkid {
    OpMode opMode;
    Gamepad gamepad1;
    CameraTest cam;
    ServoImplEx left, right;

    enum Mode {
        SCORE_POSITION,
        TRANSFER,
        LOOK,
        PICK_UP,
        TRANSFER_FROM_SUBMERSIBLE,
        RIGHT_SIDE_DROP_SAMPLE,
        RELAX
    }

    Mode currentMode = Mode.SCORE_POSITION;

    public Perkid(OpMode opMode) {
        this.opMode = opMode;
        gamepad1 = opMode.gamepad1;
        left = (ServoImplEx) opMode.hardwareMap.get(Servo.class, "left_vertical_servo");
        right = (ServoImplEx) opMode.hardwareMap.get(Servo.class, "right_vertical_servo");
        left.setDirection(Servo.Direction.REVERSE);
        cam = new CameraTest();
    }

    public void teleop() {
        switch (currentMode) {
            case TRANSFER:
                left.setPosition(PerkidConstants.TRANSFER_POS);
                right.setPosition(PerkidConstants.TRANSFER_POS);
                break;
            case LOOK:
                left.setPosition(LOOK_POS);
                right.setPosition(LOOK_POS);
                break;
            case PICK_UP:
                left.setPosition(PerkidConstants.PICK_UP);
                right.setPosition(PerkidConstants.PICK_UP);
                break;
            case TRANSFER_FROM_SUBMERSIBLE:
                left.setPosition(PerkidConstants.TRANSFER_FROM_SUBMERSABLE);
                right.setPosition(PerkidConstants.TRANSFER_FROM_SUBMERSABLE);
                break;
            case RIGHT_SIDE_DROP_SAMPLE:
                left.setPosition(PerkidConstants.RIGHT_SIDE_DROP_SAMPLE);
                right.setPosition(PerkidConstants.RIGHT_SIDE_DROP_SAMPLE);
                break;
            case RELAX:
                left.setPwmDisable();
                right.setPwmDisable();
                break;
        }
    }

    public void aimAtSpike() {
        left.setPosition(AIM_AT_SPIKE);
        right.setPosition(AIM_AT_SPIKE);
    }

    public void pickUp() {
        left.setPosition(PICK_UP);
        right.setPosition(PICK_UP);
    }
    public void look() {
        left.setPosition(LOOK_POS);
        right.setPosition(LOOK_POS);
    }

    public void transfer() {
        left.setPosition(TRANSFER_POS);
        right.setPosition(TRANSFER_POS);
    }

    public void liftSample() {
        left.setPosition(TRANSFER_FROM_SUBMERSABLE);
        right.setPosition(TRANSFER_FROM_SUBMERSABLE);
    }

    public void dropOverRightSide() {
        left.setPosition(RIGHT_SIDE_DROP_SAMPLE);
        right.setPosition(RIGHT_SIDE_DROP_SAMPLE);
    }

    public void relax() {
        left.setPwmDisable();
        right.setPwmDisable();
    }

    public void setAngle(double angle) {
        double position = angleToPosition(angle);

        left.setPosition(position);
        right.setPosition(position);
    }

    public double angleToPosition(double angle) {
        double position = ((angle / 90) + 1) / 2;

        position = (VERTICAL_POS - STRAIGHT_POS) * 2 * position + 2 * STRAIGHT_POS - VERTICAL_POS;

        return position;
    }

    @Config
    static
    class PerkidConstants {
        public static double VERTICAL_POS = 0.7;
        public static double STRAIGHT_POS = 0.29;

        public static double TRANSFER_POS = 1;
        public static double LOOK_POS = 0.33;
        public static double AIM_AT_SPIKE = 0.33;
        public static double TRANSFER_FROM_SUBMERSABLE = 0.32;

        public static double PICK_UP = 0.2;
        public static double RIGHT_SIDE_DROP_SAMPLE = 0.85;
    }
}
