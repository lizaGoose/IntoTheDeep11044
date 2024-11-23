package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Camera.SampleDetectionCameraClass;
import org.firstinspires.ftc.teamcode.Camera.SampleDetectionPipelinePNP;

public class Modules {
    LinearOpMode linearOpMode;

    SampleDetectionPipelinePNP pip;
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

        claw = new Claw(linearOpMode);
        diff = new Diff(linearOpMode);
        //extend = new Extend(linearOpMode);
        hand = new Hand(linearOpMode);
        perkid = new Perkid(linearOpMode);
        pip = new SampleDetectionPipelinePNP();

        gamepad1 = linearOpMode.gamepad1;
    }
    public void teleop(){
        claw.teleop();
        diff.teleop(pip.Angle);
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
              //  cam.visionInUse();



                if (gamepad1.dpad_left){
                    diff.currentMode = Diff.Mode.VISION_HUYNYA;
                }
                /*if(cam.sample_status == true){
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
                }*/
                break;

            case OPERATOR_CONTROL:
           //     cam.operatorControl();
              /*  cam.sample_status =  true;
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

                   if (/*hand.axonEnc.getVoltage() >= 100 && *//* perkid.left.getPosition() == 0.5 && perkid.right.getPosition() == 0.5){
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
                   }*/
                   /*if (gamepad1.dpad_up){
                       diff.currentMode = Diff.Mode.GET_UP;
                   }
                   if(gamepad1.dpad_down){
                       diff.currentMode = Diff.Mode.GET_DOWN;
                   }
                   else {
                       diff.currentMode = Diff.Mode.DRIVER_CONTROL;
                   }*/

              // }
                break;
        }
      linearOpMode.telemetry.addData("sampleStatus", diff.right.getPosition());
        linearOpMode.telemetry.addData("sampleStatus", diff.left.getPosition());
        linearOpMode.telemetry.addData("angle", pip.Angle);
        linearOpMode.telemetry.addData("x", pip.x);
        linearOpMode.telemetry.addData("y", pip.y);
        linearOpMode.telemetry.addData("area", pip.Area);

        /* linearOpMode.telemetry.addData("x", cam.x);
        linearOpMode.telemetry.addData("y", cam.y);
        linearOpMode.telemetry.addData("z", cam.z);
        linearOpMode.telemetry.addData("xcam", cam.coordX);
        linearOpMode.telemetry.addData("ycam", cam.coordY);
        linearOpMode.telemetry.addData("zcam", cam.coordZ);
        linearOpMode.telemetry.addData("Distance", cam.distanceSensor.getDistance(DistanceUnit.MM));*/
      //  linearOpMode.telemetry.addData("Distance", );

        linearOpMode.telemetry.update();

    }
}
