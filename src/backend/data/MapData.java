package backend.data;

import java.awt.*;
import java.util.Arrays;
import java.util.Locale;

import backend.Sample;

/**
 * MapData contains SignalMap regions, region keyword, coordinates and values
 *
 * @see gui.components.SignalMap
 */
public class MapData {
    // background: SignalMap background map image
    public static Image background;

    // northAmericanKeyWords: contains keywords of north america region
    public static String[] northAmericaKeyWords = new String[]
            {"america", "amerindian", "alaska"};
    // southAmericanKeyWords: contains keywords of south america region
    public static String[] southAmericaKeyWords = new String[]
            {"america", "amerindian", "mesoamerica", "amazonian"};
    // africaKeyWords: contains keywords of africa region
    public static String[] africaKeyWords = new String[]
            {"africa", "african", "khoisan", "igbo", "bantu", "fula",
                    "yoruba", "san", "nilotic", "hamitic", "berber",
                    "hausa", "guinean", "afrikaaner", "malagasy", "algeria",
                    "ethiopian", "moroccan", "somali"};
    // europeKeyWords: contains keywords of europe region
    public static String[] europeKeyWords = new String[]
            {"english", "scottish", "irish", "welsh", "german",
                    "french", "italian", "spanish", "portuguese",
                    "hungarian", "polish", "slovak", "serb", "bosnian",
                    "croatian", "greek", "russian", "ukrainian", "swedish",
                    "danish", "finnish", "norwegian", "balkan", "romanian",
                    "bulgarian", "albania", "pol", "belgium", "dutch", "austria",
                    "swiss", "latvia", "fin", "belgian", "rus", "tatar", "czech",
                    "estonian", "iceland", "karelia", "macedonia", "moldovan",
                    "mordovian", "pomak", "sardinian", "mari", "croat", "sloven"};
    // eastAsiaKeyWords: contains keywords of east asia region
    public static String[] eastAsiaKeyWords = new String[]
            {"chinese", "korean", "japanese", "altai", "mongol",
                    "kazakh", "uzbek", "krygyz", "tibet", "uyghur",
                    "han", "dai", "manchurian", "udmurt", "buryat",
                    "kyrgyz", "nepal", "yakut", "tuvan", "pamir",
                    "nogai", "kumyk", "hazara", "tubalar"};
    // westAsiaKeyWords: contains keywords of west asia region
    public static String[] westAsiaKeyWords = new String[]
            {"turkish", "turk", "turkmen", "azeri", "azerbaijan",
                    "armenian", "georgian", "svan", "iranian", "persian",
                    "kurd", "caucasus", "anatolia", "mesopotamia", "iraq",
                    "arab", "syrian", "israeli", "oman", "Yemen", "tajik",
                    "jew", "abazin", "abkhazian", "adyghe", "avar", "assyrian",
                    "Jew", "balkar", "bashkir", "bedouin", "chechen", "chuvash",
                    "circass", "dargin", "hemshin", "ingush", "jordan", "kabardin",
                    "karachay", "kalmyk", "lebanese", "lezgin", "ossetian",
                    "palestin", "saudi", "laz", "yemen", "mandean", "kuwait", "lak",
                    "karakalpak", "egypt"};
    // southAsiaKeyWords: contains keywords of south asia region
    public static String[] southAsiaKeyWords = new String[]
            {"pakistan", "india", "afghan", "sri Lanka", "bangladesh",
                    "balochi", "brahmin", "bhunjia", "gypsy", "pashtun",
                    "hindu", "tamil", "punjab", "pradesh"};
    // oceaniaKeyWords: contains keywords of oceania region
    public static String[] oceaniaKeyWords = new String[]
            {"myanmar", "vietnam", "singapore", "indonesia", "thai",
                    "philippine", "australia", "new Zealand",
                    "melanesian", "new guinean", "cambodia", "malay"};

    // mapKeyWords: contains a keyword list of all regions
    public static String[][] mapKeyWords = new String[][]
            {northAmericaKeyWords, southAmericaKeyWords, africaKeyWords, europeKeyWords,
                    eastAsiaKeyWords, westAsiaKeyWords, southAsiaKeyWords, oceaniaKeyWords};

    /**
     * @param width map width
     * @param height map height
     * @return region coordinates based on map dimensions
     */
    public static Integer[][] getMapRegionCoordinates(int width, int height) {
        return new Integer[][]{
                {(int) (width / 5.257), (int) (height / 1.852)}, {(int) (width / 3.468), (int) (height / 1.218)},
                {(int) (width / 1.84), (int) (height / 1.3228)}, {(int) (width / 1.989), (int) (height / 2.204)},
                {(int) (width / 1.338), (int) (height / 1.852)}, {(int) (width / 1.67), (int) (height / 1.815)},
                {(int) (width / 1.443), (int) (height / 1.596)}, {(int) (width / 1.206), (int) (height / 1.286)}
        };
    }

    /**
     * calculates map region values based on local database
     * @return list of map region values
     */
    public static Integer[] getMapRegionValues() {
        Integer[] mapRegionValues = new Integer[mapKeyWords.length];
        Arrays.fill(mapRegionValues, 0);
        int result;
        for (Sample sample : Sample.ALL_SAMPLES) {
            result = getSampleCategory(sample);
            if (result != -1) {
                mapRegionValues[result] += 1;
            }
        }
        return mapRegionValues;
    }

    /**
     * @param sample sample to refer to
     * @return map region category of given sample (as index)
     */
    private static int getSampleCategory(Sample sample) {
        for (int i = 0; i < mapKeyWords.length; i++) {
            for (int j = 0; j < mapKeyWords[i].length; j++){
                if (sample.getEthnicity().toLowerCase(Locale.ROOT).contains(mapKeyWords[i][j])) {
                    return i;
                }
            }
        }
        return -1;
    }
}
