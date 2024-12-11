package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.ServoControllerEx;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.ServoConfigurationType;
import com.qualcomm.robotcore.util.Range;

public class CachingServoImplEx extends ServoImplEx {
    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    protected double threshold;
    protected double lastPosition;

    public CachingServoImplEx(ServoControllerEx controller, int portNumber, @NonNull ServoConfigurationType servoType, double threshold) {
        this(controller, portNumber, Direction.FORWARD, servoType, threshold);
    }

    public CachingServoImplEx(ServoControllerEx controller, int portNumber, Direction direction, @NonNull ServoConfigurationType servoType, double threshold) {
        super(controller, portNumber, direction, servoType);
        this.threshold = threshold;
        lastPosition = super.getPosition();
    }

    @Override
    synchronized public void setPosition(double position) {
        position = Range.clip(position, MIN_POSITION, MAX_POSITION);
        if (direction == Direction.REVERSE) position = reverse(position);
        double scaled = Range.scale(position, MIN_POSITION, MAX_POSITION, limitPositionMin, limitPositionMax);
        if (Math.abs(scaled - lastPosition) >= threshold) {
            internalSetPosition(scaled);
            lastPosition = scaled;
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
