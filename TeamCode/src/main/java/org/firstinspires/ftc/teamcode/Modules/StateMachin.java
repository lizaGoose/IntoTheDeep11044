package org.firstinspires.ftc.teamcode.Modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Camera.SampleDetectionCameraClass;

@Config
public class StateMachin {
    LinearOpMode linearOpMode;
    extendError extend;
    Claw claw;
    Perkid perkid;
    ScoringPerkid scorPerkid;
    ScoringDiff scorDiff;
    Hand hand;
    ScorClaw scorClaw;
    Gamepad gamepad1, gamepad2;
    Differential diff;
   // SampleDetectionCameraClass cam;
    ElapsedTime pickedUnpickedTimer, fromClawToClawTimer, dropSpecimenTimer, goToInitialFromScoreSample, goFromScoreSpecTimer;
    public DistanceSensor distanceSensor, distInClaw;
    liftError lift;
    boolean sampleIsScored, startTimer, startTimerFromClawToClaw, startGoFromScorSpec;
    DriveTrain dt;
    public DigitalChannel frontSensor;
    public static double upToChamber = -380, upToChamber2= -430;
    public  double zeroPos = 0;
    SampleDetectionCameraClass cam;


    public enum State {
        INITIAL,
        LOOK_FOR_BLOCK,
        PICK_UP_THE_BLOCK,
        FROM_CLAW_TO_CLAW,
        SCORE_SAMPLE,
        DROP_SPECIMEN_TO_HUMAN_ZONE,
        TRANSFER_SPEC,
        SCORE_SPEC,
        PICK_SPEC
    }

    public State state = State.INITIAL;

    public StateMachin(LinearOpMode linearOpMode) {
        this.linearOpMode = linearOpMode;

        cam = new SampleDetectionCameraClass(linearOpMode);
        extend = new extendError(linearOpMode);
        claw = new Claw(linearOpMode);
        perkid = new Perkid(linearOpMode);
        scorPerkid = new ScoringPerkid(linearOpMode);
        scorDiff = new ScoringDiff(linearOpMode);
        hand = new Hand(linearOpMode);
        scorClaw = new ScorClaw(linearOpMode);
        diff = new Differential(linearOpMode);
        lift = new liftError(linearOpMode);
        dt = new DriveTrain(linearOpMode);
        //cam = new SampleDetectionCameraClass(linearOpMode);

        gamepad1 = linearOpMode.gamepad1;
        gamepad2 = linearOpMode.gamepad2;

        pickedUnpickedTimer = new ElapsedTime();
        fromClawToClawTimer = new ElapsedTime();
        dropSpecimenTimer = new ElapsedTime();
        goToInitialFromScoreSample = new ElapsedTime();

    }

