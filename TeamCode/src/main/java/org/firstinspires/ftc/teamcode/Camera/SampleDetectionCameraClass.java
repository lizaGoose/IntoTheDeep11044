package org.firstinspires.ftc.teamcode.Camera;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.math.ClawCalculation;
import org.opencv.core.RotatedRect;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import java.util.ArrayList;
import java.util.List;

public class SampleDetectionCameraClass {
    OpenCvCamera cam;
    LinearOpMode linearOpMode;
    HardwareMap hardwareMap;
    double centerCoord;
    public volatile double Angle, x, y, Area, xActual, yActual;
    int index;
    String sampleCoordinatesStatus;
    RotatedRect theRect;
    public double distance;
    boolean tf;
    public double extendingError, handError, clawError;
    public boolean sample_status;
    public SampleDetectionPipeline pip;
    //  public ColorSensor clawColorSensor;
    public DistanceSensor distanceSensor;
    //  public Diff diff;
    public int indexAngle;

    public SampleDetectionCameraClass(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        hardwareMap = linearOpMode.hardwareMap;

        //diff = new Diff(linearOpMode);
       /* clawColorSensor = hardwareMap.get(ColorSensor.class, "Color");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "Color");*/
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
                cam.startStreaming(640, 480, OpenCvCameraRotation.SIDEWAYS_RIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });

    }

    public void visionInUse() {

        //clawColorSensor = hardwareMap.get(ColorSensor.class, "Color");
        //distanceSensor = hardwareMap.get(DistanceSensor.class, "Color");
        List<Double> Ang = new ArrayList<Double>();
      //  distance = distanceSensor.getDistance(DistanceUnit.INCH);

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
            }*/
        Area = pip.Area;

        Angle = pip.Angle;

    //    x = pip.xSample;
      //  y = pip.ySample;
        clawError = Math.round(90 - Angle);
    //    xActual = ClawCalculation.camToRealWorldCoordinates(180, 4.2, 6.2, x, y, pip.InchPerPixel).get(0);
      //  yActual = ClawCalculation.camToRealWorldCoordinates(180, 4.2, 6.2, x, y, pip.InchPerPixel).get(1);
        extendingError = Math.round(y);
        handError = Math.round(x);


       /* if (extendingError == 0 && handError == 0 && ( distanceSensor.getDistance(DistanceUnit.MM) <= 30 )/*&& clawError == 0*//*) {
            sample_status = true;
        } else {
            sample_status = false;
        }


        // }


       /* if (linearOpMode.gamepad1.dpad_up) {
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

        }*/
       /* linearOpMode.telemetry.addData("sampleStatus", sample_status);
        linearOpMode.telemetry.addData("x", x);
        linearOpMode.telemetry.addData("y", y);
        linearOpMode.telemetry.update();*/
    }

    public void operatorControlBlue() {
        if (linearOpMode.gamepad1.square) {
            pip.YELLOW_MASK_THRESHOLD = 95;
            pip.BLUE_MASK_THRESHOLD = 57;
            pip.RED_MASK_THRESHOLD = 198;
        }
        if (linearOpMode.gamepad1.circle){
            pip.YELLOW_MASK_THRESHOLD = 85;
            pip.BLUE_MASK_THRESHOLD = 138;
            pip.RED_MASK_THRESHOLD = 70;
        }
        x = pip.xSample;
        y = pip.ySample;
        xActual = ClawCalculation.camToRealWorldCoordinates(180, 4.2, 6.2, x, y, pip.InchPerPixel).get(0);
        yActual = ClawCalculation.camToRealWorldCoordinates(180, 4.2, 6.2, x, y, pip.InchPerPixel).get(1);
    }
    public void operatorControlRed() {
        if (linearOpMode.gamepad1.square) {
            pip.YELLOW_MASK_THRESHOLD = 95;
            pip.BLUE_MASK_THRESHOLD = 57;
            pip.RED_MASK_THRESHOLD = 198;
        }
        if (linearOpMode.gamepad1.circle){
            pip.YELLOW_MASK_THRESHOLD = 55;
            pip.BLUE_MASK_THRESHOLD = 70;
            pip.RED_MASK_THRESHOLD = 170;
        }
        x = pip.xSample;
        y = pip.ySample;
        xActual = ClawCalculation.camToRealWorldCoordinates(180, 4.2, 6.2, x, y, pip.InchPerPixel).get(0);
        yActual = ClawCalculation.camToRealWorldCoordinates(180, 4.2, 6.2, x, y, pip.InchPerPixel).get(1);
    }
}
