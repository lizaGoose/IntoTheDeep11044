package org.firstinspires.ftc.teamcode.OpMode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Modules.Claw;
import org.firstinspires.ftc.teamcode.Modules.DriveTrain;
import org.firstinspires.ftc.teamcode.Modules.Modules;
import org.firstinspires.ftc.teamcode.Modules.ModulesRed;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOpRed extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        //DriveTrain dt = new DriveTrain(this);
        ModulesRed modules = new ModulesRed(this);

        waitForStart();
        modules.resetTimers();
        while (opModeIsActive()){
            //dt.teleOp();
            modules.teleop();
        }
    }
}
