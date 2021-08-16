package gui.windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import backend.DatabaseAccess;
import gui.Navigation;

/**
 * General Window SuperClass for Ancestry Tool App
 */
public class Window {
    // frame: main frame of the window
    public JFrame frame = new JFrame();
    // panel: main panel of the window
    public JPanel panel = new JPanel();

    // WIN_WIDTH: default width of the window
    public static int WIN_WIDTH = 1000;
    // WIN_HEIGHT: default height of the window
    public static int WIN_HEIGHT = 500;
    // BACKGROUND_COLOR: default background color of the window
    public static Color BACKGROUND_COLOR;

    // currentWindow: currently used window
    public static Window currentWindow;

    /**
     * initialises the window frame
     * @param winTitle title of the window
     * @param isVisible whether window should be visible or not
     */
    public void initFrame(String winTitle, boolean isVisible) {
        panel.setBackground(BACKGROUND_COLOR);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(winTitle);
        frame.pack();
        frame.setSize(WIN_WIDTH, WIN_HEIGHT);
        frame.setVisible(isVisible);
        if (isVisible) { currentWindow = this; }
        this.onExit();
        this.center();
    }

    /**
     * centers the window in the middle of the screen
     */
    private void center() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.frame.setLocation(dim.width / 2 - this.frame.getSize().width / 2,
                dim.height / 2 - this.frame.getSize().height / 2);
    }

    /**
     * refreshes the window
     */
    public void refreshWindow() {
        Navigation.refreshPanel(panel);
    }

    /**
     * app switches to this window
     */
    public void switchTo() {
        currentWindow.switchFrom();
        currentWindow = this;
        this.refreshWindow();
        this.frame.setVisible(true);
    }

    /**
     * app switched from this window
     */
    public void switchFrom() {
        this.frame.setVisible(false);
    }

    /**
     * called on Window exit
     */
    public void onExit() {
        this.frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                DatabaseAccess.saveDatabase();
            }
        });
    }
}
