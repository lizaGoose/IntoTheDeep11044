package org.firstinspires.ftc.teamcode.Camera;

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
