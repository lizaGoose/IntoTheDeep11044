package org.firstinspires.ftc.teamcode.hardware;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorImpl;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public class CachingDcMotorImpl extends DcMotorImpl {
    public double getPowerThreshold() {
        return powerThreshold;
    }

    public void setPowerThreshold(double powerThreshold) {
        this.powerThreshold = powerThreshold;
    }

    protected double powerThreshold;
    protected double lastPower;

    public int getPositionThreshold() {
        return positionThreshold;
    }

    public void setPositionThreshold(int positionThreshold) {
        this.positionThreshold = positionThreshold;
    }

    protected int positionThreshold;
    protected int lastPosition;

    public CachingDcMotorImpl(DcMotorController controller, int portNumber) {
        this(controller, portNumber, Direction.FORWARD);
    }

    public CachingDcMotorImpl(DcMotorController controller, int portNumber, Direction direction) {
        this(controller, portNumber, direction, MotorConfigurationType.getUnspecifiedMotorType());
    }

    public CachingDcMotorImpl(DcMotorController controller, int portNumber, Direction direction, @NonNull MotorConfigurationType motorType) {
        super(controller, portNumber, direction, motorType);
        this.powerThreshold = 0.0;
        this.lastPower = 0.0;
        this.positionThreshold = 0;
        this.lastPosition = super.getCurrentPosition();
    }

    @Override
    synchronized public void setPower(double power) {
        // Power must be positive when in RUN_TO_POSITION mode : in that mode, the
        // *direction* of rotation is controlled instead by the relative positioning
        // of the current and target positions.
        if (getMode() == RunMode.RUN_TO_POSITION)
            power = Math.abs(power);
        else
            power = adjustPower(power);
        if (Math.abs(power - lastPower) >= powerThreshold) {
            lastPower = power;
            internalSetPower(power);
        }
    }

    @Override
    synchronized public double getPower() {
        double power = controller.getMotorPower(portNumber);
        lastPower = power;
        if (getMode() == RunMode.RUN_TO_POSITION)
            power = Math.abs(power);
        else
            power = adjustPower(power);
        return power;
    }

    @Override
    synchronized public void setTargetPosition(int position) {
        if (Math.abs(lastPosition - position) >= positionThreshold) {
            lastPosition = position;
            position = adjustPosition(position);
            internalSetTargetPosition(position);
        }
    }

    @Override
    synchronized public int getTargetPosition() {
        int position = controller.getMotorTargetPosition(portNumber);
        lastPosition = position;
        return adjustPosition(position);
    }
}
