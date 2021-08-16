package gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

import app.App;
import backend.data.DistanceData;
import gui.components.CalculatorInput;
import gui.components.SignalMap;
import gui.windows.DatabaseWindow;

/**
 * DefaultObjects contains customised java swing default object generating methods
 */
public class DefaultObjects {
    // BTN_FONT: default font for large buttons
    public static final Font BTN_FONT = new Font("Arial", Font.PLAIN, 14);
    // BTN_COLOR: default color for buttons
    public static Color BTN_COLOR;
    // FOREGROUND_COLOR: default text color
    public static Color BTN_FOREGROUND_COLOR;
    // BTN_CLICK_COLOR: default color for clicked buttons
    public static Color BTN_CLICK_COLOR;
    // FIELD_FOREGROUND_COLOR: default field text color
    public static Color FIELD_FOREGROUND_COLOR;
    // FIELD_EMPTY_FOREGROUND_COLOR: empty field text color
    public static Color FIELD_EMPTY_FOREGROUND_COLOR;

    /**
     * @param text JLabel text
     * @return JLabel with default parameters
     */
    public static JLabel getDefaultLB(String text) {
        JLabel label = new JLabel(text);
        label.setOpaque(true);
        label.setBackground(BTN_COLOR);
        label.setFont(BTN_FONT);
        label.setForeground(BTN_FOREGROUND_COLOR);
        return label;
    }

    /**
     * @param text JLabel text
     * @return JLabel with default parameters and centered text
     */
    public static JLabel getCenteredLB(String text) {
        JLabel label = getDefaultLB(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    /**
     * @param ethnicity ethnicities of the distance panel
     * @param distance distance value of the distance panel
     * @param singlePopulation true if the panel represents a
     *                         single population, false otherwise
     * @return built distance panel
     */
    public static JPanel getDistancePN(String ethnicity, double distance, boolean singlePopulation) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 3));
        String distanceValue = String.valueOf(distance);
        if (String.valueOf(distance).length() > 11) {
            distanceValue = distanceValue.substring(0, 11);
        }
        JLabel labelDistance = getDistanceLB(distanceValue);

        panel.add(labelDistance);
        panel.add(getCenteredLB(ethnicity.split(",")[0]));
        if (!singlePopulation) {
            JPanel ethnicityPanel = new JPanel();
            ethnicityPanel.setLayout(new GridLayout(2, 0));
            String ethnicityProper = ethnicity.split(",")[1];
            ethnicityPanel.add(getDefaultLB(ethnicityProper.split("&\\+\\*")[0]));
            ethnicityPanel.add(getDefaultLB(ethnicityProper.split("&\\+\\*")[1]));
            panel.add(ethnicityPanel);
        }
        else {
            panel.add(getDefaultLB(ethnicity.split(",")[1]));
        }
        return panel;
    }

    /**
     * @param distance distance value of the label
     * @return built distance value label
     */
    public static JLabel getDistanceLB(String distance) {
        JLabel label = getCenteredLB(distance);
        label.setForeground(Color.BLACK);
        label.setBackground(DistanceData.getColor(Double.parseDouble(distance)));
        return label;
    }

    /**
     * @param text JButton text
     * @return JButton with default parameters
     */
    public static JButton getDefaultBtn(String text) {
        JButton button = new JButton(text);
        button.setOpaque(true);
        button.setBackground(BTN_COLOR);
        button.setFont(BTN_FONT);
        button.setForeground(BTN_FOREGROUND_COLOR);
        button.addActionListener(e -> Navigation.buttonClick(text));
        return button;
    }

    /**
     * @param isLightMode layout mode to set (light / dark)
     * @return layout mode switch button
     */
    public static JButton getLayoutModeBtn(boolean isLightMode) {
        JButton layoutModeBtn = new JButton();
        layoutModeBtn.setUI(new BasicButtonUI() {
            @Override
            public void update(Graphics g, JComponent c) {
                if (c.isOpaque()) {
                    Color fillColor = c.getBackground();

                    AbstractButton button = (AbstractButton) c;
                    ButtonModel model = button.getModel();

                    if (model.isPressed()) {
                        fillColor = fillColor.darker();
                    } else if (model.isRollover()) {
                        fillColor = fillColor.brighter();
                    }

                    g.setColor(fillColor);
                    g.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 100, 100);
                }
                paint(g, c);
            }
        });
        layoutModeBtn.addActionListener(e -> {
            SignalMap.mapSignals.clear();
            App.menuWindow.signalMap.stopSignals();
            App.initWindows(isLightMode);
        });

        layoutModeBtn.setMargin(new Insets(0, 0, 0, 0));
        Color backgroundColor = isLightMode ?
                new Color(191, 191, 31) : new Color(161, 161, 161);
        layoutModeBtn.setBackground(backgroundColor);
        layoutModeBtn.setBorder(null);

        return layoutModeBtn;
    }

    /**
     * @param optionList JComboBox options
     * @return JComboBox with default parameters
     */
    public static JComboBox<String> getDefaultCB(String[] optionList) {
        JComboBox<String> comboBox = new JComboBox<>(optionList);
        comboBox.setOpaque(true);
        comboBox.setBackground(BTN_COLOR);
        comboBox.setFont(BTN_FONT);
        comboBox.setForeground(BTN_FOREGROUND_COLOR);
        comboBox.addActionListener(e -> Navigation.buttonClick
                (Objects.requireNonNull(comboBox.getSelectedItem())));
        return comboBox;
    }

    /**
     * @param header JTextField header
     * @return JTextField with default parameters
     */
    public static JTextField getDefaultTF(String header) {
        JTextField field = new JTextField(header);
        field.setBackground(BTN_COLOR);
        field.setForeground(FIELD_EMPTY_FOREGROUND_COLOR);
        field.setFont(BTN_FONT);
        field.setCaretColor(FIELD_FOREGROUND_COLOR);
        field.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        field.setHorizontalAlignment(JTextField.CENTER);

        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.isEditable()) {
                    field.setText("");
                    field.setForeground(FIELD_FOREGROUND_COLOR);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(FIELD_EMPTY_FOREGROUND_COLOR);
                    field.setText(header);
                }
            }
        });
        return field;
    }

    /**
     * @param header JTextField header
     * @return JTextField with percentage field parameters
     */
    public static JTextField getPercentageTF(String header) {
        JTextField field = getDefaultTF(header);
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.isEditable()) {
                    field.setText("");
                    field.setForeground(FIELD_FOREGROUND_COLOR);
                    CalculatorInput.updateFieldSum();
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(FIELD_EMPTY_FOREGROUND_COLOR);
                    field.setText(header);
                    CalculatorInput.updateFieldSum();
                }
            }
        });
        return field;
    }

    /**
     * @param header JTextField header
     * @return JTextField with search field parameters
     */
    public static JTextField getSearchTF(String header) {
        JTextField field = getDefaultTF(header);

        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = field.getText();
                text = text.equals(header) ? "" : text;

                if (text.trim().length() == 0) {
                    DatabaseWindow.sampleTable.rowSorter.setRowFilter(null);
                } else {
                    DatabaseWindow.sampleTable.rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = field.getText();
                text = text.equals(header) ? "" : text;

                if (text.trim().length() == 0) {
                    DatabaseWindow.sampleTable.rowSorter.setRowFilter(null);
                } else {
                    DatabaseWindow.sampleTable.rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet");
            }
        });
        return field;
    }
}
