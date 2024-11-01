package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.python.core.PyObject;
import org.python.jsr223.PyScriptEngine;
import org.python.util.PythonInterpreter;

@TeleOp
public class ClawTest extends LinearOpMode {
    Servo claw;
    PythonInterpreter pyth;
    PyObject verticalHandAngle, horizontalHandAngle, verticalClawAngle, x, y, z;

    @Override
    public void runOpMode() throws InterruptedException {
        pyth = new PythonInterpreter();
        pyth.execfile("Untitled.ipynb");

        //right = hardwareMap.get(CRServoImplEx.class, "rightDif");

        waitForStart();

        while (opModeIsActive()){
            verticalHandAngle = pyth.get("verticalHandAngle");
            horizontalHandAngle = pyth.get("horizontalHandAngle");
            verticalClawAngle = pyth.get("verticalClawAngle");

            pyth.set("x", 5);
            pyth.set("y", 10.9058);
            pyth.set("z", 5.43);

            x = pyth.get("x");
            y = pyth.get("y");
            z = pyth.get("z");
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
