package org.firstinspires.ftc.teamcode.Modules;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Camera.SampleDetectionCameraClass;
import org.firstinspires.ftc.teamcode.Camera.SampleDetectionPipeline;
import org.firstinspires.ftc.teamcode.Camera.SampleDetectionPipelinePNP;
import org.firstinspires.ftc.teamcode.math.ClawCalculation;

public class Modules {
    LinearOpMode linearOpMode;
    SampleDetectionCameraClass cam;
    SampleDetectionPipeline pip;
    Claw claw;
    Diff diff;
    Extend extend;
    Hand hand;
    Perkid perkid;
    Gamepad gamepad1, gamepad2;
    CRServo serv;
    boolean smartButton;
    ScoringPerkid scorPer;
    ScoringDiff diffScor;
    ScorClaw clawScor;
    double scorDiffStoper = 70;

    enum Mode {
        VISION_CONTROL,
        OPERATOR_CONTROL
    }

    Mode modules = Mode.OPERATOR_CONTROL;

    public Modules(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;
        cam = new SampleDetectionCameraClass(linearOpMode);
        //claw = new Claw(linearOpMode);
        //diff = new Diff(linearOpMode);
        extend = new Extend(linearOpMode);
        diffScor = new ScoringDiff(linearOpMode);
        hand = new Hand(linearOpMode);
        perkid = new Perkid(linearOpMode);
        pip = new SampleDetectionPipeline();
        clawScor = new ScorClaw(linearOpMode);
        scorPer = new ScoringPerkid(linearOpMode);
        gamepad1 = linearOpMode.gamepad1;
        gamepad2 = linearOpMode.gamepad2;
    }

    public void teleop() {
        //   claw.teleop();
        // diff.teleop(Math.round(cam.Angle));
        extend.teleop();
        hand.teleop();
        perkid.teleop();
        clawScor.teleop();
        scorPer.teleop();
        diffScor.teleop();


        if (gamepad1.x) {
            modules = Mode.VISION_CONTROL;


        }
        if (gamepad1.a) {
            modules = Mode.OPERATOR_CONTROL;
        }


        switch (modules) {

            case VISION_CONTROL:
                cam.visionInUse();

                if (gamepad1.dpad_left) {
                    diff.currentMode = Diff.Mode.VISION_HUYNYA;
                }

                break;

            case OPERATOR_CONTROL:
                cam.operatorControl();
                extend.currentMode = Extend.Mode.DRIVER_CONTROL;
                hand.currentMode = Hand.Mode.DRIVER_CONTROL;

                if (gamepad2.dpad_up) {
                    perkid.currentMode = Perkid.Mode.SCORE_POSITION;
                }
                if (gamepad2.dpad_down) {
                    perkid.currentMode = Perkid.Mode.PICK_POSITION;
                }
                if (gamepad2.dpad_left) {
                    perkid.currentMode = Perkid.Mode.AIM_POSITION;
                }
                if(gamepad2.a){
                   diffScor.currentMode = ScoringDiff.Mode.PICK_POSITION;
                   clawScor.currentMode = ScorClaw.Mode.DROP;
                    scorPer.currentMode = ScoringPerkid.Mode.PICK;
                }
                if(gamepad2.y){
                    diffScor.currentMode = ScoringDiff.Mode.SCORE_SAMP;
                    clawScor.currentMode = ScorClaw.Mode.DROP;
                    scorPer.currentMode = ScoringPerkid.Mode.SAMPLE;
                }
                if(gamepad2.b){
                    clawScor.currentMode = ScorClaw.Mode.DROP;
                    scorPer.currentMode = ScoringPerkid.Mode.PICK_SPECIMEN;
                    diffScor.currentMode = ScoringDiff.Mode.PICK_SPEC;
                }
                if(gamepad2.x){
                    scorDiffStoper = 0;
                    scorPer.currentMode = ScoringPerkid.Mode.SPECIMEN;
                    clawScor.currentMode = ScorClaw.Mode.DROP;
                    diffScor.currentMode = ScoringDiff.Mode.SCORE_SPEC;

                }
               /* if (scorDiffStoper < 10){
                    scorDiffStoper+=1;
                }
                else {
                    diffScor.currentMode = ScoringDiff.Mode.SCORE_SPEC;
                }*/

                break;
        }
        linearOpMode.telemetry.addData("handPos", hand.hand.getPosition());

        linearOpMode.telemetry.addData("xActual", cam.xActual);
        linearOpMode.telemetry.addData("yActual", cam.yActual);
        linearOpMode.telemetry.addData("x", cam.x);
        linearOpMode.telemetry.addData("y", cam.y);
        linearOpMode.telemetry.addData("Distance", cam.distance);


        linearOpMode.telemetry.update();
        resetTimers();
    }

    public void resetTimers() {
        hand.resetTimer();
    }
}
