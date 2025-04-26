package org.firstinspires.ftc.teamcode.camera;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
/*
import cv2 as cv
import numpy as np
from copy import deepcopy
import math

MIN_AREA = 600
MAX_AREA = 4000
MIN_X = 135
MAX_X = 345


def contour_center(contour) -> tuple[int, int]:
    M = cv.moments(contour)
    cx = M['m10']/M['m00']
    cy = M['m01']/M['m00']
    return cx, cy

def runPipeline(image, llrobot):
    # Initialize variables to avoid unboundlocal errors
    largestContour = np.array([[]])
    llpython = [0, 0, 0, 0, 0, 0, 0, 0]

    # 1. Convert the input image to the HSV color space
    img_hsv = cv.cvtColor(image, cv.COLOR_BGR2HSV)

    # 2. Create a binary image using color thresholding
    # Note: Adjust these values based on your specific color target
    lower_bound = np.array([100, 0, 0])
    upper_bound = np.array([120, 255, 255])
    img_threshold = cv.inRange(img_hsv, lower_bound, upper_bound)

    mask = cv.rectangle(np.zeros(img_threshold.shape, dtype=np.uint8), (280, 0), (640, 480), (255, 255, 255), -1)

    img_threshold = cv.bitwise_and(img_threshold, mask)

    # 3. Find contours in the binary image
    contours, _ = cv.findContours(img_threshold, cv.RETR_EXTERNAL, cv.CHAIN_APPROX_SIMPLE)

    contours = list(filter(lambda contour:  MIN_X <= contour_center(contour)[1] <= MAX_X,
            filter(lambda contour: MIN_AREA <= cv.contourArea(contour) <= MAX_AREA, contours)
        )
    )

    # penis rustama
    if len(contours) > 0:
        # Get the largest contour
        largestContour = min(contours, key=lambda contour: contour_center(contour)[0])

        # 4. Calculate the rotated bounding rectangle
        rect = cv2.minAreaRect(largestContour)

        # box = cv2.boxPoints(rect)
        # box = np.int0(box)
        # cv2.drawContours(image, [box], 0, (0, 255, 0), 2)

        center = rect[0]

        tx_pixels = center[0]
        ty_pixels = center[1]  # Inverted because y increases downward in image coordinates

        rect_width, rect_height = rect[1]

        angle = rect[2]
        if rect_width < rect_height:
            angle -= 90

        # Format: [valid, tx_pixels, ty_pixels, rotation_angle, width, height, 0, 0]
        llpython = [1, tx_pixels, ty_pixels, angle, rect_width, rect_height, 0, 0]
    else:
        # If no contours found, draw a message on the image
        cv2.putText(image, "No target found", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 255), 2)
    print(llpython)
    return largestContour, img_threshold, llpython


*/

/*
mport cv2 as cv
import numpy as np
from copy import deepcopy
import math

MIN_AREA = 600
MAX_AREA = 4000
MIN_X = 135
MAX_X = 345


def contour_center(contour) -> tuple[int, int]:
    M = cv.moments(contour)
    cx = M['m10']/M['m00']
    cy = M['m01']/M['m00']
    return cx, cy

def runPipeline(image, llrobot):
    # Initialize variables to avoid unboundlocal errors
    largestContour = np.array([[]])
    llpython = [0, 0, 0, 0, 0, 0, 0, 0]

    # 1. Convert the input image to the HSV color space
    img_hsv = cv.cvtColor(image, cv.COLOR_BGR2HSV)

    # 2. Create a binary image using color thresholding
    # Note: Adjust these values based on your specific color target
    lower_bound = np.array([100, 0, 0])
    upper_bound = np.array([120, 255, 255])
    img_threshold = cv.inRange(img_hsv, lower_bound, upper_bound)

    mask = cv.rectangle(np.zeros(img_threshold.shape, dtype=np.uint8), (280, 0), (640, 480), (255, 255, 255), -1)

    img_threshold = cv.bitwise_and(img_threshold, mask)

    # 3. Find contours in the binary image
    contours, _ = cv.findContours(img_threshold, cv.RETR_EXTERNAL, cv.CHAIN_APPROX_SIMPLE)

    contours = list(filter(lambda contour:  MIN_X <= contour_center(contour)[1] <= MAX_X,
            filter(lambda contour: MIN_AREA <= cv.contourArea(contour) <= MAX_AREA, contours)
        )
    )

    # penis rustama
    if len(contours) > 0:
        # Get the largest contour
        largestContour = min(contours, key=lambda contour: contour_center(contour)[0])

        # 4. Calculate the rotated bounding rectangle
        rect = cv2.minAreaRect(largestContour)

        # box = cv2.boxPoints(rect)
        # box = np.int0(box)
        # cv2.drawContours(image, [box], 0, (0, 255, 0), 2)

        center = rect[0]

        tx_pixels = center[0]
        ty_pixels = center[1]  # Inverted because y increases downward in image coordinates

        rect_width, rect_height = rect[1]

        angle = rect[2]
        if rect_width < rect_height:
            angle -= 90

        # Format: [valid, tx_pixels, ty_pixels, rotation_angle, width, height, 0, 0]
        llpython = [1, tx_pixels, ty_pixels, angle, rect_width, rect_height, 0, 0]
    else:
        # If no contours found, draw a message on the image
        cv2.putText(image, "No target found", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 255), 2)

    return largestContour, img_threshold, llpython


 */

