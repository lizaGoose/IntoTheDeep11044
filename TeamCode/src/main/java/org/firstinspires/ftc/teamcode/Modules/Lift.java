package org.firstinspires.ftc.teamcode.Modules;

import static org.firstinspires.ftc.teamcode.Modules.LiftConstants.COEFFICIENTS;
import static org.firstinspires.ftc.teamcode.Modules.LiftConstants.MAX_POSITION;
import static org.firstinspires.ftc.teamcode.Modules.LiftConstants.MIN_POSITION;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
class LiftConstants {
    public static PIDFCoefficients COEFFICIENTS = new PIDFCoefficients(0.0, 0.0, 0.0, 0.0);
    public static int MAX_POSITION = 810;
    public static int MIN_POSITION = 0;
}


public class Lift {
    public final DcMotorEx leftMotor, rightMotor;
    private final ElapsedTime timer;
    OpMode linearOpMode;
    Gamepad gamepad;

    public Lift(OpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        leftMotor = linearOpMode.hardwareMap.get(DcMotorEx.class, "leftLift");
        rightMotor = linearOpMode.hardwareMap.get(DcMotorEx.class, "rightLift");
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        gamepad = linearOpMode.gamepad2;

        timer = new ElapsedTime();
    }
    public enum Mode{
        HIGH_BASKET,
        LOW_BASKET,
        HIGH_CHAMBER,
        LOW_CHAMBER,
        NULL_POSE
    }
    Mode currentMode = Mode.NULL_POSE;
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

    public double getReference() {
        return reference;
    }

    public void setReference(double reference) {
        reference = Math.min(reference, MAX_POSITION);
        reference = Math.max(reference, MIN_POSITION);
        this.reference = reference;
    }
    public void  teleop(){
        switch (currentMode){
            case NULL_POSE:
                if (gamepad.right_stick_y !=0) {
                    leftMotor.setPower(gamepad.left_stick_y);
                    rightMotor.setPower(gamepad.left_stick_y);
                    setReference(leftMotor.getCurrentPosition());
                }
                else {
                    PIDControl();
                }
                break;

        }
    }
}
