package unittest;

import java.util.Date;
import java.util.Locale;

public interface TestConstants {
    
    /*
    * strings used during the tests.
    */
    
    // app
    static final String APP_DEF_NAME = "Testing App";
    static final String APP_DEF_DESC = "The module test app.";
    
    static final String APP_LOC_NAME = "\u30c6\u30b9\u30c8\u30a2\u30d7\u30ea";
    static final Locale APP_LOC_NAME_LOCALE = Locale.JAPANESE;
    static final String APP_LOC_DESC = "\u8a72\u6a21\u584a\u7684\u6e2c\u8a66\u7a0b\u5e8f\u3002";
    static final Locale APP_LOC_DESC_LOCALE = Locale.CHINESE;
    
    static final String APP_SF1_ID = "serviceFeature";
    static final String APP_SF1_DEF_NAME = "A useless service feature";
    static final String APP_SF1_DEF_DESC = "If it comes to service features, it can't get worse than this.";
    
    static final String APP_SF2_ID = "OutOfServiceFeature";
    static final String APP_SF2_DEF_NAME = "You didn't expect this";
    static final String APP_SF2_DEF_DESC = "Maybe it can always get worse.";
    
    static final String APP_SF3_ID = "Servicewüste Deutschland";
    static final String APP_SF4_ID = "We maintain the right to refuse service to anyone.";
    static final String APP_SF5_ID = "Just shut up";
    
    static final String APP_SF1_LOC_NAME = "M\u1ed9t t\u00ednh n\u0103ng d\u1ecbch v\u1ee5 v\u00f4 d\u1ee5ng";
    static final Locale APP_SF1_LOC_NAME_LOCALE = new Locale("vi");
    static final String APP_SF1_LOC_DESC = "\u05d0\u05dd \u05de\u05d3\u05d5\u05d1\u05e8 \u05e2\u05dc \u05ea\u05db"
            + "\u05d5\u05e0\u05d5\u05ea \u05d4\u05e9\u05d9\u05e8\u05d5\u05ea, \u05d6"
            + "\u05d4 \u05dc\u05d0 \u05d9\u05db\u05d5\u05dc \u05dc\u05d4\u05d9\u05d5"
            + "\u05ea \u05d9\u05d5\u05ea\u05e8 \u05d2\u05e8\u05d5\u05e2 \u05de\u05d6" + "\u05d4.";
    static final Locale APP_SF1_LOC_DESC_LOCALE = new Locale("he");
    
    static final String APP_SF1_REQ_RG1 = "required.resource.group";
    static final String APP_SF1_REQ_PS1_ID = "required.privacy.setting";
    static final String APP_SF1_REQ_PS1_VALUE = "<logic> patterns 2\u2219\u03c0\u2219r;; <//\\>, ]]> <\\/> $";
    static final String APP_SF1_REQ_PS2_ID = "just4fun";
    static final String APP_SF1_REQ_PS2_VALUE = "lalalalala";
    
    // RG
    static final String RG_ID = "best.resource.group.ever";
    static final String RG_ICON = "icons/best.png";
    static final String RG_REVISION = "123456789";
    static final Date RG_REVISION_DATE = new Date(123456789);
    
    static final String RG_INVALID_REVISION = "i may look like a revision; but meh. I'm not.";
    static final String RG_CLASS_NAME = "AndroidRG";
    static final String RG_NAME = "The Champions";
    static final String RG_DESC = "Best. Resource. Group. Ever.";
    
    static final String RG_PS1_ID = "privacy.setting";
    static final String RG_PS1_NAME = "Privacy sitting";
    static final String RG_PS1_DESC = "Sits with you in your privacy.";
    
    static final String RG_PSn_ID = "privacy.setting.%d";
    static final String RG_PSn_NAME = "Privacy sitting %d";
    static final String RG_PSn_DESC = "Sits with you in your %dth privacy.";
    
    static final String RG_PS_VVD = "Valid or invalid, that is here the question.";
    static final String RG_PS_CD = "You paid with credit card, there is no change.";
    
    // preset
    static final String PRESET1_IDENTIFIER = "preset1";
    static final String PRESET1_CREATOR = ":";
    static final String PRESET1_NAME = "pretty much set of a pre";
    static final String PRESET1_DESCRIPTION = "Knock knock";
    
    static final String PRESET1_APP1_IDENTIFIER = "That_cool_app_you_got";
    
    static final String CONTEXT1_TYPE = "TimeContext";
    static final String CONTEXT1_CONDITION = "8'o clock";
    static final String CONTEXT1_OVERRIDE = "Yar must get out of beeed!";
    
    static final String CONTEXT2_TYPE = "LocationContext";
    static final String CONTEXT2_CONDITION = "Graveyard";
    static final String CONTEXT2_OVERRIDE = "You can shut down now";
    
    // default
    static final String ILLEGAL_LOCALE = "\u0623\u0646";
    
    /*
     * constants for use in creation methods
     */
    
    static final String XML_APP_INFORMATION_SET = "appInformationSet";
    static final String XML_APP_INFORMATION = "appInformation";
    static final String XML_SERVICE_FEATURES = "serviceFeatures";
    static final String XML_RESOURCE_GROUP_INFORMATION_SET = "resourceGroupInformationSet";
    static final String XML_RESOURCE_GROUP_INFORMATION = "resourceGroupInformation";
    static final String XML_ICON = "icon";
    static final String XML_CLASS_NAME = "className";
    static final String XML_REVISION = "minRevision";
    static final String XML_DEFAULT_EN = "en";
    static final String XML_PRIVACY_SETTINGS = "privacySettings";
    static final String XML_SERVICE_FEATURE = "serviceFeature";
    static final String XML_REQUIRED_RESOURCE_GROUP = "requiredResourceGroup";
    static final String XML_REQUIRED_RESOURCE_GROUP_REVISION = "minRevision";
    static final String XML_REQUIRED_PRIVACY_SETTING = "requiredPrivacySetting";
    static final String XML_PRIVACY_SETTING = "privacySetting";
    static final String XML_DEFAULT_DESCRIPTION = "description";
    static final String XML_DEFAULT_NAME = "name";
    static final String XML_IDENTIFIER = "identifier";
    static final String XML_DESCRIPTION = "description";
    static final String XML_LANG = "lang";
    static final String XML_NAME = "name";
    static final String XML_VALID_VALUE_DESCRIPTION = "validValueDescription";
    static final String XML_CHANGE_DESCRIPTION = "changeDescription";
    static final String XML_PRESET_SET = "presetSet";
    static final String XML_PRESET = "preset";
    static final String XML_CREATOR = "creator";
    static final String XML_APP = "app";
    static final String XML_ASSIGNED_APPS = "assignedApps";
    static final String XML_ASSIGNED_PRIVACY_SETTINGS = "assignedPrivacySettings";
    static final String XML_RG_IDENTIFIER = "rgIdentifier";
    static final String XML_RG_REVISION = "rgRevision";
    static final String XML_PS_IDENTIFIER = "psIdentifier";
    static final String XML_CONTEXT = "context";
    static final String XML_TYPE = "type";
    static final String XML_CONDITION = "condition";
    static final String XML_VALUE = "value";
    static final String XML_OVERRIDE_VALUE = "overrideValue";
    
    static final Locale XML_DEFAULT_EN_LOCALE = Locale.ENGLISH;
}
