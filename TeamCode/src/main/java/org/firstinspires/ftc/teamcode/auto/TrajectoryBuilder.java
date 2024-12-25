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
    private final List<TimeMarker> timeMarkers = new ArrayList<>();

    public TrajectoryBuilder(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        points = objectMapper.readValue(file, Vector2d[].class);
    }

    public TrajectoryBuilder addSpatialMarker(double t, MarkerRunnable runnable) {
        spatialMarkers.add(new SpatialMarker(t, runnable));
        return this;
    }

    public TrajectoryBuilder addTurnMarker(int curve, VariableProducer producer) {
        if (!turnMarkers.isEmpty() && turnMarkers.get(turnMarkers.size() - 1).curve == curve) {
            throw new RuntimeException(
                    "Can't add move than one turn marker to the curve"
            );
        }
        turnMarkers.add(new TurnMarker(curve, producer));
        return this;
    }

    public TrajectoryBuilder addTurnMarker(int curve, double angle) {
        return addTurnMarker(curve, (t) -> angle);
    }

    public TrajectoryBuilder waitForAt(double t, int milliseconds, double angle) {
        if (t > (double) (points.length - 1) / 3) {
            throw new IndexOutOfBoundsException(
                    "Invalid index " + t + " (max is " + (points.length - 1) % 3 + ")"
            );
        }
        stops.add(new StopPoint(t, milliseconds, angle));
        return this;
    }

    public TrajectoryBuilder waitForAt(double t, int milliseconds) {
        return waitForAt(t, milliseconds, 0);
    }

    public TrajectoryBuilder addTimeMarker(int millisecond, MarkerRunnable runnable) {
        if (stops.isEmpty()) {
            throw new RuntimeException(
                    "No stop markers to attach time marker"
            );
        }
        if (millisecond > stops.get(stops.size() - 1).milliseconds) {
            throw new IndexOutOfBoundsException(
                    "Invalid time of " + millisecond + " (max is " + stops.get(stops.size() - 1).milliseconds +  ")"
            );
        }
        timeMarkers.add(new TimeMarker(stops.size() - 1, millisecond, runnable));
        return this;
    }

    public Trajectory build() {
        return new Trajectory(points, stops, spatialMarkers, turnMarkers, timeMarkers);
    }
}
