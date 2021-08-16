package gui.windows;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import backend.data.CalculatorData;
import backend.Sample;
import backend.SampleModern;
import backend.data.TableData;
import engine.Distance;
import engine.DistanceMixed;
import gui.components.SearchList;
import gui.components.WarningPopup;
import gui.DefaultObjects;
import gui.Navigation;

/**
 * DistanceWindow displays Distance calculator input options and output
 */
public class DistanceWindow extends Window {
    // main frame and panel are inherited

    // distanceType: single populations or mixed populations
    public boolean distanceType;
    // databaseReach: database to consider when measuring distance
    public ArrayList<Sample> databaseReach;

    // distancePanel: displays Distance calculator output
    private static final JPanel distancePanel = new JPanel();
    // upperDistancePanel: contains distance type selection buttons
    private static final JPanel upperDistancePanel = new JPanel();
    // optionsPanel: contains Distance calculator input options
    private static final JPanel optionsPanel = new JPanel();

    // other inner components
    private static JTextField maxOutputNum;
    private static JComboBox<String> databaseReachOptions;
    private static JComboBox<String> calculatorTypeOptions;
    private static JButton singlePopulationsBtn;
    private static JButton mixedPopulationsBtn;

    // targetPanel: target selection panel
    private static SearchList targetPanel;

    // databaseReachOptionsList: search database restriction options
    public static String[] databaseReachOptionsList = new String[]
            {"All", "Modern Samples", "Ancient Samples", "Sample Averages"};

    /**
     * Constructs DistanceWindow
     */
    public DistanceWindow() {
        // nullifying window panels
        distancePanel.removeAll();
        upperDistancePanel.removeAll();
        optionsPanel.removeAll();
        targetPanel = new SearchList(SampleModern.getSampleSelectionList
                (CalculatorData.calculatorTypes[0]),"Enter Target..");

        // constructing panel content parameters
        JButton clearBtn = DefaultObjects.getDefaultBtn("Back");
        maxOutputNum = DefaultObjects.getDefaultTF("Max Output Number");

        JLabel databaseReachLabel = DefaultObjects.getCenteredLB("Select Database Reach");
        databaseReachOptions = DefaultObjects.getDefaultCB(databaseReachOptionsList);
        JPanel databaseReachPanel = new JPanel();
        databaseReachPanel.setLayout(new GridLayout(2, 0));
        databaseReachPanel.add(databaseReachLabel);
        databaseReachPanel.add(databaseReachOptions);

        JLabel calculatorLabel = DefaultObjects.getCenteredLB("Select Calculator Type");
        calculatorTypeOptions = DefaultObjects.getDefaultCB(CalculatorData.calculatorTypes);
        JPanel calculatorPanel = new JPanel();
        calculatorPanel.setLayout(new GridLayout(2, 0));
        calculatorPanel.add(calculatorLabel);
        calculatorPanel.add(calculatorTypeOptions);
        updateTargetList();
        JButton run = DefaultObjects.getDefaultBtn("Run");

        // constructing panels
        singlePopulationsBtn = DefaultObjects.getDefaultBtn("Single Populations");
        mixedPopulationsBtn = DefaultObjects.getDefaultBtn("Mixed Populations");
        upperDistancePanel.setLayout(new GridLayout(0, 2));
        upperDistancePanel.add(singlePopulationsBtn);
        upperDistancePanel.add(mixedPopulationsBtn);

        distancePanel.setLayout(new BorderLayout());
        distancePanel.add(upperDistancePanel, BorderLayout.NORTH);
        distancePanel.add(new JScrollPane(new JPanel()), BorderLayout.CENTER);

        optionsPanel.setLayout(new GridLayout(6, 0));
        optionsPanel.add(clearBtn);
        optionsPanel.add(maxOutputNum);
        optionsPanel.add(databaseReachPanel);
        optionsPanel.add(calculatorPanel);
        optionsPanel.add(targetPanel);
        optionsPanel.add(run);

        panel.setLayout(new GridLayout(0, 2));
        panel.add(distancePanel);
        panel.add(optionsPanel);

        // initiating the main frame
        initFrame("Autosomal Distance Window", false);
    }