    public void teleop() {
        distanceSensor = linearOpMode.hardwareMap.get(DistanceSensor.class, "Color");
        distInClaw = linearOpMode.hardwareMap.get(DistanceSensor.class, "ScorClawDist");
        frontSensor = linearOpMode.hardwareMap.get(DigitalChannel.class, "DistanceFront");
        cam.visionInUse();
        extend.teleop();
        claw.teleop();
        perkid.teleop();
        scorPerkid.teleop();
        scorDiff.teleop();
        hand.teleop();
        scorClaw.teleop();
        lift.teleop();
        diff.teleop(cam.Angle);
        dt.teleOp();
        dt.currentMode = DriveTrain.Mode.DRIVER_CONTROL;
        lift.currentMode = liftError.Mode.NULL_POSE;
        if (gamepad2.left_bumper){
            zeroPos = extend.motor.getCurrentPosition();
        }

        switch (state) {
            case INITIAL:
                sampleIsScored = false;
                startTimer = false;
                startTimerFromClawToClaw = false;
                startGoFromScorSpec = false;
                extend.currentMode = extendError.Mode.NULL_POSE;
                perkid.currentMode = Perkid.Mode.TRANSFER;
                claw.currentMode = Claw.Mode.DROP;
                scorPerkid.currentMode = ScoringPerkid.Mode.PICK_SPEC;
                scorDiff.currentMode = ScoringDiff.Mode.PICK_SPEC;
                hand.currentMode = Hand.Mode.FORWARD;
                scorClaw.currentMode = ScorClaw.Mode.DROP;
                diff.currentMode = Differential.Mode.TRANSFER;
                lift.currentMode = liftError.Mode.NULL_POSE;
                lift.pos = 0;


                if (gamepad2.square) {
                    state = State.LOOK_FOR_BLOCK;
                }
                if(gamepad2.cross){
                  state = State.TRANSFER_SPEC;
                }
                if(distInClaw.getDistance(DistanceUnit.MM) < 17){
                  state = State.PICK_SPEC;
                }
                break;
            case PICK_SPEC:
                scorClaw.currentMode = ScorClaw.Mode.PICK;
                lift.pos = -200;
                if(gamepad2.square){
                    state = State.TRANSFER_SPEC;
                }
                if(gamepad2.triangle){
                    state = State.INITIAL;
                }
                break;

            case TRANSFER_SPEC:
               // lift.pos = upToChamber;
                scorPerkid.currentMode = ScoringPerkid.Mode.TRANSFER_SPEC;
                scorClaw.currentMode = ScorClaw.Mode.PICK;
                perkid.currentMode = Perkid.Mode.LOOK;
                diff.currentMode = Differential.Mode.STRAIGHT_DOWN;
                scorDiff.currentMode = ScoringDiff.Mode.TRANSFER_SPEC;
                hand.currentMode = Hand.Mode.DRIVER_CONTROL;

                if(frontSensor.getState() == false){
               state = State.SCORE_SPEC;
                }
                if(gamepad2.triangle){
                    state = State.INITIAL;
                }
                break;
            case SCORE_SPEC:
                scorPerkid.currentMode = ScoringPerkid.Mode.SCOR_SPEC;
                scorDiff.currentMode = ScoringDiff.Mode.SCORE_SPEC;
                hand.currentMode = Hand.Mode.DRIVER_CONTROL;
                lift.pos = upToChamber2;

                if(gamepad2.square){
                    state = State.LOOK_FOR_BLOCK;
                }
                if(gamepad2.triangle){
                    state = State.INITIAL;
                }
                if (gamepad2.right_bumper){
                    scorClaw.currentMode = ScorClaw.Mode.DROP;
                  //  state = State.LOOK_FOR_BLOCK;
                }
                break;
            case LOOK_FOR_BLOCK:
                startGoFromScorSpec = false;
                perkid.currentMode = Perkid.Mode.LOOK;
                diff.currentMode = Differential.Mode.VISION_HUYNYA;
                //diff.currentMode = Differential.Mode.STRAIGHT_DOWN;
                scorPerkid.currentMode = ScoringPerkid.Mode.TRANSFER;
                scorClaw.currentMode = ScorClaw.Mode.DROP;
                scorDiff.currentMode = ScoringDiff.Mode.GET_FROM_CLAW;
                hand.currentMode = Hand.Mode.DRIVER_CONTROL;
                claw.currentMode = Claw.Mode.DROP;
               // lift.pos = 0;
                if(gamepad2.triangle){
                    state = State.INITIAL;
                }
                if (gamepad2.cross) {
                    state = State.PICK_UP_THE_BLOCK;
                    pickedUnpickedTimer.reset();
                }
                break;
            case PICK_UP_THE_BLOCK:
                perkid.currentMode = Perkid.Mode.PICK_UP;
                diff.currentMode = Differential.Mode.PICK_UP;
                scorDiff.currentMode = ScoringDiff.Mode.GET_FROM_CLAW;

                if (pickedUnpickedTimer.milliseconds() > 800 && distanceSensor.getDistance(DistanceUnit.MM) > 10.2) {
                    state = State.LOOK_FOR_BLOCK;
                } else if (pickedUnpickedTimer.milliseconds() > 800 && distanceSensor.getDistance(DistanceUnit.MM) <= 10.2) {

                    claw.currentMode = Claw.Mode.PICK;
                    perkid.currentMode = Perkid.Mode.LOOK;
                    if (Math.abs(gamepad1.left_stick_y) >= 0.1 && startTimerFromClawToClaw == false) {

                        fromClawToClawTimer.reset();
                        startTimerFromClawToClaw = true;
                    }

                    }
                if(startTimerFromClawToClaw == true && fromClawToClawTimer.milliseconds() >= 300){
                    state = State.FROM_CLAW_TO_CLAW;
                }
              /*  if(fromClawToClawTimer.milliseconds() <= 500){
                    claw.currentMode = Claw.Mode.PICK;
                    perkid.currentMode = Perkid.Mode.LOOK;
                }
                   /* if (Math.abs(gamepad1.left_stick_y) >= 0.1 && startTimerFromClawToClaw == false) {

                        fromClawToClawTimer.reset();
                        startTimerFromClawToClaw = true;
                    }

                    if (startTimerFromClawToClaw == true) {

                        if (fromClawToClawTimer.milliseconds() > 900 && fromClawToClawTimer.milliseconds() <= 1400) {



                        } else if (fromClawToClawTimer.milliseconds() > 1400) {
                            extend.pos = 0;
                            perkid.currentMode = Perkid.Mode.TRANSFER;

                            state = State.FROM_CLAW_TO_CLAW;
                        }
                    }
                    if (gamepad2.triangle) {
                        state = State.INITIAL;
                    }
                }*/
                break;
            case FROM_CLAW_TO_CLAW:
                if(fromClawToClawTimer.milliseconds()<=1300){
                    scorClaw.currentMode = ScorClaw.Mode.DROP;
                    hand.currentMode = Hand.Mode.FORWARD;
                    scorDiff.currentMode = ScoringDiff.Mode.GET_FROM_CLAW;
                    scorPerkid.currentMode = ScoringPerkid.Mode.TRANSFER;
                    claw.currentMode = Claw.Mode.PICK;
                    diff.currentMode = Differential.Mode.TRANSFER;
                    perkid.currentMode = Perkid.Mode.TRANSFER;

                }
                else if(fromClawToClawTimer.milliseconds()>1300 && fromClawToClawTimer.milliseconds()<=2000) {
                    extend.pos = zeroPos -100;

                }
                else {
                    scorClaw.currentMode = ScorClaw.Mode.PICK;
                    claw.currentMode = Claw.Mode.DROP;
                    extend.pos = zeroPos;
                    if(gamepad2.left_stick_y != 0){
                        extend.pos = extend.motor.getCurrentPosition();
                    }
                }
                if(gamepad2.square){
                    state = State.SCORE_SAMPLE;
                }
                if(gamepad2.circle){
                    dropSpecimenTimer.reset();
                    state = State.DROP_SPECIMEN_TO_HUMAN_ZONE;
                }
                if(gamepad2.triangle){
                    state = State.INITIAL;
                }
                break;
            case DROP_SPECIMEN_TO_HUMAN_ZONE:
                extend.currentMode = extendError.Mode.NULL_POSE;
                perkid.currentMode = Perkid.Mode.TRANSFER;
                claw.currentMode = Claw.Mode.DROP;
                scorPerkid.currentMode = ScoringPerkid.Mode.PICK_SPEC;
                scorDiff.currentMode = ScoringDiff.Mode.PICK_SPEC;
                hand.currentMode = Hand.Mode.FORWARD;
                diff.currentMode = Differential.Mode.TRANSFER;
                lift.currentMode = liftError.Mode.NULL_POSE;

                if (dropSpecimenTimer.milliseconds() <= 400){
                    scorClaw.currentMode = ScorClaw.Mode.PICK;
                }
                else{
                      scorClaw.currentMode = ScorClaw.Mode.DROP;
                }
                if(gamepad2.triangle){
                    state = State.INITIAL;
                }
                if(gamepad2.square){
                    pickedUnpickedTimer.reset();
                    sampleIsScored = false;
                    startTimer = false;
                    startTimerFromClawToClaw = false;
                    state = State.LOOK_FOR_BLOCK;
                }
               /* claw.currentMode = Claw.Mode.PICK;

                scorPerkid.currentMode = ScoringPerkid.Mode.PICK_SPEC;

                if(dropSpecimenTimer.milliseconds() > 400 && dropSpecimenTimer.milliseconds() <= 1200){
                    scorClaw.currentMode = ScorClaw.Mode.DROP;
                    diff.currentMode = Differential.Mode.STRAIGHT_DOWN;
                    perkid.currentMode = Perkid.Mode.LOOK;
                    hand.currentMode = Hand.Mode.FORWARD;

                }
                else if(dropSpecimenTimer.milliseconds() > 1200) {
                    claw.currentMode = Claw.Mode.DROP;
                    state = State.INITIAL;
                }*/

                break;
            case SCORE_SAMPLE:
                scorPerkid.currentMode = ScoringPerkid.Mode.SCORE_SAMP;
                scorDiff.currentMode = ScoringDiff.Mode.SCORE_SAMP;
               // lift.pos = 1330;
                if(gamepad2.right_bumper){
                    sampleIsScored = true;
                }
                if(gamepad2.triangle){
                    state = State.INITIAL;
                }
                if(gamepad2.circle){
                    pickedUnpickedTimer.reset();
                    sampleIsScored = false;
                    startTimer = false;
                    startTimerFromClawToClaw = false;
                    state = State.LOOK_FOR_BLOCK;
                }
                if (sampleIsScored == true){
                    scorClaw.currentMode = ScorClaw.Mode.DROP;
                    diff.rotation = 0;
                    if (Math.abs(gamepad1.left_stick_y) >= 0.4){
                        goToInitialFromScoreSample.reset();
                        startTimer = true;
                    }

                    if(goToInitialFromScoreSample.milliseconds() >= 550 && startTimer == true){
                        lift.pos = 0;
                        pickedUnpickedTimer.reset();
                        sampleIsScored = false;
                        startTimer = false;
                        startTimerFromClawToClaw = false;
                        state = State.LOOK_FOR_BLOCK;
                    }
                }
                break;

        }
        linearOpMode.telemetry.addData("distance", distInClaw.getDistance(DistanceUnit.MM));
        linearOpMode.telemetry.addData("state", state);
        linearOpMode.telemetry.addData("lift", lift.leftMotor.getCurrentPosition());
        linearOpMode.telemetry.addData("lift", lift.pos);
        linearOpMode.telemetry.addData("extend", extend.motor.getCurrentPosition());
        linearOpMode.telemetry.addData("StartTimer", startTimer);
        linearOpMode.telemetry.update();
    }
}
