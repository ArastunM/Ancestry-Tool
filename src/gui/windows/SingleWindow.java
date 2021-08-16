package gui.windows;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

import backend.data.CalculatorData;
import backend.Sample;
import backend.SampleModern;
import engine.Single;
import gui.components.SearchList;
import gui.components.SingleTable;
import gui.components.WarningPopup;
import gui.DefaultObjects;
import gui.Navigation;

/**
 * SingleWindow displays Single calculator input options and output table
 */
public class SingleWindow extends Window {
    // main frame and panel are inherited

    // databaseReach: database to consider when measuring single composition
    public ArrayList<Sample> databaseReach;

    // compositionPanel: displays Single calculator output as a table
    private static final JPanel compositionPanel = new JPanel();
    // optionsPanel: Single calculator input options panel
    private static final JPanel optionsPanel = new JPanel();

    // selection boxes
    private static JButton cycleBtn;
    private static JComboBox<String> databaseReachOptions;
    private static JComboBox<String> calculatorTypeOptions;
    // singleTable: composition table
    public static SingleTable singleTable;

    // targetPanel: target selection panel
    private static SearchList targetPanel;

    // databaseReachOptionsList: search database restriction options
    public static String[] databaseReachOptionsList = DistanceWindow.databaseReachOptionsList;
    // cyclesOptions: cycle count options for Single calculator
    public static String[] cycleOptions = new String[]{"0.25", "0.5", "0.75", "1", "2", "5"};

    /**
     * Constructs SingleWindow
     */
    public SingleWindow() {
        // nullifying window panels
        compositionPanel.removeAll();
        optionsPanel.removeAll();
        targetPanel = new SearchList(SampleModern.getSampleSelectionList
                (CalculatorData.calculatorTypes[0]), "Enter Target..");

        // constructing panel content parameters
        JButton backBtn = DefaultObjects.getDefaultBtn("Back");
        cycleBtn = DefaultObjects.getDefaultBtn("Cycles - " + cycleOptions[0] + "X");

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
        optionsPanel.setLayout(new GridLayout(6, 0));
        optionsPanel.add(backBtn);
        optionsPanel.add(cycleBtn);
        optionsPanel.add(databaseReachPanel);
        optionsPanel.add(calculatorPanel);
        optionsPanel.add(targetPanel);
        optionsPanel.add(run);

        panel.setLayout(new GridLayout(0, 2));
        panel.add(compositionPanel);
        panel.add(optionsPanel);

        // initiating the main frame
        initFrame("Autosomal Single Window", false);
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
     * updates cycle count selection preferences
     */
    public void updateCycle() {
        String currentCycleCount = cycleBtn.getText().substring(9, cycleBtn.getText().length() - 1);
        for (int i = 0; i < cycleOptions.length; i++) {
            if (currentCycleCount.equals(cycleOptions[i])) {
                int nextIndex = i == cycleOptions.length - 1 ? 0 : i + 1;
                cycleBtn.setText("Cycles - " + cycleOptions[nextIndex] + "X");
            }
        }
    }

    /**
     * runs the Single calculator, updates the composition panel accordingly
     */
    public void runSingle() {
        updateDatabaseReach();

        if (targetPanel.comboBox.getSelectedItem() == null) {
            new WarningPopup("Error", "Select a Target before running distance calculator").pop();
            return;
        }

        String targetId = ((String) Objects.requireNonNull(targetPanel.comboBox.getSelectedItem()))
                .split("\\|")[2].strip();
        Sample target = Sample.getSample(targetId);

        double cycleX = Double.parseDouble(cycleBtn.getText().substring(9, cycleBtn.getText().length() - 1));

        Single single = new Single(target, cycleX);
        LinkedHashMap<String, Double> singleMap = single.getSingleMap(databaseReach);
        singleTable = new SingleTable(singleMap);

        if (singleMap.size() == 0) {
            new WarningPopup("Error", "No samples found with given settings").pop();
            return;
        }

        compositionPanel.removeAll();
        compositionPanel.setLayout(new BorderLayout());
        JPanel upperCompositionPanel = new JPanel();
        upperCompositionPanel.setLayout(new GridLayout(1, 0));
        upperCompositionPanel.add(DefaultObjects.getDistanceLB(String.valueOf(single.getGeneralDistance())));

        compositionPanel.add(upperCompositionPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(singleTable);
        scrollPane.getViewport().setBackground(Window.BACKGROUND_COLOR);
        compositionPanel.add(scrollPane, BorderLayout.CENTER);
        Navigation.refreshPanel(panel);
    }

    /**
     * custom window refresh method for SingleWindow
     */
    @Override
    public void refreshWindow() {
        compositionPanel.removeAll();
        calculatorTypeOptions.setSelectedIndex(0);
        targetPanel.reset();
        Navigation.refreshPanel(panel);
    }
}
