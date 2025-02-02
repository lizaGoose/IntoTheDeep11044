package org.firstinspires.ftc.teamcode.Camera;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Modules.Extend;
import org.firstinspires.ftc.teamcode.Modules.Hand;
import org.firstinspires.ftc.teamcode.math.ClawCalculation;
import org.opencv.core.RotatedRect;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoCamera {
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
    Extend extend;
    Hand hand;
    public double xFromGamepad, yFromGamepad, xCoordMax = 499.293/25.4, yCoordMax = 544.599/25.4;
    public double xCoordSampleInches, yCoordSampleInches, maxPositionInchesY, ticksPerInchY, extendPos, handPos;



    public AutoCamera(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        hardwareMap = linearOpMode.hardwareMap;
        extend = new Extend(linearOpMode);
        hand = new Hand(linearOpMode);
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
                cam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });

    }
    public List<Double> AutonomousRecognition(){

        yCoordSampleInches = 480/yCoordMax * pip.ySample;
        xCoordSampleInches = 640/xCoordMax * pip.xSample;
        ticksPerInchY = maxPositionInchesY/yCoordMax;
        extendPos = yCoordSampleInches*ticksPerInchY;
        handPos = xCoordSampleInches/10;
        return Arrays.asList(extendPos, handPos);
    }
    public void goToElementPosExtend(){
        extend.setReference(AutonomousRecognition().get(0));
    }
    public void goToElementPosHand(){
        hand.setPosition(AutonomousRecognition().get(1));
    }

    public void visionInUse() {

        List<Double> Ang = new ArrayList<Double>();

        Area = pip.Area;

        Angle = pip.Angle;

        x = pip.xSample;
        y = pip.ySample;
        clawError = Math.round(90 - Angle);
        xActual = ClawCalculation.camToRealWorldCoordinates(180, 4.2, 6.2, x, y, pip.InchPerPixel).get(0);
        yActual = ClawCalculation.camToRealWorldCoordinates(180, 4.2, 6.2, x, y, pip.InchPerPixel).get(1);
        extendingError = Math.round(y);
        handError = Math.round(x);


    }

    public void operatorControlBlue() {
        if (linearOpMode.gamepad1.square) {
            pip.YELLOW_MASK_THRESHOLD = 110;
            pip.BLUE_MASK_THRESHOLD = 57;
            pip.RED_MASK_THRESHOLD = 198;
        }
        if (linearOpMode.gamepad1.circle){
            pip.YELLOW_MASK_THRESHOLD = 85;
            pip.BLUE_MASK_THRESHOLD = 138;
            pip.RED_MASK_THRESHOLD = 70;
        }
    }
    public void operatorControlRed() {
        if (linearOpMode.gamepad1.square) {
            pip.YELLOW_MASK_THRESHOLD = 110;
            pip.BLUE_MASK_THRESHOLD = 57;
            pip.RED_MASK_THRESHOLD = 198;
        }
        if (linearOpMode.gamepad1.circle){
            pip.YELLOW_MASK_THRESHOLD = 55;
            pip.BLUE_MASK_THRESHOLD = 70;
            pip.RED_MASK_THRESHOLD = 155;
        }
    }
}
