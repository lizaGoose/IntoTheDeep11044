package org.firstinspires.ftc.teamcode.opmodes.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.math.KinematicsCameraVision;
import org.firstinspires.ftc.teamcode.modules.Claw;
import org.firstinspires.ftc.teamcode.modules.Hand;
import org.firstinspires.ftc.teamcode.modules.Lift;
import org.firstinspires.ftc.teamcode.modules.Perkid;
import org.firstinspires.ftc.teamcode.camera.AutoCamera;
import org.firstinspires.ftc.teamcode.modules.Extend;
import org.firstinspires.ftc.teamcode.modules.ScorerPerkid;

@Config
@TeleOp
public class TestKinematics extends LinearOpMode {
    public KinematicsCameraVision kinematics;
    public boolean isPressed;
    public Extend extend;
    public Hand hand;
  //  public AutoCamera cam;

    @Override
    public void runOpMode() throws InterruptedException {
        kinematics = new KinematicsCameraVision(this);
        //cam = new AutoCamera(this);
        Perkid perkid = new Perkid(this);
        extend = new Extend(this);
        hand = new Hand(this);
        Claw claw = new Claw(this);
        //cam.operatorControlRed();

        waitForStart();

        while (opModeIsActive()) {
           // cam.operatorControlRed();

            if (gamepad1.triangle) {
                if (!isPressed) {
                   kinematics.pickFromSub();
                }
                isPressed = true;
            } else {
                isPressed = false;
            }
            if (gamepad1.square) {
                perkid.pickUp();
            } else {
                perkid.look();
            }
           /* if (gamepad1.cross){
                claw.close();
            }
            else {
                claw.open();
            }*/
            extend.setReference(kinematics.pos);
            extend.PIDControl();
            hand.setPosition(kinematics.posHand);
           // telemetry.update();
            telemetry.addData("xSample", kinematics.cam.pip.CenterStone);//70 285
            telemetry.update();
        }
    }
}
