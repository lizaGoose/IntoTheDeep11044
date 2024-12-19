package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrajectoryBuilder {
    private final Vector2d[] points;
    private final List<StopPoint> stops = new ArrayList<>();
    private final List<SpatialMarker> spatialMarkers = new ArrayList<>();
    private final List<TurnMarker> turnMarkers = new ArrayList<>();

    public TrajectoryBuilder(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        points = objectMapper.readValue(file, Vector2d[].class);
    }

    public TrajectoryBuilder addSpatialMarker(double t, MarkerRunnable runnable) {
        spatialMarkers.add(new SpatialMarker(t, runnable));
        return this;
    }

    public TrajectoryBuilder addTurnMarker(int curve, VariableProducer producer) {
        turnMarkers.add(new TurnMarker(curve, producer));
        return this;
    }

    public TrajectoryBuilder addTurnMarker(int curve, double angle) {
        turnMarkers.add(new TurnMarker(curve, (t) -> angle));
        return this;
    }

    public TrajectoryBuilder waitForAt(double t, int milliseconds) {
        if (t > (double) (points.length - 1) / 3) {
            throw new IndexOutOfBoundsException(
                    "Invalid index " + t + " (max is " + (points.length - 1) % 3 + ")"
            );
        }
        stops.add(new StopPoint(t, milliseconds));
        return this;
    }

    public Trajectory build() {
        return new Trajectory(points, stops, spatialMarkers, turnMarkers);
    }
}
