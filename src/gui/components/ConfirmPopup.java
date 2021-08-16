package gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import app.App;
import backend.DatabaseAccess;
import backend.Sample;
import gui.DefaultObjects;

/**
 * ConfirmPopup is a custom confirmation popup with YES / NO options, extending WarningPopup
 */
public class ConfirmPopup extends WarningPopup implements ActionListener {
    // action: String action to refer in case of YES option
    private final String action;

    /**
     * Constructs ConfirmPopup
     * @param title window title of the popup
     * @param content message content of the popup
     * @param action action to perform in case of YES option
     */
    public ConfirmPopup(String title, String content, String action) {
        super(title, content);
        this.action = action;

        JPanel confirmPanel = new JPanel();
        confirmPanel.setLayout(new GridLayout(0, 2));
        JButton yesBtn = DefaultObjects.getDefaultBtn("Yes");
        yesBtn.addActionListener(this);
        JButton noBtn = DefaultObjects.getDefaultBtn("No");
        noBtn.addActionListener(this);
        confirmPanel.add(yesBtn);
        confirmPanel.add(noBtn);

        panel.add(confirmPanel);
        panel.setLayout(new GridLayout(2, 0));
    }

    /**
     * executes popup action, closes the popup
     */
    private void actionExecute() {
        switch (action) {
            case "Clean Database" -> DatabaseAccess.cleanDatabase();
            default ->  {
                Sample.getSample(action).remove();
                App.databaseWindow.refreshWindow();
            }
        } this.setVisible(false);
    }

    /**
     * called upon popup button click
     * @param e action event reference
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        if (button.getText().equals("Yes")) { this.actionExecute(); }
        if (button.getText().equals("No")) { this.setVisible(false); }
    }
}
