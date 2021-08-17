package engine;

import java.util.*;

import backend.data.CalculatorData;
import backend.DatabaseAccess;
import backend.Sample;

/**
 * Single measures what a sample would theoretically consist of based on database source samples.
 *
 * It outputs the list of ethnicities that would compose the target sample with their share
 * in the composition as well as the distance to target of given composition.
 *
 * The user is also allowed to set the number of cycles performed.
 *
 * Vahaduo Single script [https://github.com/vahaduo/vahaduo/blob/master/index.html]
 * was referred to in this Class.
 *
 * @author Arastun Mammadli
 */
public class Single {
    // target: sample to build the composition of
    private Sample target;
    // generalDistance: distance from target to built composition
    private double generalDistance;

    // cycles: amount of cycles upon distance to point optimization
    public int cycles = 100; // default
    // dimensionNum: length of region list of target's calculator
    public int dimensionNum;
    // slotCount: the maximum number of samples making up the composition allowed
    // (Database size would be a good fit)
    public int slotCount = 500; // default

    // currentSlots: is an integer array where each slot represents an index of source sample
    public Integer[] currentSlots;
    // currentPoint: is a double array which represents the average of current composition
    // currentPoint should be the size of dimNum and is built based on current slots
    // currentPoint is used to measure the distance to current composition
    public Double[] currentPoint;
    // currentDistance: is the distance to current composition, distance is found by measuring
    // the currentDistance from target to point (current composition average)
    public double currentDistance;

    // ranking: is a Map that represents the ranking of each source sample based on their scores
    // the key of ranking would represent the index of the sample and value would represent the score of sample
    // ranking should be sorted in the descending order of scores of samples (Map values)
    public LinkedHashMap<Integer, Integer> ranking;

    /**
     * Constructs Single assigning the target
     * @param target sample to assign as target
     */
    public Single(Sample target) { this.target = target; }
    /**
     * Constructs Single assigning the target and cycles
     * @param target sample to assign as target
     * @param cycleX double to multiply by default cycle count
     */
    public Single(Sample target, double cycleX) {
        this.target = target;
        cycles *= cycleX;
    }

