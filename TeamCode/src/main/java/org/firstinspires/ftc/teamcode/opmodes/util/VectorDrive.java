package org.firstinspires.ftc.teamcode.opmodes.util;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.teamcode.dashboard.FieldDrawer;
import org.firstinspires.ftc.teamcode.drive.Drive;

@Config
@TeleOp
public class VectorDrive extends LinearOpMode {
    private final FtcDashboard dashboard = FtcDashboard.getInstance();
    public static double X = 0;
    public static double Y = 0;
    public static double angle = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        Drive drive = new Drive(hardwareMap, new Pose2d(-64.33725775089238, -17.939825444151644, Math.toRadians(0)));
        DigitalChannel frontSensor = hardwareMap.get(DigitalChannel.class, "DistanceFront");

        waitForStart();

        while (opModeIsActive()) {
            for (double power : drive.setPowerByPose(new Pose2d(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    gamepad1.right_trigger - gamepad1.left_trigger
            ))) {
                telemetry.addLine(String.valueOf(power));
            }
            for (double power : drive.getMotorPowers()) {
                telemetry.addLine(String.valueOf(power));
            }
            drive.updateLocalizer();
            telemetry.addData("Heading", drive.getPoseEstimate().getHeading());
            telemetry.addData("Raw External Heading", drive.getRawExternalHeading());
            telemetry.addData("Corrected Heading", drive.getCorrectedHeading(angle));
            telemetry.addData("x", drive.getPoseEstimate().getX());
            telemetry.addData("y", drive.getPoseEstimate().getY());
            telemetry.addData("front_sensor", frontSensor.getState());
            telemetry.update();

            TelemetryPacket packet = new TelemetryPacket();
            Canvas canvas = packet.fieldOverlay();
            Vector2d pos = drive.getPoseEstimate().vec();
            FieldDrawer.drawVectorFromRobot(canvas, pos, new Vector2d(-gamepad1.left_stick_y, -gamepad1.left_stick_x));
            FieldDrawer.drawPoint(canvas, pos, 1, "blue");
            FieldDrawer.drawRobot(canvas, drive.getPoseEstimate());

            dashboard.sendTelemetryPacket(packet);
        }
    }
}
