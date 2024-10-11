package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Camera.SampleDetectionCameraClass;
import org.firstinspires.ftc.teamcode.drive.LocalizerKolhoz;

public class Modules {
    LinearOpMode linearOpMode;
    SampleDetectionCameraClass cam;
    Claw claw;
    Gamepad gamepad1;

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
                }
                else {
                    claw.currentMode = Claw.Mode.DROP;
                }
                break;

            case OPERATOR_CONTROL:
                cam.operatorControl();
                break;
        }

    }
}
