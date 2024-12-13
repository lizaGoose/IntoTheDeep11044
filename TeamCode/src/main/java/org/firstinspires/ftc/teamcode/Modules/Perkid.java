package org.firstinspires.ftc.teamcode.Modules;



import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Camera.CameraTest;

@Config
class PerkidConstants {
   /* public static double MAX_POS = 0.77;
    public static double VERTICAL_POS = 0.62;
    public static double STRAIGHT_POS = 0.37;

    public static double TRANSFER_POS = 0.71;
    public static double AIM_POS = 0.37;
    public static double TAKE_POS = 0.27;
    public static double LOOK_POS = 0.5;*/
   public static double TRANSFER_POS = 0.71;
    public static double LOOK_POS = 0.47;
    public static double PICK_UP = 0.26;
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
        PICK_UP
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
        }

    }
}
