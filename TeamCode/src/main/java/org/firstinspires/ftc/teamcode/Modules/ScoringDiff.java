package org.firstinspires.ftc.teamcode.Modules;

import static org.firstinspires.ftc.teamcode.Modules.ScoringDiffConstants.AIM_SPEC;
import static org.firstinspires.ftc.teamcode.Modules.ScoringDiffConstants.GET_FROM_CLAW;
import static org.firstinspires.ftc.teamcode.Modules.ScoringDiffConstants.MOVE_SPECIMENS;
import static org.firstinspires.ftc.teamcode.Modules.ScoringDiffConstants.PICK_SPEC;
import static org.firstinspires.ftc.teamcode.Modules.ScoringDiffConstants.SCORE_SPEC;
import static org.firstinspires.ftc.teamcode.Modules.ScoringDiffConstants.TRANSFER_SPEC;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.Camera.CameraTest;

@Config
class ScoringDiffConstants {
    /*public static double PICK_POS = 0.96;
    public static double SCOR_POS = 0.5;
    public static double PICK_SPEC = 0.51;
    public static double SCORE_SPEC = 1;*/

    public static double AIM_SPEC = 0.43; //0.39;
    public static double PICK_SPEC = 0.48; //0.44;
    public static double GET_FROM_CLAW = 1;
    public static double SCORE_SAMP = 0.7;
    public static double SCORE_SPEC = 0.99;
    public static double TRANSFER_SPEC = 0.75;
    public static double MOVE_SPECIMENS = 0.9;
}

public class ScoringDiff {
    LinearOpMode linearOpMode;
    Gamepad gamepad1;
    DcMotor extend;
    CameraTest cam;
    ServoImplEx diff;


    enum Mode {
        SCORE_SAMP,
        PICK_POSITION,
        PICK_SPEC,
        SCORE_SPEC,
        GET_FROM_CLAW,
        TRANSFER_SPEC,
        RELAX,
        MOVE_SPECIMENS,
        AIM_POS
    }

    Mode currentMode = Mode.PICK_SPEC;

    public ScoringDiff(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        gamepad1 = linearOpMode.gamepad2;
        diff = (ServoImplEx) linearOpMode.hardwareMap.get(Servo.class, "scoring_diff");
        // right.setDirection(Servo.Direction.REVERSE);
        cam = new CameraTest();
    }

    public void teleop() {
        switch (currentMode) {
        /*    case SCORE_SAMP:
              diff.setPosition(ScoringDiffConstants.SCOR_POS);
                break;
            case PICK_POSITION:
                diff.setPosition(ScoringDiffConstants.PICK_POS);
                break;*/
            case PICK_SPEC:
                diff.setPosition(ScoringDiffConstants.PICK_SPEC);
                break;
            case GET_FROM_CLAW:
                diff.setPosition(GET_FROM_CLAW);
                break;
            case SCORE_SAMP:
                diff.setPosition(ScoringDiffConstants.SCORE_SAMP);
                break;
            case SCORE_SPEC:
                diff.setPosition(ScoringDiffConstants.SCORE_SPEC);
                break;
            case TRANSFER_SPEC:
                diff.setPosition(ScoringDiffConstants.TRANSFER_SPEC);
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
        /*    case SCORE_SPEC:
                diff.setPosition(ScoringDiffConstants.SCORE_SPEC);
                break;*/

        }
    }

    public void transferSample() {
        diff.setPosition(GET_FROM_CLAW);
    }

    public void transferSpecimen() {
        diff.setPosition(TRANSFER_SPEC);
    }

    public void scoreSpecimen() {
        diff.setPosition(SCORE_SPEC);
    }

    public void pickSpecimen() {
        diff.setPosition(PICK_SPEC);
    }

    public void liftSpecimenFromWall() {
        diff.setPosition(AIM_SPEC);
    }

    public void aimSpecimen() {
        diff.setPosition(AIM_SPEC);
    }

    public void moveSpecimens(){diff.setPosition(MOVE_SPECIMENS);}

}
