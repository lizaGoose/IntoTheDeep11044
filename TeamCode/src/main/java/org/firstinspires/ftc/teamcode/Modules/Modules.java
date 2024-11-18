package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Camera.SampleDetectionCameraClass;

public class Modules {
    LinearOpMode linearOpMode;
    SampleDetectionCameraClass cam;
    Claw claw;
    Diff diff;
    //Extend extend;
    Hand hand;
    Perkid perkid;
    Gamepad gamepad1;
    CRServo serv;
    boolean smartButton;
    enum Mode{
        VISION_CONTROL,
        OPERATOR_CONTROL
    }
    Mode modules = Mode.OPERATOR_CONTROL;
    public Modules(LinearOpMode linearOpMode){
        this.linearOpMode = linearOpMode;
        cam = new SampleDetectionCameraClass(linearOpMode);
        claw = new Claw(linearOpMode);
        diff = new Diff(linearOpMode);
        //extend = new Extend(linearOpMode);
        hand = new Hand(linearOpMode);
        perkid = new Perkid(linearOpMode);

        gamepad1 = linearOpMode.gamepad1;
    }
    public void teleop(){
        claw.teleop();
        diff.teleop();
        //extend.teleop();
        hand.teleop();
        perkid.teleop();


        if(gamepad1.x){
            modules = Mode.VISION_CONTROL;


        }
        if(gamepad1.a){
            modules = Mode.OPERATOR_CONTROL;

        }

      switch (modules){

            case VISION_CONTROL:
                cam.visionInUse();


                if(cam.sample_status == true){
                    claw.currentMode = Claw.Mode.PICK;
                    modules = Mode.OPERATOR_CONTROL;
                }
                else {
                    claw.currentMode = Claw.Mode.DROP;
                    if (cam.Angle > 40){
                        diff.currentMode = Diff.Mode.SET_MAX_POWER;
                    }
                    else if (cam.clawError < 20){
                        diff.currentMode = Diff.Mode.SET_MAX_POWER_REVERSE;
                    }
                    else {
                        diff.currentMode = Diff.Mode.SET_ZERO_POWER;
                    }
                }
                break;

            case OPERATOR_CONTROL:
                cam.operatorControl();
                cam.sample_status =  true;
               if (gamepad1.y){
                   claw.currentMode = Claw.Mode.PICK;
                   //hand.currentMode = Hand.Mode.GIVE_POSE;
                   perkid.currentMode = Perkid.Mode.PICK_POSITION;
                   diff.currentMode = Diff.Mode.SET_ZERO_POWER;
                  // hand.currentMode = Hand.Mode.GIVE_POSE;
                  // diff.currentMode = Diff.Mode.OPEN_CLAW;
                   smartButton = true;

                   }
               if (smartButton == true){

                   if (/*hand.axonEnc.getVoltage() >= 100 && */ perkid.left.getPosition() == 0.5 && perkid.right.getPosition() == 0.5){
                       claw.currentMode = Claw.Mode.DROP;
                       smartButton = false;
                   }

               }
               else {
                   hand.currentMode = Hand.Mode.DRIVER_CONTROL;
                   diff.currentMode = Diff.Mode.DRIVER_CONTROL;
                   if ((perkid.currentMode == Perkid.Mode.PICK_POSITION) || (perkid.currentMode == Perkid.Mode.CLOSE_POSITION)){
                     //  hand.currentMode = Hand.Mode.GIVE_POSE;
                   }
                   if (gamepad1.right_bumper){
                       claw.currentMode = Claw.Mode.PICK;
                   }
                   if (gamepad1.left_bumper){
                       claw.currentMode = Claw.Mode.DROP;
                   }

               }
                break;
        }
       linearOpMode.telemetry.addData("sampleStatus", cam.sample_status);
        linearOpMode.telemetry.addData("x", cam.x);
        linearOpMode.telemetry.addData("y", cam.y);
        linearOpMode.telemetry.addData("y", cam.Angle);
        linearOpMode.telemetry.addData("Distance", cam.distanceSensor.getDistance(DistanceUnit.MM));
        linearOpMode.telemetry.addData("Distance", smartButton);

        linearOpMode.telemetry.update();

    }
}
