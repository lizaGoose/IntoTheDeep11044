package org.firstinspires.ftc.teamcode.Modules;


import static org.firstinspires.ftc.teamcode.Modules.ClawConstants.DROP;
import static org.firstinspires.ftc.teamcode.Modules.ClawConstants.LIGHT;
import static org.firstinspires.ftc.teamcode.Modules.ClawConstants.PICK;
import static org.firstinspires.ftc.teamcode.Modules.ClawConstants.TRANSFER;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@Config
class ClawConstants{
    public static double LIGHT = 0.67;
    public static double PICK = 0.67;
    public static double DROP = 0.31;
    public static double TRANSFER = 0.4;
}
public class Claw {
    LinearOpMode linearOpMode;
    Gamepad gamepad1;
    ServoImplEx claw;

    public enum Mode {
        LIGHT,
        PICK,
        DROP,
        TRANSFER,
        RELAX
    }

    public Mode currentMode = Mode.PICK;

    public Claw(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        gamepad1 = linearOpMode.gamepad1;
        claw = (ServoImplEx) linearOpMode.hardwareMap.get(Servo.class, "claw_servo");
    }

    public void teleop() {
        switch (currentMode) {
            case PICK:
                claw.setPosition(ClawConstants.PICK);
                break;
            case DROP:
                claw.setPosition(ClawConstants.DROP);
                break;
            case TRANSFER:
                claw.setPosition(ClawConstants.TRANSFER);
                break;
            case LIGHT:
                claw.setPosition(LIGHT);
                break;
            case RELAX:
                claw.setPwmDisable();
                break;
        }
    }

    public void light() {
        claw.setPosition(LIGHT);
    }

    public void open() {
        claw.setPosition(DROP);
    }

    public void close() {
        claw.setPosition(PICK);
    }

    public void transfer() {
        claw.setPosition(TRANSFER);
    }
}
