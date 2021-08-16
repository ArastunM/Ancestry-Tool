package backend;

import java.io.Serializable;
import java.util.ArrayList;

import backend.data.CalculatorData;
import exceptions.SampleNotFoundException;

/**
 * Sample accommodates ancestry sample kit related data (instances) and methods.
 * It is used as a superclass of SampleModern, SampleAncient and SampleAverage.
 */
public class Sample implements Serializable {
    // source: source of the sample
    private String source;
    // id: id assigned to sample by local App
    private String id;

    // type: calculator type of the sample
    private String type;
    // ethnicity: ethnicity of the sample owner
    private String ethnicity;
    // percentages: ethnicity percentages of the sample using {type} calculator
    private ArrayList<Double> percentages;

    // ALL_SAMPLES: ArrayList of all added samples
    public static ArrayList<Sample> ALL_SAMPLES = new ArrayList<>();

    /**
     * Constructs the Sample
     * @param source source of the sample
     * @param id local id of the sample
     * @param ethnicity ethnicity of sample owner
     */
    public Sample(String source, String id, String ethnicity) {
        setSource(source);
        this.id = id == null ? generateID() : id;
        setEthnicity(ethnicity);
        ALL_SAMPLES.add(this);
    }

    /**
     * removes sample from the database
     */
    public void remove() {
        ALL_SAMPLES.remove(this);
        DatabaseAccess.saveDatabase();
    }

    /**
     * @return String type of the sample
     */
    public String getSampleType() {
        return "Sample";
    }

    /**
     * @return sample percentages in table contents format
     */
    public String[][] getPercentagesAsTable() {
        String[] regionList = CalculatorData.getRegionList(getType());
        String[][] percentages = new String[getPercentages().size()][2];

        for (int i = 0; i < percentages.length; i++) {
            percentages[i][0] = regionList[i];
            percentages[i][1] = String.valueOf(CalculatorData.round
                    (getPercentages().get(i), 2));
        }
        return percentages;
    }

    /**
     * generates random alphanumeric String id
     * @return generated id
     */
    public static String generateID() {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }

        String genID = sb.toString();
        return isUniqueID(genID) ? genID : generateID();
    }

    /**
     * checks if given id is unique within the system
     * @param id id to refer to
     * @return true if given id is unique, false otherwise
     */
    public static boolean isUniqueID(String id) {
        for (Sample sample : ALL_SAMPLES) {
            if (sample.getId().equals(id)) { return false; }
        } return true;
    }

    /**
     * @param id id to refer to
     * @return sample with given id
     */
    public static Sample getSample(String id) {
        for (Sample sample : ALL_SAMPLES) {
            if (sample.getId().equals(id)) { return sample; }
        } throw new SampleNotFoundException(id);
    }

    /**
     * @param ids ids to refer to
     * @return ArrayList of samples with given ids
     */
    public static ArrayList<Sample> getSamples(String[] ids) {
        ArrayList<Sample> matchingSamples = new ArrayList<>();
        for (String id : ids) {
            Sample sample = getSample(id);
            if (sample != null) { matchingSamples.add(sample); }
        } return matchingSamples;
    }

    /**
     * @param type sample or calculator type to refer to
     * @return ArrayList of samples with given type
     */
    public static ArrayList<Sample> getSamples(String type) {
        return getSamples(type, ALL_SAMPLES);
    }

    /**
     * @param type sample or calculator type to refer to
     * @param searchFrom database to search from
     * @return ArrayList of samples matching given conditions
     */
    public static ArrayList<Sample> getSamples(String type, ArrayList<Sample> searchFrom) {
        ArrayList<Sample> samples = new ArrayList<>();

        for (Sample sample : searchFrom) {
            switch (type) {
                case "Modern Sample" -> { if (sample instanceof SampleModern
                        && !(sample instanceof SampleAncient)) { samples.add(sample); }}
                case "Ancient Sample" -> { if (sample instanceof SampleAncient) { samples.add(sample); }}
                case "Sample Average" -> { if (sample instanceof SampleAverage) { samples.add(sample); }}
                case "Dodecad K12b" ->
                        { if (sample.getType().equals("Dodecad K12b")) { samples.add(sample); }}
                case "Ancient Eurasia K6" ->
                        { if (sample.getType().equals("Ancient Eurasia K6")) { samples.add(sample); }}
                case "Eurogenes K13" ->
                        { if (sample.getType().equals("Eurogenes K13")) { samples.add(sample); }}
                case "MDLP World" ->
                        { if (sample.getType().equals("MDLP World")) { samples.add(sample); }}
            }
        } return samples;
    }

    // getter methods
    public String getSource() { return source; }
    public String getId() { return id; }
    public String getType() { return type; }
    public String getEthnicity() { return ethnicity; }
    public ArrayList<Double> getPercentages() { return percentages; }

    // setter methods
    public void setSource(String source) { this.source = source; }
    public void setId(String id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setEthnicity(String ethnicity) { this.ethnicity = ethnicity; }
    public void setPercentages(ArrayList<Double> percentages) { this.percentages = percentages; }

    /**
     * @return String representation of sample
     */
    @Override
    public String toString() {
        return "[name: " + source + ", id: " + id + ", type: "
                + type + ", ethnicity: " + ethnicity;
    }
}
