package backend;

import java.util.ArrayList;

/**
 * SampleModern extends Sample, used for Modern DNA samples
 */
public class SampleModern extends Sample {
    // gedmatchID: gedmatch id of the sample (if available)
    private String gedmatchID;
    // mtDNA: maternal haplogroup of the sample
    private String mtDNA;
    // yDNA: paternal haplogroup of the sample
    private String yDNA;

    /**
     * Constructs SampleModern
     * @param source source of the sample
     * @param id local id of sample
     * @param gedmatchID gedmatch id of the sample
     * @param type calculator type used
     * @param ethnicity ethnicity of sample owner
     * @param percentages ethnicity percentages
     * @param mtDNA maternal haplogroup of sample
     * @param yDNA paternal haplogroup of sample
     */
    public SampleModern(String source, String id, String gedmatchID,
                        String type, String ethnicity,
                        ArrayList<Double> percentages, String mtDNA, String yDNA) {
        super(source, id, ethnicity);
        setGedmatchID(gedmatchID);
        setType(type);
        setPercentages(percentages);
        setEthnicity(ethnicity);
        setMtDNA(mtDNA);
        setYDNA(yDNA);
        ALL_SAMPLES.set(ALL_SAMPLES.indexOf(this), this);
    }

    /**
     * @return String type of the modern sample
     */
    @Override
    public String getSampleType() {
        return "Modern Sample";
    }

    /**
     * searches for modern and ancient samples with given calculatorType
     * @param calculatorType calculator type refer to
     * @return found sample parameters in form of {name id}
     */
    public static String[] getSampleSelectionList(String calculatorType) {
        ArrayList<String> matchingSampleParameters = new ArrayList<>();
        for (Sample sample : Sample.ALL_SAMPLES) {
            if (sample.getType().equals(calculatorType)) {
                matchingSampleParameters.add(sample.getSource()
                        + " | " + sample.getEthnicity() + " | " + sample.getId());
            }
        } return matchingSampleParameters.toArray(new String[0]);
    }

    // getter methods
    public String getGedmatchID() { return gedmatchID; }
    public String getMtDNA() { return mtDNA; }
    public String getYDNA() { return yDNA; }

    // setter methods
    public void setGedmatchID(String gedmatchID) { this.gedmatchID = gedmatchID; }
    public void setMtDNA(String mtDNA) { this.mtDNA = mtDNA; }
    public void setYDNA(String yDNA) { this.yDNA = yDNA; }

    /**
     * @return String representation of the SampleModern
     */
    @Override
    public String toString() {
        return super.toString() + ", gedmatchID: " + gedmatchID
                + ", mtDNA: " + mtDNA + ", yDNA: " + yDNA + "]";
    }
}
