package org.firstinspires.ftc.teamcode.modules;


import static org.firstinspires.ftc.teamcode.modules.ScorerPerkid.ScorerPerkidConstants.SCORE_SPECIMEN;
import static org.firstinspires.ftc.teamcode.modules.ScorerPerkid.ScorerPerkidConstants.STRAIGHT;
import static org.firstinspires.ftc.teamcode.modules.ScorerPerkid.ScorerPerkidConstants.UP;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.camera.CameraTest;

public class ScorerPerkid {
    OpMode opMode;
    Gamepad gamepad1;
    CameraTest cam;
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
        cam = new CameraTest();
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
                setAngle(ScorerPerkidConstants.SCORE_SPECIMEN);
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

    public void scoreSpecimen() {
        setAngle(SCORE_SPECIMEN);
    }

    public void scoreSample() {
        setAngle(ScorerPerkidConstants.SCORE_SAMPLE);
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

    // U * k + b = 2

    public double getAngle() {
        double position = getPosition();
        double US = 1 / ( UP - STRAIGHT );
        return ( position * US - ( UP - 2 * STRAIGHT ) * US ) * 90;
    }


    @Config
    static
    class ScorerPerkidConstants {
        // ANGLES
        public static double PICK_SPECIMEN = 325;
        public static double SCORE_SAMPLE = 225;
        public static double SCORE_SPECIMEN = 110;
        public static double TRANSFER_SAMPLE = 80;

        // POSITIONS
        public static double UP = 0.56;
        public static double STRAIGHT = 0.82;
    }
}
