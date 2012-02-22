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
package de.unistuttgart.ipvs.pmp.xmlutil.validator.issue;

/**
 * Here are listed all possible types of AISIssues
 * 
 * @author Marcus Vetter
 * 
 */
public enum IssueType {
    
    /**
     * Common
     */
    LOCALE_INVALID,
    LOCALE_MISSING,
    
    /**
     * Common for all objects extending BasicIS
     */
    NAME_LOCALE_OCCURRED_TOO_OFTEN,
    NAME_LOCALE_EN_MISSING,
    DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN,
    DESCRIPTION_LOCALE_EN_MISSING,
    
    /**
     * Common for all objects having an identifier
     */
    IDENTIFIER_MISSING,
    
    /**
     * AIS
     */
    SF_IDENTIFIER_OCCURRED_TOO_OFTEN,
    NO_SF_EXISTS,
    SFS_CONTAIN_SAME_RRG_AND_RPS_WITH_SAME_VALUE,
    
    /**
     * AISServiceFeature
     */
    RRG_IDENTIFIER_OCCURRED_TOO_OFTEN,
    NO_RRG_EXISTS,
    
    /**
     * AISRequiredResourceGroups
     */
    MINREVISION_MISSING,
    MINREVISION_INVALID,
    RPS_IDENTIFIER_OCCURRED_TOO_OFTEN,
    NO_RPS_EXISTS,
    
    /**
     * RGIS
     */
    ICON_MISSING,
    CLASSNAME_MISSING,
    PS_IDENTIFIER_OCCURRED_TOO_OFTEN,
    NO_PS_EXISTS,
    
    /**
     * RGISPrivacySetting
     */
    VALID_VALUE_DESCRIPTION_MISSING,
    CHANGE_DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN,
    CHANGE_DESCRIPTION_LOCALE_EN_MISSING,
    
    /**
     * Preset
     */
    CREATOR_MISSING,
    NAME_MISSING,
    
    /**
     * PresetSetAssignedPrivacySetting
     */
    RG_IDENTIFIER_MISSING,
    RG_REVISION_MISSING,
    RG_REVISION_INVALID,
    PS_IDENTIFIER_MISSING,
    VALUE_MISSING,
    
    /**
     * PresetPSContext
     */
    TYPE_MISSING,
    CONDITION_MISSING,
    OVERRIDE_VALUE_MISSING,
    
}
