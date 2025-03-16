package org.firstinspires.ftc.teamcode.modules;

import static org.firstinspires.ftc.teamcode.modules.ScorerDifferential.ScorerDifferentialConstants.AFTER_SCORE_SPEC;
import static org.firstinspires.ftc.teamcode.modules.ScorerDifferential.ScorerDifferentialConstants.AIM_SPEC;
import static org.firstinspires.ftc.teamcode.modules.ScorerDifferential.ScorerDifferentialConstants.GET_FROM_CLAW;
import static org.firstinspires.ftc.teamcode.modules.ScorerDifferential.ScorerDifferentialConstants.LEBRON;
import static org.firstinspires.ftc.teamcode.modules.ScorerDifferential.ScorerDifferentialConstants.MOVE_SPECIMENS;
import static org.firstinspires.ftc.teamcode.modules.ScorerDifferential.ScorerDifferentialConstants.PICK_SPECIMEN;
import static org.firstinspires.ftc.teamcode.modules.ScorerDifferential.ScorerDifferentialConstants.PUSH_INTO_SPECIMEN;
import static org.firstinspires.ftc.teamcode.modules.ScorerDifferential.ScorerDifferentialConstants.SCORE_SAMP;
import static org.firstinspires.ftc.teamcode.modules.ScorerDifferential.ScorerDifferentialConstants.SCORE_SPEC;
import static org.firstinspires.ftc.teamcode.modules.ScorerDifferential.ScorerDifferentialConstants.TRANSFER_SPEC;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.camera.CameraTest;

public class ScorerDifferential {
    LinearOpMode linearOpMode;
    Gamepad gamepad1;
    CameraTest cam;
    ServoImplEx diff;


    enum Mode {
        SCORE_SAMP,
        PICK_SPEC,
        SCORE_SPEC,
        AFTER_SCORE_SPEC,
        GET_FROM_CLAW,
        TRANSFER_SPEC,
        RELAX,
        MOVE_SPECIMENS,
        AIM_POS
    }

    Mode currentMode = Mode.PICK_SPEC;

    public ScorerDifferential(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        gamepad1 = linearOpMode.gamepad2;
        diff = (ServoImplEx) linearOpMode.hardwareMap.get(Servo.class, "scoring_diff");
        cam = new CameraTest();
    }

    public void teleop() {
        switch (currentMode) {
            case PICK_SPEC:
                diff.setPosition(ScorerDifferentialConstants.PICK_SPECIMEN);
                break;
            case GET_FROM_CLAW:
                diff.setPosition(GET_FROM_CLAW);
                break;
            case SCORE_SAMP:
                diff.setPosition(ScorerDifferentialConstants.SCORE_SAMP);
                break;
            case SCORE_SPEC:
                diff.setPosition(ScorerDifferentialConstants.SCORE_SPEC);
                break;
            case TRANSFER_SPEC:
                diff.setPosition(ScorerDifferentialConstants.TRANSFER_SPEC);
                break;
            case RELAX:
                diff.setPwmDisable();
                break;
            case MOVE_SPECIMENS:
                diff.setPosition(MOVE_SPECIMENS);
                break;
            case AIM_POS:
                diff.setPosition(AIM_SPEC);
                break;
            case AFTER_SCORE_SPEC:
                diff.setPosition(AFTER_SCORE_SPEC);
                break;
        }
    }

    public void transferSpecimen() {
        diff.setPosition(TRANSFER_SPEC);
    }

    public void scoreSpecimen() {
        diff.setPosition(SCORE_SPEC);
    }

    public void liftAfterScoreSpecimen() {
        diff.setPosition(AFTER_SCORE_SPEC);
    }

    public void pickSpecimen() {
        diff.setPosition(PICK_SPECIMEN);
    }

    public void pushIntoSpecimen() {
        diff.setPosition(PUSH_INTO_SPECIMEN);
    }

    public void liftSpecimenFromWall() {
        diff.setPosition(AIM_SPEC);
    }

    public void aimSpecimen() {
        diff.setPosition(AIM_SPEC);
    }

    public void scoreSample() {
        diff.setPosition(SCORE_SAMP);
    }

    public void slamDunkSample() {
        diff.setPosition(LEBRON);
    }

    @Config
    static
    class ScorerDifferentialConstants {
        public static double AIM_SPEC = 0.86;
        public static double PICK_SPECIMEN = 0.8;
        public static double PUSH_INTO_SPECIMEN = 0.78;
        public static double GET_FROM_CLAW = 0.3;
        public static double SCORE_SAMP = 0.6;
        public static double LEBRON = 0.4;
        public static double SCORE_SPEC = 0.87;
        public static double AFTER_SCORE_SPEC = 0.83;
        public static double TRANSFER_SPEC = 0.82;
        public static double MOVE_SPECIMENS = 0.9;
    }
}
