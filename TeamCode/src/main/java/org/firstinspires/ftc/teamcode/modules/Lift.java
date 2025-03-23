package org.firstinspires.ftc.teamcode.modules;

import static org.firstinspires.ftc.teamcode.modules.Lift.LiftConstants.COEFFICIENTS;
import static org.firstinspires.ftc.teamcode.modules.Lift.LiftConstants.MAX_POSITION;
import static org.firstinspires.ftc.teamcode.modules.Lift.LiftConstants.MIN_POSITION;
import static org.firstinspires.ftc.teamcode.modules.Lift.LiftConstants.SPECIMEN_POS;
import static org.firstinspires.ftc.teamcode.modules.Lift.LiftConstants.POWER_DOWN;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Lift {
    private final DcMotorEx leftMotor, rightMotor;
    private final ElapsedTime timer;
    OpMode opMode;
    Gamepad gamepad;
    public double pos;


    public Lift(OpMode opMode) {
        this.opMode = opMode;
        leftMotor = opMode.hardwareMap.get(DcMotorEx.class, "leftLift");
        rightMotor = opMode.hardwareMap.get(DcMotorEx.class, "rightLift");
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        gamepad = opMode.gamepad2;

        timer = new ElapsedTime();
    }

    public enum Mode {
        DEFAULT,
        NULL_POSE,
        PUSH_DOWN
    }

    Mode currentMode = Mode.DEFAULT;
    double reference = 0;
    double lastError = 0;
    double integralSum = 0;
    double out;

    public void PIDControl() {
        double position = leftMotor.getCurrentPosition();
        double error = reference - position;

        double derivative = (error - lastError) / timer.seconds();
        integralSum += error * timer.seconds();
        out = (COEFFICIENTS.p * error) + (COEFFICIENTS.i * integralSum) + (COEFFICIENTS.d * derivative) + COEFFICIENTS.f;

        leftMotor.setPower(out);
        rightMotor.setPower(out);

        lastError = error;

        timer.reset();
    }

    public void PIDControlLEFT() {
        double position = leftMotor.getCurrentPosition();
        double error = reference - position;

        double derivative = (error - lastError) / timer.seconds();
        integralSum += error * timer.seconds();
        out = (COEFFICIENTS.p * error) + (COEFFICIENTS.i * integralSum) + (COEFFICIENTS.d * derivative) + COEFFICIENTS.f;

        leftMotor.setPower(out);

        lastError = error;

        timer.reset();
    }

    public void PIDControlRIGHT() {
        double position = leftMotor.getCurrentPosition();
        double error = reference - position;

        double derivative = (error - lastError) / timer.seconds();
        integralSum += error * timer.seconds();
        out = (COEFFICIENTS.p * error) + (COEFFICIENTS.i * integralSum) + (COEFFICIENTS.d * derivative) + COEFFICIENTS.f;

        rightMotor.setPower(out);

        lastError = error;

        timer.reset();
    }

    public void setPower(double power) {
        leftMotor.setPower(power);
        rightMotor.setPower(power);
    }

    public void resetEncoder() {
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public double getReference() {
        return reference;
    }

    public double getPosition() {
        return leftMotor.getCurrentPosition();
    }

    public void setReference(double reference) {
        reference = Math.min(reference, MAX_POSITION);
        reference = Math.max(reference, MIN_POSITION);
        integralSum = 0;
        this.reference = reference;
    }

    public void setReferenceZero() {
        reference = MIN_POSITION;
        integralSum = 0;
    }

    public void setSpecimen() {
        setReference(SPECIMEN_POS);
    }

    // ?????????????????????????????
    public void setSpecimenTele() {
        setReference(SPECIMEN_POS);
    }


    public void teleop() {
        double error = pos - leftMotor.getCurrentPosition();
        switch (currentMode) {
            case DEFAULT:
                if (gamepad.right_stick_y < 0 && leftMotor.getCurrentPosition() < MAX_POSITION) {
                    leftMotor.setPower(-gamepad.right_stick_y);
                    rightMotor.setPower(-gamepad.right_stick_y);
                    setReference(leftMotor.getCurrentPosition());
                } else if (gamepad.right_stick_y > 0 && leftMotor.getCurrentPosition() > MIN_POSITION) {
                    leftMotor.setPower(-gamepad.right_stick_y);
                    rightMotor.setPower(-gamepad.right_stick_y);
                    setReference(leftMotor.getCurrentPosition());
                } else {
                    clampReference();
                    PIDControl();
                }
                break;
            case NULL_POSE:

                if (gamepad.right_stick_y != 0) {
                    leftMotor.setPower(-gamepad.right_stick_y);
                    rightMotor.setPower(-gamepad.right_stick_y);
                    pos = leftMotor.getCurrentPosition();
                } else if (-15 <= leftMotor.getCurrentPosition() && leftMotor.getCurrentPosition() <= 15) {
                    if (-gamepad.right_stick_y < 0) {
                        leftMotor.setPower(0);
                        rightMotor.setPower(0);
                    } else if (-gamepad.right_stick_y >= 0) {
                        leftMotor.setPower(-gamepad.right_stick_y);
                        rightMotor.setPower(-gamepad.right_stick_y);
                    }
                } else {
                    leftMotor.setPower(-error * LiftConstants.d);
                    rightMotor.setPower(-error * LiftConstants.d);
                }


                break;
            case PUSH_DOWN:
                leftMotor.setPower(POWER_DOWN);
                rightMotor.setPower(POWER_DOWN);
                leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                break;

        }
    }

    private void clampReference() {
        reference = Math.min(reference, MAX_POSITION);
        reference = Math.max(reference, MIN_POSITION);
    }

    @Config
    static
    class LiftConstants {
        public static PIDFCoefficients COEFFICIENTS = new PIDFCoefficients(0.01, 0.005, 0.00001, 0.0);
        public static double d = 0.00047;

        public static int SPECIMEN_POS = 400;

        public static int MAX_POSITION = 830;
        public static int MIN_POSITION = 0;

        public static double POWER_DOWN = -0.6;
    }
}
