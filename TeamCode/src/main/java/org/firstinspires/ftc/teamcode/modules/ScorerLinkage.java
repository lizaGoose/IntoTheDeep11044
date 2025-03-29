package org.firstinspires.ftc.teamcode.modules;

import static org.firstinspires.ftc.teamcode.modules.ScorerLinkage.ScorerLinkageConstants.LEBRON;
import static org.firstinspires.ftc.teamcode.modules.ScorerLinkage.ScorerLinkageConstants.SAMPLE;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;


public class ScorerLinkage {
    public final ServoImplEx servo;

    @Config
    public static class ScorerLinkageConstants {
        public static double RETRACTED = 0.05;
        public static double LEBRON = 0.65;
        public static double EXTENDED = 0.94;
        public static double SAMPLE = 0.65;
    }

    public ScorerLinkage(HardwareMap hardwareMap) {
        servo = (ServoImplEx) hardwareMap.get(Servo.class, "Linkage");
    }

    public double getPosition() {
        return servo.getPosition();
    }

    public void extend() {
        servo.setPosition(ScorerLinkageConstants.EXTENDED);
    }

    public void lebron() {
        servo.setPosition(LEBRON);
    }

    public void sample() {
        servo.setPosition(SAMPLE);
    }

    public void retract() {
        servo.setPosition(ScorerLinkageConstants.RETRACTED);
    }

    public void setPwmDisable() {
        servo.setPwmDisable();
    }
}
