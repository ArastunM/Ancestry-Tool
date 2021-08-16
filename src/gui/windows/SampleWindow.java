package gui.windows;

import javax.swing.*;
import java.awt.*;

import app.App;
import backend.Sample;
import backend.SampleAverage;
import backend.SampleModern;
import backend.data.TableData;
import gui.components.SampleDataTable;
import gui.components.SampleTable;
import gui.DefaultObjects;
import gui.components.SamplePie;
import gui.Navigation;

/**
 * SampleWindow displays selected sample information, breakdown table and pie chart
 */
public class SampleWindow extends Window {
    // main frame and panel are inherited

    // parentSample: sample to display
    public Sample parentSample;
    // samplePie: pie chart of SampleWindow
    public SamplePie samplePie;
    public Window sourceWindow;

    // inner panels
    private final JPanel upperPanel = new JPanel();
    private final JPanel centralPanel = new JPanel();
    public JPanel leftPanel = new JPanel();

    // percentagesBtn: show percentages button for sample averages
    public JButton percentagesBtn = DefaultObjects.getDefaultBtn("Percentages");
    // componentsBtn: show components button for sample averages
    public JButton componentsBtn = DefaultObjects.getDefaultBtn("Components");

    /**
     * empty constructor
     */
    public SampleWindow() {}

    /**
     * Constructs SampleWindow
     * @param sample sample to display
     */
    public SampleWindow(Sample sample) {
        this.sourceWindow = currentWindow;
        parentSample = sample;

        setUpperPanel();
        setLowerPanel();

        // setting up main panel parameters
        panel.setLayout(new BorderLayout());
        panel.add(upperPanel, BorderLayout.NORTH);
        panel.add(centralPanel, BorderLayout.CENTER);

        // initialising the frame
        initFrame("Sample Window", false);
    }

    /**
     * sets up the upperPanel
     */
    private void setUpperPanel() {
        JPanel lowerContentPanel = new JPanel();
        String gedmatchID = parentSample instanceof SampleModern ?
                ((SampleModern) parentSample).getGedmatchID() : "N/A";

        lowerContentPanel.setLayout(new GridLayout(0, 5));
        JLabel sampleId = DefaultObjects.getCenteredLB(parentSample.getId());
        JLabel sampleGedmatchID = DefaultObjects.getCenteredLB(gedmatchID);
        JLabel sampleCalculatorType = DefaultObjects.getCenteredLB(parentSample.getType());
        JLabel sampleName = DefaultObjects.getCenteredLB(parentSample.getSource());
        JLabel sampleEthnicity = DefaultObjects.getCenteredLB(parentSample.getEthnicity());

        lowerContentPanel.add(sampleId);
        lowerContentPanel.add(sampleGedmatchID);
        lowerContentPanel.add(sampleCalculatorType);
        lowerContentPanel.add(sampleName);
        lowerContentPanel.add(sampleEthnicity);

        upperPanel.setLayout(new GridLayout(2, 0));
        upperPanel.add(DefaultObjects.getDefaultBtn("Back"));
        upperPanel.add(lowerContentPanel);
    }

    /**
     * sets up the lower panel
     */
    private void setLowerPanel() {
        samplePie = new SamplePie(parentSample);
        centralPanel.setLayout(new BorderLayout());

        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        if (parentSample instanceof SampleAverage) {
            JPanel upperLeftPanel = new JPanel();
            upperLeftPanel.setLayout(new GridLayout(0, 2));
            upperLeftPanel.add(percentagesBtn);
            upperLeftPanel.add(componentsBtn);
            leftPanel.add(upperLeftPanel, BorderLayout.NORTH);
        }
        showPercentages();

        double leftPanelRatio = parentSample instanceof SampleAverage ? 0.45 : 0.3;
        leftPanel.setPreferredSize(new Dimension((int) (Window.WIN_WIDTH * leftPanelRatio),
                (int) (Window.WIN_HEIGHT * 0.8)));
        centralPanel.add(leftPanel, BorderLayout.WEST);
        centralPanel.add(samplePie, BorderLayout.CENTER);
    }

    /**
     * used to show percentage breakdown table
     */
    public void showPercentages() {
        if (leftPanel.getComponentCount() == 2) { leftPanel.remove(1); }
        componentsBtn.setBackground(DefaultObjects.BTN_COLOR);
        percentagesBtn.setBackground(DefaultObjects.BTN_CLICK_COLOR);
        SampleDataTable sampleDataTable = new SampleDataTable(samplePie);
        JScrollPane scrollPane = new JScrollPane(sampleDataTable);
        scrollPane.getViewport().setBackground(TableData.BACKGROUND_COLOR);
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        Navigation.refreshPanel(panel);
    }

    /**
     * used to show component table of sample average
     */
    public void showComponents() {
        if (leftPanel.getComponentCount() == 2) { leftPanel.remove(1); }
        percentagesBtn.setBackground(DefaultObjects.BTN_COLOR);
        componentsBtn.setBackground(DefaultObjects.BTN_CLICK_COLOR);
        JScrollPane pane = new JScrollPane((new SampleTable((SampleAverage) parentSample)));
        pane.getViewport().setBackground(TableData.BACKGROUND_COLOR);
        leftPanel.add(pane, BorderLayout.CENTER);
        Navigation.refreshPanel(panel);
    }

    /**
     * custom switchTo method of SampleWindow
     */
    @Override
    public void switchTo() {
        currentWindow.switchFrom();
        currentWindow = this;
        App.sampleWindow = this;
        App.windows.add(this);

        this.refreshWindow();
        this.frame.setVisible(true);
    }

    /**
     * used when switching to source window
     */
    public void switchToSource() {
        this.switchFrom();
        App.windows.remove(this);
        sourceWindow.switchTo();
    }
}

