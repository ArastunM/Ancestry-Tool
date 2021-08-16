package backend.data;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import backend.Sample;
import backend.SampleModern;
import exceptions.SortingNotRecognizedException;
import gui.windows.DatabaseWindow;

/**
 * TableData contains sample table headers and content retrieval methods
 */
public class TableData {
    // header: table header (column) names
    public static String[] header = new String[]{"SOURCE", "ID",
            "CALCULATOR TYPE", "ETHNICITY", "mtDNA", "Y-DNA", "SAMPLE TYPE"};
    // componentHeader: component table header (column) names
    public static String[] componentHeader = new String[]{"SOURCE", "ID", "ETHNICITY", "SAMPLE TYPE"};
    // percentageHeader: percentage table header (column) names
    public static String[] percentageHeader = new String[]{"Population", ""};
    // singleHeader: composition table header for Single calculator output
    public static String[] singleHeader = new String[]{"SOURCE", "ETHNICITY", "PERCENTAGE"};

    // BACKGROUND_COLOR: default table background color
    public static Color BACKGROUND_COLOR;
    // HEADER_FOREGROUND_COLOR: table header text color
    public static Color HEADER_FOREGROUND_COLOR;
    // CONTENT_FOREGROUND_COLOR: table content text color
    public static Color CONTENT_FOREGROUND_COLOR;

    /**
     * @param samples ArrayList of samples to refer to
     * @return given samples as table content
     */
    public static String[][] getTableContent(ArrayList<Sample> samples) {
        String[][] rec = new String[samples.size()][header.length];

        for (int i = 0; i < samples.size(); i++) {
            Sample sample = samples.get(i);
            rec[i][0] = sample.getSource();
            rec[i][1] = sample.getId();
            rec[i][2] = sample.getType();
            rec[i][3] = sample.getEthnicity();
            rec[i][4] = rec[i][5] = "N/A";

            if (sample instanceof SampleModern) {
                SampleModern sampleModern = (SampleModern) sample;
                rec[i][4] = sampleModern.getMtDNA();
                rec[i][5] = sampleModern.getYDNA();
            }
            rec[i][6] = sample.getSampleType();
        }
        return rec;
    }

    /**
     * @return sorted samples as table content based on filter selections
     */
    public static String[][] getSortedTableContent() {
        String sampleType = (String) DatabaseWindow.sampleTypeFilterBtn.getSelectedItem();
        ArrayList<Sample> samples = Sample.ALL_SAMPLES;
        if (sampleType != null) {
            sampleType = sampleType.substring(5);
            sampleType = sampleType.substring(0, sampleType.length() - 1);
            samples = sampleType.equals("Al") ? samples : Sample.getSamples(sampleType);
        }

        String calculatorType = (String) DatabaseWindow.calculatorTypeFilterBtn.getSelectedItem();
        if (calculatorType == null || !calculatorType.equals("All Calculators")) {
            samples = Sample.getSamples(calculatorType, samples);
        }
        return getTableContent(samples);
    }

    /**
     * @param sorting sorting type to refer to
     * @return sorted samples as table content
     */
    public static String[][] getSortedTableContent(String sorting) {
        if (sorting.equals("Show All"))
        { return getTableContent(Sample.ALL_SAMPLES); }
        if (sorting.equals("Show Modern Samples"))
        { return getTableContent(Sample.getSamples("Modern Sample")); }
        if (sorting.equals("Show Ancient Samples"))
        { return getTableContent(Sample.getSamples("Ancient Sample")); }
        if (sorting.equals("Show Sample Averages"))
        { return getTableContent(Sample.getSamples("Sample Average")); }
        if (sorting.equals("Dodecad K12b") || sorting.equals("Ancient Eurasia K6")
                || sorting.equals("Eurogenes K13")) {
            return getTableContent(Sample.getSamples(sorting));
        }
        throw new SortingNotRecognizedException(sorting);
    }

    /**
     * @param singleMap single map to refer to
     * @return Single calculator composition table
     */
    public static String[][] getSingleTableContent(LinkedHashMap<String, Double> singleMap) {
        String[][] rec = new String[singleMap.size()][singleHeader.length];

        int i = 0;
        for (Map.Entry<String, Double> entry : singleMap.entrySet()) {
            rec[i][0] = entry.getKey().split(",")[0];
            rec[i][1] = entry.getKey().split(",")[1].trim();
            rec[i][2] = String.valueOf(entry.getValue());
            i++;
        }
        return rec;
    }
}
