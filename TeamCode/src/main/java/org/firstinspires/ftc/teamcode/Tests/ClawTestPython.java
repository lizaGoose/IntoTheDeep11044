package org.firstinspires.ftc.teamcode.Tests;


import com.chaquo.python.Kwarg;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.file.FilesystemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@TeleOp
public class ClawTestPython extends LinearOpMode {
    Python pyth;
    public double x,y,z;

    @Override
    public void runOpMode() throws InterruptedException {

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(AppUtil.getDefContext()));
        }

        pyth = Python.getInstance();
        final PyObject file = pyth.getModule("inverseKinematics");

        PyObject arm = file.callAttr("Arm", FilesystemUtil.loadFile("arm.urdf").getAbsolutePath());

        waitForStart();

        while (opModeIsActive()) {
            double[] target_position = {x, y, z};
            double[] target_orientation = {0, 0, 0};

           // if(gamepad1.y) x+=1;
            //if(gamepad1.a)x-=1;
            x+=1;
            y+=2;
            z+=5;

            List<PyObject> res = arm.callAttr("inverse_kinematics",
                    target_position,
                    target_orientation,
                    new Kwarg("orientation_mode", "all")
            ).asList();

            telemetry.addData("verticalHandAngle", res.get(2));
            telemetry.addData("horizontalHandAngle", res.get(3));
            telemetry.addData("verticalClawAngle", res.get(4));
            telemetry.addData("x", target_position[0]);
            telemetry.addData("y", target_position[1]);
            telemetry.addData("z", target_position[2]);
            telemetry.update();
        }
    }
}
