package gui.components;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import backend.data.MapData;
import gui.Navigation;

/**
 * SignalMap is a world map used to display the regional
 * distribution of the local database with signals.
 *
 * Individual signals are called using a timer and drawn into
 * the map based on regional values obtained from MapData.
 *
 * @see Signal
 * @see MapData
 */
public class SignalMap extends JPanel {
    // startTimer: used as a delay to wait for full initialisation
    private final Timer startTimer;
    // signalTimer: used to initiate new signals
    private Timer signalTimer;
    // refreshTimer: used to refresh the map
    private Timer refreshTimer;

    // mapRegionValues: values of each region
    public static Integer[] mapRegionValues = MapData.getMapRegionValues();
    // mapRegionValuesSum: sum of all region values (used to calculate proportions)
    public static int mapRegionValuesSum = Arrays.stream(mapRegionValues).mapToInt(a -> a).sum();

    // mapSignals: ArrayList of all active signals on SignalMap
    public static ArrayList<Signal> mapSignals = new ArrayList<>();
    // signalColor: default signal color
    public static Color signalColor;

    /**
     * Constructs SignalMap
     */
    public SignalMap() {
        signalTimer = null;
        refreshTimer = null;
        this.setLayout(null);
        startTimer = new Timer(10, e -> {
            initSignals();
            stopStartTimer();
        });
        startTimer.start();
    }

    /**
     * stops the startTimer
     */
    public void stopStartTimer() {
        startTimer.stop();
    }

    /**
     * initiates signals, start timer and refresh timer are activated
     */
    public void initSignals() {
        // new signal every 0.1 second
        signalTimer = new Timer(500, e -> {
            new Signal(this);
        });
        // window refresh every 0.002 second
        refreshTimer = new Timer(10, e -> {
            Navigation.refreshPanel(this);
        });

        signalTimer.start();
        refreshTimer.start();
    }

    /**
     * stops the calling and drawing of signals
     */
    public void  stopSignals() {
        signalTimer.stop();
        refreshTimer.stop();
        mapSignals.clear();
    }

    /**
     * paints the map with background and available signals on each refresh
     * @param g graphics component
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(signalColor);

        // painting the background (map)
        Image scaledImage = MapData.background.getScaledInstance
                (getWidth(), getHeight(),Image.SCALE_SMOOTH);
        g.drawImage(scaledImage, 0, 0, this);

        // drawing and filling all signals
        for (Signal signal : mapSignals) {
            g.drawOval(signal.getX() - (int) (signal.getRadius() / 2),
                    signal.getY() - (int) (signal.getRadius() / 2),
                    (int) signal.getRadius(), (int) signal.getRadius());
            g.fillOval(signal.getX() - (int) (signal.getRadius() / 2),
                    signal.getY() - (int) (signal.getRadius() / 2),
                    (int) signal.getRadius(), (int) signal.getRadius());
        }
    }
}
