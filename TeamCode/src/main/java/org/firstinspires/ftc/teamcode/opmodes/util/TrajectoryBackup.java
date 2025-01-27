package org.firstinspires.ftc.teamcode.opmodes.util;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.file.FilesystemUtil;

import java.io.File;
import java.io.IOException;

@Config
public class TrajectoryBackup extends LinearOpMode {
    public static String FILENAME = "";
    @Override
    public void runOpMode() throws InterruptedException {
        ObjectMapper mapper = new ObjectMapper();

        waitForStart();

        File file = FilesystemUtil.loadFile(FILENAME + ".json");

        Vector2d[] pointArray = null;
        if (file.isFile()) {
            try {
                pointArray = mapper.readValue(file, Vector2d[].class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        assert pointArray != null;

        File newFile = FilesystemUtil.loadFile(FILENAME + ".bak.json");

        try {
            mapper.writeValue(newFile, pointArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
