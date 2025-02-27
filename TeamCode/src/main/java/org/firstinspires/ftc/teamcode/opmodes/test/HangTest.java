package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.Hang;

@Config
@TeleOp(group = "test")
public class HangTest extends OpMode {
    private Hang hangLift;
    private FtcDashboard dashboard;
    private MultipleTelemetry mulTelemetry;
    private double lastReference = 0;
    public static double reference = 0;
    public static double power = 0.5;

    @Override
    public void init() {
        hangLift = new Hang(this);
        dashboard = FtcDashboard.getInstance();
        mulTelemetry = new MultipleTelemetry(dashboard.getTelemetry(), telemetry);
    }

    @Override
    public void loop() {
        hangLift.control();

        TelemetryPacket packet = new TelemetryPacket();
        packet.put("motor_pos", hangLift.hang.getCurrentPosition());
        packet.put("reference", hangLift.getReference());
        dashboard.sendTelemetryPacket(packet);

        mulTelemetry.addData("motor_pos", hangLift.hang.getCurrentPosition());
        mulTelemetry.addData("reference", hangLift.getReference());
        mulTelemetry.update();
    }
}
