package backend.data;

import app.App;
import gui.DefaultObjects;
import gui.Navigation;
import gui.components.SamplePie;
import gui.components.SignalMap;
import gui.windows.Window;

import java.awt.*;

/**
 * LayoutData regulates App layout color schemes
 */
public class LayoutData {
    // light mode color schemes
    public static Color lightModeMain = new Color(255, 255, 255);
    public static Color lightModeText1 = new Color(0, 0, 0);
    public static Color lightModeText2 = new Color(128, 128, 128);
    public static Color lightModeBtnClick = new Color(184,207,229);
    public static Color lightModeSignal = new Color(0, 0, 0, 20);

    // dark mode color schemes
    public static Color darkModeMain = new Color(18, 18, 18);
    public static Color darkModeInner = new Color(33, 33, 33);
    public static Color darkModeText1 = new Color(255, 255, 255, 222);
    public static Color darkModeText2 = new Color(255, 255, 255, 153);
    public static Color darkModeText3 = new Color(0, 255, 255);
    public static Color darkModeBtnClick = new Color(90, 90, 90);
    public static Color darkModeSignal = new Color(255, 255, 255, 77);

    /**
     * sets up light layout mode color schemes
     */
    public static void setLightMode() {
        Window.BACKGROUND_COLOR = lightModeMain;
        DefaultObjects.BTN_COLOR = lightModeMain;
        DefaultObjects.BTN_FOREGROUND_COLOR = lightModeText1;
        DefaultObjects.BTN_CLICK_COLOR = lightModeBtnClick;
        DefaultObjects.FIELD_FOREGROUND_COLOR = lightModeText1;
        DefaultObjects.FIELD_EMPTY_FOREGROUND_COLOR = lightModeText2;

        TableData.BACKGROUND_COLOR = lightModeMain;
        TableData.HEADER_FOREGROUND_COLOR = lightModeText1;
        TableData.CONTENT_FOREGROUND_COLOR = lightModeText1;

        SamplePie.CHART_COLOR = lightModeMain;
        MapData.background = Navigation.getImage(App.mapTemplatePath);
        SignalMap.signalColor = lightModeSignal;
    }

    /**
     * sets up dark layout mode color schemes
     */
    public static void setDarkMode() {
        Window.BACKGROUND_COLOR = darkModeMain;
        DefaultObjects.BTN_COLOR = darkModeMain;
        DefaultObjects.BTN_FOREGROUND_COLOR = darkModeText1;
        DefaultObjects.BTN_CLICK_COLOR = darkModeBtnClick;
        DefaultObjects.FIELD_FOREGROUND_COLOR = darkModeText3;
        DefaultObjects.FIELD_EMPTY_FOREGROUND_COLOR = darkModeText3;

        TableData.BACKGROUND_COLOR = darkModeInner;
        TableData.HEADER_FOREGROUND_COLOR = darkModeText3;
        TableData.CONTENT_FOREGROUND_COLOR = darkModeText2;

        SamplePie.CHART_COLOR = darkModeInner;
        MapData.background = Navigation.getImage(App.mapDarkTemplatePath);
        SignalMap.signalColor = darkModeSignal;
    }
}
