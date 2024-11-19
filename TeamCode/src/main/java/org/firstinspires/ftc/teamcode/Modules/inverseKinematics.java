package org.firstinspires.ftc.teamcode.Modules;


import com.chaquo.python.Kwarg;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Camera.SampleDetectionPipelinePNP;
import org.firstinspires.ftc.teamcode.file.FilesystemUtil;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class inverseKinematics{
    public Python pyth;
    public double x,y,z;
    public PyObject arm;
    LinearOpMode linearOpMode;
    HardwareMap hardwareMap;
    Diff diff;
    DistanceSensor distanceSensor;
    OpenCvCamera cam;
    SampleDetectionPipelinePNP pip;
    double centerCoord;

    public inverseKinematics(LinearOpMode linearOpMode){

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(AppUtil.getDefContext()));
        }

        pyth = Python.getInstance();
        final PyObject file = pyth.getModule("inverseKinematics");

        arm = file.callAttr("Arm", FilesystemUtil.loadFile("arm.urdf").getAbsolutePath());

        this.linearOpMode = linearOpMode;
        hardwareMap = linearOpMode.hardwareMap;

        diff = new Diff(linearOpMode);

        int cameraMonitorViewId = hardwareMap.appContext
                .getResources().getIdentifier("cameraMonitorViewId",
                        "id", hardwareMap.appContext.getPackageName());
        cam = OpenCvCameraFactory.getInstance()
                .createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        cam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        pip = new SampleDetectionPipelinePNP();
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

    public void setCoordinates(){
        distanceSensor = hardwareMap.get(DistanceSensor.class, "Color");
        double[] target_position = {x, y, z};
        double[] target_orientation = {0, 0, 0};

        x+=1;
        y+=2;
        z+=5;

        List<PyObject> res = arm.callAttr("inverse_kinematics",
                target_position,
                target_orientation,
                new Kwarg("orientation_mode", "all")
        ).asList();






        if (pip.Center.size() != 0) {


            centerCoord = Collections.min(pip.Center);
            if (Math.round(pip.rotatedRectFitToContour.center.x/96*2.54) == centerCoord){
                if (pip.CenterStone.isEmpty() == false) {
                    theRect = pip.rotatedRectFitToContour;
                    x = theRect.center.x * pip.CmPerPixel/640 * pip.matrixKoeff;
                    y = theRect.center.y * pip.CmPerPixel/640 * pip.matrixKoeff;

                    Angle = pip.rotatedRectFitToContour.angle;
                }
                Ang.add(Angle);

            }

            clawError = Math.round(90-Angle);

            extendingError = Math.round(y);
            handError = Math.round(x);


            if(extendingError == 0 && handError == 0 && (distanceSensor.getDistance(DistanceUnit.MM) <= 30 )/*&& clawError == 0*/){
                sample_status = true;
            }
            else {
                sample_status = false;
            }



        }


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
            pip.YELLOW_MASK_THRESHOLD = 100;
            pip.BLUE_MASK_THRESHOLD = 57;
            pip.RED_MASK_THRESHOLD = 198;

        } else {
            pip.YELLOW_MASK_THRESHOLD = 57;
            pip.BLUE_MASK_THRESHOLD = 150;
            pip.RED_MASK_THRESHOLD = 198;

        }
    }
}
