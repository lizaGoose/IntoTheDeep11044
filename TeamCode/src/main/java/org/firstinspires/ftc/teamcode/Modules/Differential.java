package org.firstinspires.ftc.teamcode.Modules;

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

import java.util.List;

@Config
class DifferentialConstants {
    public static double HORIZONTAL_DOWN = 0.2;
    public static double PICK_UP = 0.4;
    public static double TRANSFER = 0.76 + 0.17;
    public static double SCORE_SAMP = 0.68;

}

@Config
public class Differential {
    LinearOpMode linearOpMode;
    Gamepad gamepad2;

    Servo right, left;

    public double rotation = 0;
    private ElapsedTime timer;

    public enum Mode {
        TRANSFER,
        PICK_UP,
        STRAIGHT_DOWN,
        SCORE_SAMP
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

    public void teleop() {
        switch (currentMode) {

            case TRANSFER:
                left.setPosition(TRANSFER);
                right.setPosition(TRANSFER);
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
        }
    }
}
