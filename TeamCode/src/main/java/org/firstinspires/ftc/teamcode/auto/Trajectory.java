package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.math.BezierCurve;

import java.util.List;

public class Trajectory {
    private final Vector2d[] points;
    private final List<StopPoint> stopPoints;
    private final List<SpatialMarker> markers;
    private final List<TurnMarker> turns;

    Trajectory(Vector2d[] points, List<StopPoint> stopPoints, List<SpatialMarker> markers, List<TurnMarker> turns) {
        this.points = points;
        this.stopPoints = stopPoints;
        this.markers = markers;
        this.turns = turns;
    }

    public BezierCurve get(int i) {
        return new BezierCurve(
                points[i * 3],
                points[i * 3 + 1],
                points[i * 3 + 2],
                points[i * 3 + 3]
        );
    }

    public int size() {
        return (points.length - 1) / 3;
    }

    public List<SpatialMarker> getMarkers() {
        return markers;
    }

    public List<StopPoint> getStopPoints() {
        return stopPoints;
    }

    public List<TurnMarker> getTurns() {
        return turns;
    }
}
