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
package de.unistuttgart.ipvs.pmp.xmlutil.validator;

import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedApp;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetSet;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.Issue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetSetValidator extends AbstractValidator {
    
    /**
     * Validate the whole PresetSet
     * 
     * @param presetSet
     *            the PresetSet
     * @param attachData
     *            set this flag true, if the given data should be attached with the issues
     * @return List with issues as result of the validation
     */
    public List<IIssue> validatePresetSet(IPresetSet presetSet, boolean attachData) {
        List<IIssue> issueList = new ArrayList<IIssue>();
        
        // Clear the attached issues, if the issues should be attached
        if (attachData)
            presetSet.clearIssuesAndPropagate();
        
        // Validate all Preset
        for (IPreset preset : presetSet.getPresets()) {
            issueList.addAll(validatePreset(preset, attachData));
        }
        
        return issueList;
    }
    
    
    /**
     * Validate one preset
     * 
     * @param preset
     *            the preset
     * @param attachData
     *            set this flag true, if the given data should be attached with the issues
     * @return List with issues as result of the validation
     */
    private List<IIssue> validatePreset(IPreset preset, boolean attachData) {
        List<IIssue> issueList = new ArrayList<IIssue>();
        
        // Clear the attached issues, if the issues should be attached
        if (attachData)
            preset.clearIssuesAndPropagate();
        
        /*
         * Validate, if the identifier is set
         */
        if (!checkValueSet(preset.getIdentifier()))
            issueList.add(new Issue(IssueType.IDENTIFIER_MISSING, preset));
        
        /*
         * Validate, if the creator is set
         */
        if (!checkValueSet(preset.getCreator()))
            issueList.add(new Issue(IssueType.CREATOR_MISSING, preset));
        
        /*
         * Validate, if the name is set
         */
        if (!checkValueSet(preset.getName()))
            issueList.add(new Issue(IssueType.NAME_MISSING, preset));
        
        // Attach data
        attachData(issueList, attachData);
        
        // Validate all assigned apps
        issueList.addAll(validateAssignedApps(preset, attachData));
        
        // Validate all assigned privacy settings
        issueList.addAll(validateAssignedPrivacySettings(preset, attachData));
        
        return issueList;
    }
    
    
    /**
     * Validate the assigned apps
     * 
     * @param preset
     *            the preset
     * @param attachData
     *            set this flag true, if the given data should be attached with the issues
     * @return List with issues as result of the validation
     */
    private List<IIssue> validateAssignedApps(IPreset preset, boolean attachData) {
        List<IIssue> issueList = new ArrayList<IIssue>();
        
        // Clear the attached issues, if the issues should be attached
        if (attachData)
            preset.clearIssuesAndPropagate();
        
        // Validate all assigned apps
        for (IPresetAssignedApp app : preset.getAssignedApps()) {
            // Validate, if the identifier is set
            if (!checkValueSet(app.getIdentifier()))
                issueList.add(new Issue(IssueType.IDENTIFIER_MISSING, app));
        }
        
        // Attach data
        attachData(issueList, attachData);
        
        return issueList;
    }
    
    
    /**
     * Validate the assigned privacy settings
     * 
     * @param preset
     *            the preset
     * @param attachData
     *            set this flag true, if the given data should be attached with the issues
     * @return List with issues as result of the validation
     */
    private List<IIssue> validateAssignedPrivacySettings(IPreset preset, boolean attachData) {
        List<IIssue> issueList = new ArrayList<IIssue>();
        
        // Clear the attached issues, if the issues should be attached
        if (attachData)
            preset.clearIssuesAndPropagate();
        
        return issueList;
    }
    
    
    /**
     * Validate one assigned privacy setting
     * 
     * @param assignedPS
     *            the assigned privacy setting
     * @param attachData
     *            set this flag true, if the given data should be attached with the issues
     * @return List with issues as result of the validation
     */
    private List<IIssue> validateAssignedPrivacySetting(IPresetAssignedPrivacySetting assignedPS, boolean attachData) {
        List<IIssue> issueList = new ArrayList<IIssue>();
        
        // Clear the attached issues, if the issues should be attached
        if (attachData)
            assignedPS.clearIssuesAndPropagate();
        
        return issueList;
    }
    
    
    /**
     * Validate the contexts of a assigned privacy setting
     * 
     * @param assignedPS
     *            the assigned privacy setting
     * @param attachData
     *            set this flag true, if the given data should be attached with the issues
     * @return List with issues as result of the validation
     */
    private List<IIssue> validatePSContexts(IPresetAssignedPrivacySetting assignedPS, boolean attachData) {
        List<IIssue> issueList = new ArrayList<IIssue>();
        
        // Clear the attached issues, if the issues should be attached
        if (attachData)
            assignedPS.clearIssuesAndPropagate();
        
        return issueList;
    }
    
}
