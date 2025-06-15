package org.firstinspires.ftc.teamcode.modules;


import static org.firstinspires.ftc.teamcode.modules.Claw.ClawConstants.DROP_APPENDIX;
import static org.firstinspires.ftc.teamcode.modules.Claw.ClawConstants.LIGHT;
import static org.firstinspires.ftc.teamcode.modules.Claw.ClawConstants.OPEN;
import static org.firstinspires.ftc.teamcode.modules.Claw.ClawConstants.CLOSED;
import static org.firstinspires.ftc.teamcode.modules.Claw.ClawConstants.PICK_APPENDIX;
import static org.firstinspires.ftc.teamcode.modules.Claw.ClawConstants.SOME_APPENDIX;
import static org.firstinspires.ftc.teamcode.modules.Claw.ClawConstants.STRANGLE;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

public class Claw {
    OpMode opMode;
    Gamepad gamepad1;
    ServoImplEx claw;
    Servo appendix;

    public enum Mode {
        PICK,
        DROP,
        HALF_DROP,
        TRANSFER,
        TRANSFER_LIGHT,
        PICK_LIGHT,
        RELAX
    }

    public Mode currentMode = Mode.PICK;

    public Claw(OpMode opMode) {
        this.opMode = opMode;
        gamepad1 = opMode.gamepad1;
        claw = (ServoImplEx) opMode.hardwareMap.get(Servo.class, "claw_servo");
        appendix = opMode.hardwareMap.get(Servo.class, "claw_appendix_servo");
    }

    public void teleop() {
        switch (currentMode) {
            case PICK:
                claw.setPosition(ClawConstants.CLOSED);
                appendix.setPosition(ClawConstants.SOME_APPENDIX);
                break;
            case DROP:
                claw.setPosition(ClawConstants.OPEN);
                appendix.setPosition(ClawConstants.DROP_APPENDIX);
                break;
            case TRANSFER:
                claw.setPosition(ClawConstants.STRANGLE);
                appendix.setPosition(ClawConstants.PICK_APPENDIX);
                break;
            case TRANSFER_LIGHT:
                claw.setPosition(LIGHT);
                appendix.setPosition(PICK_APPENDIX);
                break;
            case PICK_LIGHT:
                claw.setPosition(LIGHT);
                appendix.setPosition(SOME_APPENDIX);
            case RELAX:
                claw.setPwmDisable();
                break;
        }
    }

    public void open() {
        claw.setPosition(OPEN);
        appendix.setPosition(DROP_APPENDIX);
    }

    public void close() {
        claw.setPosition(CLOSED);
        appendix.setPosition(SOME_APPENDIX);
    }

    public void transfer() {
        claw.setPosition(STRANGLE);
        appendix.setPosition(PICK_APPENDIX);
    }

    public void transferLight() {
        claw.setPosition(LIGHT);
        appendix.setPosition(PICK_APPENDIX);
    }

    public void closeAppendix() {
        appendix.setPosition(SOME_APPENDIX);
    }

    public void openAppendix() {
        appendix.setPosition(PICK_APPENDIX);
    }

    @Config
    static
    class ClawConstants{
        public static double CLOSED = 0.6;
        public static double LIGHT = 0.585;
        public static double OPEN = 0.1;
        public static double STRANGLE = 0.63;
        public static double DROP_APPENDIX = 0.325;
        public static double SOME_APPENDIX = 0.37;
        public static double PICK_APPENDIX = 0.845;
    }
}
