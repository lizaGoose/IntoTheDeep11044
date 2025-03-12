package org.firstinspires.ftc.teamcode.modules;


import static org.firstinspires.ftc.teamcode.modules.Claw.ClawConstants.DROP;
import static org.firstinspires.ftc.teamcode.modules.Claw.ClawConstants.LIGHT;
import static org.firstinspires.ftc.teamcode.modules.Claw.ClawConstants.PICK;
import static org.firstinspires.ftc.teamcode.modules.Claw.ClawConstants.TRANSFER;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

public class Claw {
    LinearOpMode linearOpMode;
    Gamepad gamepad1;
    ServoImplEx claw;
    Servo appendix;

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
        appendix = linearOpMode.hardwareMap.get(Servo.class, "claw_appendix_servo");
    }

    public void teleop() {
        switch (currentMode) {
            case PICK:
                claw.setPosition(ClawConstants.PICK);
                appendix.setPosition(ClawConstants.PICK_APPENDIX);
                break;
            case DROP:
                claw.setPosition(ClawConstants.DROP);
                appendix.setPosition(ClawConstants.DROP_APPENDIX);
                break;
            case TRANSFER:
                claw.setPosition(ClawConstants.PICK);
                appendix.setPosition(ClawConstants.TRANSFER_APPENDIX);
                break;
            case LIGHT:
                claw.setPosition(LIGHT);
                appendix.setPosition(ClawConstants.TRANSFER_APPENDIX);
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

    @Config
    static
    class ClawConstants{
        public static double LIGHT = 0.67;
        public static double PICK = 0.67;
        public static double DROP = 0.31;
        public static double TRANSFER = 0.4;
        public static double TRANSFER_APPENDIX = 0.76;
        public static double DROP_APPENDIX = 0.24;
        public static double PICK_APPENDIX = 0.26;//0.24 0.76 0.26
    }
}
