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
class ScoringDiffConstants {
    public static double PICK_POS = 0.92;
    public static double SCOR_POS = 0.5;
    public static double PICK_SPEC = 0.4;
    public static double SCORE_SPEC = 0.6;
}

public class ScoringDiff {
    LinearOpMode linearOpMode;
    Gamepad gamepad1;
    DcMotor extend;
    CameraTest cam;
    Servo diff;

    enum Mode {
        SCORE_SAMP,
        PICK_POSITION,
        PICK_SPEC,
        SCORE_SPEC
    }

    Mode currentMode = Mode.PICK_POSITION;

    public ScoringDiff(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        gamepad1 = linearOpMode.gamepad2;
        diff = linearOpMode.hardwareMap.get(Servo.class, "scoring_diff");
       // right.setDirection(Servo.Direction.REVERSE);
        cam = new CameraTest();
    }

    public void teleop() {
        switch (currentMode) {
            case SCORE_SAMP:
              diff.setPosition(ScoringDiffConstants.SCOR_POS);
                break;
            case PICK_POSITION:
                diff.setPosition(ScoringDiffConstants.PICK_POS);
                break;
            case PICK_SPEC:
                diff.setPosition(ScoringDiffConstants.PICK_SPEC);
                break;
            case SCORE_SPEC:
                diff.setPosition(ScoringDiffConstants.SCORE_SPEC);
                break;

        }

    }
}
