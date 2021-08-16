package backend.data;

import javax.swing.*;
import java.awt.*;

/**
 * DistanceData regulates Distance calculator color scheme
 *
 * @see engine.Distance
 */
public class DistanceData {
    /**
     * @param distance distance value
     * @return built Color for given distance value
     */
    public static Color getColor(double distance) {
        if (distance < 7.3) {
            return new Color((int) (distance * 33.333), 255, 0);
        } else if (distance < 15) {
            return new Color(255, (int) (500 - (distance * 33.333)), 0);
        } else if (distance < 22.5) {
            return new Color(255, 0, (int) ((distance * 33.333) - 500));
        } else {
            double val = distance * 33.333 >= 1000 ? 1000 : distance * 33.333;
            return new Color((int) (1000 - val), 0, 255);
        }
    }

    // testing
    public static void main(String[] args) {
        double distance = 15;

        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        panel.setBackground(getColor(distance));
        frame.add(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Distance " + distance + " Color");
        frame.pack();
        frame.setSize(500, 500);
        frame.setVisible(true);
    }
}
