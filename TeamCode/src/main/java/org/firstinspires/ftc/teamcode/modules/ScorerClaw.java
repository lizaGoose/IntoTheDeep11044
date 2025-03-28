package org.firstinspires.ftc.teamcode.modules;

import static org.firstinspires.ftc.teamcode.modules.ScorerClaw.ScorerClawConstants.HALF_OPEN;
import static org.firstinspires.ftc.teamcode.modules.ScorerClaw.ScorerClawConstants.OPEN;
import static org.firstinspires.ftc.teamcode.modules.ScorerClaw.ScorerClawConstants.CLOSE;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;


public class ScorerClaw {
    OpMode opMode;
    Gamepad gamepad1;
    ServoImplEx claw;

    @Config
    public static class ScorerClawConstants {
        public static double CLOSE = 1;
        public static double OPEN = 0.53;
        public static double HALF_OPEN = 0.7;
    }

    enum Mode {
        PICK,
        DROP,
        HALF_DROP,
        RELAX
    }

    ScorerClaw.Mode currentMode = Mode.PICK;

    public ScorerClaw(OpMode opMode) {
        this.opMode = opMode;
        gamepad1 = this.opMode.gamepad2;
        claw = (ServoImplEx) this.opMode.hardwareMap.get(Servo.class, "scoring_claw");
    }

    public void teleop() {
        switch (currentMode) {
            case PICK:
                claw.setPosition(CLOSE);
                break;
            case DROP:
                claw.setPosition(OPEN);
                break;
            case RELAX:
                claw.setPwmDisable();
                break;
            case HALF_DROP:
                claw.setPosition(HALF_OPEN);
                break;
        }

    }

    public void open() {
        claw.setPosition(OPEN);
    }

    public void close() {
        claw.setPosition(CLOSE);
    }

    public void relax() {
        claw.setPwmDisable();
    }

    public void unrelax() {
        claw.setPwmEnable();
    }
}
