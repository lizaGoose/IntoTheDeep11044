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
class ScoringPerkidConstants {
    public static double PICK_POS = 0.512;
    public static double SAMP_POS = 0.1;
    public static double SPEC_POS = 0.84;
    public static double PICK_SPEC = 0.35;
}

public class ScoringPerkid {
    LinearOpMode linearOpMode;
    Gamepad gamepad1;
    DcMotor extend;
    CameraTest cam;
    Servo left, right;

    enum Mode {
        SPECIMEN,
    PICK,
    SAMPLE,
        PICK_SPECIMEN
    }

    Mode currentMode = Mode.PICK;

    public ScoringPerkid(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        gamepad1 = linearOpMode.gamepad1;
        left = linearOpMode.hardwareMap.get(Servo.class, "left_scor_perkid");
        right = linearOpMode.hardwareMap.get(Servo.class, "right_scor_perkid");
        right.setDirection(Servo.Direction.REVERSE);
        cam = new CameraTest();
    }

    public void teleop() {
        switch (currentMode) {
            case SPECIMEN:
                left.setPosition(ScoringPerkidConstants.SPEC_POS);
                right.setPosition(ScoringPerkidConstants.SPEC_POS);
                break;
            case SAMPLE:
                left.setPosition(ScoringPerkidConstants.SAMP_POS);
                right.setPosition(ScoringPerkidConstants.SAMP_POS);
                break;
            case PICK:
                left.setPosition(ScoringPerkidConstants.PICK_POS);
                right.setPosition(ScoringPerkidConstants.PICK_POS);
                break;
            case PICK_SPECIMEN:
                left.setPosition(ScoringPerkidConstants.PICK_SPEC);
                right.setPosition(ScoringPerkidConstants.PICK_SPEC);
                break;

        }

    }
}
