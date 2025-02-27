package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.Extend;

@Config
@TeleOp(group = "test")
public class ExtendTest extends OpMode {
    private Servo leftServo, rightServo;
    private Extend extend;
    private FtcDashboard dashboard;
    private MultipleTelemetry mulTelemetry;
    public static double reference = 0;
    public static double power = 0;
    public static double position = 0.5;

    @Override
    public void init() {
        extend = new Extend(this);
        dashboard = FtcDashboard.getInstance();
        mulTelemetry = new MultipleTelemetry(dashboard.getTelemetry(), telemetry);
        leftServo = hardwareMap.get(Servo.class, "left_vertical_servo");
        rightServo = hardwareMap.get(Servo.class, "right_vertical_servo");
        rightServo.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void loop() {
        leftServo.setPosition(position);
        rightServo.setPosition(position);


//        extend.setReference(reference);
//        extend.PIDControl();

        TelemetryPacket packet = new TelemetryPacket();
        packet.put("motor_pos", extend.motor.getCurrentPosition());
        packet.put("reference", extend.getReference());
        dashboard.sendTelemetryPacket(packet);

        mulTelemetry.addData("motor_pos", extend.motor.getCurrentPosition());
        mulTelemetry.addData("reference", extend.getReference());
        mulTelemetry.update();
    }
}
