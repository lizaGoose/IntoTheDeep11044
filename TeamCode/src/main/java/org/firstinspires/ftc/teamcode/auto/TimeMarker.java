package org.firstinspires.ftc.teamcode.auto;

public class TimeMarker {
    public int waitIndex;
    public int milliseconds;
    private MarkerRunnable runnable;

    TimeMarker(int waitIndex, int milliseconds, MarkerRunnable runnable) {
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
}
