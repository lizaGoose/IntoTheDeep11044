package org.firstinspires.ftc.teamcode.camera;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.modules.Extend;
import org.firstinspires.ftc.teamcode.modules.Hand;
import org.firstinspires.ftc.teamcode.math.ClawCalculation;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.ScorerPerkid;
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
    OpMode opMode;
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
    public static double /*k = 50,*/ k2 = 340;

    public Vector3D L, P, point;
    public double ticksPerInchLift = 21.25 / 811, fromZeroToFrontOfExtendInches;//0=220/25.4*k+b
    public double a, l, x1, y1, b = 38, k;// 285 = (544.599+220)/25.4*k+b k = 7.36 b = -63.75 k = 13.29 b = -115.11
    public int Wp = 480, Hp = 640;//lHand = 125/25.4
    public Vector3D C, Current, vector;//0=
    public ScorerPerkid scorerPerkid;


    public AutoCamera(OpMode opMode) {
        this.opMode = opMode;
        hardwareMap = opMode.hardwareMap;
        extend = new Extend(opMode);
        hand = new Hand(opMode);
        lift = new Lift(opMode);
        //diff = new Diff(opMode);
       /* clawColorSensor = hardwareMap.get(ColorSensor.class, "Color");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "Color");*/
        scorerPerkid = new ScorerPerkid(opMode);
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
                cam.startStreaming(Hp, Wp, OpenCvCameraRotation.SIDEWAYS_RIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });
//operatorControlRed();
    }

    public List<Double> AutonomousRecognition() {
        //  if(pip.xSample <=300 && pip.xSample >= 100) {177 343
        handPos = ( pip.xSample/* + lift.leftMotor.getCurrentPosition() */ ) / k2;
        //  distanceY = Math.pow(356/21,2)+Math.pow(66/2.54, 2)/*21*k/(356/pip.ySample - 1 + k) /*+ 6*Math.cos(Math.abs(handPos-0.5)*120)*/;
        //Math.pow(Math.pow(distanceY, 2) - Math.pow(66/2.54, 2), 0.5)*ticksPerInchY;
        //  }
        extendPos = ( ( pip.ySample/* + lift.leftMotor.getCurrentPosition()*/ ) - Math.sin(handPos * 30 + 30) * 0 ) * ticksPerInchY * 14 / 132 - k;//*(21/356)
        /*yCoordSampleInches = 480/yCoordMax * pip.ySample;
        xCoordSampleInches = 640/xCoordMax * pip.xSample;
        ticksPerInchY = maxPositionInchesY/yCoordMax;
        extendPos = yCoordSampleInches*ticksPerInchY*10;
        handPos = xCoordSampleInches/10;*/
        return Arrays.asList(extendPos, handPos);
    }

    public Vector3D kinematics(double liftPosition) {
        a = scorerPerkid.getPos() * 310 - 179;
        l = 113.5 / 25.4;

        x1 = ( pip.xSample - (double) Wp / 2 ) * 0.85 / Wp;
        y1 = -( pip.ySample - (double) Hp / 2 ) * 1.2 / Hp;
        L = new Vector3D(0, -liftPosition * ticksPerInchLift * Math.sin(Math.toRadians(10)), liftPosition * ticksPerInchLift * Math.cos(Math.toRadians(10)) + ( 422 / 25.4 ));
        P = new Vector3D(0, L.getY() + l * Math.sin(Math.toRadians(a)), L.getZ() - l * Math.cos(Math.toRadians(a)));
        C = new Vector3D(P.getX() + x1, P.getY() + Math.sin(Math.toRadians(a - b)) + y1 * Math.cos(Math.toRadians(a - b)), P.getZ() - Math.cos(Math.toRadians(a - b)) + y1 * Math.sin(Math.toRadians(a - b)));
        k = P.getZ() / ( P.getZ() - C.getZ() );
        Current = mul(C, k, P, ( 1 - k ));

        return new Vector3D(Current.getX(), ( ( Current.getY() - Math.cos(Math.asin(Current.getX() / ( 125 / 25.4 ))) * ( 125 / 25.4 ) ) * 13.29 - 115.11 ), Current.getZ());
    }

    public Vector3D getSamplePosition(double liftPosition) {
        a = scorerPerkid.getPos() * 310 - 179;
        l = 113.5 / 25.4;

        x1 = ( pip.xSample - (double) Wp / 2 ) * 0.85 / Wp;
        y1 = -( pip.ySample - (double) Hp / 2 ) * 1.2 / Hp;
        L = new Vector3D(0, -liftPosition * ticksPerInchLift * Math.sin(Math.toRadians(10)), liftPosition * ticksPerInchLift * Math.cos(Math.toRadians(10)) + ( 422 / 25.4 ));
        P = new Vector3D(0, L.getY() + l * Math.sin(Math.toRadians(a)), L.getZ() - l * Math.cos(Math.toRadians(a)));
        C = new Vector3D(P.getX() + x1, P.getY() + Math.sin(Math.toRadians(a - b)) + y1 * Math.cos(Math.toRadians(a - b)), P.getZ() - Math.cos(Math.toRadians(a - b)) + y1 * Math.sin(Math.toRadians(a - b)));
        k = P.getZ() / ( P.getZ() - C.getZ() );
        Current = mul(C, k, P, ( 1 - k ));

        return Current;
    }

    public Vector3D mul(Vector3D vector3D, double constant, Vector3D vector2, double constant2) {
        vector = new Vector3D(vector3D.getX() * constant + vector2.getX() * constant2, vector3D.getY() * constant + vector2.getY() * constant2, vector3D.getZ() * constant + vector2.getZ() * constant2);
        //   vector =  new Vector3D(vector3D.getX()*constant+mulPoint(point3, (constant2)).x, vector3D.getY()*constant+mulPoint(point3, (constant2)).y, vector3D.getZ()*constant+mulPoint(point3, (constant2)).z);
        return vector;
    }

    /*public Point3 mulPoint(Point3 point3, double constant){
        point = new Point3(point3.x*constant, point3.y*constant, point3.z*constant);
        return point;
    }*/
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

        SampleDetectionPipeline.YELLOW_MASK_THRESHOLD = 85;
        SampleDetectionPipeline.BLUE_MASK_THRESHOLD = 138;
        SampleDetectionPipeline.RED_MASK_THRESHOLD = 70;

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

    public void operatorControlYellow() {

        SampleDetectionPipeline.YELLOW_MASK_THRESHOLD = 95;
        SampleDetectionPipeline.BLUE_MASK_THRESHOLD = 57;
        SampleDetectionPipeline.RED_MASK_THRESHOLD = 198;

        x = pip.xSample;
        y = pip.ySample;
    }
}
