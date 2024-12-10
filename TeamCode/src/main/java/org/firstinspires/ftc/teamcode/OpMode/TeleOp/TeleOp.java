package org.firstinspires.ftc.teamcode.OpMode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Modules.Claw;
import org.firstinspires.ftc.teamcode.Modules.DriveTrain;
import org.firstinspires.ftc.teamcode.Modules.Modules;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        //DriveTrain dt = new DriveTrain(this);
        Modules modules = new Modules(this);

        waitForStart();
        modules.resetTimers();
        while (opModeIsActive()){
            //dt.teleOp();
            modules.teleop();
        }
    }
}
