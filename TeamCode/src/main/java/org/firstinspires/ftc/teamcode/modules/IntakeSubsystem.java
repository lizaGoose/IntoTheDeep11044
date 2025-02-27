package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.math.ClawCalculation;

import java.util.List;

public class IntakeSubsystem {
    private final Servo verticalServoLeft, verticalServoRight;
    private final Servo horizontalServo, differentialServoLeft, differentialServoRight;
    private final DcMotorEx extendMotor;
    private final double clawOpen = 0.6;
    private final double clawClose = 0.05;
    // 20 : 16 ratio


    public IntakeSubsystem(HardwareMap hardwareMap) {
        verticalServoLeft = hardwareMap.get(Servo.class, "verticalServoLeft");
        verticalServoRight = hardwareMap.get(Servo.class, "verticalServoRight");
        verticalServoRight.setDirection(Servo.Direction.REVERSE);

        horizontalServo = hardwareMap.get(Servo.class, "horizontalServo");
        differentialServoLeft = hardwareMap.get(Servo.class, "diffLeft");
        differentialServoRight = hardwareMap.get(Servo.class, "diffRight");

        extendMotor = hardwareMap.get(DcMotorEx.class, "extendMotor");
    }

    public void updateTest() {
        double x = 0;
        double y = 0.0243;
        double z = 0.12745;

        List<Double> result = ClawCalculation.inverseKinematics(x, y, z);

        double theta1 = result.get(1);
        double theta2 = result.get(2);
        double theta3 = result.get(3);
        double theta4 = 0;

        verticalServoLeft.setPosition(theta1);
        verticalServoRight.setPosition(-ClawCalculation.angleToServoPos(theta2, 90));
        horizontalServo.setPosition(theta2);

    }
}
