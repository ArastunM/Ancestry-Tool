package gui.components;

import javax.swing.*;
import java.awt.*;

import gui.DefaultObjects;
import gui.Navigation;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleEdge;

import backend.DatabaseAccess;
import backend.data.CalculatorData;
import backend.Sample;

/**
 * SamplePie represents pie chart based on parentSample percentages
 */
public class SamplePie extends JPanel {
    // chart: pie chart of SamplePie
    private final JFreeChart chart;
    // parentSample: sample to refer to
    private final Sample parentSample;

    // CHART_COLOR: pie chart background color
    public static Color CHART_COLOR;

    /**
     * Constructs SamplePie
     * @param sample parent sample of SamplePie
     */
    public SamplePie(Sample sample) {
        this.parentSample = sample;
        this.chart = getPieChart();
        this.chart.setBackgroundPaint(CHART_COLOR);
        this.setLayout(new BorderLayout());
        this.add(new ChartPanel(chart));
    }

    /**
     * @return constructed pie chart of parentSample
     */
    private JFreeChart getPieChart() {
        JFreeChart chart = ChartFactory.createPieChart
                (null, getDataset(), true, false, false);

        PiePlot plot = (PiePlot) chart.getPlot();

        // painting pie chart background
        plot.setBackgroundPaint(CHART_COLOR);
        // removing pie chart border
        plot.setOutlinePaint(null);
        // removing pie chart shadow
        plot.setShadowPaint(null);
        // removing interior gap
        plot.setInteriorGap(0.0);
        // removing chart labels
        plot.setLabelGenerator(null);
        // setting plot section outline paint
        plot.setBaseSectionOutlinePaint(new Color(0, 0, 0, 0));
        // setting legend to right side
        chart.getLegend().setPosition(RectangleEdge.RIGHT);
        // removing legend border
        chart.getLegend().setBorder(0, 0, 0, 0);
        // setting legend colors
        chart.getLegend().setBackgroundPaint(CHART_COLOR);
        chart.getLegend().setItemPaint(DefaultObjects.BTN_FOREGROUND_COLOR);
        // setting legend shape as rectangle
        plot.setLegendItemShape(Plot.DEFAULT_LEGEND_ITEM_BOX);
        // rotating the chart to default position
        plot.setStartAngle(0);

        return fixColor(chart);
    }

    /**
     * @return dataset of pie chart based on parentSample
     */
    private PieDataset getDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        String[] regionList = CalculatorData.getRegionList(parentSample.getType());
        Double[] percentages = parentSample.getPercentages().toArray(new Double[0]);

        for (int i = 0; i < regionList.length; i++) {
            if (percentages[i] != 0.0) { dataset.setValue(regionList[i], percentages[i]); }
        } return dataset;
    }

    /**
     * fixes color scheme of pie chart based on used calculator
     * @param chart chart to refer to
     * @return chart with fixed color scheme
     */
    private JFreeChart fixColor(JFreeChart chart) {
        PiePlot plot = (PiePlot) chart.getPlot();
        String[] regionList = CalculatorData.getRegionList(parentSample.getType());
        Color[] colorList = CalculatorData.getColorList(parentSample.getType());

        for (int i = 0; i < regionList.length; i++) {
            plot.setSectionPaint(regionList[i], colorList[i]);
        } return chart;
    }

    /**
     * selects a section of the pie chart
     * @param sectionName String name of section to select
     */
    public void selectSection(String sectionName) {
        PiePlot plot = (PiePlot) this.chart.getPlot();
        resetSections();
        plot.setExplodePercent(sectionName, 0.1);
        Navigation.refreshPanel(this);
    }

    /**
     * resets all pie chart sections to initial, default position (unselected)
     */
    private void resetSections() {
        PiePlot plot = (PiePlot) this.chart.getPlot();
        for (String region : CalculatorData.getRegionList(parentSample.getType())) {
            if (plot.getExplodePercent(region) == 0.1) {
                plot.setExplodePercent(region, 0.0);
            }
        }
    }

    // getter method of parent sample
    public Sample getParentSample() { return parentSample; }

    // testing
    public static void main(String[] args) {
        DatabaseAccess.loadDatabase();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));
        SamplePie samplePie1 = new SamplePie(Sample.ALL_SAMPLES.get(0));
        SamplePie samplePie2 = new SamplePie(Sample.ALL_SAMPLES.get(1));

        panel.add(samplePie1);
        panel.add(samplePie2);

        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Sample Pie Window");
        frame.pack();
        frame.setSize(800, 400);
        frame.setVisible(true);
    }
}
