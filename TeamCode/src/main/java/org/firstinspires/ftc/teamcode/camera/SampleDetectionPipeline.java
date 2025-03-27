package org.firstinspires.ftc.teamcode.camera;

import com.acmerobotics.dashboard.config.Config;

import org.openftc.easyopencv.OpenCvPipeline;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

@Config
public class SampleDetectionPipeline extends OpenCvPipeline {
    /*
     * Working image buffers
     */
    public RotatedRect rotatedRectFitToContour;
    Mat ycrcbMat = new Mat();
    Mat crMat = new Mat();
    Mat cbMat = new Mat();

    Mat blueThresholdMat = new Mat();
    Mat redThresholdMat = new Mat();
    Mat yellowThresholdMat = new Mat();

    Mat morphedBlueThreshold = new Mat();
    Mat morphedRedThreshold = new Mat();
    Mat morphedYellowThreshold = new Mat();

    Mat contoursOnPlainImageMat = new Mat();
    public double xSample, ySample, Angle, Area, lastAngle;

    public double InchPerPixel = 10.4, matrixKoeff = -0.2;

    /*
     * Threshold values
     */
    public static int YELLOW_MASK_THRESHOLD = 110;
    public static int BLUE_MASK_THRESHOLD = 57;
    public static int RED_MASK_THRESHOLD = 198;
    public static double MAX_AREA = 2000;
    public static double MIN_AREA = 600, k = 118, k2 = 384;
    public static int MAX_X = 345, MIN_X = 135;


