package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.Perkid;

@Config
@TeleOp(group = "test")
public class PerkidTest extends LinearOpMode {
    public static double angle = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        Perkid perkid = new Perkid(this);

        waitForStart();

        while (opModeIsActive()) {
            perkid.setAngle(angle);
        }
    }
}
