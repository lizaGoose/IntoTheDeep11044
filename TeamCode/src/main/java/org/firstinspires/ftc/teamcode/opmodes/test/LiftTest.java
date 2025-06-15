package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.Lift;

@Config
@TeleOp(group = "test")
public class LiftTest extends OpMode {
    private Lift hangLift;
    private FtcDashboard dashboard;
    private MultipleTelemetry mulTelemetry;
    private double lastReference = 0;
    public static double reference = 0;
    public static double power = 0.5;

    @Override
    public void init() {
        hangLift = new Lift(this);
        dashboard = FtcDashboard.getInstance();
        mulTelemetry = new MultipleTelemetry(dashboard.getTelemetry(), telemetry);
    }

    @Override
    public void loop() {
        if (gamepad1.left_stick_y != 0) {
            hangLift.setPower(-gamepad1.left_stick_y);
            reference = hangLift.getPosition();
        }
        else {
            if (lastReference != reference) {
                lastReference = hangLift.getReference();
                hangLift.setReference(reference);
            }
            hangLift.PIDControl();
        }

        TelemetryPacket packet = new TelemetryPacket();
        packet.put("left_motor_pos", hangLift.getPosition());
        packet.put("right_motor_pos", hangLift.getPosition());
        packet.put("reference", hangLift.getReference());
        dashboard.sendTelemetryPacket(packet);

        mulTelemetry.addData("left_motor_pos", hangLift.getPosition());
        mulTelemetry.addData("right_motor_pos", hangLift.getPosition());
        mulTelemetry.addData("reference", hangLift.getReference());
        mulTelemetry.update();
    }
}