    /**
     * builds a sorted single map of calculated composition for target
     * @param databaseReach database to refer to when calculating the composition
     * @return sorted LinkedHashMap of the composition
     */
    public LinkedHashMap<String, Double> getSingleMap(ArrayList<Sample> databaseReach) {
        // setting up local variables
        databaseReach.remove(target); // removing target from source
        ArrayList<Double[]> sourcePercentages = getFixedSourcePercentages(databaseReach);
        Double[] target = getFixedTargetPercentages();
        dimensionNum = CalculatorData.getRegionList(this.target.getType()).length;
        LinkedHashMap<String, Double> singleMap = new LinkedHashMap<>();
        LinkedHashMap<String, Double> sortedSingleMap = new LinkedHashMap<>();

        // obtaining the composition score using fast monte carlo method
        Double[] scores = fastMonteCarloMethod(sourcePercentages, target);
        generalDistance = getGeneralDistance(databaseReach, scores);

        // assigning score values to the map
        for (int i = 0; i < databaseReach.size(); i++) {
            if (scores[i] > 0) {
                singleMap.put(databaseReach.get(i).getSource() + ", "
                                + databaseReach.get(i).getEthnicity(),
                        CalculatorData.round(scores[i] * 100, 3));
            }
        }

        // sorting the built map in descending order
        singleMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedSingleMap.put(x.getKey(), x.getValue()));
        return sortedSingleMap;
    }

    /**
     * fastMonteCarloMethod is a Vahaduo Single calculator method,
     * it was broadly reWritten from the above sourced script.
     *
     * @param sourcePercentages ArrayList of all source percentages in fixed form
     * @param targetPercentages target percentages in fixed form
     * @return array of scores representing built single composition
     */
    public Double[] fastMonteCarloMethod(ArrayList<Double[]> sourcePercentages, Double[] targetPercentages) {
        // step 1: subtracting target percentages from all source percentages
        for (int i = 0; i < sourcePercentages.size(); i++) {
            sourcePercentages.set(i, Distance.subArray(sourcePercentages.get(i), targetPercentages));
        }

        // step 2: initialising slots, point and distance
        currentSlots = new Integer[slotCount]; Arrays.fill(currentSlots, -1);
        currentSlots = randomizedSlots(currentSlots, sourcePercentages.size());
        currentPoint = buildPoint(sourcePercentages, currentSlots);
        currentDistance = distance(currentPoint);

        // step 3: optimizing distance to currentPoint, currentSlots
        optimizePointDistance(sourcePercentages);

        // step 4: initiating scores values and ranking
        // scores array is filled with scores of each source sample, representing how many
        // times these samples have been referred to from currentSlots
        Integer[] scores = new Integer[sourcePercentages.size()];
        Arrays.fill(scores, 0);
        for (int i = 0; i < slotCount; i++) {
            scores[currentSlots[i]] += 1;
        }

        // step 5: initiating ranking map and sorting it
        ranking = new LinkedHashMap<>();
        for (int i = 0; i < sourcePercentages.size(); i++) {
            if (scores[i] > 0) {
                ranking.put(i, scores[i]);
            }
        } sortRanking();

        // step 6: optimizing using the second method
        optimization2(sourcePercentages);

        // step 7: assigning final scores
        int rankingNum = ranking.size();
        Double[] lastScores = new Double[scores.length];
        for (int i = 0; i < scores.length; i++) { lastScores[i] = scores[i] * 1.0; }

        for (int i = 0; i < rankingNum; i++) {
            Integer iKey = (Integer) ranking.keySet().toArray()[i];
            lastScores[iKey] = ranking.get(iKey) * 1.0;
        }

        for (int i = 0; i < sourcePercentages.size(); i++) {
            lastScores[i] = lastScores[i] / slotCount;
        } return lastScores;
    }


    /**
     * By iterating through each slot in given number of cycles new randomized
     * slots (that represent a composition of samples) are created. The nextPoint is adjusted
     * to these slots which is used to calculate the nextDistance.
     *
     * If the nextDistance is smaller (better fit) than current distance we save changes:
     * (current slots = new slots, current point = new point, current distance = new distance)
     *
     * @param sourcePercentages source sample percentages to refer to
     */
    public void optimizePointDistance(ArrayList<Double[]> sourcePercentages) {
        Integer[] nextSlots;
        Double[] nextPoint;
        double nextDistance;

        for (int i = 0; i < cycles; i++) {
            nextSlots = randomizedSlots(currentSlots, sourcePercentages.size());
            for (int j = 0; j < slotCount; j++) {
                nextPoint = Distance.subArray(currentPoint, sourcePercentages.get(currentSlots[j]));
                nextPoint = Distance.addArray(nextPoint, sourcePercentages.get(nextSlots[j]));
                nextDistance = distance(nextPoint);
                // saving changes when new distance is better fit
                if (nextDistance < currentDistance) {
                    currentSlots[j] = nextSlots[j];
                    currentPoint = nextPoint;
                    currentDistance = nextDistance;
                }
            }
        }
    }

    /**
     * @param sourcePercentages source sample percentages to refer to
     */
    private void optimization2(ArrayList<Double[]> sourcePercentages) {
        long bigNumber = 100000000000000000L;
        int rankingNum = ranking.size();
        currentDistance = Math.round(bigNumber * currentDistance);

        Integer iKey, jKey;
        Double[] nextPoint;
        double previousDistance;
        double nextDistance;

        do {
            previousDistance = currentDistance;
            for (int i = rankingNum - 1; i > -1; i--) {
                iKey = (Integer) ranking.keySet().toArray()[i];
                if (ranking.get(iKey) > 0) {
                    for (int j = 0; j < rankingNum; j++) {
                        if (i == j) { continue; }
                        jKey = (Integer) ranking.keySet().toArray()[j];
                        nextPoint = Distance.subArray(currentPoint, sourcePercentages.get(iKey));
                        nextPoint = Distance.addArray(nextPoint, sourcePercentages.get(jKey));
                        nextDistance = Math.round(bigNumber * distance(nextPoint));

                        if (nextDistance < currentDistance) {
                            ranking.put(iKey, ranking.get(iKey) - 1);
                            ranking.put(jKey, ranking.get(jKey) + 1);
                            currentPoint = nextPoint;
                            currentDistance = nextDistance;
                            break;
                        }

                    }
                }
            }
        } while(currentDistance < previousDistance);
    }

    /**
     * @return fixed target percentages ( /500)
     */
    private Double[] getFixedTargetPercentages() {
        Double[] fixedTargetPercentages = target.getPercentages().toArray(new Double[0]);
        return Distance.divArray(fixedTargetPercentages, 500);
    }

    /**
     * @param source source percentages to fix
     * @return fixed source percentages ( /500)
     */
    private ArrayList<Double[]> getFixedSourcePercentages(ArrayList<Sample> source) {
        ArrayList<Double[]> fixedSourcePercentages = new ArrayList<>();
        for (Sample sample : source) {
            fixedSourcePercentages.add(Distance.divArray
                    (sample.getPercentages().toArray(new Double[0]), 500));
        } return fixedSourcePercentages;
    }


    /**
     * Created and assigns newSlots with random values from 0 to sourceNum
     * @param oldSlots previous slots to refer to
     * @param sourceNum number of source samples
     * @return new randomized slots
     */
    private Integer[] randomizedSlots(Integer[] oldSlots, int sourceNum) {
        Integer[] newSlots = new Integer[slotCount];
        for (int i = 0; i < slotCount; i++) {
            newSlots[i] = new Random().nextInt(sourceNum);
            while (newSlots[i].equals(oldSlots[i])) {
                newSlots[i] = new Random().nextInt(sourceNum);
            }
        } return newSlots;
    }

    /**
     * @param sourcePercentages source percentages to refer to
     * @param fromSlots slots to refer to
     * @return new point based on source percentages and slot values
     */
    private Double[] buildPoint(ArrayList<Double[]> sourcePercentages, Integer[] fromSlots) {
        Double[] tempLine;
        Double[] newPoint = new Double[dimensionNum];
        Arrays.fill(newPoint, 0.0);
        for (int i = 0; i < slotCount; i++) {
            tempLine = CalculatorData.round(sourcePercentages.get(fromSlots[i]), 5);
            newPoint = Distance.addArray(newPoint, tempLine);
        } return newPoint;
    }

    /**
     * @param fromPoint point to refer to
     * @return calculated distance from given point
     */
    private double distance(Double[] fromPoint) {
        Double[] distance = Distance.squareArray(fromPoint);
        return Distance.arraySum(distance);
    }

    /**
     * sorts ranking map in descending value order
     */
    private void sortRanking() {
        LinkedHashMap<Integer, Integer> sortedRanking = new LinkedHashMap<>();
        ranking.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedRanking.put(x.getKey(), x.getValue()));
        ranking = sortedRanking;
    }

    /**
     * @param databaseReach database user when calculating the composition
     * @param scores output scores to refer to
     * @return general distance to calculated composition
     */
    private double getGeneralDistance(ArrayList<Sample> databaseReach, Double[] scores) {
        Double[] averagePercentages = new Double[dimensionNum]; Arrays.fill(averagePercentages, 0.0);
        Double[] weightedPercentages;

        for (int i = 0; i < databaseReach.size(); i++) {
            if (scores[i] > 0) {
                weightedPercentages = Distance.mulArray
                        (databaseReach.get(i).getPercentages().toArray(new Double[0]), scores[i]);
                averagePercentages = Distance.addArray(averagePercentages, weightedPercentages);
            }
        }

        Distance distance = new Distance(target);
        return distance.getDistanceTo(averagePercentages);
    }

    // getter method
    public Sample getTarget() { return target; }
    public double getGeneralDistance() { return generalDistance; }

    // setter method
    public void setTarget(Sample target) { this.target = target; }
    public void setGeneralDistance(double generalDistance) { this.generalDistance = generalDistance; }

    // testing
    public static void main(String[] args) {
        DatabaseAccess.loadDatabase();
        Single single = new Single(Sample.ALL_SAMPLES.get(0));

        System.out.println(single.getSingleMap(Sample.ALL_SAMPLES));
    }
}
