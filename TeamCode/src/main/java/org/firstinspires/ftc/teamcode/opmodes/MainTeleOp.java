package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import dev.frozenmilk.mercurial.Mercurial;

@Mercurial.Attach
@TeleOp
public class MainTeleOp extends OpMode {

    public enum State {
        INITIAL
    }
    public static State state = State.INITIAL;

    @Override
    public void init() {
        Mercurial.gamepad1()
    }

    @Override
    public void loop() {

    }
}
