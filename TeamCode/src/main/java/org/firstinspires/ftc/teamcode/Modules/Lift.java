package org.firstinspires.ftc.teamcode.Modules;

import static org.firstinspires.ftc.teamcode.Modules.LiftConstants.COEFFICIENTS;
import static org.firstinspires.ftc.teamcode.Modules.LiftConstants.MAX_POSITION;
import static org.firstinspires.ftc.teamcode.Modules.LiftConstants.MIN_POSITION;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Lift {
    public final DcMotorEx leftMotor, rightMotor;
    private final ElapsedTime timer;

    public Lift(HardwareMap hardwareMap) {
        leftMotor = hardwareMap.get(DcMotorEx.class, "leftLift");
        rightMotor = hardwareMap.get(DcMotorEx.class, "rightLift");
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        timer = new ElapsedTime();
    }

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
}
