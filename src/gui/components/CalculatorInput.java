package gui.components;

import backend.data.CalculatorData;
import gui.DefaultObjects;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * CalculatorInput is used to construct a custom calculator input panel
 */
public class CalculatorInput extends JPanel {
    // addedFields: list of text fields containing calculator percentage inputs
    public static ArrayList<JTextField> addedFields = new ArrayList<>();
    // fieldSum: contains sum of added fields
    public static JLabel fieldSum;

    /**
     * Retrieves an input interface based on given calculator
     * @param calculatorType calculator type to refer to
     */
    public CalculatorInput(String calculatorType) {
        addedFields.clear();

        // assigning regionList to use
        String[] regionList = CalculatorData.getRegionList(calculatorType);
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new GridLayout(regionList.length, 0));

        for (String regionName : regionList) {
            JTextField field = DefaultObjects.getPercentageTF(regionName);
            addedFields.add(field);
            subPanel.add(field);
        }

        this.setLayout(new BorderLayout());
        fieldSum = DefaultObjects.getCenteredLB("Percentage Sum: ");
        updateFieldSum();
        this.add(fieldSum, BorderLayout.NORTH);
        this.add(new JScrollPane(subPanel));
    }

    /**
     * @return percentage sum of entered percentage input fields
     */
    public static double findPercentageSum() {
        double totalSum = 0;
        for (JTextField field : addedFields) {
            try {
                double fieldValue = Double.parseDouble(field.getText());
                totalSum += fieldValue;
            } catch (NumberFormatException ignored) {}
        } return totalSum;
    }

    /**
     * updates field sum values of CalculatorInput
     */
    public static void updateFieldSum() {
        double sum = findPercentageSum();
        fieldSum.setText("Percentage Sum: " + sum);
        if (sum == 100.0) { fieldSum.setForeground(Color.GREEN); }
        else if (sum > 100.0) { fieldSum.setForeground(Color.RED); }
        else { fieldSum.setForeground(DefaultObjects.BTN_FOREGROUND_COLOR); }
    }
}
