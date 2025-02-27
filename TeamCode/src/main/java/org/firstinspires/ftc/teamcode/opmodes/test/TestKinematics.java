package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.camera.AutoCamera;
import org.firstinspires.ftc.teamcode.modules.Extend;

@TeleOp
public class TestKinematics extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        AutoCamera cam = new AutoCamera(this);
        Extend extend = new Extend(this);

        waitForStart();
        while (opModeIsActive()){
            cam.operatorControlRed();
            extend.setPower(gamepad1.right_stick_y);
            //cam.goToElementPosExtend();
            //cam.goToElementPosHand();
            telemetry.addData("yCoord", cam.AutonomousRecognition().get(0));
            telemetry.update();
        }
    }
}
