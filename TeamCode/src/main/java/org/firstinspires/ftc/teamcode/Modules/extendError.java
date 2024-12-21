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
class ExtendErrorConstants{
    public static double d = 0.03;
}

public class extendError {
    OpMode linearOpMode;
    Gamepad gamepad;
    public final DcMotorEx motor;
    public double pos;


    public extendError(OpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        motor = linearOpMode.hardwareMap.get(DcMotorEx.class, "extend_motor");
        gamepad = linearOpMode.gamepad2;

        //   timer = new ElapsedTime();
    }

    public enum Mode {
        P_REGULATOR,
        NULL_POSE
    }

    Mode currentMode = Mode.NULL_POSE;
    double reference = 0;
    double lastError = 0;
    double integralSum = 0;
    double out;

    /*public void PIDControl() {
        double position = leftMotor.getCurrentPosition();
        double error = reference - position;

        double derivative = ( error - lastError ) / timer.seconds();
        integralSum += error * timer.seconds();
        out = ( COEFFICIENTS.p * error ) + ( COEFFICIENTS.i * integralSum ) + ( COEFFICIENTS.d * derivative ) + COEFFICIENTS.f;

        leftMotor.setPower(out);
        rightMotor.setPower(out);

        lastError = error;

        timer.reset();
    }*/

    public double getReference() {
        return reference;
    }

    public void setReference(double reference) {
        reference = Math.min(reference, MAX_POSITION);
        reference = Math.max(reference, MIN_POSITION);
        this.reference = reference;
    }

    public void teleop() {
        double error = pos - motor.getCurrentPosition();
        switch (currentMode) {
            case NULL_POSE:

                if (-gamepad.left_stick_y != 0){
                   motor.setPower(-gamepad.left_stick_y);
                    pos = motor.getCurrentPosition();
                }
                else{
                    motor.setPower(error*ExtendErrorConstants.d);
                }

                break;
            case P_REGULATOR:
             /*   if(gamepad.left_stick_y !=0){
                    leftMotor.setPower(-gamepad.right_stick_y);
                    rightMotor.setPower(-gamepad.right_stick_y);
                    pos = rightMotor.getCurrentPosition();
                }
                else {
                    //  double error = pos - rightMotor.getCurrentPosition();
                    leftMotor.setPower(-error);
                    rightMotor.setPower(-error);
                }
                if (rightMotor.getCurrentPosition() >= 0){
                    if(gamepad.left_stick_y >=0){

                    }
                }*/
                break;


        }
    }
}
