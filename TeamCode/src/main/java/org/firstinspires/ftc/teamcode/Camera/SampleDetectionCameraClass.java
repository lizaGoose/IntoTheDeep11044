package org.firstinspires.ftc.teamcode.Camera;

import android.graphics.Color;

import com.acmerobotics.dashboard.FtcDashboard;
import com.chaquo.python.Kwarg;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Modules.Diff;
import org.firstinspires.ftc.teamcode.file.FilesystemUtil;
import org.opencv.core.RotatedRect;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SampleDetectionCameraClass {
    OpenCvCamera cam;
    LinearOpMode linearOpMode;
    HardwareMap hardwareMap;
    double centerCoord;
    public volatile double Angle, x, y, Area;
    int index;
    String sampleCoordinatesStatus;
    RotatedRect theRect;
    boolean tf;
    public double extendingError, handError, clawError;
    public boolean sample_status;
    public SampleDetectionPipeline pip;
    public ColorSensor clawColorSensor;
    public DistanceSensor distanceSensor;
    //  public Diff diff;
    public int indexAngle;

    public SampleDetectionCameraClass(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        hardwareMap = linearOpMode.hardwareMap;

        //diff = new Diff(linearOpMode);
        clawColorSensor = hardwareMap.get(ColorSensor.class, "Color");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "Color");
        int cameraMonitorViewId = hardwareMap.appContext
                .getResources().getIdentifier("cameraMonitorViewId",
                        "id", hardwareMap.appContext.getPackageName());
        cam = OpenCvCameraFactory.getInstance()
                .createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        cam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        FtcDashboard.getInstance().startCameraStream(cam, 60);

        pip = new SampleDetectionPipeline();
        cam.setPipeline(pip);

        cam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                cam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });

    }

    public void visionInUse() {

        clawColorSensor = hardwareMap.get(ColorSensor.class, "Color");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "Color");
        List<Double> Ang = new ArrayList<Double>();

       /* if (pip.Center.size() != 0) {


            centerCoord = Collections.min(pip.Center);

           /* if (Math.round(pip.rotatedRectFitToContour.center.x) == centerCoord) {
                if (pip.CenterStone.isEmpty() == false) {
                    theRect = pip.rotatedRectFitToContour;
                    x = theRect.center.x * pip.CmPerPixel / 640 * pip.matrixKoeff;
                    y = theRect.center.y * pip.CmPerPixel / 640 * pip.matrixKoeff;

                }
                Ang.add(Angle);
               /* if((Angle == 90) && (Ang.size() != 0)){
                    Ang.add((double) 0);
                    indexAngle = Ang.indexOf(Angle);
                    Angle = Ang.get(indexAngle - 1);
                }
                else {
                    Angle = pip.rotatedRectFitToContour.angle;
                }*/
              /*  Angle = theRect.angle;
            }*/ Area = pip.Area;

            Angle = pip.Angle;


            clawError = Math.round(90 - Angle);

            extendingError = Math.round(y);
            handError = Math.round(x);


            if (extendingError == 0 && handError == 0 && ( distanceSensor.getDistance(DistanceUnit.MM) <= 30 )/*&& clawError == 0*/) {
                sample_status = true;
            } else {
                sample_status = false;
            }


       // }


        if (linearOpMode.gamepad1.dpad_up) {
            tf = true;
            x = 0;
            y = 0;
            Angle = 0;
        }
        if (linearOpMode.gamepad1.dpad_down) {
            tf = false;
            x = 0;
            y = 0;
            Angle = 0;
        }
        if (tf == true) {
            pip.YELLOW_MASK_THRESHOLD = 57;
            pip.BLUE_MASK_THRESHOLD = 150;
            pip.RED_MASK_THRESHOLD = 198;

        } else {

            pip.YELLOW_MASK_THRESHOLD = 100;
            pip.BLUE_MASK_THRESHOLD = 57;
            pip.RED_MASK_THRESHOLD = 198;

        }
       /* linearOpMode.telemetry.addData("sampleStatus", sample_status);
        linearOpMode.telemetry.addData("x", x);
        linearOpMode.telemetry.addData("y", y);
        linearOpMode.telemetry.update();*/
    }

    public void operatorControl() {
        pip.YELLOW_MASK_THRESHOLD = 0;
        pip.BLUE_MASK_THRESHOLD = 0;
        pip.RED_MASK_THRESHOLD = 0;
    }
}
