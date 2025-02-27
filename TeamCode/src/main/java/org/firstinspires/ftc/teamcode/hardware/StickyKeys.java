package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.lang.reflect.Field;

public class StickyKeys {
    public boolean dpad_up, dpad_down, dpad_left, dpad_right;
    public boolean triangle, circle, square, cross;
    public boolean left_bumper, right_bumper;
    public boolean left_stick_button, right_stick_button;
    public boolean back, ps;

    private boolean dpad_up_down, dpad_down_down, dpad_left_down, dpad_right_down;
    private boolean triangle_down, circle_down, square_down, cross_down;
    private boolean left_bumper_down, right_bumper_down;
    private boolean left_stick_button_down, right_stick_button_down;
    private boolean back_down, ps_down;

    public void update(Gamepad gamepad) {
        dpad_up = gamepad.dpad_up && !dpad_up_down;
        dpad_up_down = gamepad.dpad_up;
        dpad_left = gamepad.dpad_left && !dpad_left_down;
        dpad_left_down = gamepad.dpad_left;
        dpad_down = gamepad.dpad_down && !dpad_down_down;
        dpad_down_down = gamepad.dpad_down;
        dpad_right = gamepad.dpad_right && !dpad_right_down;
        dpad_right_down = gamepad.dpad_right;
        triangle = gamepad.triangle && !triangle_down;
        triangle_down = gamepad.triangle;
        circle = gamepad.circle && !circle_down;
        circle_down = gamepad.circle;
        square = gamepad.square && !square_down;
        square_down = gamepad.square;
        cross = gamepad.cross && !cross_down;
        cross_down = gamepad.cross;
        left_bumper = gamepad.left_bumper && !left_bumper_down;
        left_bumper_down = gamepad.left_bumper;
        right_bumper = gamepad.right_bumper && !right_bumper_down;
        right_bumper_down = gamepad.right_bumper;
        left_stick_button = gamepad.left_stick_button && !left_stick_button_down;
        left_stick_button_down = gamepad.left_stick_button;
        right_stick_button = gamepad.right_stick_button && !right_stick_button_down;
        right_stick_button_down = gamepad.right_stick_button;
        back = gamepad.back && !back_down;
        back_down = gamepad.back;
        ps = gamepad.ps && !ps_down;
        ps_down = gamepad.ps;
    }

    public void reset() throws IllegalAccessException {
       for (Field field: StickyKeys.class.getFields()) {
           field.setBoolean(this.getClass(), false);
       }
    }
}