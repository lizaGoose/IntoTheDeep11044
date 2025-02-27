package org.firstinspires.ftc.teamcode.modules;

import static org.firstinspires.ftc.teamcode.modules.Hang.HangConstants.COEFFICIENTS;
import static org.firstinspires.ftc.teamcode.modules.Hang.HangConstants.MAX_POSITION;
import static org.firstinspires.ftc.teamcode.modules.Hang.HangConstants.MIN_POSITION;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


public class Hang {
    public final DcMotorEx hang;
    private final ElapsedTime timer;
    OpMode linearOpMode;
    Gamepad gamepad;
    Servo left, right;
    public double l, r;


    public Hang(OpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        hang = linearOpMode.hardwareMap.get(DcMotorEx.class, "hangMotor");
        //    left = linearOpMode.hardwareMap.get(Servo.class, "left_lift_servo");
        //  right = linearOpMode.hardwareMap.get(Servo.class, "right_lift_servo");
        hang.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hang.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        gamepad = linearOpMode.gamepad2;

        timer = new ElapsedTime();
    }

    public enum Mode {
        UP_POS,
        DOWN_POS,
        ENGAGED,
        NOT_ENGAGED,
        DEFAULT

    }

    Mode currentMode = Mode.NOT_ENGAGED;
    double reference = MIN_POSITION;
    double lastError = 0;
    double integralSum = 0;
    double out;

    //0.5 - right
//0.58
    public void PIDControl() {
        double position = hang.getCurrentPosition();
        double error = reference - position;

        double derivative = (error - lastError) / timer.seconds();
        integralSum += error * timer.seconds();
        out = (COEFFICIENTS.p * error) + (COEFFICIENTS.i * integralSum) + (COEFFICIENTS.d * derivative) + COEFFICIENTS.f;

        hang.setPower(out);

        lastError = error;

        timer.reset();
    }

    public double getReference() {
        return reference;
    }

    public void clampReference() {
        reference = Math.min(reference, MAX_POSITION);
        reference = Math.max(reference, MIN_POSITION);
    }

    public void setReference(double reference) {
        integralSum = 0;
        this.reference = reference;
        clampReference();
    }

    public void control() {
        if (gamepad.left_stick_y < 0 && hang.getCurrentPosition() < MAX_POSITION) {
            hang.setPower(-gamepad.left_stick_y);
            setReference(hang.getCurrentPosition());
        } else if (gamepad.left_stick_y > 0 && hang.getCurrentPosition() > MIN_POSITION) {
            hang.setPower(-gamepad.left_stick_y);
            setReference(hang.getCurrentPosition());
        } else {
            clampReference();
            PIDControl();
        }
    }

    public void discreteControl() {
        double power = ((gamepad.dpad_up ? 1 : 0) - (gamepad.dpad_down ? 1 : 0));
        if (power > 0 && hang.getCurrentPosition() < MAX_POSITION) {
            hang.setPower(power);
            setReference(hang.getCurrentPosition());
        } else if (power < 0 && hang.getCurrentPosition() > MIN_POSITION) {
            hang.setPower(power);
            setReference(hang.getCurrentPosition());
        } else {
            clampReference();
            PIDControl();
        }
    }

    public void teleop() {

        switch (currentMode) {
            case DOWN_POS:
             /*   hang.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                hang.setTargetPosition(HangConstants.DOWN_POS);*/
                break;
            case UP_POS:
              /*  hang.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                hang.setTargetPosition(HangConstants.UP_POS);*/
                break;
            case ENGAGED:
              /*  left.setPosition(HangConstants.LEFT_ENGAGED);
                right.setPosition(HangConstants.RIGHT_ENGAGED);*/
                timer.reset();

                break;
            case NOT_ENGAGED:
              /*  left.setPosition(HangConstants.LEFT_NOT_ENGAGED);
                right.setPosition(HangConstants.RIGHT_NOT_ENGAGED);*/
                break;
        }
        if (hang.getCurrentPosition() <= MIN_POSITION + 10) {
            if (-(gamepad.left_stick_y) <= 0) {
                hang.setPower(0);
            } else {
                hang.setPower(-(gamepad.left_stick_y));
            }
        } else if (hang.getCurrentPosition() >= MAX_POSITION + 50) {
            if (-(gamepad.left_stick_y) >= 0) {
                hang.setPower(0);
            } else {
                hang.setPower(-(gamepad.left_stick_y));
            }
        } else {
            hang.setPower(-(gamepad.left_stick_y));
        }
    }

    @Config
    static
    class HangConstants {
        public static PIDFCoefficients COEFFICIENTS = new PIDFCoefficients(0.01, 0.0, 0.0, 0.0);
        public static double MAX_POSITION = 3450;
        public static double MIN_POSITION = 10;
        public static int DOWN_POS = 0;
        public static int UP_POS = 200;
        public static double LEFT_ENGAGED = 0.4;//0.24 0.4
        public static double LEFT_NOT_ENGAGED = 0.24;
        public static double RIGHT_ENGAGED = 0.48;
        public static double RIGHT_NOT_ENGAGED = 0.58;
        public static double SPEED = 100;
    }
}
