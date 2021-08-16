package gui.components;

import javax.swing.*;
import java.util.Random;

import app.App;
import backend.data.MapData;

/**
 * Signal refers to individual SignalMap signals.
 *
 * Each signal has size (max radius) and spawn location
 * based on its region and region value.
 *
 * @see SignalMap
 */
public class Signal {
    // parentMap: parent map of the signal
    private final SignalMap parentMap;
    // timer: signal timer regulating radius change
    private Timer timer;

    // radius: current signal radius (initial: 0)
    private double radius;
    // maxRadius: final radius size of the signal
    private int maxRadius;
    // x: x axis coordinate
    private int x;
    // y: y axis coordinate
    private int y;

    /**
     * Constructs Signal
     * @param parentMap parent map of the signal
     */
    public Signal(SignalMap parentMap) {
        this.parentMap = parentMap;
        // assigning signal values
        assignSignal();

        if (maxRadius > 0) {
            timer = new Timer(10, e -> {
                if (radius > maxRadius) {
                    // when maximum radius is reached signal is removed, timer stops
                    SignalMap.mapSignals.remove(this);
                    timer.stop();
                }
                // every 0.002 second radius is increased
                else { increaseRadius(); }
            });
            timer.start();
            SignalMap.mapSignals.add(this);
        }
    }

    /**
     * assigns signal values
     */
    public void assignSignal() {
        double mapRegionPercentage = 0;
        int callIndex = 0;
        // random map region is selected and assigned to the signal
        while (mapRegionPercentage == 0) {
            callIndex = new Random().nextInt(SignalMap.mapRegionValues.length);
            mapRegionPercentage = SignalMap.mapRegionValues[callIndex] / (SignalMap.mapRegionValuesSum * 1.0);
        }

        this.x = MapData.getMapRegionCoordinates(parentMap.getWidth(), parentMap.getHeight())[callIndex][0];
        this.y = MapData.getMapRegionCoordinates(parentMap.getWidth(), parentMap.getHeight())[callIndex][1];
        this.radius = 0;
        // maxRadius is based on selected map region value percentage (proportion to other values)
        this.maxRadius = (int) (200 * mapRegionPercentage);
    }

    /**
     * increases signal radius based on current parentMap dimensions
     */
    public void increaseRadius() {
        //System.out.println((parentMap.getWidth() + parentMap.getHeight()) / 1500.0);
        radius += (parentMap.getWidth() + parentMap.getHeight()) / 1500.0;
    }

    // getter methods
    public double getRadius() { return radius; }
    public int getX() { return x; }
    public int getY() { return y; }
}

