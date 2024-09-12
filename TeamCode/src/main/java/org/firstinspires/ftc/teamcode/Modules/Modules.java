package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.LocalizerKolhoz;

public class Modules {
    LinearOpMode linearOpMode;

    enum Mode{
        STATE
    }
    Mode modules = Mode.STATE;
    public Modules(LinearOpMode linearOpMode){
        this.linearOpMode = linearOpMode;

        switch (modules){
            case STATE:
                break;
        }
    }
}
