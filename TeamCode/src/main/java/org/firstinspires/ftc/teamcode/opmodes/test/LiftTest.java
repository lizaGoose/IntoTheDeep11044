package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Modules.Lift;
import org.firstinspires.ftc.teamcode.Modules.LiftError;

@Config
@TeleOp(group = "test")
public class LiftTest extends OpMode {
    private LiftError hangLift;
    private FtcDashboard dashboard;
    private MultipleTelemetry mulTelemetry;
    private double lastReference = 0;
    public static double reference = 0;
    public static double power = 0.5;

    @Override
    public void init() {
        hangLift = new LiftError(this);
        dashboard = FtcDashboard.getInstance();
        mulTelemetry = new MultipleTelemetry(dashboard.getTelemetry(), telemetry);
    }

    @Override
    public void loop() {
        if (gamepad1.left_stick_y != 0) {
            hangLift.leftMotor.setPower(-gamepad1.left_stick_y);
            hangLift.rightMotor.setPower(-gamepad1.left_stick_y);
            reference = hangLift.rightMotor.getCurrentPosition();
        }
        else {
            if (lastReference != reference) {
                lastReference = hangLift.getReference();
                hangLift.setReference(reference);
            }
            hangLift.PIDControl();
        }

        TelemetryPacket packet = new TelemetryPacket();
        packet.put("left_motor_pos", hangLift.leftMotor.getCurrentPosition());
        packet.put("right_motor_pos", hangLift.rightMotor.getCurrentPosition());
        packet.put("reference", hangLift.getReference());
        dashboard.sendTelemetryPacket(packet);

        mulTelemetry.addData("left_motor_pos", hangLift.leftMotor.getCurrentPosition());
        mulTelemetry.addData("right_motor_pos", hangLift.rightMotor.getCurrentPosition());
        mulTelemetry.addData("reference", hangLift.getReference());
        mulTelemetry.update();
    }
}
