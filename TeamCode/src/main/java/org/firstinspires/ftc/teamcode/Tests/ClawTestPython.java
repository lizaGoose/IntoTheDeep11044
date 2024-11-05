package org.firstinspires.ftc.teamcode.Tests;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.file.FilesystemUtil;

import java.util.List;
import java.util.Objects;


@TeleOp
public class ClawTestPython extends LinearOpMode {
    // Servo claw;
    Python py = Python.getInstance();

    @Override
    public void runOpMode() throws InterruptedException {

        PyObject ikpy = py.getModule("ikpy.chain");
        boolean[] mask = {false, true, true, true, true};
        PyObject arm = Objects.requireNonNull(ikpy.get("Chain"))
                .call(
                        "from_urdf_file",
                        FilesystemUtil.loadFile("arm.urdf").getAbsolutePath(),
                        mask
                );

        waitForStart();

        while (opModeIsActive()) {
            double[] target_position = {0, 0, 0};
            double[] target_orientation = {0, 0, 0};
            List<PyObject> res = arm.callAttr("inverse_kinematics", target_position, target_orientation, "all").asList();

            telemetry.addData("verticalHandAngle", res.get(1));
            telemetry.addData("horizontalHandAngle", res.get(2));
            telemetry.addData("verticalClawAngle", res.get(3));
            telemetry.addData("x", target_position[0]);
            telemetry.addData("y", target_position[1]);
            telemetry.addData("z", target_position[2]);
            telemetry.update();
        }
    }
}