    /**
     * used to update available target list based on selected calculator type
     */
    public void updateTargetList() {
        targetPanel.options = SampleModern.getSampleSelectionList
                ((String) calculatorTypeOptions.getSelectedItem());
        targetPanel.reset();
        Navigation.refreshPanel(panel);
    }

    /**
     * updates database to consider when calculating Single,
     * based on selected calculator and sample type
     */
    public void updateDatabaseReach() {
        String calcType = (String) calculatorTypeOptions.getSelectedItem();
        databaseReach = Sample.getSamples(calcType);

        switch (databaseReachOptions.getSelectedIndex()) {
            case 0 -> databaseReach = Sample.getSamples(calcType);
            case 1 -> databaseReach = Sample.getSamples("Modern Sample", databaseReach);
            case 2 -> databaseReach = Sample.getSamples("Ancient Sample", databaseReach);
            case 3 -> databaseReach = Sample.getSamples("Sample Average", databaseReach);
        }
    }

    /**
     * runs the Distance calculator, updates the distance panel accordingly
     */
    public void runDistance() {
        updateDatabaseReach();

        int maxOutput;
        try { maxOutput = Integer.parseInt(maxOutputNum.getText()); }
        catch (NumberFormatException e) {
            new WarningPopup("Error", "Max Output Number should be a number").pop();
            return;
        }

        if (targetPanel.comboBox.getSelectedItem() == null) {
            new WarningPopup("Error", "Select a Target before running distance calculator").pop();
            return;
        }

        String targetId = ((String) Objects.requireNonNull(targetPanel.comboBox.getSelectedItem()))
                .split("\\|")[2].strip();
        Sample target = Sample.getSample(targetId);

        Map<String, Double> distanceMap;
        if (distanceType) {
            Distance distance = new Distance(target);
            distanceMap = distance.getTopDistances(databaseReach, maxOutput);
        } else {
            DistanceMixed distanceMixed = new DistanceMixed(target);
            distanceMap = distanceMixed.getTopDistances(databaseReach, maxOutput);
        }

        if (distanceMap.size() == 0) {
            new WarningPopup("Error", "No samples found with given settings").pop();
            return;
        }

        JPanel distancesPanel = new JPanel();
        distancesPanel.setLayout(new GridLayout(distanceMap.size(), 0));

        for (String key : distanceMap.keySet()) {
            distancesPanel.add(DefaultObjects.getDistancePN(key, distanceMap.get(key), distanceType));
        }
        distancePanel.removeAll();
        distancePanel.add(upperDistancePanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(distancesPanel);
        scrollPane.getViewport().setBackground(TableData.BACKGROUND_COLOR);
        distancePanel.add(scrollPane, BorderLayout.CENTER);
        Navigation.refreshPanel(panel);
    }

    /**
     * updates the distance type (single or mixed populations)
     * @param distanceType new distance type
     */
    public void updateDistanceType(String distanceType) {
        if (distanceType.equals("Single Populations")) {
            singlePopulationsBtn.setBackground(DefaultObjects.BTN_CLICK_COLOR);
            mixedPopulationsBtn.setBackground(DefaultObjects.BTN_COLOR);
            this.distanceType = true;

        } else if (distanceType.equals("Mixed Populations")) {
            singlePopulationsBtn.setBackground(DefaultObjects.BTN_COLOR);
            mixedPopulationsBtn.setBackground(DefaultObjects.BTN_CLICK_COLOR);
            this.distanceType = false;
        }
    }

    /**
     * custom window refresh method for DistanceWindow
     */
    @Override
    public void refreshWindow() {
        distanceType = true;
        maxOutputNum.setText("Max Output Number");
        maxOutputNum.setForeground(DefaultObjects.FIELD_EMPTY_FOREGROUND_COLOR);
        calculatorTypeOptions.setSelectedIndex(0);
        targetPanel.reset();
        singlePopulationsBtn.setBackground(DefaultObjects.BTN_CLICK_COLOR);
        mixedPopulationsBtn.setBackground(DefaultObjects.BTN_COLOR);
        updateTargetList();
        if (distancePanel.getComponentCount() == 2) { distancePanel.remove(1); }
        Navigation.refreshPanel(panel);
    }
}
