package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.ServoImpl;
import com.qualcomm.robotcore.util.Range;

public class CachingServoImpl extends ServoImpl {
    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    protected double threshold;
    protected double lastPosition;

    public CachingServoImpl(ServoController controller, int portNumber, double threshold) {
        this(controller, portNumber, Direction.FORWARD, threshold);

    }

    public CachingServoImpl(ServoController controller, int portNumber, Direction direction, double threshold) {
        super(controller, portNumber, direction);
        this.threshold = threshold;
        lastPosition = super.getPosition();
    }

    @Override
    synchronized public void setPosition(double position) {
        if (Math.abs(position - lastPosition) >= threshold) {
            lastPosition = position;
            position = Range.clip(position, MIN_POSITION, MAX_POSITION);
            if (direction == Direction.REVERSE) position = reverse(position);
            double scaled = Range.scale(position, MIN_POSITION, MAX_POSITION, limitPositionMin, limitPositionMax);
            internalSetPosition(scaled);
        }
    }

    @Override
    synchronized public double getPosition() {
        double position = controller.getServoPosition(portNumber);
        if (direction == Direction.REVERSE) position = reverse(position);
        double scaled = Range.scale(position, limitPositionMin, limitPositionMax, MIN_POSITION, MAX_POSITION);
        lastPosition = scaled;
        return Range.clip(scaled, MIN_POSITION, MAX_POSITION);
    }

    private double reverse(double position) {
        return MAX_POSITION - position + MIN_POSITION;
    }
}
