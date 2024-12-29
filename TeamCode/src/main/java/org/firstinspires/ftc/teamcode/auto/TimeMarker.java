package org.firstinspires.ftc.teamcode.auto;

import androidx.annotation.NonNull;

public class TimeMarker {
    public int waitIndex;
    public double milliseconds;
    private MarkerRunnable runnable;

    TimeMarker(int waitIndex, double milliseconds, MarkerRunnable runnable) {
        this.waitIndex = waitIndex;
        this.milliseconds = milliseconds;
        this.setRunnable(runnable);
    }

    public MarkerRunnable getRunnable() {
        return runnable;
    }

    public void setRunnable(MarkerRunnable runnable) {
        this.runnable = runnable;
    }

    @NonNull
    @Override
    public TimeMarker clone() {
        return new TimeMarker(this.waitIndex, this.milliseconds, this.runnable);
    }
}
