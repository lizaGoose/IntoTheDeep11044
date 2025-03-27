package org.firstinspires.ftc.teamcode.camera;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.Optional;

/*
import cv2
import numpy as np


def runPipeline(image, llrobot):
    # Initialize variables to avoid unboundlocal errors
    largestContour = np.array([[]])
    llpython = [0, 0, 0, 0, 0, 0, 0, 0]

    # 1. Convert the input image to the HSV color space
    img_hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)

    # 2. Create a binary image using color thresholding
    # Note: Adjust these values based on your specific color target
    lower_bound = np.array([100, 0, 0])
    upper_bound = np.array([120, 255, 255])
    img_threshold = cv2.inRange(img_hsv, lower_bound, upper_bound)

    mask = cv2.rectangle(np.zeros(img_threshold.shape, dtype=np.uint8), (600, 0), (1280, 960), (255, 255, 255), -1)

    img_threshold = cv2.bitwise_and(img_threshold, mask)

    # 3. Find contours in the binary image
    contours, _ = cv2.findContours(img_threshold, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    for contour in contours:
        rect = cv2.minAreaRect()


    print(llpython)
    return largestContour, image, llpython


 */


public class LimelightCamera {
    private final Limelight3A limelight;
    public double[] pythonOutputs;

    public enum Pipeline {
        PYTHON_BLUE(0),
        PYTHON_SAMPLE(1),
        BLUE_THRESH(2);

        final int index;

        Pipeline(int index) {
            this.index = index;
        }
    }

    private Pipeline currentPipeline = Pipeline.PYTHON_BLUE;


    public LimelightCamera(LinearOpMode linearOpMode) {
        limelight = linearOpMode.hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.pipelineSwitch(currentPipeline.index);
    }

    public void setPipeline(Pipeline pipeline) {
        currentPipeline = pipeline;
        limelight.pipelineSwitch(pipeline.index);
    }

    public void start() {
        limelight.start();
    }

    public void pause() {
        limelight.pause();
    }

    public void stop() {
        limelight.stop();
    }

    public boolean isRunning() {
        return limelight.isRunning();
    }

    // Ема
    public Optional<double[]> getPythonOutput() {
        if (!isRunning())
            return Optional.empty();
        return getResult().map(LLResult::getPythonOutput).filter(arr -> arr.length > 0);
    }

    public Optional<LLResult> getResult() {
        if (!isRunning())
            return Optional.empty();
        return Optional.ofNullable(limelight.getLatestResult()).filter(llResult -> llResult.getPipelineIndex() == currentPipeline.index);
    }

}
