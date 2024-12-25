package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.config.Config;

@Config
public class ThreeClawConstants {
    public static double X = -48;
    public static double Y = -50;

    public static double X_HUMAN = -60;
    public static double Y_HUMAN = -48;


    public static int WAIT_AT_LEFT = 400;
    public static int EXTEND_TO_LEFT = 83;
    public static double ANGLE_TO_LEFT = -57.5;
    public static double DIFF_ROTATION_LEFT = -0.25;

    public static int EXTEND_LEFT_TO_HUMAN = 350;
    public static double ANGLE_LEFT_TO_HUMAN = -145;
    public static int WAIT_FOR_LEFT_ROTATION = 700;

    public static double MIDDLE_STOP_POINT = 1.5;
    public static int WAIT_AT_MIDDLE = 800;
    public static int EXTEND_TO_MIDDLE = 100;
    public static double ANGLE_TO_MIDDLE = -60;
    public static double DIFF_ROTATION_MIDDLE = -0.25;

    public static int EXTEND_MIDDLE_TO_HUMAN = 350;
    public static double ANGLE_MIDDLE_TO_HUMAN = -145;
    public static int WAIT_FOR_MIDDLE_ROTATION = 700;

    public static double RIGHT_STOP_POINT = 2.0;
    public static int EXTEND_HALF_CLOSE = 1000;
    public static int WAIT_AT_RIGHT = 500;
    public static int EXTEND_TO_RIGHT = 50;
    public static double ANGLE_TO_RIGHT = 30;
    public static double DIFF_ROTATION_RIGHT = 0;

    public static int EXTEND_RIGHT_TO_HUMAN = 350;
    public static double ANGLE_RIGHT_TO_HUMAN = -145;
    public static int WAIT_FOR_RIGHT_ROTATION = 700;

    public static double EXTEND_VELOCITY_THRESHOLD = 5;
    public static double EXTEND_POSITION_THRESHOLD = 5;
}
