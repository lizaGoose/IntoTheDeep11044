package org.firstinspires.ftc.teamcode.opmodes.util;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.auto.Trajectory;
import org.firstinspires.ftc.teamcode.auto.Drive;

import java.io.IOException;

@Autonomous(group = "util")
@Config
public class PointPIDTuner extends OpMode {
    private Drive drive;
    private Trajectory trajectoryForward, trajectoryBack;
    public static double SLOWDOWN_POINT = 0.5;
    public static double SLOWDOWN_FORCE = 0.15;
    public static int WAIT_POS = 1000;

    public enum State {
        FORWARD,
        BACK
    }
    State currentState;

    private FtcDashboard dashboard;

    @Override
    public void init() {
        drive = new Drive(hardwareMap);

        try {
            trajectoryForward = drive.buildTrajectory("traj16.json")
                    .addSpatialMarker(SLOWDOWN_POINT, () -> {
                        drive.setForce(SLOWDOWN_FORCE);
                    })
                    .waitForAt(1, WAIT_POS, -180)
                    .addTimeMarker(900, () -> {
                        drive.resetForce();
                    })
                    .build();
            trajectoryBack = drive.buildTrajectory("traj17.json")
                    .addSpatialMarker(SLOWDOWN_POINT, () -> {
                        drive.setForce(SLOWDOWN_FORCE);
                    })
                    .waitForAt(1, WAIT_POS, -180)
                    .addTimeMarker(900, () -> {
                        drive.resetForce();
                    })
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        currentState = State.FORWARD;
    }

    @Override
    public void start() {
        drive.followTrajectory(trajectoryForward);
    }

    @Override
    public void loop() {
        if (!drive.isBusy()) {
            switch (currentState) {
                case FORWARD:
                    drive.followTrajectory(trajectoryBack);
                    currentState = State.BACK;
                    break;
                case BACK:
                    drive.followTrajectory(trajectoryForward);
                    currentState = State.FORWARD;
                    break;
            }
        }
        drive.update();
    }
}
