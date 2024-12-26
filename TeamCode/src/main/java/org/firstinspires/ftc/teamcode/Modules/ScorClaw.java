package org.firstinspires.ftc.teamcode.Modules;


import static org.firstinspires.ftc.teamcode.Modules.ScorClawConstants.DROP;
import static org.firstinspires.ftc.teamcode.Modules.ScorClawConstants.PICK;
import static org.firstinspires.ftc.teamcode.Modules.ScorClawConstants.VERY_DROP;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@Config
class ScorClawConstants {
    public static double PICK = 0.82;
    public static double DROP = 0.49;
    public static double VERY_DROP = 0.38;
}

public class ScorClaw {
    LinearOpMode linearOpMode;
    Gamepad gamepad1;
    ServoImplEx claw;
//open: 0.51
    //close: 0.76
    enum Mode {
        PICK,
        DROP,
        RELAX
    }
    ScorClaw.Mode currentMode = Mode.PICK;

    public ScorClaw(LinearOpMode linearOpMode){
        this.linearOpMode = linearOpMode;
        gamepad1 = linearOpMode.gamepad2;
        claw = (ServoImplEx) linearOpMode.hardwareMap.get(Servo.class, "scoring_claw");
    }
    public void teleop(){
        switch (currentMode) {
            case PICK:
                claw.setPosition(ScorClawConstants.PICK);
                break;
            case DROP:
                claw.setPosition(DROP);
                break;
            case RELAX:
                claw.setPwmDisable();
                break;
        }

    }

    public void open() {
        claw.setPosition(DROP);
    }

    public void close() {
        claw.setPosition(PICK);
    }

    public void relax() {
        claw.setPwmDisable();
    }

    public void unrelax() {
        claw.setPwmEnable();
    }
}
