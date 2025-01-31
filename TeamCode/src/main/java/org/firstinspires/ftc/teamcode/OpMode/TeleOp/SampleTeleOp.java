package org.firstinspires.ftc.teamcode.OpMode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Modules.SampleStateMachine;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class SampleTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        //DriveTrain dt = new DriveTrain(this);
        SampleStateMachine modules = new SampleStateMachine(this);

        waitForStart();
        //modules.resetTimers();
        while (opModeIsActive()){
            //dt.teleOp();
            modules.teleop();
        }
    }
}
