package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.camera.LimelightCamera;

@Config
@TeleOp(group = "test")
public class LimelightCameraTest extends LinearOpMode {
    public LimelightCamera cam;

    @Override
    public void runOpMode() throws InterruptedException {
        cam = new LimelightCamera(this);
        cam.setPipeline(LimelightCamera.Pipeline.BLUE_THRESH);
        cam.start();

        waitForStart();

        while (opModeIsActive()) {
            cam.getResult().ifPresent(result -> {
                telemetry.addData("tx", result.getTx());
                telemetry.addData("ty", result.getTy());
                telemetry.addData("ta", result.getTa());
                telemetry.update();
            });
        }
    }
}
