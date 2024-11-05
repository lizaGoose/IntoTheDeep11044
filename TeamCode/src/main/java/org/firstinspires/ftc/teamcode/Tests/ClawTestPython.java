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

    @Override
    public void runOpMode() throws InterruptedException {

        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(AppUtil.getDefContext()));
        }

        pyth = Python.getInstance();
        final PyObject ik = pyth.getModule("ikpy.chain");
        PyObject mask = pyth.getModule("array").get("array");
        PyObject arm = Objects.requireNonNull(ik.get("Chain"))
                .call(
                        "from_urdf_file",
                        FilesystemUtil.loadFile("arm.urdf").getAbsolutePath(),
                        new Kwarg("active_links_mask", (Object) mask)
                );

        waitForStart();

        while (opModeIsActive()) {
            double[] target_position = {0, 0, 0};
            double[] target_orientation = {0, 0, 0};

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
