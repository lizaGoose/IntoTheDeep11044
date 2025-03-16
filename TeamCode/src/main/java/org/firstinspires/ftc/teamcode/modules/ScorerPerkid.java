package org.firstinspires.ftc.teamcode.modules;


import static org.firstinspires.ftc.teamcode.modules.Perkid.PerkidConstants.TRANSFER_POS;
import static org.firstinspires.ftc.teamcode.modules.ScorerPerkid.ScorerPerkidConstants.AIM_SPEC;
import static org.firstinspires.ftc.teamcode.modules.ScorerPerkid.ScorerPerkidConstants.DROP_SAMPLE_TO_HUMAN;
import static org.firstinspires.ftc.teamcode.modules.ScorerPerkid.ScorerPerkidConstants.EXTRA_PICK_SPEC;
import static org.firstinspires.ftc.teamcode.modules.ScorerPerkid.ScorerPerkidConstants.PICK_SPEC;
import static org.firstinspires.ftc.teamcode.modules.ScorerPerkid.ScorerPerkidConstants.SCORE_SAMP;
import static org.firstinspires.ftc.teamcode.modules.ScorerPerkid.ScorerPerkidConstants.SCOR_SPEC;
import static org.firstinspires.ftc.teamcode.modules.ScorerPerkid.ScorerPerkidConstants.TRANSFER_SPEC;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.camera.CameraTest;

public class ScorerPerkid {
    OpMode opMode;
    Gamepad gamepad1;
    DcMotor extend;
    CameraTest cam;
    Servo left, right;

    enum Mode {
        PICK_SPEC,
        TRANSFER,
        SCORE_SAMP,
        TRANSFER_SPEC,
        SCOR_SPEC

    }

    Mode currentMode = Mode.PICK_SPEC;

    public ScorerPerkid(OpMode opMode) {
        this.opMode = opMode;
        gamepad1 = opMode.gamepad1;
        left = opMode.hardwareMap.get(Servo.class, "left_scor_perkid");
        right = opMode.hardwareMap.get(Servo.class, "right_scor_perkid");
        right.setDirection(Servo.Direction.REVERSE);
        cam = new CameraTest();
    }

    public void teleop() {
        switch (currentMode) {
            case PICK_SPEC:
                left.setPosition(ScorerPerkidConstants.PICK_SPEC);
                right.setPosition(ScorerPerkidConstants.PICK_SPEC);
                break;
            case TRANSFER:
                left.setPosition(ScorerPerkidConstants.TRANSFER_POS);
                right.setPosition(ScorerPerkidConstants.TRANSFER_POS);
                break;
            case SCORE_SAMP:
                left.setPosition(ScorerPerkidConstants.SCORE_SAMP);
                right.setPosition(ScorerPerkidConstants.SCORE_SAMP);
                break;
            case TRANSFER_SPEC:
                left.setPosition(ScorerPerkidConstants.TRANSFER_SPEC);
                right.setPosition(ScorerPerkidConstants.TRANSFER_SPEC);
                break;
            case SCOR_SPEC:
                left.setPosition(ScorerPerkidConstants.SCOR_SPEC);
                right.setPosition(ScorerPerkidConstants.SCOR_SPEC);
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

    public void pushIntoSpecimen() {
        left.setPosition(EXTRA_PICK_SPEC);
        right.setPosition(EXTRA_PICK_SPEC);
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

    public void scoreSample() {
        left.setPosition(SCORE_SAMP);
        right.setPosition(SCORE_SAMP);
    }
    public double getPos(){
        return  left.getPosition();
    }

    @Config
    static
    class ScorerPerkidConstants {
        public static double PICK_SPEC = 0.37;
        public static double EXTRA_PICK_SPEC = 0.33;
        public static double AIM_SPEC = 0.45;
        public static double DROP_SAMPLE_TO_HUMAN = 0.35;
        public static double TRANSFER_POS = 0.5;
        public static double SCORE_SAMP = 0;
        public static double TRANSFER_SPEC = 0.845;
        public static double SCOR_SPEC = 0.845;
    }
}
