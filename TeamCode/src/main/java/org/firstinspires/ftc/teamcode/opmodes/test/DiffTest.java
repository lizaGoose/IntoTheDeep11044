package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.Differential;

@Config
@TeleOp(group = "test")
public class DiffTest extends LinearOpMode {
    public static double angle = 0;
    public static double rotation = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        Differential differential = new Differential(this);

        waitForStart();

        while (opModeIsActive()) {
            differential.setAngleRotation(angle, rotation);

            telemetry.addData("left_calc", differential.angleRotationToPosition(angle, rotation).get(0));
            telemetry.addData("right_calc", differential.angleRotationToPosition(angle, rotation).get(1));
            telemetry.addData("left", differential.getPositions().get(0));
            telemetry.addData("right", differential.getPositions().get(1));
            telemetry.update();
        }
    }
}
