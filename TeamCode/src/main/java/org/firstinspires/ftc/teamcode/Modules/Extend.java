package org.firstinspires.ftc.teamcode.Modules;

import static org.firstinspires.ftc.teamcode.Modules.ExtendConstants.COEFFICIENTS;
import static org.firstinspires.ftc.teamcode.Modules.ExtendConstants.MAX_POSITION;
import static org.firstinspires.ftc.teamcode.Modules.ExtendConstants.MIN_POSITION;
import static org.firstinspires.ftc.teamcode.Modules.ExtendConstants.TICKS_PER_TAP;
import static org.firstinspires.ftc.teamcode.Modules.ExtendConstants.TRANSFER;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
class ExtendConstants {
    public static PIDFCoefficients COEFFICIENTS = new PIDFCoefficients(-0.018, 0.0, -0.000004, 0.015);
    public static double TICKS_PER_TAP = 30;

    public static int MAX_POSITION = 250;
    public static int MIN_POSITION = 0;
    public static int TRANSFER = 60;
}

public class Extend {
    OpMode linearOpMode;
    Gamepad gamepad, gamepad2;
    public final DcMotorEx motor;
    private final ElapsedTime timer;
    boolean lastTriangleState = false, lastCrossState = false;
    public double setNewReference = 0;


    enum Mode {
        DRIVER_CONTROL,
        VISION_CONTROL,
        CLOSE,
        PURE_PID
    }

    Extend.Mode currentMode = Mode.DRIVER_CONTROL;

    public Extend(OpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        gamepad = linearOpMode.gamepad1;
        gamepad2 = linearOpMode.gamepad2;
        motor = linearOpMode.hardwareMap.get(DcMotorEx.class, "extend_motor");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        timer = new ElapsedTime();
    }

    public void teleop() {
        switch (currentMode) {
            case DRIVER_CONTROL:
                if (gamepad.triangle && !lastTriangleState) {
                    // setReference(motor.getCurrentPosition() + TICKS_PER_TAP);
                    setNewReference = motor.getCurrentPosition() + TICKS_PER_TAP;
                }

                lastTriangleState = gamepad.triangle;
                if (gamepad.cross && !lastCrossState) {
                    // setReference(motor.getCurrentPosition() - TICKS_PER_TAP);
                    setNewReference = motor.getCurrentPosition() - TICKS_PER_TAP;
                }

                lastTriangleState = gamepad.cross;

                if (gamepad2.right_stick_y != 0) {
                    motor.setPower(-gamepad2.right_stick_y);
                    //setReference(motor.getCurrentPosition());
                    setNewReference = motor.getCurrentPosition();
                } else {
                    if (!( gamepad.triangle && !lastTriangleState ) && !( gamepad.cross && !lastCrossState ))
                        setReference(setNewReference);
                    PIDControl();
                }
                break;
            case PURE_PID:
                setNewReference = reference;
                PIDControl();
                break;
            case VISION_CONTROL:
                //itor.setPower(cam.extendingError);
                PIDControl();
                break;
            case CLOSE:
                motor.setTargetPosition(0);
                break;
        }
        if (gamepad.right_stick_y != 0) {
            currentMode = Mode.DRIVER_CONTROL;
        }

    }

    double reference = 0;
    double lastError = 0;
    double integralSum = 0;
    double out;

    public void PIDControl() {
        double position = motor.getCurrentPosition();
        double error = position - reference;

        double derivative = ( error - lastError ) / timer.seconds();
        integralSum += error * timer.seconds();
        out = ( COEFFICIENTS.p * error ) + ( COEFFICIENTS.i * integralSum ) + ( COEFFICIENTS.d * derivative ) + COEFFICIENTS.f;

        motor.setPower(out);

        lastError = error;

        timer.reset();
    }

    public double getReference() {
        return reference;
    }

    public void setReference(double reference) {
        reference = Math.min(reference, MAX_POSITION);
        reference = Math.max(reference, MIN_POSITION);
        this.reference = reference;
    }

    public void setTransfer() {
        reference = TRANSFER;
    }
}
