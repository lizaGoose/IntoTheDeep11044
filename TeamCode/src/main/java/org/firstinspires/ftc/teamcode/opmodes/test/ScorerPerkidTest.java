package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.Perkid;
import org.firstinspires.ftc.teamcode.modules.ScorerPerkid;

@Config
@TeleOp(group = "test")
public class ScorerPerkidTest extends LinearOpMode {
    public static double angle = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        ScorerPerkid perkid = new ScorerPerkid(this);

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("position", perkid.setAngleDebug(angle));
            telemetry.update();
            perkid.setAngle(angle);
        }
    }
}
