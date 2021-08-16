package engine;

import java.util.*;
import java.util.stream.Collectors;

import backend.DatabaseAccess;
import backend.Sample;

/**
 * Distance measures a theoretical distance from target to all database source samples.
 *
 * It outputs a list of source samples with their respective distance to target in
 * ascending distance order.
 *
 * @author Arastun Mammadli
 */
public class Distance {
    // target: sample to find distances to
    private Sample target;

    /**
     * Constructs Distance assigning the target
     * @param target sample to assign as target
     */
    public Distance(Sample target) { setTarget(target); }

    /**
     * builds a distance map containing all source samples and their distance to target
     * @param databaseReach database to refer to in calculations
     * @return build distance map
     */
    public Map<String, Double> getDistanceMap(ArrayList<Sample> databaseReach) {
        Map<String, Double> distanceMap = new HashMap<>();
        for (Sample sample : databaseReach) {
            if (sample != target) {
                distanceMap.put(sample.getSource() + ", " + sample.getEthnicity(),
                        getDistanceTo(sample.getPercentages().toArray(new Double[0])));
            }
        } return distanceMap;
    }

    /**
     * builds a distance map and sorts it in ascending distance order
     * @param databaseReach database to refer to in calculations
     * @param top top X samples to sort
     * @return sorted distance map
     */
    public Map<String, Double> getTopDistances(ArrayList<Sample> databaseReach, int top) {
        Map<String, Double> distanceMap = getDistanceMap(databaseReach);
        return distanceMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
                .limit(top)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    /**
     * calculates distance from target to given source sample
     * @param sourcePercentages percentages of source sample
     * @return calculated distance
     */
    public double getDistanceTo(Double[] sourcePercentages) {
        Double[] targetDouble = target.getPercentages().toArray(new Double[0]);
        Double[] array = subArray(targetDouble, sourcePercentages);
        array = squareArray(array);
        return Math.sqrt(arraySum(array));
    }

    // AUXILIARY METHODS
    // addArray:  adds the two given double arrays
    public static Double[] addArray(Double[] list1, Double[] list2) {
        Double[] listNew = new Double[list1.length];
        for (int i = 0; i < list1.length; i++) {
            listNew[i] = list1[i] + list2[i];
        } return listNew;
    }
    // divArray: divides the double array by given divisor
    public static Double[] divArray(Double[] list, double divisor) {
        Double[] listNew = new Double[list.length];
        for (int i = 0; i < list.length; i++) {
            listNew[i] = list[i] / divisor;
        } return listNew;
    }
    // subArray: subtracts the two given double arrays
    public static Double[] subArray(Double[] list1, Double[] list2) {
        Double[] listNew = new Double[list1.length];
        for (int i = 0; i < list1.length; i++) {
            listNew[i] = list1[i] - list2[i];
        } return listNew;
    }
    // mulArray: multiplies the double array by given factor
    public static Double[] mulArray(Double[] list1, double factor) {
        Double[] listNew = new Double[list1.length];
        for (int i = 0; i < list1.length; i++) {
            listNew[i] = list1[i] * factor;
        } return listNew;
    }
    // squareArray: squares the double array
    public static Double[] squareArray(Double[] list) {
        Double[] listNew = new Double[list.length];
        for (int i = 0; i < list.length; i++) {
            listNew[i] = list[i] * list[i];
        } return listNew;
    }
    // arraySum: sums the double array
    public static double arraySum(Double[] list) {
        double sum = 0;
        for (double element : list) { sum += element; }
        return sum;
    }

    // getter method
    public Sample getTarget() { return target; }

    // setter method
    public void setTarget(Sample target) { this.target = target; }

    // testing
    public static void main(String[] args) {
        DatabaseAccess.loadDatabase();
        Sample target = Sample.ALL_SAMPLES.get(0);
        Distance distance = new Distance(target);
        System.out.println(distance.getDistanceMap(Sample.ALL_SAMPLES));
        DistanceMixed distanceMixed = new DistanceMixed(target);
        System.out.println(distanceMixed.getDistanceMap(Sample.ALL_SAMPLES));
    }
}
