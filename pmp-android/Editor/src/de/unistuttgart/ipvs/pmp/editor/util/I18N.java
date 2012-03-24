package de.unistuttgart.ipvs.pmp.editor.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

/**
 * Handles the access to the external string- and issue-resources.
 * 
 * @author Patrick Strobel
 * 
 */
public class I18N extends NLS {
    
    /**
     * Bundle storing the string-resources used to translate the ui-widgets
     */
    private static final String STRING_BUNDLE_NAME = "de.unistuttgart.ipvs.pmp.editor.strings"; //$NON-NLS-1$
    /**
     * Bundle storing the issue-resources used to translate the issues. Used for issue-tooltips
     */
    private static final String ISSUE_BUNDLE_NAME = "de.unistuttgart.ipvs.pmp.editor.issues"; //$NON-NLS-1$
    private static final ResourceBundle ISSUE_RESOURCE_BUNDLE = ResourceBundle.getBundle(ISSUE_BUNDLE_NAME);
    
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
    public static String editor_ais_sf_minimalrev_tooltip;
    public static String editor_ais_sf_pschangevaluedialog_emptyvalue;
    public static String editor_ais_sf_pschangevaluedialog_title;
    public static String editor_ais_sf_pschangevaluedialog_text1;
    public static String editor_ais_sf_pschangevaluedialog_text2;
    public static String editor_ais_sf_psdialog_choose;
    public static String editor_ais_sf_psdialog_nodescription;
    public static String editor_ais_sf_psdialog_novalidvaluedescription;
    public static String editor_ais_sf_psdialog_title;
    public static String editor_ais_sf_removesfmsg_text;
    public static String editor_ais_sf_removesfmsg_title;
    public static String editor_ais_sf_requiredps;
    public static String editor_ais_sf_rgdialog_choose;
    public static String editor_ais_sf_rgdialog_nodescription;
    public static String editor_ais_sf_rgdialog_noname;
    public static String editor_ais_sf_rgdialog_title;
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
    public static String general_add;
    public static String general_attribute;
    public static String general_browse;
    public static String general_calendar_tooltip;
    public static String general_datetimedialog_text;
    public static String general_datetimedialog_title;
    public static String general_description;
    public static String general_descriptions;
    public static String general_identifier;
    public static String general_information;
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
    public static String general_rgdownloaddialog_downloaderror_text;
    public static String general_rgdownloaddialog_downloaderror_title;
    public static String general_rgdownloaddialog_job;
    public static String general_rgdownloaddialog_task;
    public static String general_seedetails;
    public static String general_undefined;
    public static String general_value;
    public static String general_validvalues;
    public static String preferences_jpmpps_connecting;
    public static String preferences_jpmpps_description;
    public static String preferences_jpmpps_port;
    public static String preferences_jpmpps_test;
    public static String preferences_jpmpps_testfailed;
    public static String preferences_jpmpps_testok;
    public static String preferences_jpmpps_testupdate;
    public static String preferences_jpmpps_timeout;
    public static String preferences_jpmpps_unknown;
    public static String preferences_jpmpps_url;
    public static String wizard_ais_description;
    public static String wizard_ais_replacemsg_text;
    public static String wizard_ais_replacemsg_title;
    public static String wizard_ais_title;
    public static String wizard_ais_windowtitle;
    public static String wizard_ais_writeerrormsg_text;
    public static String wizard_ais_writeerrormsg_title;
    public static String wizard_general_cannotopenprojectmsg_text;
    public static String wizard_general_cannotopenprojectmsg_title;
    public static String wizard_general_error_androidproject;
    public static String wizard_general_error_folder;
    public static String wizard_general_error_pathstart;
    public static String wizard_general_error_projectcontainer;
    public static String wizard_general_error_projectspecified;
    public static String wizard_general_error_projectwritable;
    public static String wizard_general_file;
    public static String wizard_general_folder;
    public static String wizard_general_projectnotexistsmsg_text;
    public static String wizard_general_projectnotexistsmsg_title;
    public static String wizard_general_selectdialog_text;
    public static String wizard_rgis_description;
    public static String wizard_rgis_replacemsg_text;
    public static String wizard_rgis_replacemsg_title;
    public static String wizard_rgis_title;
    public static String wizard_rgis_windowtitle;
    public static String wizard_rgis_writeerrormsg_text;
    public static String wizard_rgis_writeerrormsg_title;
    static {
        // initialize resource bundle
        NLS.initializeMessages(STRING_BUNDLE_NAME, I18N.class);
    }
    
    
    private I18N() {
    }
    
    
    /**
     * Replaces the placeholders from a read string with the given variables *
     * 
     * @param input
     *            String in which the placeholders should be replaced
     * @param vars
     *            Variables used while replacing
     * @return The modified input-string
     */
    public static String addVariables(String input, Object... vars) {
        return MessageFormat.format(input, vars);
    }
    
    
    /**
     * Reads a translation for a given issue
     * 
     * @param type
     *            Issue to get the translation for
     * @param vars
     *            Variables used to replace placeholder inside the read string
     * @return The issue's translation
     */
    public static String getIssue(IssueType type, Object... vars) {
        try {
            String string = ISSUE_RESOURCE_BUNDLE.getString(type.name());
            if (vars.length > 0) {
                string = MessageFormat.format(string, vars);
            }
            return string;
        } catch (MissingResourceException e) {
            return '!' + type.toString() + '!';
        }
    }
}
