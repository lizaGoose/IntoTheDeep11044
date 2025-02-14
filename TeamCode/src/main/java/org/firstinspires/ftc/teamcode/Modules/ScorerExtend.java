package org.firstinspires.ftc.teamcode.Modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;


public class ScorerExtend {
    public final ServoImplEx servo;

    @Config
    public static class Constants {
        public static double RETRACTED = 0;
        public static double EXTENDED = 1;
    }

    public ScorerExtend(HardwareMap hardwareMap) {
        servo = (ServoImplEx) hardwareMap.get(Servo.class, "servo_extend");
    }

    public double getPosition() {
        return servo.getPosition();
    }

    public void extend() {
        servo.setPosition(Constants.EXTENDED);
    }

    public void retract() {
        servo.setPosition(Constants.RETRACTED);
    }

    public void setPwmDisable() {
        servo.setPwmDisable();
    }
}
