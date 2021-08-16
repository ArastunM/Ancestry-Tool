package app;

import java.util.ArrayList;

import backend.DatabaseAccess;
import backend.Sample;
import backend.data.LayoutData;
import gui.windows.*;
import gui.windows.Window;

/**
 * App launches the Ancestry Tool App
 */
public class App {
    // app windows
    public static MenuWindow menuWindow;
    public static UploadWindow uploadWindow;
    public static DatabaseWindow databaseWindow;
    public static DatabaseAddWindow databaseAddWindow;
    public static SampleWindow sampleWindow;
    public static DistanceWindow distanceWindow;
    public static SingleWindow singleWindow;

    // windows: array of all windows
    public static ArrayList<Window> windows = new ArrayList<>();

    // resource paths
    public static final String mapTemplatePath = "/map_template.jpg";
    public static final String mapDarkTemplatePath = "/map_template_dark.png";

    /**
     * main method, launches the app
     * @param args empty
     */
    public static void main(String[] args) {
        DatabaseAccess.loadDatabase();
        initWindows(true);
    }

    /**
     * initWindows initialises all App Windows
     * @param isLightMode initiates the App in light or dark layout mode
     */
    public static void initWindows(boolean isLightMode) {
        if (isLightMode) { LayoutData.setLightMode(); }
        else { LayoutData.setDarkMode(); }
        for (Window window : windows) { window.frame.dispose(); }
        windows.clear();

        menuWindow = new MenuWindow();
        uploadWindow = new UploadWindow();
        databaseWindow = new DatabaseWindow();
        databaseAddWindow = new DatabaseAddWindow();
        sampleWindow = new SampleWindow();
        distanceWindow = new DistanceWindow();
        singleWindow = new SingleWindow();

        windows.add(menuWindow);
        windows.add(uploadWindow);
        windows.add(databaseWindow);
        windows.add(databaseAddWindow);
        windows.add(distanceWindow);
        windows.add(singleWindow);
    }
}
