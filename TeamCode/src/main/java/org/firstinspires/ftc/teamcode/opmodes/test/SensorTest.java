package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.internal.ftdi.eeprom.FT_EE_Ctrl;

@Config
@TeleOp(group = "test")
public class SensorTest extends LinearOpMode {
    public static String sensorConfig = "";

    @Override
    public void runOpMode() throws InterruptedException {
        MultipleTelemetry telemetry1 = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        DistanceSensor distanceSensor = hardwareMap.get(DistanceSensor.class, sensorConfig);

        waitForStart();

        while (opModeIsActive()) {
            telemetry1.addData("sensor cm", distanceSensor.getDistance(DistanceUnit.CM));
            telemetry1.addData("sensor mm", distanceSensor.getDistance(DistanceUnit.MM));
            telemetry1.addData("sensor m", distanceSensor.getDistance(DistanceUnit.METER));
            telemetry1.update();
        }
    }
}
