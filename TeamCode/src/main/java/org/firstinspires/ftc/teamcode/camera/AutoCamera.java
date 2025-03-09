package org.firstinspires.ftc.teamcode.camera;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.modules.Extend;
import org.firstinspires.ftc.teamcode.modules.Hand;
import org.firstinspires.ftc.teamcode.math.ClawCalculation;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.opencv.core.RotatedRect;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Config
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
    public Hand hand;
    public Lift lift;
    public double xFromGamepad, yFromGamepad, xCoordMax = 499.293 / 2.54, yCoordMax = 544.599 / 2.54;
    public double xCoordSampleInches, yCoordSampleInches, maxPositionInchesY = 16, ticksPerInchY = 259 / 16, extendPos, handPos, distanceY, distanceX;
    public static double k = 50, k2 = 350;


    public AutoCamera(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        hardwareMap = linearOpMode.hardwareMap;
        extend = new Extend(linearOpMode);
        hand = new Hand(linearOpMode);
        lift = new Lift(linearOpMode);
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

    public List<Double> AutonomousRecognition() {
        //  if(pip.xSample <=300 && pip.xSample >= 100) {177 343
        handPos = (pip.xSample + lift.leftMotor.getCurrentPosition())/ k2;
        //  distanceY = Math.pow(356/21,2)+Math.pow(66/2.54, 2)/*21*k/(356/pip.ySample - 1 + k) /*+ 6*Math.cos(Math.abs(handPos-0.5)*120)*/;
        //Math.pow(Math.pow(distanceY, 2) - Math.pow(66/2.54, 2), 0.5)*ticksPerInchY;
        //  }
        extendPos = ((pip.ySample+lift.leftMotor.getCurrentPosition()) - Math.sin(handPos * 30 + 30) * 0 ) * ticksPerInchY * 25 / 427 - k;//*(21/356)
        /*yCoordSampleInches = 480/yCoordMax * pip.ySample;
        xCoordSampleInches = 640/xCoordMax * pip.xSample;
        ticksPerInchY = maxPositionInchesY/yCoordMax;
        extendPos = yCoordSampleInches*ticksPerInchY*10;
        handPos = xCoordSampleInches/10;*/
        return Arrays.asList(extendPos, handPos);
    }

    public void goToElementPosExtend(double pos) {
        extend.setReference(pos);
        extend.PIDControl();
    }

    public void goToElementPosHand(double pos) {
        hand.setPosition(pos);
    }

    public double getAngle() {
        Angle = pip.Angle;
        return Angle;
    }

    public void visionInUse() {

        List<Double> Ang = new ArrayList<Double>();

        Area = pip.Area;

        Angle = pip.Angle;

        //     x = pip.xSample;
        //   y = pip.ySample;
        clawError = Math.round(90 - Angle);
        xActual = ClawCalculation.camToRealWorldCoordinates(180, 4.2, 6.2, x, y, pip.InchPerPixel).get(0);
        yActual = ClawCalculation.camToRealWorldCoordinates(180, 4.2, 6.2, x, y, pip.InchPerPixel).get(1);
        extendingError = Math.round(y);
        handError = Math.round(x);


    }

    public void operatorControlBlue() {
        if (linearOpMode.gamepad1.square) {
            pip.YELLOW_MASK_THRESHOLD = 95;
            pip.BLUE_MASK_THRESHOLD = 57;
            pip.RED_MASK_THRESHOLD = 198;
        }
        if (linearOpMode.gamepad1.circle) {
            pip.YELLOW_MASK_THRESHOLD = 85;
            pip.BLUE_MASK_THRESHOLD = 138;
            pip.RED_MASK_THRESHOLD = 70;
        }
        x = pip.xSample;
        y = pip.ySample;
    }

    public void operatorControlRed() {
       /* if (linearOpMode.gamepad1.square) {
            pip.YELLOW_MASK_THRESHOLD = 95;
            pip.BLUE_MASK_THRESHOLD = 57;
            pip.RED_MASK_THRESHOLD = 198;
        }*/
     //   if (linearOpMode.gamepad1.circle) {
            pip.YELLOW_MASK_THRESHOLD = 5;
            pip.BLUE_MASK_THRESHOLD = 70;
            pip.RED_MASK_THRESHOLD = 170;
        //}
        x = pip.xSample;
        y = pip.ySample;
    }
}
