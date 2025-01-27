package org.firstinspires.ftc.teamcode.OpMode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Modules.SampleStateMachine;
import org.firstinspires.ftc.teamcode.Modules.StateMachine;

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
