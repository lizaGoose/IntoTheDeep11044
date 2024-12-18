package org.firstinspires.ftc.teamcode.Modules;

import static org.firstinspires.ftc.teamcode.Modules.DiffConstants.DOWN;
import static org.firstinspires.ftc.teamcode.Modules.DiffConstants.FORWARD;
import static org.firstinspires.ftc.teamcode.Modules.DiffConstants.HORIZONTAL_DOWN;

import static org.firstinspires.ftc.teamcode.Modules.DiffConstants.PICK_UP;
import static org.firstinspires.ftc.teamcode.Modules.DiffConstants.ROTATION_SPEED;
import static org.firstinspires.ftc.teamcode.Modules.DiffConstants.TRANSFER;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.math.ClawCalculation;

import java.util.Arrays;
import java.util.List;

@Config
class DifferentialConstants {
    public static double HORIZONTAL_DOWN = 0.3;
    public static double PICK_UP = 0.55;
    public static double TRANSFER = 0.76 + 0.17;
    public static double SCORE_SAMP = 0.68;


}

@Config
public class Differential {
    LinearOpMode linearOpMode;
    Gamepad gamepad2;
    public double rotationOfDiff = 0;
    Servo right, left;
    public static double wait = 0.5;
    private double lastCamera = 0;
    public static double angle_config = 0.0;
    public static double rotation_config = 0.0;
    private List<Double> diff = ClawCalculation.angleAndRotToDiffFix(angle_config, rotation_config);
    public boolean buttonState = false, leftButonState = false, buttonState3 = false, buttonState2 = false;

    public double rotation = 0;
    private ElapsedTime timer;

    public enum Mode {
        TRANSFER,
        PICK_UP,
        STRAIGHT_DOWN,
        SCORE_SAMP,
        ROTATION_FROM_DPAD,
        VISION_HUYNYA
    }

    public Mode currentMode = Mode.TRANSFER;

    public Differential(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        gamepad2 = linearOpMode.gamepad2;
        left = linearOpMode.hardwareMap.get(Servo.class, "left_diff_servo");
        right = linearOpMode.hardwareMap.get(Servo.class, "right_diff_servo");
        left.setDirection(Servo.Direction.REVERSE);

        timer = new ElapsedTime();

    }

