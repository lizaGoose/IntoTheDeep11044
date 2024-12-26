package org.firstinspires.ftc.teamcode.Modules;


import static org.firstinspires.ftc.teamcode.Modules.PerkidConstants.PICK_UP;
import static org.firstinspires.ftc.teamcode.Modules.PerkidConstants.TRANSFER_POS;
import static org.firstinspires.ftc.teamcode.Modules.ScoringPerkidConstants.AIM_SPEC;
import static org.firstinspires.ftc.teamcode.Modules.ScoringPerkidConstants.DROP_SAMPLE_TO_HUMAN;
import static org.firstinspires.ftc.teamcode.Modules.ScoringPerkidConstants.PICK_SPEC;
import static org.firstinspires.ftc.teamcode.Modules.ScoringPerkidConstants.SCOR_SPEC;
import static org.firstinspires.ftc.teamcode.Modules.ScoringPerkidConstants.TRANSFER_SPEC;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Camera.CameraTest;

@Config
class ScoringPerkidConstants {
    /*  public static double TRANSITIVE_POS = 0.46;
      public static double PICK_POS = 0.512;
      public static double SAMP_POS = 0.1;
      public static double SPEC_POS = 0.84;
      public static double PICK_SPEC = 0.42;*/
    public static double PICK_SPEC = 0.38;
    public static double AIM_SPEC = 0.45;
    public static double DROP_SAMPLE_TO_HUMAN = 0.35;
    public static double TRANSFER_POS = 0.58;
    public static double SCORE_SAMP = 0.05;
    public static double TRANSFER_SPEC = 0.9;
    public static double SCOR_SPEC = 0.94;
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
        PICK_SPEC,
        TRANSFER,
        SCORE_SAMP,
        TRANSFER_SPEC,
        SCOR_SPEC

    }

    Mode currentMode = Mode.PICK_SPEC;

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
            /*case SPECIMEN:
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
                break;*/
            case PICK_SPEC:
                left.setPosition(ScoringPerkidConstants.PICK_SPEC);
                right.setPosition(ScoringPerkidConstants.PICK_SPEC);
                break;
            case TRANSFER:
                left.setPosition(ScoringPerkidConstants.TRANSFER_POS);
                right.setPosition(ScoringPerkidConstants.TRANSFER_POS);
                break;
            case SCORE_SAMP:
                left.setPosition(ScoringPerkidConstants.SCORE_SAMP);
                right.setPosition(ScoringPerkidConstants.SCORE_SAMP);
                break;
            case TRANSFER_SPEC:
                left.setPosition(ScoringPerkidConstants.TRANSFER_SPEC);
                right.setPosition(ScoringPerkidConstants.TRANSFER_SPEC);
                break;
            case SCOR_SPEC:
                left.setPosition(ScoringPerkidConstants.SCOR_SPEC);
                right.setPosition(ScoringPerkidConstants.SCOR_SPEC);
                break;

        }
    }

    public void transferSample() {
        left.setPosition(TRANSFER_POS);
        right.setPosition(TRANSFER_POS);
    }

    public void transferSpecimen() {
        left.setPosition(TRANSFER_SPEC);
        right.setPosition(TRANSFER_SPEC);
    }

    public void pickSpecimen() {
        left.setPosition(PICK_SPEC);
        right.setPosition(PICK_SPEC);
    }

    public void aimAtSpecimen() {
        left.setPosition(AIM_SPEC);
        right.setPosition(AIM_SPEC);
    }

    public void scoreSpecimen() {
        left.setPosition(SCOR_SPEC);
        right.setPosition(SCOR_SPEC);
    }

    public void dropSampleToHuman() {
        left.setPosition(DROP_SAMPLE_TO_HUMAN);
        right.setPosition(DROP_SAMPLE_TO_HUMAN);
    }
}
