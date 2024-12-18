package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Modules.Differential;
import org.firstinspires.ftc.teamcode.Modules.Hand;
import org.firstinspires.ftc.teamcode.Modules.Perkid;
import org.firstinspires.ftc.teamcode.math.ClawCalculation;

import java.util.List;

@Config
@TeleOp(group = "test")
public class IKTest extends LinearOpMode {
    public static double x = 0.0243;
    public static double y = 0;
    public static double z = 0.12745;

    @Override
    public void runOpMode() throws InterruptedException {
        Differential diff = new Differential(this);
        Perkid perkid = new Perkid(this);
        Hand hand = new Hand(this);

        FtcDashboard dashboard = FtcDashboard.getInstance();

        MultipleTelemetry mulTelemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        waitForStart();

        while (opModeIsActive()) {
            List<Double> result = ClawCalculation.inverseKinematics(x, y, z);

            //perkid.setAngle(result.get(1));

            //hand.setAngle(result.get(2));

            //diff.setAngleRotation(result.get(3), 0);

            mulTelemetry.addData("d0", result.get(0));
            mulTelemetry.addData("theta1", result.get(1));
            mulTelemetry.addData("theta2", result.get(2));
            mulTelemetry.addData("theta3", result.get(3));
            mulTelemetry.update();
        }
    }
}
