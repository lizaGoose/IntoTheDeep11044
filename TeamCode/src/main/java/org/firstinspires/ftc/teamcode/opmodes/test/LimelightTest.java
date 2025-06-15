package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(group = "test")
public class LimelightTest extends OpMode {
    private Limelight3A limelight;

    @Override
    public void init() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
    }

    @Override
    public void loop() {
        LLResult result = limelight.getLatestResult();
        if (result == null) {
            telemetry.addLine("result is invalid");
            return;
        }
        double[] pythonOutputs = result.getPythonOutput();
        if (pythonOutputs == null || pythonOutputs.length == 0) {
            telemetry.addLine("output is invalid");
            return;
        }
        for (double el: pythonOutputs) {
            telemetry.addLine(String.valueOf(el));
        }
    }
}
