package org.firstinspires.ftc.teamcode.Modules;



import static org.firstinspires.ftc.teamcode.Modules.PerkidConstants.LOOK_POS;
import static org.firstinspires.ftc.teamcode.Modules.PerkidConstants.PICK_UP;
import static org.firstinspires.ftc.teamcode.Modules.PerkidConstants.RIGHT_SIDE_DROP_SAMPLE;
import static org.firstinspires.ftc.teamcode.Modules.PerkidConstants.STRAIGHT_POS;
import static org.firstinspires.ftc.teamcode.Modules.PerkidConstants.TRANSFER_FROM_SUBMERSABLE;
import static org.firstinspires.ftc.teamcode.Modules.PerkidConstants.TRANSFER_POS;
import static org.firstinspires.ftc.teamcode.Modules.PerkidConstants.VERTICAL_POS;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Camera.CameraTest;

@Config
class PerkidConstants {
   /* public static double MAX_POS = 0.77;
    */
    public static double VERTICAL_POS = 0.63;
    public static double STRAIGHT_POS = 0.39;
    /*
    public static double TRANSFER_POS = 0.71;
    public static double AIM_POS = 0.37;
    public static double TAKE_POS = 0.27;
    public static double LOOK_POS = 0.5;*/
    public static double TRANSFER_POS = 0.77;
    public static double LOOK_POS = 0.42;
    public static double TRANSFER_FROM_SUBMERSABLE = 0.53;
    //0.37
    public static double PICK_UP = 0.37;
    public static double RIGHT_SIDE_DROP_SAMPLE = 0.67;
}

public class Perkid {
    LinearOpMode linearOpMode;
    Gamepad gamepad1;
    DcMotor extend;
    CameraTest cam;
    Servo left, right;

    enum Mode {
        SCORE_POSITION,
        PICK_POSITION,
        AIM_POSITION,
        UP_POSITION,

        TRANSFER,
        LOOK,
        PICK_UP,
        TRANSFER_FROM_SUBMERSABLE,
        RIGHT_SIDE_DROP_SAMPLE
    }

    Mode currentMode = Mode.SCORE_POSITION;

    public Perkid(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        gamepad1 = linearOpMode.gamepad1;
        left = linearOpMode.hardwareMap.get(Servo.class, "left_vertical_servo");
        right = linearOpMode.hardwareMap.get(Servo.class, "right_vertical_servo");
        right.setDirection(Servo.Direction.REVERSE);
        cam = new CameraTest();
    }

    public void teleop() {
        switch (currentMode) {
        /*    case SCORE_POSITION:
                left.setPosition(TRANSFER_POS);
                right.setPosition(TRANSFER_POS);
                break;
            case PICK_POSITION:
                left.setPosition(TAKE_POS);
                right.setPosition(TAKE_POS);
                break;
            case AIM_POSITION:
                left.setPosition(AIM_POS);
                right.setPosition(AIM_POS);
                break;
            case UP_POSITION:
                left.setPosition(STRAIGHT_POS);
                right.setPosition(STRAIGHT_POS);
                break;
            case LOOK:
                left.setPosition(LOOK_POS);
                right.setPosition(LOOK_POS);
                break;*/
            case TRANSFER:
                left.setPosition(PerkidConstants.TRANSFER_POS);
                right.setPosition(PerkidConstants.TRANSFER_POS);
                break;
            case LOOK:
                left.setPosition(PerkidConstants.LOOK_POS);
                right.setPosition(PerkidConstants.LOOK_POS);
                break;
            case PICK_UP:
                left.setPosition(PerkidConstants.PICK_UP);
                right.setPosition(PerkidConstants.PICK_UP);
                break;
            case TRANSFER_FROM_SUBMERSABLE:
                left.setPosition(PerkidConstants.TRANSFER_FROM_SUBMERSABLE);
                right.setPosition(PerkidConstants.TRANSFER_FROM_SUBMERSABLE);
                break;
            case RIGHT_SIDE_DROP_SAMPLE:
                left.setPosition(PerkidConstants.RIGHT_SIDE_DROP_SAMPLE);
                right.setPosition(PerkidConstants.RIGHT_SIDE_DROP_SAMPLE);
                break;
        }
    }

    public void aim() {
        left.setPosition(LOOK_POS);
        right.setPosition(LOOK_POS);
    }

    public void pickUp() {
        left.setPosition(PICK_UP);
        right.setPosition(PICK_UP);
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
}
