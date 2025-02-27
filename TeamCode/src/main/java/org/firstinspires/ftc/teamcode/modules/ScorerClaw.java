package org.firstinspires.ftc.teamcode.modules;

import static org.firstinspires.ftc.teamcode.modules.ScorerClaw.ScorerClawConstants.DROP;
import static org.firstinspires.ftc.teamcode.modules.ScorerClaw.ScorerClawConstants.PICK;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;


public class ScorerClaw {
    LinearOpMode linearOpMode;
    Gamepad gamepad1;
    ServoImplEx claw;

    @Config
    public static class ScorerClawConstants {
        public static double PICK = 1;
        public static double DROP = 0.49;
    }

    enum Mode {
        PICK,
        DROP,
        RELAX
    }

    ScorerClaw.Mode currentMode = Mode.PICK;

    public ScorerClaw(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        gamepad1 = linearOpMode.gamepad2;
        claw = (ServoImplEx) linearOpMode.hardwareMap.get(Servo.class, "scoring_claw");
    }

    public void teleop() {
        switch (currentMode) {
            case PICK:
                claw.setPosition(PICK);
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
