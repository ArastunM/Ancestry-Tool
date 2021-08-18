package backend;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

import backend.data.CalculatorData;
import gui.components.CalculatorInput;
import gui.windows.*;

/**
 * DatabaseAccess manages interactions with the local Database
 */
public class DatabaseAccess {
    // accessStatus: indicates the communication status with local database
    public static int accessStatus = 500;
    // resourcePath: path to database file
    public static String resourcePath = "database/database.ser";

    /**
     * Clears up the Database
     */
    public static void cleanDatabase() {
        Sample.ALL_SAMPLES.clear();
        saveDatabase();
    }

    /**
     * Saves current sample data to Database
     */
    public static void saveDatabase() {
        // serializing all samples
        try (ObjectOutputStream out = new ObjectOutputStream
                (new FileOutputStream(resourcePath))) {
            out.writeObject(Sample.ALL_SAMPLES);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves sample data from Database
     */
    public static void loadDatabase() {
        // deserializing all samples
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(resourcePath))) {
            Sample.ALL_SAMPLES = (ArrayList<Sample>) in.readObject();
            accessStatus = 200;
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds Samples to the Database according to Database Add Window inputs
     * @return status code of operation
     */
    public static int databaseAdd() {
        // status codes:
        // 400: Bad Request
        // 200: Modern Sample Request
        // 201: Ancient Sample Request
        // 202: Sample Average Request

        JTextField[] fields = DatabaseAddWindow.metaPanelFields;
        String[] fieldText = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldText[i] = fields[i].getText().equals
                    (DatabaseAddWindow.metaPanelFieldHeaders[i]) ? "N/A" : fields[i].getText().strip();
        }

        String type = (String) DatabaseAddWindow.calcTypeBtn.getSelectedItem();
        ArrayList<Double> percentages = new ArrayList<>();
        for (int i = 0; i < CalculatorInput.addedFields.size(); i++) {
            try {
                double fieldValue = Double.parseDouble(CalculatorInput.addedFields.get(i).getText());
                percentages.add(fieldValue);
            } catch (NumberFormatException ignored) { percentages.add(0.0); }
        }

        if (fields[0].getText().equals("N/A") || fields[0].getText().equals("")
                || CalculatorInput.findPercentageSum() != 100.0) { return 400; }

        if (fieldText.length == 5) {
            new SampleModern(fieldText[0], null, fieldText[1], type,
                    fieldText[2], percentages, fieldText[3], fieldText[4]);
            saveDatabase();
            return 200;

        } else {
            new SampleAncient(fieldText[0], null, fieldText[1], type,
                    fieldText[2], percentages, fieldText[3], fieldText[4], fieldText[5]);
            saveDatabase();
            return 201;
        }
    }

    /**
     * Adds Sample Averages to the Database according to Database Add Window inputs
     * @return status code of operation
     */
    public static int databaseAddAverage() {
        String name = DatabaseAddWindow.metaPanelFields[0].getText().equals("Owner Source")
                ? "N/A" : DatabaseAddWindow.metaPanelFields[0].getText().strip();
        String ethnicity = DatabaseAddWindow.metaPanelFields[2].getText().equals("Ethnicity")
                ? "N/A" : DatabaseAddWindow.metaPanelFields[2].getText().strip();

        String[] ids = DatabaseAddWindow.componentSelected.getText().split(" ");
        ArrayList<Sample> matchingSamples = Sample.getSamples(ids);

        if (name.equals("N/A") || name.equals("") || matchingSamples.size() == 0
                || !SampleAverage.areValidComponents(matchingSamples)) { return 400; }
        new SampleAverage(name, null, ethnicity, matchingSamples);
        saveDatabase();
        return 200;
    }

    /**
     * Loads Samples to the Database according to Upload Window input
     * @return status code of operation
     */
    public static int databaseUpload() {
        int statusCode = 400;

        String sourceName = UploadWindow.sourceField.getText().equals("Upload Source") ?
                "Upload" : UploadWindow.sourceField.getText();
        String suffix = UploadWindow.suffixField.getText().equals("Suffix to add to all Ethnicities (Optional)") ?
                "" : UploadWindow.suffixField.getText();

        String calculatorType = (String) UploadWindow.uploadFormatBtn.getSelectedItem();
        int sampleType = UploadWindow.sampleTypeBtn.getSelectedIndex();
        assert calculatorType != null;
        String[] regionList = CalculatorData.getRegionList(calculatorType);
        String[] potentialSamples = UploadWindow.inputArea.getText().strip().split("\n");

        for (String string : potentialSamples) {
            String[] potentialSample = string.split(",");
            String[] potentialPercentages = Arrays.copyOfRange(potentialSample, 1, potentialSample.length);
            ArrayList<Double> percentages = CalculatorData.convertPercentages(potentialPercentages);
            assert percentages != null;
            double percentageSum = percentages.stream().mapToDouble(a -> a).sum();
            double errorMargin = percentageSum - 100;
            if (Math.abs(errorMargin) > 5) { return 400; }

            percentages = CalculatorData.fixPercentage(percentages, errorMargin);
            percentageSum = percentages.stream().mapToDouble(a -> a).sum();
            double percentageRound = CalculatorData.round(percentageSum, 2);

            if (percentages.size() == regionList.length && percentageRound == 100) {
                if (sampleType == 0) {
                    new SampleModern(sourceName, null, "N/A", calculatorType,
                            potentialSample[0] + suffix, percentages, "N/A", "N/A");
                } else if (sampleType == 1) {
                    new SampleAncient(sourceName, null, "N/A", calculatorType,
                            potentialSample[0] + suffix, percentages, "N/A",
                            "N/A", "N/A");
                }
                statusCode = 200;
            } else { return 400; }

        }
        saveDatabase();
        return statusCode;
    }
}
