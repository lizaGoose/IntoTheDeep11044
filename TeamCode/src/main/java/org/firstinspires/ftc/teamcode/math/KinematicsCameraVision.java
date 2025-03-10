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
    public double x, y;
    public double xRomaniaMin, xRomaniaMax, yRomaniaMin, yRomaniaMax;

    public KinematicsCameraVision(LinearOpMode linearOpMode) {
        cam = new AutoCamera(linearOpMode);
        extend = new Extend(linearOpMode);
        perkid = new Perkid(linearOpMode);
        perkid2 = new ScorerPerkid(linearOpMode);
        leftDiff = linearOpMode.hardwareMap.get(Servo.class, "left_diff_servo");
        rightDiff = linearOpMode.hardwareMap.get(Servo.class, "right_diff_servo");

        leftDiff.scaleRange(minRange, maxRange);
        rightDiff.scaleRange(minRange1, maxRange1);
    }

    public void pickFromSub(String mode) {
        perkid2.scoreSpecimen();
        cam.operatorControlRed();
        pos = cam.AutonomousRecognition().get(0) - k;
        posHand = 1 - cam.AutonomousRecognition().get(1);
        leftDiff.setPosition(1 - ( Math.abs(cam.getAngle() / 90 - cam.hand.getPosition()) ));
        rightDiff.setPosition(1 - ( Math.abs(cam.getAngle() / 90 - cam.hand.getPosition()) ));
/*

        if(mode == "Red"){

            cam.operatorControlRed();
            x = cam.pip.xSample;
            y = cam.pip.ySample;
            cam.pip.Yellow.add(0);
            cam.pip.Blue.add(0);
            for (double i = 0; i < cam.pip.Yellow.size(); i++) {
                cam.pip.Yellow.remove(0);
                cam.operatorControlYellow();
                if ((Math.abs(x-cam.pip.Yellow.get((int) i)) <= xRomaniaMax && Math.abs(x-cam.pip.Yellow.get((int) i)) >=xRomaniaMin)||(Math.abs(y-cam.pip.Yellow.get((int) i))<= yRomaniaMax && Math.abs(y-cam.pip.Yellow.get((int) i))>=yRomaniaMin)){
                    cam.operatorControlRed();
                    x = cam.pip.Center.get(cam.pip.Center.indexOf(x)+1);
                    y = cam.pip.Center.get(cam.pip.Center.indexOf(y)+1);
                }
            }
            for (double i = 0; i < cam.pip.Blue.size(); i++) {
                cam.pip.Blue.remove(0);
                cam.operatorControlBlue();
                if ((Math.abs(x-cam.pip.Blue.get((int) i)) <= xRomaniaMax && Math.abs(x-cam.pip.Blue.get((int) i)) >=xRomaniaMin)||(Math.abs(y-cam.pip.Blue.get((int) i))<= yRomaniaMax && Math.abs(y-cam.pip.Blue.get((int) i))>=yRomaniaMin)){
                    cam.operatorControlRed();
                    x = cam.pip.Center.get(cam.pip.Center.indexOf(x)+1);
                    y = cam.pip.Center.get(cam.pip.Center.indexOf(y)+1);
                }
            }
        }
        else if(mode == "Blue"){
            cam.operatorControlBlue();
            x = cam.pip.xSample;
            y = cam.pip.ySample;
            cam.pip.Yellow.add(0);
            cam.pip.Red.add(0);
            for (double i = 0; i < cam.pip.Yellow.size(); i++) {
                cam.pip.Yellow.remove(0);
                cam.operatorControlYellow();
                if ((Math.abs(x-cam.pip.Yellow.get((int) i)) <= xRomaniaMax && Math.abs(x-cam.pip.Yellow.get((int) i)) >=xRomaniaMin)||(Math.abs(y-cam.pip.Yellow.get((int) i))<= yRomaniaMax && Math.abs(y-cam.pip.Yellow.get((int) i))>=yRomaniaMin)){
                    cam.operatorControlBlue();
                    x = cam.pip.Center.get(cam.pip.Center.indexOf(x)+1);
                    y = cam.pip.Center.get(cam.pip.Center.indexOf(y)+1);
                }
            }
            for (double i = 0; i < cam.pip.Red.size(); i++) {
                cam.pip.Red.remove(0);
                cam.operatorControlRed();
                if ((Math.abs(x-cam.pip.Red.get((int) i)) <= xRomaniaMax && Math.abs(x-cam.pip.Red.get((int) i)) >=xRomaniaMin)||(Math.abs(y-cam.pip.Red.get((int) i))<= yRomaniaMax && Math.abs(y-cam.pip.Red.get((int) i))>=yRomaniaMin)){
                    cam.operatorControlBlue();
                    x = cam.pip.Center.get(cam.pip.Center.indexOf(x)+1);
                    y = cam.pip.Center.get(cam.pip.Center.indexOf(y)+1);
                }
            }
        }
        else if(mode == "Yellow"){
            cam.operatorControlYellow();
            x = cam.pip.xSample;
            y = cam.pip.ySample;
            cam.pip.Red.add(0);
            cam.pip.Blue.add(0);
            for (double i = 0; i < cam.pip.Red.size(); i++) {
                cam.pip.Red.remove(0);
                cam.operatorControlRed();
                if ((Math.abs(x-cam.pip.Red.get((int) i)) <= xRomaniaMax && Math.abs(x-cam.pip.Red.get((int) i)) >=xRomaniaMin)||(Math.abs(y-cam.pip.Red.get((int) i))<= yRomaniaMax && Math.abs(y-cam.pip.Red.get((int) i))>=yRomaniaMin)){
                    cam.operatorControlYellow();
                    x = cam.pip.Center.get(cam.pip.Center.indexOf(x)+1);
                    y = cam.pip.Center.get(cam.pip.Center.indexOf(y)+1);
                }
            }
            for (double i = 0; i < cam.pip.Blue.size(); i++) {
                cam.pip.Blue.remove(0);
                cam.operatorControlBlue();
                if ((Math.abs(x-cam.pip.Blue.get((int) i)) <= xRomaniaMax && Math.abs(x-cam.pip.Blue.get((int) i)) >=xRomaniaMin)||(Math.abs(y-cam.pip.Blue.get((int) i))<= yRomaniaMax && Math.abs(y-cam.pip.Blue.get((int) i))>=yRomaniaMin)){
                    cam.operatorControlYellow();
                    x = cam.pip.Center.get(cam.pip.Center.indexOf(x)+1);
                    y = cam.pip.Center.get(cam.pip.Center.indexOf(y)+1);
                }
            }
        }
        if(x > -5 && x < 270 && y > 70 && y < 285) {
            pos = cam.AutonomousRecognition().get(0) - k;
            posHand = 1 - cam.AutonomousRecognition().get(1);
            leftDiff.setPosition(1 - ( Math.abs(cam.getAngle() / 90 - cam.hand.getPosition()) )/* - 0.25*///);
        //rightDiff.setPosition(1 - ( Math.abs(cam.getAngle() / 90 - cam.hand.getPosition()) )/* - 0.25*/);
        //  }*/
    }
}

