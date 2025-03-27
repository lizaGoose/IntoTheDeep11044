package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.camera.LimelightCamera;

import java.util.Optional;

@Config
@TeleOp(group = "test")
public class LimelightCameraTest extends LinearOpMode {
    public LimelightCamera cam;

    @Override
    public void runOpMode() throws InterruptedException {
        cam = new LimelightCamera(this);

        telemetry.setMsTransmissionInterval(11);
        cam.setPipeline(LimelightCamera.Pipeline.THRESHOLD_BLUE);
        cam.start();

        waitForStart();

        while (opModeInInit()) {
            cam.getResult().ifPresent(llResult -> {
                telemetry.addData("pipeline_index", llResult.getPipelineIndex());
                telemetry.addData("latency", llResult.getTargetingLatency());
                telemetry.addData("type", llResult.getPipelineType());
                telemetry.update();
            });
        }

        cam.setPipeline(LimelightCamera.Pipeline.PYTHON_BLUE_2);

        while (opModeIsActive()) {
            Optional<double[]> outputOpt = cam.getPythonOutput();
            if (outputOpt.isPresent()) {
                for (double el : outputOpt.get())
                    telemetry.addLine(String.valueOf(el));
            } else {
                telemetry.addLine("ERROR!");
            }
            telemetry.update();
        }
        cam.stop();
    }
}
