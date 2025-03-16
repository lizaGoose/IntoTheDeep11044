package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

public class SampleClawSubsystem {

    private DcMotorEx linkageMotor;
    private ServoImplEx leftVerticalServo, rightVerticalServo;
    private ServoImplEx horizontalServo;
    private ServoImplEx leftDiffServo, rightDiffServo;
    private ServoImplEx clawServo;

    public SampleClawSubsystem(HardwareMap hardwareMap) {
        linkageMotor = hardwareMap.get(DcMotorEx.class, "extend_motor");
        leftVerticalServo = (ServoImplEx) hardwareMap.get(Servo.class, "left_vertical_servo");
        rightVerticalServo = (ServoImplEx) hardwareMap.get(Servo.class, "right_vertical_servo");
        horizontalServo = (ServoImplEx) hardwareMap.get(Servo.class, "horizontal_servo");
        leftDiffServo = (ServoImplEx) hardwareMap.get(Servo.class, "left_diff_servo");
        rightDiffServo = (ServoImplEx) hardwareMap.get(Servo.class, "right_diff_servo");
    }
}
