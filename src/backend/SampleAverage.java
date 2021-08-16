package backend;

import backend.data.TableData;

import java.util.ArrayList;

/**
 * SampleAverage extends Sample, refers to several Samples grouped as one.
 *
 * Usually SampleAverage is used to represent several Samples of
 * one region or ethnicity.
 */
public class SampleAverage extends Sample {
    // components: sample components of sample average
    private ArrayList<Sample> components;

    /**
     * Constructs SampleAverage
     * @param source source of the sample
     * @param id id of sample average
     * @param ethnicity ethnicity making up sample average
     * @param components components of sample average
     */
    public SampleAverage(String source, String id, String ethnicity, ArrayList<Sample> components) {
        super(source, id, ethnicity);
        setType(components.get(0).getType());
        setComponents(components);
        setPercentages(calculatePercentage());
        ALL_SAMPLES.set(ALL_SAMPLES.indexOf(this), this);
    }

    /**
     * calculates percentages based on given components
     * @return calculated percentages
     */
    public ArrayList<Double> calculatePercentage() {
        ArrayList<Double> percentages = new ArrayList<>();
        // setting initial percentage values
        while (percentages.size() != components.get(0).getPercentages().size()) { percentages.add(0.0); }

        for (Sample component : components) {
            for (int i = 0; i < component.getPercentages().size(); i++) {
                percentages.set(i, percentages.get(i) + component.getPercentages().get(i));
            }
        }
        for (int i = 0; i < percentages.size(); i++) {
            percentages.set(i, percentages.get(i) / getComponents().size());
        } return percentages;
    }

    /**
     * removes a component of SampleAverage
     * @param component component to remove
     */
    public void removeComponent(Sample component) {
        this.components.remove(component);
        // updating percentages
        setPercentages(calculatePercentage());
        // saving changes to database
        DatabaseAccess.saveDatabase();
    }

    /**
     * adds a component to SampleAverage
     * @param component component to add
     */
    public void addComponent(SampleModern component) {
        this.components.add(component);
        // updating percentages
        setPercentages(calculatePercentage());
        // saving changes to database
        DatabaseAccess.saveDatabase();
    }

    /**
     * @return String type of the sample
     */
    @Override
    public String getSampleType() {
        return "Sample Average";
    }

    /**
     * @return sample average components as table content
     */
    public String[][] getComponentsAsTable() {
        String[][] componentsAsTable = new String[this.getComponents().size()][TableData.componentHeader.length];
        for (int i = 0; i < this.getComponents().size(); i++) {
            Sample sample = this.getComponents().get(i);
            componentsAsTable[i][0] = sample.getSource();
            componentsAsTable[i][1] = sample.getId();
            componentsAsTable[i][2] = sample.getEthnicity();
            componentsAsTable[i][3] = sample.getSampleType();
        } return componentsAsTable;
    }

    /**
     * Checks if given components are valid.
     *
     * To be valid components list must have a size of >0 and
     * sample within must be of same calculator, sample types
     * @param components list of samples / sample average components
     * @return true if given components are valid, false otherwise
     */
    public static boolean areValidComponents(ArrayList<Sample> components) {
        if (components.isEmpty()) { return false; }

        String calcType = components.get(0).getType();
        String sampleType = components.get(0).getSampleType();
        for (Sample component : components) {
            if (!component.getType().equals(calcType)
                    || !component.getSampleType().equals(sampleType)) {
                return false;
            }
        } return true;
    }

    // getter methods
    public ArrayList<Sample> getComponents() { return components; }

    // setter methods
    public void setComponents(ArrayList<Sample> components) { this.components = components; }

    /**
     * @return String representation of SampleAverage
     */
    @Override
    public String toString() {
        return super.toString() + ", components: " + components.toString() + "]";
    }
}
