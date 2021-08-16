package backend.data;

import java.awt.Color;
import java.util.ArrayList;

/**
 * CalculatorData contains Gedmatch calculator data (breakdown / color scheme).
 * App supports 4 Gedmatch calculators.
 */
public class CalculatorData {
    // calculatorTypes: String names of calculators supported by the app
    public static String[] calculatorTypes = new String[]
            {"Dodecad K12b", "Ancient Eurasia K6", "Eurogenes K13", "MDLP World"};

    // calculator pie colors
    private static final Color dodecadGedrosia = new Color(254,0,0);
    private static final Color dodecadSiberian = new Color(253,129,2);
    private static final Color dodecadNorthwestAfrican = new Color(191,126,63);
    private static final Color dodecadSoutheastAsian = new Color(253,253,9);
    private static final Color dodecadAtlanticMed = new Color(194,192,65);
    private static final Color dodecadNorthEuropean = new Color(202,255,127);
    private static final Color dodecadSouthAsian = new Color(62,193,59);
    private static final Color dodecadEastAfrican = new Color(130,255,207);
    private static final Color dodecadSouthwestAsian = new Color(64,192,192);
    private static final Color dodecadEastAsian = new Color(127,134,252);
    private static final Color dodecadCaucasus = new Color(190,64,195);
    private static final Color dodecadSubSaharan = new Color(250,113,198);

    private static final Color eurogenesSubSaharan = new Color(255,129,129);
    private static final Color eurogenesNorthEastAfrican = new Color(232,129,255);

    // dodecadK12bRegions: String regions of Dodecad K12b
    private static final String[] dodecadK12bRegions = new String[]{"Gedrosia", "Siberian", "Northwest African",
            "Southeast Asian", "Atlantic Med", "North European", "South Asian", "East African",
            "Southwest Asian", "East Asian", "Caucasus", "Sub Saharan"};
    // ancientEurasiaK6: String regions of Ancient Eurasia K6
    private static final String[] ancientEurasiaK6 = new String[]{"Ancestral North Eurasian",
            "Ancestral South Eurasian", "East Asian",
            "West European Hunter Gatherer", "Natufian", "Sub Saharan"};
    // eurogenesK13: String regions of Eurogenes K13
    private static final String[] eurogenesK13 = new String[]{"North Atlantic", "Baltic",
            "West Med", "West Asian", "East Med", "Red Sea", "South Asian", "East Asian", "Siberian",
            "Amerindian", "Oceanian", "Northeast African", "Sub Saharan"};
    // mdlpWorld: String regions of MDLP World
    private static final String[] mdlpWorld = new String[]{"Caucasus Parsia", "Middle East",
            "Indian", "South and West European", "Melanesian", "Sub Saharian", "North and East European",
            "Arctic Amerind", "East Asian", "Paleo African", "Mesoamerican", "North Asian"};

    // dodecadK12bColors: Color complexion of Dodecad K12b regions
    private static final Color[] dodecadK12bColors = new Color[]{dodecadGedrosia, dodecadSiberian,
            dodecadNorthwestAfrican, dodecadSoutheastAsian, dodecadAtlanticMed, dodecadNorthEuropean,
            dodecadSouthAsian, dodecadEastAfrican, dodecadSouthwestAsian, dodecadEastAsian,
            dodecadCaucasus, dodecadSubSaharan};
    // ancientEurasiaK6Colors: Color complexion of Ancient Eurasia K6 regions
    private static final Color[] ancientEurasiaK6Colors = new Color[]{dodecadGedrosia, dodecadSiberian,
            dodecadNorthwestAfrican, dodecadSoutheastAsian, dodecadAtlanticMed, dodecadNorthEuropean};
    // eurogenesK13Colors: Color complexion of Eurogenes K13 regions
    private static final Color[] eurogenesK13Colors = new Color[]{dodecadGedrosia, dodecadSiberian,
            dodecadNorthwestAfrican, dodecadSoutheastAsian, dodecadAtlanticMed, dodecadNorthEuropean,
            dodecadSouthAsian, dodecadEastAfrican, dodecadSouthwestAsian, dodecadEastAsian, dodecadCaucasus,
            eurogenesNorthEastAfrican, eurogenesSubSaharan};
    // mdlpWorldColors: Color complexion of MDLP World regions
    private static final Color[] mdlpWorldColors = new Color[]{dodecadGedrosia, dodecadSiberian,
            dodecadNorthwestAfrican, dodecadSoutheastAsian, dodecadAtlanticMed, dodecadNorthEuropean,
            dodecadSouthAsian, dodecadEastAfrican, dodecadSouthwestAsian, dodecadEastAsian,
            dodecadCaucasus, eurogenesNorthEastAfrican};

    /**
     * @param calculatorType calculator type to refer to
     * @return region list of given calculator type
     */
    public static String[] getRegionList(String calculatorType) {
        switch (calculatorType) {
            case "Dodecad K12b" -> { return dodecadK12bRegions; }
            case "Ancient Eurasia K6" -> { return ancientEurasiaK6; }
            case "Eurogenes K13" -> { return eurogenesK13; }
            case "MDLP World" -> { return mdlpWorld; }
            default -> { return new String[0]; }
        }
    }

    /**
     * @param calculatorType calculator type to refer to
     * @return color scheme / list of given calculator type
     */
    public static Color[] getColorList(String calculatorType) {
        switch (calculatorType) {
            case "Dodecad K12b" -> { return dodecadK12bColors; }
            case "Ancient Eurasia K6" -> { return ancientEurasiaK6Colors; }
            case "Eurogenes K13" -> { return eurogenesK13Colors; }
            case "MDLP World" -> { return mdlpWorldColors; }
            default -> { return new Color[0]; }
        }
    }

    /**
     * converts percentages from String array to ArrayList Double
     * @param percentages percentage array to convert
     * @return ArrayList of converted percentages
     */
    public static ArrayList<Double> convertPercentages(String[] percentages) {
        ArrayList<Double> convertedPercentages = new ArrayList<>();
        for (String percentage : percentages) {
            try {
                convertedPercentages.add(Double.parseDouble(percentage.replaceAll("\\s", "")));
            } catch (NumberFormatException ignored) { return null; }
        } return convertedPercentages;
    }

    /**
     * fixes given percentages by the error margin
     * @param percentages ArrayList of percentages to fix
     * @param errorMargin error margin to apply
     * @return fixed percentages
     */
    public static ArrayList<Double> fixPercentage(ArrayList<Double> percentages, double errorMargin) {
        ArrayList<Double> fixedPercentages = new ArrayList<>();
        double indError = errorMargin / percentages.size();
        for (double percentage : percentages) { fixedPercentages.add(percentage - indError); }
        return fixedPercentages;
    }

    /**
     * rounds given double values
     * @param input input to round
     * @param decimalPlace rounding error
     * @return rounded value
     */
    public static double round(double input, int decimalPlace) {
        double roundValue = 10d * decimalPlace;
        return (double)Math.round(input * roundValue) / roundValue;
    }
    public static Double[] round(Double[] input, int decimalPlace) {
        for (int i = 0; i < input.length; i++) {
            input[i] = round(input[i], decimalPlace);
        } return input;
    }
}
