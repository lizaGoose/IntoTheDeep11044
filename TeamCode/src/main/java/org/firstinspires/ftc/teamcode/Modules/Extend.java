package org.firstinspires.ftc.teamcode.Modules;

import static org.firstinspires.ftc.teamcode.Modules.ExtendConstants.COEFFICIENTS;
import static org.firstinspires.ftc.teamcode.Modules.ExtendConstants.MAX_POSITION;
import static org.firstinspires.ftc.teamcode.Modules.ExtendConstants.MIN_POSITION;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Camera.CameraTest;

@Config
class ExtendConstants {
    public static PIDFCoefficients COEFFICIENTS = new PIDFCoefficients(-0.025, 0.0, 0.0, -0.0005);

    public static int MAX_POSITION = 200;
    public static int MIN_POSITION = 8;
}

public class Extend {
    OpMode linearOpMode;
    Gamepad gamepad;
    public final DcMotorEx motor;
    private final ElapsedTime timer;

    enum Mode {
        DRIVER_CONTROL,
        VISION_CONTROL,
        CLOSE
    }

    Extend.Mode currentMode = Mode.DRIVER_CONTROL;

    public Extend(OpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        gamepad = linearOpMode.gamepad2;
        motor = linearOpMode.hardwareMap.get(DcMotorEx.class, "extend_motor");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        timer = new ElapsedTime();
    }

    public void teleop() {
        switch (currentMode) {
            case DRIVER_CONTROL:
                if (gamepad.right_stick_y !=0) {
                    motor.setPower(gamepad.right_stick_y);
                    setReference(motor.getCurrentPosition());
                }
                else {
                    PIDControl();
                }
                break;
            case VISION_CONTROL:
               //itor.setPower(cam.extendingError);
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
        double error = position - reference ;

        double derivative = (error - lastError) / timer.seconds();
        integralSum += error * timer.seconds();
        out = (COEFFICIENTS.p * error) + (COEFFICIENTS.i * integralSum) + (COEFFICIENTS.d * derivative) + COEFFICIENTS.f;

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
}
