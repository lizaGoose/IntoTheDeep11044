package org.firstinspires.ftc.teamcode.OpMode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Modules.Claw;
import org.firstinspires.ftc.teamcode.Modules.DriveTrain;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DriveTrain dt = new DriveTrain(this);
        Claw cl = new Claw(this);

        waitForStart();

        while (opModeIsActive()){
            dt.teleOp();
            cl.teleop();
        }
    }
}
