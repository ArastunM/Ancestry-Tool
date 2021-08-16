package engine;

import java.util.*;
import java.util.stream.Collectors;

import backend.Sample;

/**
 * DistanceMixed is an extension of Distance used to obtain
 * a broadly mixed distance map to a given target.
 *
 * It averages the source samples (with ratio of 1:1 or 3:1) and
 * returns a map of top X list of distances in ascending order.
 *
 * @author Arastun Mammadli
 */
public class DistanceMixed extends Distance {
    /**
     * Constructs DistanceMixed assigning the target
     * @param target sample to assign as target
     */
    public DistanceMixed(Sample target) { super(target); }

    /**
     * builds a distance map containing all source sample mixes and their distance to target
     * @param databaseReach database to refer to in calculations
     * @return built distance map
     */
    public Map<String, Double> getDistanceMap(ArrayList<Sample> databaseReach) {
        Map<String, Double> mixedDistanceMap = new HashMap<>();
        String mixedEthnicities;
        Double[] mixedPercentages;
        double distance;

        for (int i = 0; i < databaseReach.size(); i++) {
            for (int j = i + 1; j < databaseReach.size(); j++) {
                if (databaseReach.get(i) == getTarget() || databaseReach.get(j) == getTarget()) {
                    break;
                }
                for (double m = 0.25; m < 1; m += 0.25) {
                    mixedEthnicities = databaseReach.get(i).getSource() + ", " +
                            (m * 100) + "% " + databaseReach.get(i).getEthnicity() + " &+* " +
                            (100 - m * 100) + "% " + databaseReach.get(j).getEthnicity();
                    mixedPercentages = getMixedPercentages
                            (databaseReach.get(i).getPercentages().toArray(new Double[0]),
                                    databaseReach.get(j).getPercentages().toArray(new Double[0]), m);
                    distance = getDistanceTo(mixedPercentages);
                    mixedDistanceMap.put(mixedEthnicities, distance);
                }
            }
        }
        return mixedDistanceMap;
    }

    /**
     * builds a mixed distance map and sorts it in ascending target order
     * @param databaseReach database to refer to in calculations
     * @param top top X samples to sort
     * @return built mixed distance map
     */
    @Override
    public Map<String, Double> getTopDistances(ArrayList<Sample> databaseReach, int top) {
        Map<String, Double> distanceMap = getDistanceMap(databaseReach);
        return distanceMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
                .limit(top)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    /**
     * obtains mixed percentages based on given sample percentages and weight
     * @param percentage1 first percentage array to consider
     * @param percentage2 second percentage array to consider
     * @param weight weight of mixed percentages
     * @return calculated mixed percentages
     */
    public Double[] getMixedPercentages(Double[] percentage1, Double[] percentage2, double weight) {
        Double[] mixedPercentages = percentage1;
        if (weight == 0.25) {
            mixedPercentages = addArray(percentage1, percentage2);
            mixedPercentages = addArray(mixedPercentages, percentage2);
            mixedPercentages = addArray(mixedPercentages, percentage2);
            mixedPercentages = divArray(mixedPercentages, 4);
        } else if (weight == 0.5) {
            mixedPercentages = addArray(percentage1, percentage2);
            mixedPercentages = divArray(mixedPercentages, 2);
        } else if (weight == 0.75) {
            mixedPercentages = addArray(percentage1, percentage2);
            mixedPercentages = addArray(mixedPercentages, percentage1);
            mixedPercentages = addArray(mixedPercentages, percentage1);
            mixedPercentages = divArray(mixedPercentages, 4);
        }
        return mixedPercentages;
    }
}
