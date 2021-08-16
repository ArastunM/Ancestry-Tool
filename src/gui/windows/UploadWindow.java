package gui.windows;

import javax.swing.*;
import java.awt.*;

import backend.data.CalculatorData;
import gui.DefaultObjects;
import gui.Navigation;

/**
 * UploadWindow facilitates database uploads
 */
public class UploadWindow extends Window {
    // main frame and panel are inherited

    // uploadFormatBtn: combo box of upload formats
    public static JComboBox<String> uploadFormatBtn;
    // sampleTypeBtn: combo box of sample types
    public static JComboBox<String> sampleTypeBtn;
    // sourceField: text field for source name
    public static JTextField sourceField;
    // suffixField: text field for an additional ethnicity suffix
    public static JTextField suffixField;
    // inputArea: text area to insert data to be uploaded
    public static JTextArea inputArea;

    // uploadFormatList: contains list of upload formats
    private static final String[] uploadFormatList = CalculatorData.calculatorTypes;
    // sampleTypeList: contains list of sample types
    private static final String[] sampleTypeList = new String[]{"Modern Sample", "Ancient Sample"};

    /**
     * Constructs UploadWindow
     */
    public UploadWindow() {
        // constructing panel contents
        JLabel uploadFormatLB = DefaultObjects.getDefaultLB("Select Upload Format");
        uploadFormatBtn = DefaultObjects.getDefaultCB(uploadFormatList);
        JLabel sampleTypeLB = DefaultObjects.getDefaultLB("Select Sample Type");
        sampleTypeBtn = DefaultObjects.getDefaultCB(sampleTypeList);
        sourceField = DefaultObjects.getDefaultTF("Upload Source");
        suffixField = DefaultObjects.getDefaultTF("Suffix to add to all Ethnicities (Optional)");

        inputArea = new JTextArea();
        inputArea.setBackground(Color.BLACK);
        inputArea.setForeground(Color.WHITE);
        inputArea.setCaretColor(Color.WHITE);
        inputArea.setFont(new Font("Arial", Font.BOLD, 20));
        JScrollPane scrollPane = new JScrollPane(inputArea);

        JButton uploadBtn = DefaultObjects.getDefaultBtn("Upload");
        JButton cancelBtn = DefaultObjects.getDefaultBtn("Cancel");

        // setting up toolPanel parameters
        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new GridLayout(2, 0));
        JPanel upperToolPanel = new JPanel();
        upperToolPanel.setLayout(new GridLayout(2, 2));
        upperToolPanel.add(uploadFormatLB);
        upperToolPanel.add(sampleTypeLB);
        upperToolPanel.add(uploadFormatBtn);
        upperToolPanel.add(sampleTypeBtn);
        JPanel lowerToolPanel = new JPanel();
        lowerToolPanel.setLayout(new GridLayout(0, 2));
        lowerToolPanel.add(sourceField);
        lowerToolPanel.add(suffixField);
        toolPanel.add(upperToolPanel);
        toolPanel.add(lowerToolPanel);

        // setting up submitPanel parameters
        JPanel submitPanel = new JPanel();
        submitPanel.setLayout(new GridLayout(0, 2));
        submitPanel.add(uploadBtn);
        submitPanel.add(cancelBtn);

        // setting up the main panel parameters
        panel.setLayout(new BorderLayout());
        panel.add(toolPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(submitPanel, BorderLayout.SOUTH);

        // initiating the main frame
        initFrame("Upload Window", false);
    }

    /**
     * custom window refresh for UploadWindow
     */
    @Override
    public void refreshWindow() {
        uploadFormatBtn.setSelectedIndex(0);
        sampleTypeBtn.setSelectedIndex(0);
        inputArea.setText("");
        Navigation.refreshPanel(panel);
    }
}
