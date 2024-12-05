package org.firstinspires.ftc.teamcode.Modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@Config
public class LiftConstants {
    public static PIDFCoefficients COEFFICIENTS = new PIDFCoefficients(0.0, 0.0, 0.0, 0.0);
    public static int MAX_POSITION = 0;
    public static int MIN_POSITION = 0;
}
