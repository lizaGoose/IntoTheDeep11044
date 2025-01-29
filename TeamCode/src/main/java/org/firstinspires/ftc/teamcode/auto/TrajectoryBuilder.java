package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TrajectoryBuilder {
    private List<Vector2d> points;
    private final List<StopPoint> stops = new ArrayList<>();
    private final List<SpatialMarker> spatialMarkers = new ArrayList<>();
    private final List<TurnMarker> turnMarkers = new ArrayList<>();
    private final List<TimeMarker> timeMarkers = new ArrayList<>();

    public TrajectoryBuilder(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Vector2d[] pointsArray = objectMapper.readValue(file, Vector2d[].class);
        points = Arrays.asList(pointsArray);
    }

    public TrajectoryBuilder reverse() {
        List<Vector2d> newPoints = new ArrayList<>();
        for (int i = points.size(); i >= 0; --i) {
            newPoints.add(points.get(0));
        }
        points = newPoints;
        return this;
    }

    public TrajectoryBuilder plusPoint(int index, Vector2d point) throws IndexOutOfBoundsException {
        Vector2d newPoint = points.get(index).plus(point);
        points.set(index, newPoint);
        return this;
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
        return addTurnMarker(curve, (t) -> Math.toRadians(angle));
    }

    public TrajectoryBuilder waitForAt(double t, double milliseconds, double angle) {
        if (t > (double) ( points.size() - 1 ) / 3) {
            throw new IndexOutOfBoundsException(
                    "Invalid index " + t + " (max is " + ( points.size() - 1 ) % 3 + ")"
            );
        }
        stops.add(new StopPoint(t, milliseconds, Math.toRadians(angle)));
        return this;
    }

    public TrajectoryBuilder waitForAt(double t, double milliseconds) {
        return waitForAt(t, milliseconds, 0);
    }

    public TrajectoryBuilder addTimeMarker(double millisecond, MarkerRunnable runnable) {
        if (stops.isEmpty()) {
            throw new RuntimeException(
                    "No stop markers to attach time marker"
            );
        }
        if (millisecond > stops.get(stops.size() - 1).milliseconds) {
            throw new IndexOutOfBoundsException(
                    "Invalid time of " + millisecond + " (max is " + stops.get(stops.size() - 1).milliseconds + ")"
            );
        }
        timeMarkers.add(new TimeMarker(stops.size() - 1, millisecond, runnable));
        return this;
    }

    public TrajectoryBuilder addTimeMarker(double millisecond, boolean relative, MarkerRunnable runnable) {
        if (!relative) {
            return addTimeMarker(millisecond, runnable);
        }
        if (stops.isEmpty()) {
            throw new RuntimeException(
                    "No stop markers to attach time marker"
            );
        }
        if (millisecond > stops.get(stops.size() - 1).milliseconds) {
            throw new IndexOutOfBoundsException(
                    "Invalid time of " + millisecond + " (max is " + stops.get(stops.size() - 1).milliseconds + ")"
            );
        }
        if (timeMarkers.isEmpty()) {
            timeMarkers.add(new TimeMarker(stops.size() - 1, millisecond, runnable));
            return this;
        } else if (timeMarkers.get(timeMarkers.size() - 1).waitIndex < stops.size() - 1) {
            timeMarkers.add(new TimeMarker(stops.size() - 1, millisecond, runnable));
            return this;
        }
        timeMarkers.add(new TimeMarker(stops.size() - 1, millisecond + timeMarkers.get(timeMarkers.size() - 1).milliseconds, runnable));
        return this;
    }

    public Trajectory build() {

        spatialMarkers.sort(Comparator.comparingDouble(spatialMarker -> spatialMarker.t));
        return new Trajectory(points.toArray(new Vector2d[0]), stops, spatialMarkers.stream().map(SpatialMarker::clone).collect(Collectors.toList()), turnMarkers, timeMarkers);
    }
}