    public void teleop(double cameraAngle) {
        switch (currentMode) {

            case TRANSFER:
                left.setPosition(TRANSFER);
                right.setPosition(TRANSFER);
                break;
            case ROTATION_FROM_DPAD:

                if(gamepad2.right_bumper){
                    buttonState2 = gamepad2.right_bumper;
                    if(buttonState2 != buttonState) {
                        rotationOfDiff =  0.1;
                    }

                }
                buttonState = gamepad2.right_bumper;

               if(gamepad2.left_bumper){
                   buttonState3 = gamepad2.left_bumper;
                   if (buttonState3 != leftButonState) {
                       rotationOfDiff = - 0.1;
                   }
                }
                leftButonState = gamepad2.left_bumper;

               if(!(gamepad2.left_bumper)&& !(gamepad2.right_bumper)){
                   rotationOfDiff = 0;
               }
                //rotationOfDiff =  -gamepad2.right_trigger + gamepad2.left_trigger;

                rotation = rotation - Math.pow((rotationOfDiff) / 2, 3) * timer.milliseconds() * ROTATION_SPEED;
                rotation = Math.max(rotation, -0.5);
                rotation = Math.min(rotation, 0.5);
                left.setPosition(HORIZONTAL_DOWN + rotation);
                right.setPosition(HORIZONTAL_DOWN - rotation);
                timer.reset();
                if((leftButonState == buttonState2 && gamepad2.right_bumper )||(buttonState3 == leftButonState && gamepad2.left_bumper) ){
                    rotationOfDiff = 0;//adb connect 192.168.43.1:5555
                }
                break;
            case PICK_UP:
                rotation = rotation - Math.pow(( -gamepad2.right_trigger + gamepad2.left_trigger ) / 2, 3) * timer.milliseconds() * ROTATION_SPEED;
                rotation = Math.max(rotation, -0.5);
                rotation = Math.min(rotation, 0.5);
                left.setPosition(PICK_UP + rotation);
                right.setPosition(PICK_UP - rotation);
                timer.reset();

                break;
            case STRAIGHT_DOWN:
                rotation = rotation - Math.pow(( -gamepad2.right_trigger + gamepad2.left_trigger ) / 2, 3) * timer.milliseconds() * ROTATION_SPEED;
                rotation = Math.max(rotation, -0.5);
                rotation = Math.min(rotation, 0.5);
                left.setPosition(DifferentialConstants.HORIZONTAL_DOWN + rotation);
                right.setPosition(DifferentialConstants.HORIZONTAL_DOWN  - rotation);
                timer.reset();
                break;
            case SCORE_SAMP:
                left.setPosition(DifferentialConstants.SCORE_SAMP);
                right.setPosition(DifferentialConstants.SCORE_SAMP);
                break;
            case VISION_HUYNYA:

                   /* if((lastCamera <= 90) && (cameraAngle < 0) && (cameraAngle > -10) && (lastCamera > 0)){
                        rotation = 0;
                    }
                    else if((lastCamera >= -90) && (cameraAngle > 0)&& (cameraAngle < 10) && (lastCamera < 0)){
                        rotation = 0;
                    }*/

                //  if(!(cameraAngle > -10 && cameraAngle < 10)){
                if (cameraAngle > 10 && left.getPosition() > 0.7) {
                    rotation = -90;
                } else if (left.getPosition() == 1 && cameraAngle > -10) {
                    rotation = -90;
                } else if (cameraAngle < -10 && left.getPosition() < 0.3) {
                    rotation = 90;
                } else if (left.getPosition() == 0 && cameraAngle > 10) {
                    rotation = 90;
                } else {
                    if (timer.seconds() > wait) {
                        if (cameraAngle == -95) {
                            cameraAngle = lastCamera;
                        }
                        rotation = ClawCalculation.rotationFromAngle(HORIZONTAL_DOWN, left.getPosition()) + cameraAngle;
                        if (rotation > 90) {
                            rotation = -90 + rotation - 90;
                        }
                        if (rotation < -90) {
                            rotation = 90 + rotation + 90;
                        }
                        if (gamepad2.dpad_left) {
                            angle_config = 45;
                        }
                        diff = ClawCalculation.angleAndRotToDiffFix(HORIZONTAL_DOWN, rotation);
                        lastCamera = cameraAngle;
                        timer.reset();
                    }
                    //
                }


              /*  else if(left.getPosition() >= 0.6 || left.getPosition() <= 0.4) {
                    diff = ClawCalculation.angleAndRotToDiffFix(angle_config, 90);
                }*/

                left.setPosition(diff.get(0));
                right.setPosition(diff.get(1));
                break;
        }
    }

    public void setAngleRotation(double angle, double rotation) {
        List<Double> positions = angleRotationToPosition(angle, rotation);

        left.setPosition(positions.get(0));
        right.setPosition(positions.get(1));
    }

    public void setServoPositions(double leftPos, double rightPos) {
        left.setPosition(leftPos);
        right.setPosition(rightPos);
    }

    public List<Double> angleRotationToPosition(double angle, double rotation) {
        double leftDiff  = ((angle + rotation) / 90 + 1) / 2;
        double rightDiff = ((angle - rotation) / 90 + 1) / 2;

        leftDiff = (FORWARD - DOWN) * 2 * leftDiff + DOWN;
        rightDiff = (FORWARD - DOWN) * 2 * rightDiff + DOWN;

        return Arrays.asList(leftDiff, rightDiff);
    }
}
