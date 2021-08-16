package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import app.App;
import gui.components.ConfirmPopup;
import gui.components.WarningPopup;
import gui.windows.*;
import backend.DatabaseAccess;
import gui.windows.Window;

/**
 * Navigation contains various generic GUI methods used by App Windows
 */
public class Navigation {
    /**
     * reloads panels
     * @param panel panel to refresh / reload
     */
    public static void refreshPanel(JPanel panel) {
        panel.revalidate();
        panel.repaint();
    }

    /**
     * @param resourceName resource path of the Image
     * @return requested Image file
     */
    public static Image getImage(String resourceName) {
        BufferedImage image;
        try {
            image = ImageIO.read(Navigation.class.getResource(resourceName));
        } catch (IOException e) {
            e.printStackTrace();
            image = null;
        }
        return image;
    }

    /**
     * Simulates a button click / unclick
     * @param button button to refer to
     */
    public static void simulateClick(JButton button) {
        button.setBackground(button.getBackground() == DefaultObjects.BTN_COLOR ?
                DefaultObjects.BTN_CLICK_COLOR : DefaultObjects.BTN_COLOR);
    }

    /**
     * Called upon a button press / click
     * @param text text of clicked button
     */
    public static void buttonClick(Object text) {
        String textString = (String) text;

        if (textString.startsWith("Cycles")) { App.singleWindow.updateCycle(); }

        // from Menu Window
        if (App.menuWindow.equals(Window.currentWindow)) {
            switch (textString) {
                case "View Database" -> App.databaseWindow.switchTo();
                case "Clean Database" -> {
                    ConfirmPopup popup = new ConfirmPopup("Warning",
                            "Are you sure to clean up the Database?",
                            "Clean Database");
                    popup.pop();
                }
                case "Upload Data" -> { App.uploadWindow.switchTo(); }

                case "Autosomal Distance" -> { App.distanceWindow.switchTo(); }
                case "Autosomal Single" -> { App.singleWindow.switchTo(); }
            }
        }
        // from Database Window
        else if (App.databaseWindow.equals(Window.currentWindow)) {
            switch (textString) {
                case "Home" -> App.menuWindow.switchTo();
                case "Add Sample" -> App.databaseAddWindow.switchTo();
                case "Remove Sample" -> simulateClick(DatabaseWindow.removeSampleBtn);

                // filtering and sorting
                case "Show All", "Show Modern Samples", "Show Ancient Samples",
                        "Show Sample Averages", "All Calculators", "Dodecad K12b",
                        "Ancient Eurasia K6", "Eurogenes K13", "MDLP World" -> App.databaseWindow.setTable();
            }
        }
        // from Database Add Window
        else if (App.databaseAddWindow.equals(Window.currentWindow)) {
            switch (textString) {
                case "Sample Modern", "Sample Ancient" -> App.databaseAddWindow.setPercentagesPanel();
                case "Sample Average" -> App.databaseAddWindow.setSelectionPanel();

                case "Dodecad K12b", "Ancient Eurasia K6", "Eurogenes K13", "MDLP World" -> {
                    if (DatabaseAddWindow.sampleTypeBtn.getSelectedIndex() == 2) {
                        App.databaseAddWindow.setSelectionPanel();
                    }
                    else {
                        App.databaseAddWindow.setPercentagesPanel();
                    }
                }
                case "Reset Selections" -> DatabaseAddWindow.componentSelected.setText("");

                case "Add" -> {
                    int statusCode = DatabaseAddWindow.sampleTypeBtn.getSelectedIndex() == 2 ?
                            DatabaseAccess.databaseAddAverage() : DatabaseAccess.databaseAdd();
                    if (statusCode == 400)
                    { new WarningPopup("Error", "Input detail are incorrect").pop(); }
                    else { App.databaseWindow.switchTo(); }
                }
                case "Cancel" -> App.databaseWindow.switchTo();
            }
        }
        // from Sample Window
        else if (App.sampleWindow.equals(Window.currentWindow)) {
            switch (textString) {
                case "Back" -> App.sampleWindow.switchToSource();

                case "Percentages" -> App.sampleWindow.showPercentages();
                case "Components" -> App.sampleWindow.showComponents();
            }
        }
        // from Upload Window
        else if (App.uploadWindow.equals(Window.currentWindow)) {
            switch (textString) {
                case "Upload" -> {
                    int statusCode = DatabaseAccess.databaseUpload();
                    if (statusCode == 400) {
                        new WarningPopup("Error", "Given data is not in appropriate form").pop();
                    }
                    else {
                        new WarningPopup("Information", "Data Uploaded Successfully").pop();
                    }
                    App.uploadWindow.refreshWindow();
                }
                case "Cancel" -> App.menuWindow.switchTo();
            }
        }
        // from Distance Window
        else if (App.distanceWindow.equals(Window.currentWindow)) {
            switch (textString) {
                case "Run" ->  App.distanceWindow.runDistance();
                case "Back" -> App.menuWindow.switchTo();

                case "Single Populations", "Mixed Populations" -> App.distanceWindow.updateDistanceType(textString);

                case "Dodecad K12b", "Ancient Eurasia K6",
                        "Eurogenes K13", "MDLP World" -> App.distanceWindow.updateTargetList();
            }
        }
        // from Single Window
        else if (App.singleWindow.equals(Window.currentWindow)) {
            switch (textString) {
                case "Run" ->  App.singleWindow.runSingle();
                case "Back" -> App.menuWindow.switchTo();

                case "Dodecad K12b", "Ancient Eurasia K6",
                        "Eurogenes K13", "MDLP World" -> App.singleWindow.updateTargetList();
            }
        }
    }
}
