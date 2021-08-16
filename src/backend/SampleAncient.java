package backend;

import java.util.ArrayList;

/**
 * SampleAncient extends SampleModern (and Sample), used for Ancient DNA samples
 */
public class SampleAncient extends SampleModern {
    // timePeriod: time period sample refers to
    private String timePeriod;

    /**
     * Constructs SampleAncient
     * @param source source of the sample
     * @param id local id of sample
     * @param gedmatchID gedmatch id of sample
     * @param type calculator type used
     * @param ethnicity ethnicity of sample owner
     * @param percentages ethnicity percentages
     * @param mtDNA maternal haplogroup of sample
     * @param yDNA paternal haplogroup of sample
     * @param timePeriod time period sample refers to
     */
    public SampleAncient(String source, String id, String gedmatchID,
                         String type, String ethnicity,
                         ArrayList<Double> percentages, String mtDNA,
                         String yDNA, String timePeriod) {
        super(source, id, gedmatchID, type, ethnicity, percentages, mtDNA, yDNA);
        setTimePeriod(timePeriod);
        ALL_SAMPLES.set(ALL_SAMPLES.indexOf(this), this);
    }

    /**
     * @return String type of the ancient sample
     */
    @Override
    public String getSampleType() {
        return "Ancient Sample";
    }

    // getter method
    public String getTimePeriod() { return timePeriod; }

    // setter method
    public void setTimePeriod(String timePeriod) { this.timePeriod = timePeriod; }

    /**
     * @return String representation of SampleAncient
     */
    @Override
    public String toString() {
        return super.toString().substring(0, super.toString().length() - 1)
                + ", timePeriod: " + timePeriod + "]";
    }
}
