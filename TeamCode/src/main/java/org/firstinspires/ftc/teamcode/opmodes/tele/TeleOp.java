package org.firstinspires.ftc.teamcode.opmodes.tele;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.modules.StateMachine;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        StateMachine modules = new StateMachine(this);

        telemetry.setMsTransmissionInterval(11);

        waitForStart();
        while (opModeIsActive()){
            modules.teleop();
        }
    }
}
