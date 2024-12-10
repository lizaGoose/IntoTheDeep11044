package org.firstinspires.ftc.teamcode.Modules;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

public class ScorClaw {
    LinearOpMode linearOpMode;
    Gamepad gamepad1;
    Servo claw;
//open: 0.51
    //close: 0.76
    enum Mode {
        PICK,
        DROP
    }
    ScorClaw.Mode currentMode = Mode.PICK;

    public ScorClaw(LinearOpMode linearOpMode){
        this.linearOpMode = linearOpMode;
        gamepad1 = linearOpMode.gamepad2;
        claw = linearOpMode.hardwareMap.get(Servo.class, "scoring_claw");
    }
    public void teleop(){
        switch (currentMode) {
            case PICK:
                claw.setPosition(0.76);
                break;
            case DROP:
                claw.setPosition(0.56);
                break;
        }

    }
}
