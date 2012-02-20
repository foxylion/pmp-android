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
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetPSContext;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetSet;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.Issue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

/**
 * Validator for PresetSet
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
        
        // Clear all issues
        if (attachData) {
            clearIssuesAndPropagate(presetSet);
        }
        
        // Validate all preset
        for (IPreset preset : presetSet.getPresets()) {
            
            // Clear the attached issues, if the issues should be attached
            if (attachData) {
                preset.clearIssues();
            }
            
            /*
             * Validate, if the identifier is set
             */
            if (!checkValueSet(preset.getIdentifier())) {
                issueList.add(new Issue(IssueType.IDENTIFIER_MISSING, preset));
            }
            
            /*
             * Validate, if the creator is set
             */
            if (!checkValueSet(preset.getCreator())) {
                issueList.add(new Issue(IssueType.CREATOR_MISSING, preset));
            }
            
            /*
             * Validate, if the name is set
             */
            if (!checkValueSet(preset.getName())) {
                issueList.add(new Issue(IssueType.NAME_MISSING, preset));
            }
            
            // Validate all assigned apps
            issueList.addAll(validateAssignedApps(preset, attachData));
            
            // Validate all assigned privacy settings
            issueList.addAll(validateAssignedPrivacySettings(preset, attachData));
            
        }
        
        // Attach data
        attachData(issueList, attachData);
        
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
        
        // Validate all assigned apps
        for (IPresetAssignedApp app : preset.getAssignedApps()) {
            // Clear the attached issues, if the issues should be attached
            if (attachData) {
                app.clearIssues();
            }
            
            // Validate, if the identifier is set
            if (!checkValueSet(app.getIdentifier())) {
                issueList.add(new Issue(IssueType.IDENTIFIER_MISSING, app));
            }
        }
        
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
        
        // Validata all assgined privacy settings
        for (IPresetAssignedPrivacySetting assignedPS : preset.getAssignedPrivacySettings()) {
            
            /*
             * Validate, if the rgIdentifier is set
             */
            if (!checkValueSet(assignedPS.getRgIdentifier())) {
                issueList.add(new Issue(IssueType.RG_IDENTIFIER_MISSING, assignedPS));
            }
            
            /*
             * Validate, if the rgMinRevision is set
             */
            if (!checkValueSet(assignedPS.getRgRevision())) {
                issueList.add(new Issue(IssueType.RG_REVISION_MISSING, assignedPS));
            } else {
                /*
                 * Validate, if the rgMinRevision is valid
                 */
                try {
                    Integer.valueOf(assignedPS.getRgRevision());
                } catch (NumberFormatException nfe) {
                    Issue issue = new Issue(IssueType.RG_REVISION_INVALID, assignedPS);
                    issue.addParameter(assignedPS.getRgRevision());
                    issueList.add(issue);
                }
            }
            
            /*
             * Validate, if the psIdentifier is set
             */
            if (!checkValueSet(assignedPS.getPsIdentifier())) {
                issueList.add(new Issue(IssueType.PS_IDENTIFIER_MISSING, assignedPS));
            }
            
            /*
             * Validate, if the value is set
             */
            if (!checkValueSet(assignedPS.getValue())) {
                issueList.add(new Issue(IssueType.VALUE_MISSING, assignedPS));
            }
            
            // Validate all contexts
            issueList.addAll(validatePSContexts(assignedPS, attachData));
            
        }
        
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
        
        for (IPresetPSContext context : assignedPS.getContexts()) {
            /*
             * Validate, if the type is set
             */
            if (!checkValueSet(context.getType())) {
                issueList.add(new Issue(IssueType.TYPE_MISSING, context));
            }
            
            /*
             * Validate, if the condition is set
             */
            if (!checkValueSet(context.getCondition())) {
                issueList.add(new Issue(IssueType.CONDITION_MISSING, context));
            }
            
            /*
             * Validate, if the override value is set
             */
            if (!checkValueSet(context.getOverrideValue())) {
                issueList.add(new Issue(IssueType.OVERRIDE_VALUE_MISSING, context));
            }
            
        }
        
        return issueList;
    }
    
    
    /**
     * Clear all issues, begin at the given presetSet and propagate
     * 
     * @param presetSet
     *            The IPresetSet
     */
    public void clearIssuesAndPropagate(IPresetSet presetSet) {
        presetSet.clearIssues();
        for (IPreset preset : presetSet.getPresets()) {
            preset.clearIssues();
            for (IPresetAssignedApp app : preset.getAssignedApps()) {
                app.clearIssues();
            }
            for (IPresetAssignedPrivacySetting ps : preset.getAssignedPrivacySettings()) {
                ps.clearIssues();
                for (IPresetPSContext context : ps.getContexts()) {
                    context.clearIssues();
                }
            }
        }
    }
    
}