    /*
     * Elements for noise reduction
     */
    Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3.5, 3.5));
    Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3.5, 3.5));

    /*
     * Colors
     */
    static final Scalar RED = new Scalar(255, 0, 0);
    static final Scalar BLUE = new Scalar(0, 0, 255);
    static final Scalar YELLOW = new Scalar(255, 255, 0);

    static final int CONTOUR_LINE_THICKNESS = 2;

    ArrayList<AnalyzedSample> internalSampleList = new ArrayList<>();
    ArrayList<AnalyzedSample> internalRedSample = new ArrayList<>();
    ArrayList<AnalyzedSample> internalBlueSample = new ArrayList<>();
    ArrayList<AnalyzedSample> internalYellowSample = new ArrayList<>();

    volatile ArrayList<AnalyzedSample> clientSampleList = new ArrayList<>();
    volatile ArrayList<AnalyzedSample> clientRedSample = new ArrayList<>();
    volatile ArrayList<AnalyzedSample> clientBlueSample = new ArrayList<>();
    volatile ArrayList<AnalyzedSample> clientYellowSample = new ArrayList<>();

    /*
     * Viewport stages
     */
    enum Stage {
        FINAL,
        YCrCb,
        MASKS,
        MASK_RED,
        MASK_RED_NR,
        MASK_YELLOW,
        MASK_YELLOW_NR,
        MASK_BLUE,
        MASK_BLUE_NR,
        MASKS_NR,
        CONTOURS
    }

    Stage[] stages = Stage.values();
    public static int stageNum = 0;


    @Override
    public void onViewportTapped() {
        int nextStageNum = stageNum + 1;

        if (nextStageNum >= stages.length) {
            nextStageNum = 0;
        }

        stageNum = nextStageNum;
    }

    @Override
    public Mat processFrame(Mat input) {
        // We'll be updating this with new data below
        internalSampleList.clear();
        internalRedSample.clear();
        internalBlueSample.clear();
        internalYellowSample.clear();

        /*
         * Run the image processing
         */
        findContours(input);

        clientSampleList = new ArrayList<>(internalSampleList);
        clientRedSample = new ArrayList<>(internalRedSample);
        clientBlueSample = new ArrayList<>(internalBlueSample);
        clientYellowSample = new ArrayList<>(internalYellowSample);

        /*
         * Decide which buffer to send to the viewport
         */
        switch (stages[stageNum]) {
            case YCrCb: {
                return ycrcbMat;
            }

            case MASKS: {
                Mat masks = new Mat();
                Core.addWeighted(yellowThresholdMat, 1.0, redThresholdMat, 1.0, 0.0, masks);
                Core.addWeighted(masks, 1.0, blueThresholdMat, 1.0, 0.0, masks);
                return masks;
            }

            case MASK_BLUE: {
                return blueThresholdMat;
            }

            case MASK_BLUE_NR: {
                return morphedBlueThreshold;
            }

            case MASK_RED: {
                return redThresholdMat;
            }

            case MASK_RED_NR: {
                return morphedRedThreshold;
            }

            case MASK_YELLOW: {
                return yellowThresholdMat;
            }

            case MASK_YELLOW_NR: {
                return morphedYellowThreshold;
            }

            case MASKS_NR: {
                Mat masksNR = new Mat();
                Core.addWeighted(morphedYellowThreshold, 1.0, morphedRedThreshold, 1.0, 0.0, masksNR);
                Core.addWeighted(masksNR, 1.0, morphedBlueThreshold, 1.0, 0.0, masksNR);
                return masksNR;
            }

            case CONTOURS: {
                return contoursOnPlainImageMat;
            }

            default: {
                return input;
            }
        }
    }

    public ArrayList<AnalyzedSample> getDetectedSamples() {
        return clientSampleList;
    }

    public ArrayList<AnalyzedSample> getRedSamples() {
        return clientRedSample;
    }

    public ArrayList<AnalyzedSample> getBlueSamples() {
        return clientBlueSample;
    }

    public ArrayList<AnalyzedSample> getYellowSamples() {
        return clientYellowSample;
    }

    void findContours(Mat input) {
        // Convert the input image to YCrCb color space
        Imgproc.cvtColor(input, ycrcbMat, Imgproc.COLOR_RGB2YCrCb);

        // Extract the Cb and Cr channels
        Core.extractChannel(ycrcbMat, cbMat, 2); // Cb channel index is 2
        Core.extractChannel(ycrcbMat, crMat, 1); // Cr channel index is 1

        // Threshold the channels to form masks
        Imgproc.threshold(cbMat, blueThresholdMat, BLUE_MASK_THRESHOLD, 255, Imgproc.THRESH_BINARY);
        Imgproc.threshold(crMat, redThresholdMat, RED_MASK_THRESHOLD, 255, Imgproc.THRESH_BINARY);
        Imgproc.threshold(cbMat, yellowThresholdMat, YELLOW_MASK_THRESHOLD, 255, Imgproc.THRESH_BINARY_INV);

        // Apply morphology to the masks
        morphMask(blueThresholdMat, morphedBlueThreshold);
        morphMask(redThresholdMat, morphedRedThreshold);
        morphMask(yellowThresholdMat, morphedYellowThreshold);

        // Find contours in the masks
        ArrayList<MatOfPoint> blueContoursList = new ArrayList<>();
        Imgproc.findContours(morphedBlueThreshold, blueContoursList, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

        ArrayList<MatOfPoint> redContoursList = new ArrayList<>();
        Imgproc.findContours(morphedRedThreshold, redContoursList, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

        ArrayList<MatOfPoint> yellowContoursList = new ArrayList<>();
        Imgproc.findContours(morphedYellowThreshold, yellowContoursList, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

        // Filter contours
        blueContoursList.removeIf(this::filterCountour);
        redContoursList.removeIf(this::filterCountour);
        yellowContoursList.removeIf(this::filterCountour);

        // Create a plain image for drawing contours
        contoursOnPlainImageMat = Mat.zeros(input.size(), input.type());

        // Analyze and draw contours
        blueContoursList.forEach(contour -> analyzeContour(contour, input, SampleColor.BLUE));

        redContoursList.forEach(contour -> analyzeContour(contour, input, SampleColor.RED));

        yellowContoursList.forEach(contour -> analyzeContour(contour, input, SampleColor.YELLOW));
    }

    void morphMask(Mat input, Mat output) {
        /*
         * Apply erosion and dilation for noise reduction
         */
        Imgproc.erode(input, output, erodeElement);
        Imgproc.erode(output, output, erodeElement);

        Imgproc.dilate(output, output, dilateElement);
        Imgproc.dilate(output, output, dilateElement);
    }

    boolean filterCountour(MatOfPoint countour) {
        double countourArea = Imgproc.contourArea(countour);
        return MAX_AREA <= countourArea || countourArea <= MIN_AREA;
    }

    void analyzeContour(MatOfPoint contour, Mat input, SampleColor sampleColor) {
        // Transform the contour to a different format
        Point[] points = contour.toArray();
        MatOfPoint2f contour2f = new MatOfPoint2f(points);

        // Fit a rotated rectangle to the contour and draw it
        rotatedRectFitToContour = Imgproc.minAreaRect(contour2f);

        // Adjust the angle based on rectangle dimensions
        double rotRectAngle = rotatedRectFitToContour.angle;
        if (rotatedRectFitToContour.size.width < rotatedRectFitToContour.size.height) {
            rotRectAngle += 90;
        }

        double rotRectX = Math.round(rotatedRectFitToContour.center.x);
        double rotRectY = Math.round(rotatedRectFitToContour.center.y);
        if (MIN_X >= rotRectX || rotRectX >= MAX_X) {
            return;
        }
        drawRotatedRect(rotatedRectFitToContour, input, sampleColor);
        drawRotatedRect(rotatedRectFitToContour, contoursOnPlainImageMat, sampleColor);

        // Compute the angle and store it
        double angle = rotRectAngle - 90;
        drawTagText(rotatedRectFitToContour, (int) Math.round(angle) + " deg, " + (int) rotRectY + " y", input, sampleColor);

        // Store the detected sample information
        AnalyzedSample analyzedSample = new AnalyzedSample();
        analyzedSample.angle = angle;
        analyzedSample.sampleColor = sampleColor;
        analyzedSample.x = rotRectX;
        analyzedSample.y = rotRectY;
        analyzedSample.area = rotatedRectFitToContour.size.area();

        internalSampleList.add(analyzedSample);

        switch (sampleColor) {
            case RED:
                internalRedSample.add(analyzedSample);
                break;
            case BLUE:
                internalBlueSample.add(analyzedSample);
                break;
            case YELLOW:
                internalYellowSample.add(analyzedSample);
                break;
        }
    }

    static void drawTagText(RotatedRect rect, String text, Mat mat, SampleColor sampleColor) {
        Scalar colorScalar = getColorScalar(sampleColor);

        Imgproc.putText(
                mat, // The buffer we're drawing on
                text, // The text we're drawing
                new Point( // The anchor point for the text
                        rect.center.x - 50,  // x anchor point
                        rect.center.y + 25), // y anchor point
                Imgproc.FONT_HERSHEY_PLAIN, // Font
                2, // Font size
                colorScalar, // Font color
                2); // Font thickness
    }

    static void drawRotatedRect(RotatedRect rect, Mat drawOn, SampleColor sampleColor) {
        /*
         * Draws a rotated rectangle by drawing each of the 4 lines individually
         */
        Point[] points = new Point[4];
        rect.points(points);

        Scalar colorScalar = getColorScalar(sampleColor);

        for (int i = 0; i < 4; ++i) {
            Imgproc.line(drawOn, points[i], points[( i + 1 ) % 4], colorScalar, 2);
        }
    }


    static Scalar getColorScalar(SampleColor sampleColor) {
        switch (sampleColor) {
            case BLUE:
                return BLUE;
            case YELLOW:
                return YELLOW;
            default:
                return RED;
        }
    }
}