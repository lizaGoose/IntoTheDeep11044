package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.camera.AnalyzedSample;
import org.firstinspires.ftc.teamcode.camera.AutoCamera;
import org.firstinspires.ftc.teamcode.camera.LimelightCamera;
import org.firstinspires.ftc.teamcode.camera.SampleColor;
import org.firstinspires.ftc.teamcode.math.ClawCalculation;
import org.firstinspires.ftc.teamcode.modules.Differential;
import org.firstinspires.ftc.teamcode.modules.Linkage;
import org.firstinspires.ftc.teamcode.modules.Hand;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Perkid;
import org.firstinspires.ftc.teamcode.modules.ScorerPerkid;

import java.util.List;

@Config
@TeleOp(group = "test")
public class InverseKinematicsTest extends OpMode {
    public static double X = 25.3 - 83.76641;
    public static double Y = 0;
    public static double Z = 127.45;
    private static final AnalyzedSample defaultSample = new AnalyzedSample(Y, Z, 0, SampleColor.RED, 1);

    private Linkage linkage;
    private Differential differential;
    private Perkid perkid;
    private Hand hand;
    private Lift lift;
    private ScorerPerkid scorerPerkid;
    private LimelightCamera camera;

    @Override
    public void init() {
        linkage = new Linkage(this);
        differential = new Differential(this);
        perkid = new Perkid(this);
        hand = new Hand(this);
        lift = new Lift(this);
        scorerPerkid = new ScorerPerkid(this);
        camera = new LimelightCamera(this);

        telemetry.setMsTransmissionInterval(11);

        camera.setPipeline(LimelightCamera.Pipeline.THRESHOLD_BLUE);
        camera.start();
    }

    @Override
    public void init_loop() {
        camera.getResult().ifPresent(llResult -> {
            telemetry.addData("pipeline_index", llResult.getPipelineIndex());
            telemetry.addData("latency", llResult.getTargetingLatency());
            telemetry.addData("type", llResult.getPipelineType());
        });
    }

    @Override
    public void start() {
        scorerPerkid.scoreSpecimen();
        camera.setPipeline(LimelightCamera.Pipeline.PYTHON_BLUE_2);
    }

    @Override
    public void loop() {
        List<Double> res = ClawCalculation.inverseKinematics_noAngle(X, Y, Z);

        AnalyzedSample sample = camera.getSamplePose(lift.getPosition(), scorerPerkid.getPos()).orElse(defaultSample);

        if (!res.contains(null)){
            linkage.setPositionMillimeters(res.get(0));
            perkid.setAngle(Math.toDegrees(res.get(1)));
            hand.setAngle(Math.toDegrees(res.get(2)));
            differential.setAngleRotation(Math.toDegrees(res.get(3)), 0);
        }

        lift.setSpecimen();
        lift.PIDControl();
        linkage.PIDControl();

        telemetry.addData("extend", linkage.getPositionMillimeters());
        telemetry.addData("d0", res.get(0));
        telemetry.addData("theta1", Math.toDegrees(res.get(1)));
        telemetry.addData("theta2", Math.toDegrees(res.get(2)));
        telemetry.addData("theta3", Math.toDegrees(res.get(1)));

        telemetry.addData("sample_X", sample.x);
        telemetry.addData("sample_Y", sample.y);
        telemetry.addData("sample_angle", sample.angle);
    }
}
