/*
 * Copyright 2011 pmp-android development team
 * Project: PMP
 * Project-Site: http://code.google.com/p/pmp-android/
 * 
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.xmlutil.common;

import java.text.SimpleDateFormat;

/**
 * There are xml constants defined.
 * 
 * @author Marcus Vetter
 * 
 */
public class XMLConstants {
    
    /**
     * The simple date format for the revision
     */
    public static final SimpleDateFormat REVISION_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
    
    /**
     * AIS
     */
    public static final String AIS = "appInformationSet";
    public static final String AI = "appInformation";
    public static final String SFS = "serviceFeatures";
    public static final String SF = "serviceFeature";
    public static final String RRG = "requiredResourceGroup";
    public static final String RPS = "requiredPrivacySetting";
    
    public static final String MINREVISION_ATTR = "minRevision";
    public static final String EMPTY_VALUE_ATTR = "emptyValue";
    
    /**
     * RGIS
     */
    public static final String RGIS = "resourceGroupInformationSet";
    public static final String RGI = "resourceGroupInformation";
    public static final String PSS = "privacySettings";
    
    public static final String ICON_ATTR = "icon";
    public static final String CLASS_NAME_ATTR = "className";
    public static final String VALID_VALUE_DESCRIPTION_ATTR = "validValueDescription";
    public static final String CHANGE_DESCRIPTION = "changeDescription";
    
    /**
     * PresetSet
     */
    public static final String PRESET_SET = "presetSet";
    public static final String PRESET = "preset";
    public static final String ASSIGNED_APPS = "assignedApps";
    public static final String APP = "app";
    public static final String ASSIGNED_PRIVACY_SETTINGS = "assignedPrivacySettings";
    public static final String VALUE = "value";
    public static final String CONTEXT = "context";
    public static final String CONTEXT_OVERRIDE_VALUE = "overrideValue";
    
    public static final String NAME_ATTR = "name";
    public static final String DESCRIPTION_ATTR = "description";
    public static final String CREATOR_ATTR = "creator";
    public static final String RG_IDENTIFIER_ATTR = "rgIdentifier";
    public static final String RG_REVISION_ATTR = "rgRevision";
    public static final String PS_IDENTIFIER_ATTR = "psIdentifier";
    public static final String CONTEXT_TYPE_ATTR = "type";
    public static final String CONTEXT_CONDITION_ATTR = "condition";
    public static final String CONTEXT_EMPTY_CONDITION_ATTR = "emptyCondition";
    public static final String CONTEXT_EMPTY_OVERRIDE_VALUE_ATTR = "emptyOverrideValue";
    
    /**
     * Common
     */
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    
    public static final String LANGUAGE_ATTR = "lang";
    public static final String IDENTIFIER_ATTR = "identifier";
    
    public static final String PRIVACY_SETTING = "privacySetting";
    
}
