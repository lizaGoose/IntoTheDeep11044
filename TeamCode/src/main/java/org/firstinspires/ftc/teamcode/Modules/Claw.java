package org.firstinspires.ftc.teamcode.Modules;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    LinearOpMode linearOpMode;
    Gamepad gamepad1;
    Servo claw;

    enum Mode {
        PICK,
        DROP
    }
    Claw.Mode currentMode = Mode.PICK;

    public Claw(LinearOpMode linearOpMode){
        this.linearOpMode = linearOpMode;
        gamepad1 = linearOpMode.gamepad1;
        claw = linearOpMode.hardwareMap.get(Servo.class, "Claw");


    }
    public void teleop(){
        switch (currentMode) {
            case PICK:
                claw.setPosition(0.35);


                break;
            case DROP:
              claw.setPosition(0);
                break;
    }

    }
}
