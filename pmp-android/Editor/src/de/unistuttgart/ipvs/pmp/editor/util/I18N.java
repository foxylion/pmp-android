package de.unistuttgart.ipvs.pmp.editor.util;

import java.text.MessageFormat;

import org.eclipse.osgi.util.NLS;

public class I18N extends NLS {
    
    private static final String BUNDLE_NAME = "de.unistuttgart.ipvs.pmp.editor.strings"; //$NON-NLS-1$
    public static String editor_ais_general_activityaddedmsg_text;
    public static String editor_ais_general_activityaddedmsg_title;
    public static String editor_ais_general_activityalreadydeclaredmsg_text;
    public static String editor_ais_general_activityalreadydeclaredmsg_title;
    public static String editor_ais_general_activitynotaddedmsg_text;
    public static String editor_ais_general_activitynotaddedmsg_title;
    public static String editor_ais_general_addactivity;
    public static String editor_ais_general_addservice;
    public static String editor_ais_general_appnodemissing_text;
    public static String editor_ais_general_appnodemissing_title;
    public static String editor_ais_general_manifestfunctions_text;
    public static String editor_ais_general_manifestfunctions_title;
    public static String editor_ais_general_manifestnotfoundmsg_text;
    public static String editor_ais_general_manifestnotfoundmsg_title;
    public static String editor_ais_general_moreappnodesmsg_text;
    public static String editor_ais_general_moreappnodesmsg_title;
    public static String editor_ais_general_nomainactivitymsg_text;
    public static String editor_ais_general_nomainactivitymsg_title;
    public static String editor_ais_general_serviceaddedmsg_text;
    public static String editor_ais_general_serviceaddedmsg_title;
    public static String editor_ais_general_servicealreadydeclaredmsg_text;
    public static String editor_ais_general_servicealreadydeclaredmsg_title;
    public static String editor_ais_general_servicenotaddedmsg_text;
    public static String editor_ais_general_servicenotaddedmsg_title;
    public static String editor_ais_general_tab;
    public static String editor_ais_general_title;
    public static String editor_ais_sf_addrg;
    public static String editor_ais_sf_addsf;
    public static String editor_ais_sf_addsfmsg_text;
    public static String editor_ais_sf_addsfmsg_title;
    public static String editor_ais_sf_allpsaddedmsg_text;
    public static String editor_ais_sf_allpsaddedmsg_title;
    public static String editor_ais_sf_minimalrev;
    public static String editor_ais_sf_pschangevaluedialog_emptyvalue;
    public static String editor_ais_sf_pschangevaluedialog_title;
    public static String editor_ais_sf_pschangevaluedialog_text1;
    public static String editor_ais_sf_pschangevaluedialog_text2;
    public static String editor_ais_sf_removesfmsg_text;
    public static String editor_ais_sf_removesfmsg_title;
    public static String editor_ais_sf_requiredps;
    public static String editor_ais_sf_rgnotaddedmsg_text;
    public static String editor_ais_sf_rgnotaddedmsg_title;
    public static String editor_ais_sf_sf;
    public static String editor_ais_sf_unknownrgmsg_text;
    public static String editor_ais_sf_unknownrgmsg_title;
    public static String editor_ais_sf_updaterglist_tooltip;
    public static String editor_ais_sf_tab;
    public static String editor_ais_sf_title;
    public static String editor_rgis_general_class;
    public static String editor_rgis_general_title;
    public static String editor_rgis_general_icon;
    public static String editor_rgis_general_preferences;
    public static String editor_rgis_general_tab;
    public static String editor_rgis_ps_changedescription;
    public static String editor_rgis_ps_ps;
    public static String editor_rgis_ps_removemsg_text;
    public static String editor_rgis_ps_removemsg_title;
    public static String editor_rgis_ps_tab;
    public static String editor_rgis_ps_title;
    public static String editor_rgis_ps_validvalues;
    public static String general_add;
    public static String general_attribute;
    public static String general_description;
    public static String general_descriptions;
    public static String general_identifier;
    public static String general_locale;
    public static String general_localization;
    public static String general_name;
    public static String general_names;
    public static String general_notopenedmsg_text;
    public static String general_notopenedmsg_title;
    public static String general_notsavedmsg_text;
    public static String general_notsavedmsg_title;
    public static String general_parserconfigurationexceptionmsg_text;
    public static String general_parserconfigurationexceptionmsg_title;
    public static String general_remove;
    public static String general_seedetails;
    public static String general_undefined;
    public static String general_value;
    public static String preferences_jpmpps_0;
    public static String preferences_jpmpps_connecting;
    public static String preferences_jpmpps_description;
    public static String preferences_jpmpps_port;
    public static String preferences_jpmpps_test;
    public static String preferences_jpmpps_testfailed;
    public static String preferences_jpmpps_testok;
    public static String preferences_jpmpps_testupdate;
    public static String preferences_jpmpps_timeout;
    public static String preferences_jpmpps_url;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, I18N.class);
    }
    
    
    private I18N() {
    }
    
    
    public static String addVariables(String input, Object... vars) {
        return MessageFormat.format(input, vars);
    }
}
