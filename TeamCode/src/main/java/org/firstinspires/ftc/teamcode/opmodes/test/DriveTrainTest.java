package org.firstinspires.ftc.teamcode.opmodes.test;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Modules.DriveTrain;

@TeleOp(group = "test")
public class DriveTrainTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DriveTrain dt = new DriveTrain(this);

        waitForStart();

        while (opModeIsActive()) {
            dt.teleOp();
        }
    }
}
