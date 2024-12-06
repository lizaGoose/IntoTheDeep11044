package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Camera.CameraTest;

public class Extend {
    LinearOpMode linearOpMode;
    Gamepad gamepad;
    DcMotor extend;
    CameraTest cam;

    enum Mode {
        DRIVER_CONTROL,
        VISION_CONTROL,
        CLOSE
    }
    Extend.Mode currentMode = Mode.DRIVER_CONTROL;

    public Extend(LinearOpMode linearOpMode){
        this.linearOpMode = linearOpMode;
        gamepad = linearOpMode.gamepad2;
        extend = linearOpMode.hardwareMap.get(DcMotor.class, "extendMotor") ;
        cam = new CameraTest();
    }
    public void teleop(){
        switch (currentMode) {
            case DRIVER_CONTROL:
                extend.setPower(gamepad.right_stick_y);
                break;
            case VISION_CONTROL:
                extend.setPower(cam.extendingError);
                break;
            case CLOSE:
                extend.setTargetPosition(0);
                break;
        }
      if (gamepad.right_stick_y != 0){
          currentMode = Mode.DRIVER_CONTROL;
      }
    /*  if(gamepad1.a){
          currentMode = Mode.VISION_CONTROL;
      }
      if((cam.sample_status == true) && currentMode == Mode.VISION_CONTROL){
          currentMode = Mode.CLOSE;

      }*/
    }
}
