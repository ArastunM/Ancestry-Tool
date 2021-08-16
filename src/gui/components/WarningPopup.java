package gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import gui.DefaultObjects;
import gui.windows.Window;

/**
 * WarningPopup is a warning popup with custom message
 */
public class WarningPopup extends JFrame {
    // parentWindow: window calling the popup
    public Window parentWindow;
    // panel: main panel of the popup
    public JPanel panel = new JPanel();

    // POPUP_WIDTH: default width of the popup
    private static final int POPUP_WIDTH = 200;
    // POPUP_HEIGHT: default height of the popup
    private static final int POPUP_HEIGHT = 100;

    /**
     * Constructs WarningPopup
     * @param title window title of the popup
     * @param content message content of the popup
     */
    public WarningPopup(String title, String content) {
        this.parentWindow = Window.currentWindow;
        JLabel contentLabel = DefaultObjects.getCenteredLB(content);
        panel.setLayout(new GridLayout());
        panel.add(contentLabel);

        this.add(panel);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setTitle(title);
        this.pack();
        this.setSize(content.length() * 10, POPUP_HEIGHT);
        this.focusChange();
    }

    /**
     * shows the popup
     */
    public void pop() {
        this.center();
        this.setVisible(true);
    }

    /**
     * manages popup focus changes
     */
    public void focusChange() {
        JFrame _this = this;
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}
            @Override
            public void focusLost(FocusEvent e) { _this.dispose(); }
        });
    }

    /**
     * centers the popup in the middle of the screen
     */
    private void center() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2,
                dim.height / 2 - this.getSize().height / 2);
    }
}
