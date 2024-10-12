package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Camera.SampleDetectionCameraClass;
import org.firstinspires.ftc.teamcode.drive.LocalizerKolhoz;

public class Modules {
    LinearOpMode linearOpMode;
    SampleDetectionCameraClass cam;
    Claw claw;
    Gamepad gamepad1;
    CRServo serv;

    enum Mode{
        VISION_CONTROL,
        OPERATOR_CONTROL
    }
    Mode modules = Mode.OPERATOR_CONTROL;
    public Modules(LinearOpMode linearOpMode){
        this.linearOpMode = linearOpMode;
        cam = new SampleDetectionCameraClass(linearOpMode);
        claw = new Claw(linearOpMode);
        gamepad1 = linearOpMode.gamepad1;
    }
    public void teleop(){
        claw.teleop();
        if(gamepad1.x){
            modules = Mode.VISION_CONTROL;
            claw.currentMode = Claw.Mode.PICK;
        }
        if(gamepad1.a){
            modules = Mode.OPERATOR_CONTROL;
            claw.currentMode = Claw.Mode.DROP;
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
                }
                break;

            case OPERATOR_CONTROL:
                cam.operatorControl();
                cam.sample_status =  true;
                if(gamepad1.y){

                }
                break;
        }
       linearOpMode.telemetry.addData("sampleStatus", cam.sample_status);
        linearOpMode.telemetry.addData("x", cam.x);
        linearOpMode.telemetry.addData("y", cam.y);
        linearOpMode.telemetry.addData("Distance", cam.distanceSensor.getDistance(DistanceUnit.MM));
        linearOpMode.telemetry.update();

    }
}
