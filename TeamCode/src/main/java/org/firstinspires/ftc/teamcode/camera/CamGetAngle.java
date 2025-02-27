package org.firstinspires.ftc.teamcode.camera;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class CamGetAngle {
    SampleDetectionCameraClass cam;
    LinearOpMode lin;
    public CamGetAngle(){
        cam = new SampleDetectionCameraClass(lin);

    }
    public double getAngle(){
        return cam.Angle;
    }
}
