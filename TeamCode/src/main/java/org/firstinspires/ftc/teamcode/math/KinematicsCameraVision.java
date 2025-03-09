package org.firstinspires.ftc.teamcode.math;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.camera.AutoCamera;
import org.firstinspires.ftc.teamcode.modules.Extend;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Perkid;
import org.firstinspires.ftc.teamcode.modules.ScorerPerkid;

@Config
public class KinematicsCameraVision {
    public double pos, posHand;
    Servo leftDiff, rightDiff;
    public static double minRange = 0.25, maxRange = 0.75, minRange1 = 0.45, maxRange1 = 0.55, k = 60;
    public ScorerPerkid perkid2;
    public Perkid perkid;
    public Extend extend;
    public AutoCamera cam;
    public boolean calculated = false;

    public KinematicsCameraVision(LinearOpMode linearOpMode){
        cam = new AutoCamera(linearOpMode);
        extend = new Extend(linearOpMode);
        perkid = new Perkid(linearOpMode);
        perkid2 = new ScorerPerkid(linearOpMode);
        leftDiff = linearOpMode.hardwareMap.get(Servo.class, "left_diff_servo");
        rightDiff = linearOpMode.hardwareMap.get(Servo.class, "right_diff_servo");

        leftDiff.scaleRange(minRange, maxRange);
        rightDiff.scaleRange(minRange1, maxRange1);
    }
    public void pickFromSub(){
        perkid2.scoreSpecimen();
        cam.operatorControlRed();

        if(cam.pip.xSample > -5 && cam.pip.xSample < 270 && cam.pip.ySample > 70 && cam.pip.ySample < 285) {
            pos = cam.AutonomousRecognition().get(0) - k;
            posHand = 1 - cam.AutonomousRecognition().get(1);
            leftDiff.setPosition(1 - ( Math.abs(cam.getAngle() / 90 - cam.hand.getPosition()) )/* - 0.25*/);
            rightDiff.setPosition(1 - ( Math.abs(cam.getAngle() / 90 - cam.hand.getPosition()) )/* - 0.25*/);
        }
        }
}

