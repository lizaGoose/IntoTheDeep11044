package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Hand;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Perkid;
import org.firstinspires.ftc.teamcode.camera.AutoCamera;
import org.firstinspires.ftc.teamcode.modules.Linkage;
import org.firstinspires.ftc.teamcode.modules.ScorerPerkid;

@Config
@TeleOp
public class TestKinematics extends LinearOpMode {
 //   public KinematicsCameraVision kinematics;
    public boolean isPressed;
    public Linkage linkage;
    public Hand hand;
    public AutoCamera cam;
    public ScorerPerkid scorerPerkid;

    @Override
    public void runOpMode() throws InterruptedException {
       // kinematics = new KinematicsCameraVision(this);
        cam = new AutoCamera(this);
        Perkid perkid = new Perkid(this);
        linkage = new Linkage(this);
        hand = new Hand(this);
        Claw claw = new Claw(this);
        Lift lift = new Lift(this);
        scorerPerkid = new ScorerPerkid(this);
        //cam.operatorControlRed();

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.triangle){
                cam.operatorControlRed();
                linkage.setReference(cam.kinematics(lift.getPosition(), scorerPerkid.getPos()).getY());
            }
           // extend.motor.setPower(gamepad1.left_stick_y);
            scorerPerkid.scoreSpecimen();
            linkage.PIDControl();
          //  cam.operatorControlRed();
            lift.setReference(810*gamepad1.right_stick_y);
            lift.PIDControl();
            //kinematics.perkid2.scoreSpecimen();

            telemetry.addData("xSample", cam.kinematics(lift.getPosition(), scorerPerkid.getPos()).getX());
            telemetry.addData("ySample", cam.kinematics(lift.getPosition(), scorerPerkid.getPos()).getY());
            telemetry.addData("zSample", cam.kinematics(lift.getPosition(), scorerPerkid.getPos()).getZ());
            telemetry.addData("a", cam.a);
            telemetry.addData("h", lift.getPosition() *cam.ticksPerInchLift);
            telemetry.addData("ExtendPos", linkage.motor.getCurrentPosition());
            telemetry.addData("Xp", cam.P.getX());
            telemetry.addData("Yp", cam.P.getY());
            telemetry.addData("Zp", cam.P.getZ());
            telemetry.addData("x", cam.pip.xSample);
            telemetry.addData("y", cam.pip.ySample);
            telemetry.update();
        }
    }
}
