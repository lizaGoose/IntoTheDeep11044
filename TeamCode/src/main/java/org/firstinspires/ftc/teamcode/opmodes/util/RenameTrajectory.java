package org.firstinspires.ftc.teamcode.opmodes.util;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.file.FilesystemUtil;

import java.io.File;
import java.io.IOException;

@Config
@TeleOp(group = "util")
public class RenameTrajectory extends LinearOpMode {
    public static String OLD_FILE_NAME = "";
    public static String NEW_FILE_NAME = "";

    @Override
    public void runOpMode() throws InterruptedException {
        ObjectMapper mapper = new ObjectMapper();

        waitForStart();

        File file = FilesystemUtil.loadFile(OLD_FILE_NAME);


        Vector2d[] pointArray = null;
        if (file.isFile()) {
            try {
                pointArray = mapper.readValue(file, Vector2d[].class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        assert pointArray != null;

        File newFile = FilesystemUtil.loadFile(NEW_FILE_NAME);

        try {
            mapper.writeValue(newFile, pointArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
