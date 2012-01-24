package de.unistuttgart.ipvs.pmp.gui.util;

import android.graphics.Color;

/**
 * {@link GUIConstants} provides some general guidelines as consistent background colors or equal text colors.
 */
public class GUIConstants {
    
    /**
     * Identifiers
     */
    public static final String APP_IDENTIFIER = "appIdentifier";
    public static final String PRESET_IDENTIFIER = "presetIdentifier";
    
    /**
     * Text colors
     */
    public static final int COLOR_TEXT_GRAYED_OUT = Color.GRAY;
    
    /**
     * Background colors
     */
    public static final int COLOR_BG_GREEN = Color.parseColor("#004800");
    public static final int COLOR_BG_RED = Color.parseColor("#480000");
    public static final int COLOR_BG_GRAY = Color.parseColor("#333333");
    
    /**
     * Intent Actions
     */
    public static final String ACTIVITY_ACTION = "activityAction";
    public static final String CHANGE_SERVICEFEATURE = "changeServiceFeature";
    public static final String FILTER_AVAILABLE_RGS = "filterRGs";
    
    /**
     * Intent Parameters
     */
    public static final String REQUIRED_SERVICE_FEATURE = "requiredServiceFeature";
    public static final String RGS_FILTER = "rgsFilter";
    
}
