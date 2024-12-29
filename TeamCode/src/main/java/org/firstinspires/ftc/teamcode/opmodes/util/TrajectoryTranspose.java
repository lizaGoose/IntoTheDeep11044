package org.firstinspires.ftc.teamcode.opmodes.util;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.file.FilesystemUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Config
@TeleOp
public class TrajectoryTranspose extends LinearOpMode {
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

        Vector2d[] transposedPoints = new Vector2d[pointArray.length];

        for (int i = 0; i < pointArray.length; ++i) {
            Vector2d point = pointArray[i];
            Vector2d transposedPoint = new Vector2d(point.getY(), -point.getX());
            transposedPoints[i] = transposedPoint;
        }

        File newFile = FilesystemUtil.loadFile(FILENAME + "transposed.json");

        try {
            mapper.writeValue(newFile, transposedPoints);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
