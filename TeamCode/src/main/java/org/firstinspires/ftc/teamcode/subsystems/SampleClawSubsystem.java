package org.firstinspires.ftc.teamcode.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.util.OpModeLazyCell;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.mercurial.subsystems.SDKSubsystem;
import dev.frozenmilk.mercurial.subsystems.Subsystem;
import dev.frozenmilk.mercurial.subsystems.SubsystemObjectCell;
import dev.frozenmilk.util.cell.Cell;
import kotlin.annotation.MustBeDocumented;

@Config
public class SampleClawSubsystem extends SDKSubsystem {
    // Subsystem stuff
    public static final SampleClawSubsystem INSTANCE = new SampleClawSubsystem();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    public @interface Attach {
    }

    private Dependency<?> dependency =
            Subsystem.DEFAULT_DEPENDENCY.and(new SingleAnnotation<>(Attach.class));

    @NonNull
    @Override
    public Dependency<?> getDependency() {
        return dependency;
    }

    @Override
    public void setDependency(@NonNull Dependency<?> dependency) {
        this.dependency = dependency;
    }

    // State
    private static enum State {
        IDLE_POS,
        AIM_POS
    }
    private static State state = State.IDLE_POS;

    private enum ClawState {
        EMPTY,
        LOADED
    }
    private static ClawState clawState = ClawState.EMPTY;

    private final Cell<DcMotorEx> extendMotor = subsystemCell(() -> getHardwareMap().get(DcMotorEx.class, "extend_motor"));

    private final Cell<Servo> leftVerticalServo = subsystemCell(() -> getHardwareMap().get(Servo.class, "left_vertical_servo"));
    private final Cell<Servo> rightVerticalServo = subsystemCell(() -> getHardwareMap().get(Servo.class, "right_vertical_servo"));

    private final Cell<Servo> horizontalServo =subsystemCell(() -> getHardwareMap().get(Servo.class, "horizontal_servo"));

    private final Cell<Servo> leftDiffServo = subsystemCell(() -> getHardwareMap().get(Servo.class, "left_diff_servo"));
    private final Cell<Servo> rightDiffServo = subsystemCell(() -> getHardwareMap().get(Servo.class, "right_diff_servo"));


    @Override
    public void preUserInitHook(@NonNull Wrapper opMode) {
        state = State.IDLE_POS;
        clawState = ClawState.EMPTY;
    }
}
