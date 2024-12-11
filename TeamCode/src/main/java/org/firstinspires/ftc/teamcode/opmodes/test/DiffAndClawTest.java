package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Modules.Claw;
import org.firstinspires.ftc.teamcode.Modules.Diff;

@TeleOp
public class DiffAndClawTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Diff diff = new Diff(this);
        Claw claw = new Claw(this);


        waitForStart();
        claw.currentMode = Claw.Mode.DROP;

        while (opModeIsActive()) {
            diff.teleop(0);
            claw.teleop();
        }
    }
}
