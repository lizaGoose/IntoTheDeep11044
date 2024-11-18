package org.firstinspires.ftc.teamcode.Tests;



import android.content.Context;
import android.content.res.Configuration;
import android.view.ViewConfiguration;

import com.acmerobotics.dashboard.config.Config;
import com.chaquo.python.PyException;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.sun.tools.javac.api.DiagnosticFormatter;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;


@TeleOp
public class ClawTest extends LinearOpMode {
    Servo claw;
    Python pyth;
    PyObject verticalHandAngle, horizontalHandAngle, verticalClawAngle, x, y, z;

    @Override
    public void runOpMode() throws InterruptedException {

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(AppUtil.getDefContext()));
        }
      //  pyth.execfile("Untitled.ipynb");

        //right = hardwareMap.get(CRServoImplEx.class, "rightDif");
        pyth = Python.getInstance();
        final PyObject ik = pyth.getModule("inverseKinematics");

        waitForStart();

        while (opModeIsActive()){
      /*      verticalHandAngle = pyth.get("verticalHandAngle");
            horizontalHandAngle = pyth.get("horizontalHandAngle");
            verticalClawAngle = pyth.get("verticalClawAngle");

            pyth.set("x", 5);
            pyth.set("y", 10.9058);
            pyth.set("z", 5.43);

            x = pyth.get("x");
            y = pyth.get("y");
            z = pyth.get("z");*/

            verticalHandAngle = ik.get("verticalHandAngle");
            horizontalHandAngle = ik.get("horizontalHandAngle");
            verticalClawAngle = ik.get("verticalClawAngle");
            x = ik.get("x");
            y = ik.get("y");
            z = ik.get("z");

            telemetry.addData("verticalHandAngle", verticalHandAngle);
            telemetry.addData("horizontalHandAngle", horizontalHandAngle);
            telemetry.addData("verticalClawAngle", verticalClawAngle);
            telemetry.addData("x", x);
            telemetry.addData("y", y);
            telemetry.addData("z", z);
            telemetry.update();
        }
    }
}
