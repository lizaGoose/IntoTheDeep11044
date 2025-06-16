package org.firstinspires.ftc.teamcode.modules;


import static org.firstinspires.ftc.teamcode.modules.ScorerPerkid.ScorerPerkidConstants.STRAIGHT;
import static org.firstinspires.ftc.teamcode.modules.ScorerPerkid.ScorerPerkidConstants.TAKE_SAMPLE;
import static org.firstinspires.ftc.teamcode.modules.ScorerPerkid.ScorerPerkidConstants.UP;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

public class ScorerPerkid {
    OpMode opMode;
    Gamepad gamepad1;
    Servo left, right;

    enum Mode {
        PICK_SPECIMEN,
        SCORE_SAMPLE,
        SCORE_SPECIMEN,
        TRANSFER_SAMPLE
    }

    Mode currentMode = Mode.PICK_SPECIMEN;

    public ScorerPerkid(OpMode opMode) {
        this.opMode = opMode;
        gamepad1 = opMode.gamepad1;
        left = opMode.hardwareMap.get(Servo.class, "left_scor_perkid");
        right = opMode.hardwareMap.get(Servo.class, "right_scor_perkid");
        right.setDirection(Servo.Direction.REVERSE);
    }

    public void teleop() {
        switch (currentMode) {
            case PICK_SPECIMEN:
                setAngle(ScorerPerkidConstants.PICK_SPECIMEN);
                break;
            case SCORE_SAMPLE:
                setAngle(ScorerPerkidConstants.SCORE_SAMPLE);
                break;
            case SCORE_SPECIMEN:
                setAngle(ScorerPerkidConstants.SCORE_SPECIMEN_1);
                break;
            case TRANSFER_SAMPLE:
                setAngle(ScorerPerkidConstants.TRANSFER_SAMPLE);
                break;
        }
    }

    public void pickSpecimen() {
        setAngle(ScorerPerkidConstants.PICK_SPECIMEN);
    }

    @Deprecated
    public void pushIntoSpecimen() {
        setAngle(ScorerPerkidConstants.PICK_SPECIMEN);
    }

    @Deprecated
    public void aimAtSpecimen() {
        setAngle(ScorerPerkidConstants.PICK_SPECIMEN);
    }

    public void posBetween() {
        setAngle(ScorerPerkidConstants.SCORE_SPECIMEN_0);
    }

    public void scoreSpecimen() {
        setAngle(ScorerPerkidConstants.SCORE_SPECIMEN_1);
    }

    public void scoreSample() {
        setAngle(ScorerPerkidConstants.SCORE_SAMPLE);
    }

    public void transferSample() {
        setAngle(ScorerPerkidConstants.TRANSFER_SAMPLE);
    }

    public void takeSample() {
        setAngle(ScorerPerkidConstants.TAKE_SAMPLE);
    }

    public double getPosition() {
        return left.getPosition();
    }

    public void setAngle(double angle) {
        double position = ( angle / 90 ) * ( UP - STRAIGHT ) - ( UP - 2 * STRAIGHT );

        left.setPosition(position);
        right.setPosition(position);
    }

    public double setAngleDebug(double angle) {
        return ( angle / 90 ) * ( UP - STRAIGHT ) - ( UP - 2 * STRAIGHT );
    }

    public double getAngle() {
        double position = getPosition();
        double US = 1 / ( UP - STRAIGHT );
        return ( position * US + ( UP - 2 * STRAIGHT ) * US ) * 90;
    }


    @Config
    static class ScorerPerkidConstants {
        // ANGLES
        public static double PICK_SPECIMEN = 324;
        public static double SCORE_SAMPLE = 205;
        public static double SCORE_SPECIMEN_0 = 270;
        public static double SCORE_SPECIMEN_1 = 105;
        public static double TRANSFER_SAMPLE = 85;
        public static double TAKE_SAMPLE = 60;

        // POSITIONS
        public static double UP = 0.56;
        public static double STRAIGHT = 0.8065;
    }
}
