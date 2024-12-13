package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Modules.Diff;

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
        //tes.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        //test1.setDirection(Servo.Direction.REVERSE);
        test1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        test.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        timer = new ElapsedTime();

        waitForStart();
        timer.reset();
        double reference = 0;
        while (opModeIsActive()){
            distanceSensor = hardwareMap.get(DistanceSensor.class, "DistanceFront");
            test1.setPower(gamepad1.right_stick_y);
            test.setPower(gamepad1.right_stick_y);
          /*  if (gamepad1.dpad_left){
                test.setPosition(1);
                test1.setPosition(1);
            }
            if (gamepad1.dpad_right){
                test.setPosition(0);
                test1.setPosition(0);
            }
            if (gamepad1.dpad_down){
                test.setPosition(0.5);
                test1.setPosition(0.5);
            }
           /* if (gamepad1.dpad_up){
                test.setPosition(0.9);
                test1.setPosition(0.1);
            }
            if (gamepad1.a){
                rr.setPower(0.5);
            }
            if (gamepad1.b){
                rf.setPower(0.5);
            }
            if (gamepad1.y){
                lf.setPower(0.5);
            }
            if (gamepad1.x){
                lr.setPower(0.5);
            }

            reference += (gamepad1.right_stick_y) * timer.seconds() * 2000000;

            reference = Math.max(0, Math.min(reference, 400));

            double error = reference - tes.getCurrentPosition();

            tes.setPower(error * 0.01);



            telemetry.addData("enc", tes.getCurrentPosition());*/
            telemetry.addData("handPos", distanceSensor.getDistance(DistanceUnit.MM));
           /* telemetry.addData("ref", reference);
            telemetry.addData("enc", gamepad1.right_stick_y);*/
            telemetry.update();
          //  timer.reset();
        }
    }
}
