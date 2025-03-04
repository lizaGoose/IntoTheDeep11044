package org.firstinspires.ftc.teamcode.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.camera.AutoCamera;
import org.firstinspires.ftc.teamcode.camera.SampleDetectionCameraClass;
import org.firstinspires.ftc.teamcode.math.ClawCalculation;

import java.util.List;

@TeleOp
public class AnglesToServos extends LinearOpMode {
    DcMotorEx rr, rf, lr,lf;
    double encoder;
    boolean buttonState;
    public int pos;
    private ElapsedTime timer;
    DcMotor test;
    DcMotor test1;
    public DistanceSensor distanceSensor;
    public ColorSensor color;
    SampleDetectionCameraClass cam;
    public double xFromGamepad, yFromGamepad, xCoordMax = 499.293/25.4, yCoordMax = 544.599/25.4;
    public List<Double> ik;
    AutoCamera camera;

    @Override
    public void runOpMode() throws InterruptedException {

        camera = new AutoCamera(this);

        timer = new ElapsedTime();

        waitForStart();

        double reference = 0;
        while (opModeIsActive()){
            camera.goToElementPosExtend(pos);
            camera.goToElementPosHand(pos);
            xFromGamepad = gamepad1.right_stick_x * xCoordMax;
            yFromGamepad = gamepad1.right_stick_y * yCoordMax;
            ik = ClawCalculation.inverseKinematics(10, xFromGamepad, yFromGamepad);
            telemetry.addData("xFromGamepad", xFromGamepad);
            telemetry.addData("yFromGamepad", yFromGamepad);
            telemetry.addData("inverseKinematics", ik);
            telemetry.update();// yCoordMax = ; xCoordMax = ;

        }
    }
}
