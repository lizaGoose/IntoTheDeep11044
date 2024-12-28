package org.firstinspires.ftc.teamcode.math;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClawCalculationTest {

    @Test
    public void angleToServoPos() {
        double position = ClawCalculation.angleToServoPos(0, 90);
        assertThat(position).isEqualTo(0.5);
    }

    @Test
    public void angleToServoPos45() {
        double position = ClawCalculation.angleToServoPos(45, 90);
        assertThat(position).isEqualTo(0.75);
    }

    @Test
    public void servoPosToAngle() {
        double angle = ClawCalculation.servoPosToAngle(0.5, 90);
        assertThat(angle).isEqualTo(0);
    }

    @Test
    public void servoPosToAngle45() {
        double angle = ClawCalculation.servoPosToAngle(0.75, 90);
        assertThat(angle).isEqualTo(45);
    }
}