package org.firstinspires.ftc.teamcode.Modules;


import static org.firstinspires.ftc.teamcode.Modules.ClawConstants.DROP;
import static org.firstinspires.ftc.teamcode.Modules.ClawConstants.LIGHT;
import static org.firstinspires.ftc.teamcode.Modules.ClawConstants.PICK;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@Config
class ClawConstants{
    public static double LIGHT = 0.605;
    public static double PICK = 0.67;
    public static double DROP = 0.1;
    public static double TRANSFER = 0.37;
}
public class Claw {
    LinearOpMode linearOpMode;
    Gamepad gamepad1;
    Servo claw;

    public enum Mode {
        LIGHT,
        PICK,
        DROP,
        TRANSFER
    }

    public Mode currentMode = Mode.PICK;

    public Claw(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        gamepad1 = linearOpMode.gamepad1;
        claw = linearOpMode.hardwareMap.get(Servo.class, "claw_servo");
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
}
