package gui.windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import backend.DatabaseAccess;
import backend.data.LayoutData;
import gui.DefaultObjects;
import gui.components.SignalMap;
import gui.components.WarningPopup;

/**
 * MenuWindow is the main menu window of the App started upon launch
 */
public class MenuWindow extends Window {
    // main frame and panel are inherited

    public SignalMap signalMap;
    public JButton layoutMode;

    // databaseSelectionList: String selection list for database windows
    private static final String[] databaseSelectionList = new String[]
            {"View Database", "Upload Data", "Clean Database"};
    // ancestrySelectionList: String selection list for menu windows
    private static final String[] ancestrySelectionList = new String[]
            {"Autosomal Distance", "Autosomal Single"};

    /**
     * Constructs the MenuWindow
     */
    public MenuWindow() {
        // constructing panel contents
        JLabel databaseLB = DefaultObjects.getDefaultLB("Database");
        JLabel ancestryLB = DefaultObjects.getDefaultLB("Ancestry");
        JComboBox<String> databaseBtn = DefaultObjects.getDefaultCB(databaseSelectionList);
        JComboBox<String> ancestryBtn = DefaultObjects.getDefaultCB(ancestrySelectionList);

        // setting up tool panel parameters
        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new GridLayout(2, 2));
        toolPanel.add(databaseLB);
        toolPanel.add(ancestryLB);
        toolPanel.add(databaseBtn);
        toolPanel.add(ancestryBtn);

        // initiating the SignalMap
        signalMap = new SignalMap();

        // setting layout mode button
        layoutMode = DefaultObjects.getLayoutModeBtn(BACKGROUND_COLOR != LayoutData.lightModeMain);
        layoutMode.setBounds(WIN_WIDTH - (WIN_WIDTH / 10),
                25, WIN_WIDTH / 20, WIN_HEIGHT / 10);
        signalMap.add(layoutMode);

        // setting up the main panel parameters
        panel.setLayout(new BorderLayout());
        panel.add(toolPanel, BorderLayout.NORTH);
        panel.add(signalMap, BorderLayout.CENTER);

        // initialising the frame
        initFrame("Menu Window", true);
        // called upon frame resizing
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                // adjusting layout mode button position
                layoutMode.setBounds(panel.getWidth() - (panel.getWidth() / 10),
                        25, panel.getWidth() / 20, panel.getHeight() / 10);
                // clearing existing map signals
                SignalMap.mapSignals.clear();
            }
        });

        if (DatabaseAccess.accessStatus != 200) {
            new WarningPopup("Error", "Database could not be accessed, check instructions").pop();
        }
    }

    /**
     * custom switchTo method of MenuWindow
     */
    @Override
    public void switchTo() {
        currentWindow.switchFrom();
        currentWindow = this;
        this.refreshWindow();
        this.frame.setVisible(true);

        this.signalMap.initSignals();
    }

    /**
     * custom switchFrom method of MenuWindow
     */
    @Override
    public void switchFrom() {
        this.frame.setVisible(false);
        this.signalMap.stopSignals();
    }
}
