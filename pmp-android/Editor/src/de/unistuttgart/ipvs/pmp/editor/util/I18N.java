package de.unistuttgart.ipvs.pmp.editor.util;

import org.eclipse.osgi.util.NLS;

public class I18N extends NLS {
    
    private static final String BUNDLE_NAME = "de.unistuttgart.ipvs.pmp.editor.strings"; //$NON-NLS-1$
    public static String editor_rgis_general_class;
    public static String editor_rgis_general_title;
    public static String editor_rgis_general_icon;
    public static String editor_rgis_general_identifier;
    public static String editor_rgis_general_preferences;
    public static String editor_rgis_general_tab;
    public static String editor_rgis_ps_identifier;
    public static String editor_rgis_ps_ps;
    public static String editor_rgis_ps_removemsg_text;
    public static String editor_rgis_ps_removemsg_title;
    public static String editor_rgis_ps_tab;
    public static String editor_rgis_ps_title;
    public static String editor_rgis_ps_validvalues;
    public static String general_add;
    public static String general_localization;
    public static String general_remove;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, I18N.class);
    }
    
    
    private I18N() {
    }
}
