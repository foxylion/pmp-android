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

import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.common.IBasicIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.IIdentifierIS;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetPSContext;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGISPrivacySetting;

/**
 * Here are listed all possible types of {@link IIssue}s
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
     * Common for all classes implementing {@link IBasicIS}
     */
    NAME_LOCALE_OCCURRED_TOO_OFTEN,
    NAME_LOCALE_EN_MISSING,
    DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN,
    DESCRIPTION_LOCALE_EN_MISSING,
    
    /**
     * Common for all classes implementing {@link IIdentifierIS}
     */
    IDENTIFIER_MISSING,
    
    /**
     * {@link IAIS}
     */
    SF_IDENTIFIER_OCCURRED_TOO_OFTEN,
    NO_SF_EXISTS,
    SFS_CONTAIN_SAME_RRG_AND_RPS_WITH_SAME_VALUE,
    
    /**
     * {@link IAISServiceFeature}
     */
    RRG_IDENTIFIER_OCCURRED_TOO_OFTEN,
    NO_RRG_EXISTS,
    
    /**
     * {@link IAISRequiredResourceGroup}
     */
    MINREVISION_MISSING,
    MINREVISION_INVALID,
    RPS_IDENTIFIER_OCCURRED_TOO_OFTEN,
    NO_RPS_EXISTS,
    
    /**
     * {@link IRGIS}
     */
    ICON_MISSING,
    CLASSNAME_MISSING,
    PS_IDENTIFIER_OCCURRED_TOO_OFTEN,
    NO_PS_EXISTS,
    
    /**
     * {@link IRGISPrivacySetting}
     */
    VALID_VALUE_DESCRIPTION_MISSING,
    CHANGE_DESCRIPTION_LOCALE_OCCURRED_TOO_OFTEN,
    CHANGE_DESCRIPTION_LOCALE_EN_MISSING,
    
    /**
     * {@link IPreset}
     */
    CREATOR_MISSING,
    NAME_MISSING,
    
    /**
     * {@link IPresetAssignedPrivacySetting}
     */
    RG_IDENTIFIER_MISSING,
    RG_REVISION_MISSING,
    RG_REVISION_INVALID,
    PS_IDENTIFIER_MISSING,
    VALUE_MISSING,
    
    /**
     * {@link IPresetPSContext}
     */
    TYPE_MISSING,
    CONDITION_MISSING,
    OVERRIDE_VALUE_MISSING,
    
    /**
     * Summary types
     */
    AIS_SF_NAME_ISSUES,
    AIS_SF_DESCRIPTION_ISSUES,
    AIS_SF_REQUIRED_RESOURCE_GROUP_ISSUES,
    RGIS_PS_NAME_ISSUES,
    RGIS_PS_DESCRIPTION_ISSUES,
    RGIS_PS_CHANGE_DESCRIPTION_ISSUES
}
