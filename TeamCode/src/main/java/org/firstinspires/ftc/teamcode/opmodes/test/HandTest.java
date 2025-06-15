package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.Hand;

@Config
@TeleOp(group = "test")
public class HandTest extends LinearOpMode {
    public static double angle = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        Hand hand = new Hand(this);

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("position", hand.getAngle());
            telemetry.update();
            hand.setAngle(angle);
        }
    }
}
