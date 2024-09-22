package org.firstinspires.ftc.teamcode.Camera;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import java.util.Arrays;
import java.util.Collections;

@Autonomous
public class CameraTest extends LinearOpMode {
    OpenCvCamera cam;
    double centerCoord = 0;
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
            cam.resumeViewport();
            telemetry.addData("Angle", pip.RotateStone);
            telemetry.addData("Center Coordinates", pip.CenterStone);
            telemetry.addData("Sample Coordinates Status", sampleCoordinatesStatus);
            if(Collections.min(pip.CenterStone) != centerCoord){
                sampleCoordinatesStatus = "is not ready to be taken";
            }
            else {
                sampleCoordinatesStatus = "is ready to be taken";

            }
            telemetry.update();


        }
        cam.stopStreaming();
    }
}
