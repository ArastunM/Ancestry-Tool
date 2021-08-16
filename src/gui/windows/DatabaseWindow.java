package gui.windows;

import javax.swing.*;
import java.awt.*;

import backend.data.TableData;
import gui.DefaultObjects;
import gui.Navigation;
import gui.components.SampleTable;

/**
 * DatabaseWindow contains main database table and its toolbar
 */
public class DatabaseWindow extends Window {
    // main frame and panel are inherited

    // constructing tool and table panels
    private static final JPanel toolPanel = new JPanel();
    private static final JPanel tablePanel = new JPanel();

    // sampleTable: sample table to refer to
    public static SampleTable sampleTable;

    // toolPanel buttons
    public static JButton removeSampleBtn;
    public static JComboBox<String> sampleTypeFilterBtn;
    public static JComboBox<String> calculatorTypeFilterBtn;
    public static JTextField searchField;

    // filter selection list
    private static final String[] sampleTypeFilterList = new String[]
            {"Show All", "Show Modern Samples", "Show Ancient Samples", "Show Sample Averages"};
    private static final String[] calculatorTypeFilterList = new String[]
            {"All Calculators", "Dodecad K12b", "Ancient Eurasia K6", "Eurogenes K13", "MDLP World"};

    /**
     * Constructs View Database Window
     */
    public DatabaseWindow() {
        // constructing panel contents
        JButton homeBtn = DefaultObjects.getDefaultBtn("Home");
        JButton addSampleBtn = DefaultObjects.getDefaultBtn("Add Sample");
        removeSampleBtn = DefaultObjects.getDefaultBtn("Remove Sample");
        sampleTypeFilterBtn = DefaultObjects.getDefaultCB(sampleTypeFilterList);
        calculatorTypeFilterBtn = DefaultObjects.getDefaultCB(calculatorTypeFilterList);
        searchField =  DefaultObjects.getSearchTF("Search...");

        // setting up lowerToolPanel parameters
        JPanel lowerToolPanel = new JPanel();
        lowerToolPanel.setLayout(new GridLayout(0, 5));
        lowerToolPanel.add(homeBtn);
        lowerToolPanel.add(addSampleBtn);
        lowerToolPanel.add(removeSampleBtn);
        lowerToolPanel.add(sampleTypeFilterBtn);
        lowerToolPanel.add(calculatorTypeFilterBtn);

        // setting up toolPanel parameters
        toolPanel.removeAll();
        toolPanel.setLayout(new BorderLayout());
        toolPanel.add(searchField, BorderLayout.NORTH);
        toolPanel.add(lowerToolPanel, BorderLayout.CENTER);

        // setting up tablePanel parameters
        tablePanel.setLayout(new GridLayout());

        // setting up main panel parameters
        panel.setLayout(new BorderLayout());
        panel.add(toolPanel, BorderLayout.NORTH);
        panel.add(tablePanel, BorderLayout.CENTER);

        // initialising the frame
        initFrame("Database Window", false);
    }

    /**
     * updates sample table based on current database
     */
    public void setTable() {
        tablePanel.removeAll();
        sampleTable = new SampleTable();
        JScrollPane scrollPane = new JScrollPane(sampleTable);
        scrollPane.getViewport().setBackground(TableData.BACKGROUND_COLOR);
        tablePanel.add(scrollPane);
        Navigation.refreshPanel(tablePanel);
    }
    /**
     * updates sample table based on current database
     * @param bySort sorting method (null = no sorting applied)
     */
    public void setTable(String bySort) {
        tablePanel.removeAll();
        sampleTable = new SampleTable(bySort);
        JScrollPane scrollPane = new JScrollPane(sampleTable);
        scrollPane.getViewport().setBackground(TableData.BACKGROUND_COLOR);
        tablePanel.add(scrollPane);
        Navigation.refreshPanel(tablePanel);
    }

    /**
     * custom window refresh for DatabaseWindow
     */
    @Override
    public void refreshWindow() {
        sampleTypeFilterBtn.setSelectedIndex(0);
        calculatorTypeFilterBtn.setSelectedIndex(0);
        // updates Table
        setTable();
        // sets default button parameters
        removeSampleBtn.setBackground(DefaultObjects.BTN_COLOR);
        searchField.setForeground(DefaultObjects.FIELD_EMPTY_FOREGROUND_COLOR);
        searchField.setText("Search...");
        Navigation.refreshPanel(toolPanel);
    }
}
