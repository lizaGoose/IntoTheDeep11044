package org.firstinspires.ftc.teamcode.Modules;

import static org.firstinspires.ftc.teamcode.Modules.PerkidConstants.AIM_POS;
import static org.firstinspires.ftc.teamcode.Modules.PerkidConstants.MAX_POS;
import static org.firstinspires.ftc.teamcode.Modules.PerkidConstants.TAKE_POS;
import static org.firstinspires.ftc.teamcode.Modules.PerkidConstants.TRANSFER_POS;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Camera.CameraTest;

@Config
class VerticalHandConstants {
    public static double GIVE_POS = 0.512;
    public static double DETECT_POS = 0.1;
    public static double PICK_POS = 0.84;

}

public class VerticalHand {
    LinearOpMode linearOpMode;
    Gamepad gamepad1;
    DcMotor extend;
    CameraTest cam;
    Servo left, right;

    enum Mode {
        GIVE,
        DETETCT,
        PICK
    }

    Mode currentMode = Mode.PICK;

    public VerticalHand(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        gamepad1 = linearOpMode.gamepad1;
        left = linearOpMode.hardwareMap.get(Servo.class, "left_vertical_servo");
        right = linearOpMode.hardwareMap.get(Servo.class, "right_vertical_servo");
        right.setDirection(Servo.Direction.REVERSE);
        cam = new CameraTest();
    }

    public void teleop() {
        switch (currentMode) {
            case GIVE:
                break;
            case DETETCT:
                break;
            case PICK:
                break;

        }

    }
}
