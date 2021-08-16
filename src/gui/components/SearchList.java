package gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Objects;

import gui.DefaultObjects;

/**
 * SearchList is a JPanel extension providing a searchable JComboBox
 */
public class SearchList extends JPanel {
    // comboBox: combo box of SearchList
    public final JComboBox<String> comboBox;
    // options: option list for the combo box
    public String[] options;

    // searchField: search field of SearchList
    private final JTextField searchField;
    // fieldHeader: header of searchField
    private final String fieldHeader;

    /**
     * Constructs SearchList
     * @param allOptions String options of combo box
     * @param fieldHeader search bar header
     */
    public SearchList(String[] allOptions, String fieldHeader) {
        this.options = allOptions;
        comboBox = DefaultObjects.getDefaultCB(this.options);

        // constructing the search field
        this.fieldHeader = fieldHeader;
        searchField = DefaultObjects.getDefaultTF(this.fieldHeader);
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent ke) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        filterList(searchField.getText());
                    }
                });
            }
        });

        // constructing the panel
        this.setLayout(new GridLayout(2, 0));
        this.add(searchField);
        this.add(comboBox);
    }

    /**
     * @param allOptions String options of combo box
     * @param fieldHeader search bar header
     * @param writeTo label to write selections to
     */
    public SearchList(String[] allOptions, String fieldHeader, JLabel writeTo) {
        this(allOptions, fieldHeader);
        comboBox.addActionListener(e -> {
            try {
                String selectedID = ((String) Objects.requireNonNull
                        (comboBox.getSelectedItem())).split("\\|")[2].strip();
                writeTo.setText(writeTo.getText() + selectedID + " ");
            } catch (NullPointerException ignored) {}
        });
    }

    /**
     * filters the combo box based on search field input
     * @param enteredText search field input
     */
    public void filterList(String enteredText) {
        if (enteredText.trim().length() == 0) {
            comboBox.setModel(new DefaultComboBoxModel<>(options));
            comboBox.hidePopup();
            return; // returning if search field is empty
        }

        ArrayList<String> filterArray = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase().contains(enteredText.toLowerCase())) {
                filterArray.add(option);
            }
        }

        if (filterArray.size() > 0) {
            comboBox.setModel(new DefaultComboBoxModel<>(filterArray.toArray(new String[0])));
            comboBox.setSelectedItem(enteredText);
            comboBox.showPopup();
        }
        else { comboBox.hidePopup(); }
    }

    /**
     * resets the SearchList to initial settings
     */
    public void reset() {
        searchField.setText(fieldHeader);
        searchField.setForeground(DefaultObjects.FIELD_EMPTY_FOREGROUND_COLOR);
        comboBox.setModel(new DefaultComboBoxModel<>(options));
    }
}
