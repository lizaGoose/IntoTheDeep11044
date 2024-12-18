package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Camera.SampleDetectionCameraClass;
import org.firstinspires.ftc.teamcode.Modules.Diff;
import org.firstinspires.ftc.teamcode.math.ClawCalculation;

import java.io.DataInput;

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
    SampleDetectionCameraClass cam;

    @Override
    public void runOpMode() throws InterruptedException {
        rr = hardwareMap.get(DcMotorEx.class, "rightRear");
        rr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf = hardwareMap.get(DcMotorEx.class, "rightFront");
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lr = hardwareMap.get(DcMotorEx.class, "leftRear");
        lr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lf = hardwareMap.get(DcMotorEx.class, "leftFront");
        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        test = hardwareMap.get(DcMotor.class, "rightLift");
        test1 = hardwareMap.get(DcMotor.class, "leftLift");
        cam = new SampleDetectionCameraClass(this);
        //tes.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        //test1.setDirection(Servo.Direction.REVERSE);
        test1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        test.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        timer = new ElapsedTime();

        waitForStart();
        timer.reset();
        double reference = 0;
        while (opModeIsActive()){
       cam.visionInUse();
            telemetry.addData("ik", ClawCalculation.inverseKinematics(0 ,cam.xActual, cam.yActual));
           /* telemetry.addData("ref", reference);
            telemetry.addData("enc", gamepad1.right_stick_y);*/
            telemetry.update();
          //  timer.reset();
        }
    }
}
