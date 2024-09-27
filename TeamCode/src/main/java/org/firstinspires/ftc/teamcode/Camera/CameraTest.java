package org.firstinspires.ftc.teamcode.Camera;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.sun.tools.javac.util.ArrayUtils;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@TeleOp
public class CameraTest extends LinearOpMode {
    OpenCvCamera cam;
    double centerCoord;
    double Angle, x, y;
    double index;
    String sampleCoordinatesStatus;

    @Override
    public void runOpMode() throws InterruptedException {
        int cameraMonitorViewId = hardwareMap.appContext
                .getResources().getIdentifier("cameraMonitorViewId",
                        "id", hardwareMap.appContext.getPackageName());
        cam = OpenCvCameraFactory.getInstance()
                .createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        cam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        SampleDetectionPipelinePNP pip = new SampleDetectionPipelinePNP();
        cam.setPipeline(pip);

        cam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                cam.startStreaming(800, 448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });


        waitForStart();

        while (opModeIsActive()){
            List<Double> Center = new ArrayList<Double>(pip.CenterStone);
            //cam.resumeViewport();

           // telemetry.addData("Angle", pip.RotateStone);
           // telemetry.addData("Center Coordinates", pip.CenterStone);
            telemetry.addData("Center Coordinates", centerCoord);
            telemetry.addData("Angle", Angle);
            telemetry.addData("x", x);
            telemetry.addData("y", y);
            telemetry.addData("index", index);


            if (pip.CenterStone.isEmpty() == true) {
                centerCoord = 0;
                Angle = 0;
                x = 0;
                y = 0;
            }
            else {
                centerCoord = Collections.min(pip.CenterStone.stream().collect(Collectors.toList()));
                if (centerCoord != 0){

                    if (Angle != 180) {

                    }
                    if(x !=0){

                    }
                    if(y!=0){

                    }
                }

                if ((pip.rotatedRectFitToContour.center.dot(pip.rotatedRectFitToContour.center)) == centerCoord){
                    Angle = pip.rotatedRectFitToContour.angle;
                    x = pip.rotatedRectFitToContour.center.x;
                    y = pip.rotatedRectFitToContour.center.y;
                    index = Center.indexOf(pip.rotatedRectFitToContour.center.dot(pip.rotatedRectFitToContour.center));
                }

                if(gamepad1.a){
                    pip.CenterStone.clear();
                    pip.RotateStone.clear();
                }

            }
            telemetry.update();

            //telemetry.addData("Sample Coordinates Status", sampleCoordinatesStatus);
           /* if(Collections.min(pip.CenterStone) != centerCoord){
                sampleCoordinatesStatus = "is not ready to be taken";
            }
            else {
                sampleCoordinatesStatus = "is ready to be taken";

            }*/



        }
        cam.stopStreaming();
    }
}