public class LimelightCamera {
    private final Limelight3A limelight;

    /**
     * @noinspection unused
     */
    public enum Pipeline {
        PYTHON_BLUE(0), PYTHON_SAMPLE(1), THRESHOLD_BLUE(2),
        PYTHON_BLUE_2(3), PYTHON_RED(4);

        final int index;

        Pipeline(int index) {
            this.index = index;
        }
    }

    private volatile Pipeline currentPipeline = Pipeline.PYTHON_BLUE_2;


    public LimelightCamera(OpMode opMode) {
        limelight = opMode.hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(currentPipeline.index);
    }

    public CompletableFuture<Void> asyncSetPipeline(Pipeline pipeline) {
        return CompletableFuture.runAsync(() -> {
            boolean result = false;
            while (!result) {
                result = limelight.pipelineSwitch(pipeline.index);
            }
            currentPipeline = pipeline;
        });
    }

    public void setPipeline(Pipeline pipeline) {
        currentPipeline = pipeline;
        boolean result = false;
        while (!result) {
            result = limelight.pipelineSwitch(pipeline.index);
        }
    }

    public void setPipelineSingle(Pipeline pipeline) {
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

    public void takeSnapshot() {
        takeSnapshot("snap " + System.currentTimeMillis());
    }

    public void takeSnapshot(String name) {
        limelight.captureSnapshot(name);
    }

    public LLStatus getStatus() {
        return limelight.getStatus();
    }

    // Ема
    @NonNull
    public Optional<double[]> getPythonOutput() {
        if (!isRunning()) return Optional.empty();
        return getResult().map(LLResult::getPythonOutput).filter(arr -> arr.length > 0);
    }

    @NonNull
    public Optional<LLResult> getResult() {
        if (!isRunning()) return Optional.empty();
        return Optional.ofNullable(limelight.getLatestResult()).filter(llResult -> llResult.getPipelineIndex() == currentPipeline.index);
    }

    private final int WIDTH = 480;
    private final int HEIGHT = 640;
    private final double b = 69;
    private final double ticksPerInchLift = 21.25 / 811;

    public Optional<AnalyzedSample> getSamplePose(double liftPosition, double scorerAngle) {
        return getPythonOutput().map(output -> {
            // 0.5774195 * 310 - 179 = 0 ????????????????????
            double a = scorerAngle;
            double l = 116 / 25.4;

            double x1 = ( output[2] - (double) WIDTH / 2 ) * 0.475 / WIDTH;
            double y1 = ( output[1] - (double) HEIGHT / 2 ) * 1.2 / HEIGHT;
            Vector3D L = new Vector3D(0, -liftPosition * ticksPerInchLift * Math.sin(Math.toRadians(10)), liftPosition * ticksPerInchLift * Math.cos(Math.toRadians(10)) + ( 422 / 25.4 ));
            Vector3D P = new Vector3D(0, L.getY() + l * Math.sin(Math.toRadians(a)), L.getZ() - l * Math.cos(Math.toRadians(a)));
            double sinAB = Math.sin(Math.toRadians(a - b));
            double cosAB = Math.cos(Math.toRadians(a - b));
            Vector3D C = new Vector3D(P.getX() + x1, P.getY() + sinAB + y1 * cosAB, P.getZ() - cosAB + y1 * sinAB);
            double k = P.getZ() / ( P.getZ() - C.getZ() );
            Vector3D Current = mul(C, k, P, ( 1 - k ));

            SampleColor color;
            if (Objects.requireNonNull(currentPipeline) == Pipeline.PYTHON_RED) {
                color = SampleColor.RED;
            } else {
                color = SampleColor.BLUE;
            }

            return new AnalyzedSample(
                    Current.getX() * 25.4,
                    Current.getY() * 25.4 - 220.04,
                    output[3],
                    color
            );
        });
    }

    public Vector3D mul(Vector3D vector3D, double constant, Vector3D vector2, double constant2) {
        return new Vector3D(vector3D.getX() * constant + vector2.getX() * constant2, vector3D.getY() * constant + vector2.getY() * constant2, vector3D.getZ() * constant + vector2.getZ() * constant2);
    }

}
