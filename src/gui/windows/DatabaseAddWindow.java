package gui.windows;

import javax.swing.*;
import java.awt.*;

import backend.data.CalculatorData;
import backend.SampleModern;
import gui.DefaultObjects;
import gui.Navigation;
import gui.components.CalculatorInput;
import gui.components.SearchList;

/**
 * DatabaseAddWindow is used to add new samples to database
 */
public class DatabaseAddWindow extends Window {
    // main frame and panel are inherited

    // metaPanel: contains general meta data of a sample
    private static final JPanel metaPanel = new JPanel();
    // breakdownPanel: contains percentage input and type selection
    private static final JPanel breakdownPanel = new JPanel();

    // inner panels
    private static final JPanel percentagesPanel = new JPanel();
    private static final JPanel selectionPanel = new JPanel();
    private static final JPanel submitPanel = new JPanel();

    // public buttons
    public static JComboBox<String> sampleTypeBtn;
    public static JComboBox<String> calcTypeBtn;
    public static JLabel componentSelected;

    // defining and constructing metaPanel text fields
    public static JTextField[] metaPanelFields = new JTextField[5];
    // metaPanelFieldHeader: contains meta panel field header names
    public static String[] metaPanelFieldHeaders = new String[]{"Owner, Source", "Gedmatch ID", "Ethnicity",
            "Maternal Haplogroup", "Paternal Haplogroup", "Time Period (Ancient Samples)"};

    /**
     * Constructs Database Add Window
     */
    public DatabaseAddWindow() {
        String[] sampleTypeCB = new String[]{"Sample Modern", "Sample Ancient", "Sample Average"};
        sampleTypeBtn = DefaultObjects.getDefaultCB(sampleTypeCB);
        calcTypeBtn = DefaultObjects.getDefaultCB(CalculatorData.calculatorTypes);

        // setting up percentages panel
        setPercentagesPanel();
        // setting up submit panel
        setSubmitPanel();

        // setting up breakdown panel
        setBreakdownPanel(percentagesPanel);
        // setting up metaPanel parameters
        setMetaPanel(sampleTypeBtn.getSelectedIndex() == 1);
        // setting up the main panel parameters
        panel.setLayout(new GridLayout(0, 2));
        panel.add(breakdownPanel);
        panel.add(metaPanel);

        // initialising the frame
        initFrame("Database Add Window", false);
    }

    /**
     * sets up the breakdownPanel
     * @param content main (CENTER) content of the breakdownPanel
     */
    public void setBreakdownPanel(JPanel content) {
        breakdownPanel.setLayout(new BorderLayout());
        breakdownPanel.removeAll();
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 2));
        topPanel.add(DefaultObjects.getDefaultLB("Select Sample Type"));
        topPanel.add(DefaultObjects.getDefaultLB("Select Calculator Type"));
        topPanel.add(sampleTypeBtn);
        topPanel.add(calcTypeBtn);
        breakdownPanel.add(topPanel, BorderLayout.NORTH);
        breakdownPanel.add(content, BorderLayout.CENTER);
        breakdownPanel.add(submitPanel, BorderLayout.SOUTH);
        Navigation.refreshPanel(breakdownPanel);
    }

    /**
     * sets up the percentagesPanel
     */
    public void setPercentagesPanel() {
        setMetaPanel(sampleTypeBtn.getSelectedIndex() == 1);
        percentagesPanel.removeAll();
        percentagesPanel.setLayout(new BorderLayout());
        String calculatorType = (String) calcTypeBtn.getSelectedItem();
        percentagesPanel.add(new CalculatorInput(calculatorType));
        setBreakdownPanel(percentagesPanel);
    }

    /**
     * sets up the selectionPanel
     */
    public void setSelectionPanel() {
        setMetaPanel(false);
        selectionPanel.removeAll();
        selectionPanel.setLayout(new GridLayout(2, 0));

        JPanel componentSelectedPanel = new JPanel();
        componentSelectedPanel.setLayout(new GridLayout(2, 0));
        componentSelected = DefaultObjects.getDefaultLB("");
        JPanel componentSelection = new SearchList
                (SampleModern.getSampleSelectionList((String) calcTypeBtn.getSelectedItem()),
                        "Enter components to be included...", componentSelected);

        componentSelectedPanel.add(componentSelected);
        componentSelectedPanel.add(DefaultObjects.getDefaultBtn("Reset Selections"));
        selectionPanel.add(componentSelection);
        selectionPanel.add(componentSelectedPanel);

        setBreakdownPanel(selectionPanel);
    }

    /**
     * sets up the submitPanel
     */
    public static void setSubmitPanel() {
        submitPanel.removeAll();
        submitPanel.setLayout(new GridLayout(0, 2));
        submitPanel.add(DefaultObjects.getDefaultBtn("Add"));
        submitPanel.add(DefaultObjects.getDefaultBtn("Cancel"));
    }

    /**
     * sets up the metaPanel
     */
    public void setMetaPanel(boolean isAncient) {
        metaPanelFields = new JTextField[isAncient ? 6 : 5];

        for (int i = 0; i < metaPanelFields.length; i++) {
            metaPanelFields[i] = DefaultObjects.getDefaultTF(metaPanelFieldHeaders[i]);
        }
        metaPanel.removeAll();
        metaPanel.setLayout(new GridLayout(metaPanelFields.length, 0));

        for (JTextField field : metaPanelFields) { metaPanel.add(field); }
        Navigation.refreshPanel(metaPanel);
    }

    /**
     * custom window refresh for DatabaseAddWindow
     */
    @Override
    public void refreshWindow() {
        sampleTypeBtn.setSelectedIndex(0);
        setPercentagesPanel();
    }
}
