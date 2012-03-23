/*
 * Copyright 2012 pmp-android development team
 * Project: Editor
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
package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis.internal;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.Images;
import de.unistuttgart.ipvs.pmp.editor.util.I18N;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

public class PrivacySettingsLabelProvider extends LabelProvider {
    
    @Override
    public String getText(Object element) {
        if (element instanceof RGISPrivacySetting) {
            RGISPrivacySetting ps = (RGISPrivacySetting) element;
            return ps.getIdentifier() + " (" + ps.getValidValueDescription() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        if (element instanceof EncapsulatedString) {
            EncapsulatedString es = (EncapsulatedString) element;
            String string = es.getString();
            
            if (string == null) {
                string = I18N.general_undefined;
            }
            
            // Trim the string
            if (string.length() > 50) {
                string = string.substring(0, 50) + "..."; //$NON-NLS-1$
            }
            
            if (element instanceof NameString) {
                return I18N.general_name + ":" + string; //$NON-NLS-1$
            }
            if (element instanceof DescriptionString) {
                return I18N.general_description + ":" + string; //$NON-NLS-1$
            }
            
            if (element instanceof ChangeDescriptionString) {
                return I18N.editor_rgis_ps_changedescription + ":" + string; //$NON-NLS-1$
            }
        }
        
        return I18N.general_undefined;
    }
    
    
    @Override
    public Image getImage(Object element) {
        // Get PS
        RGISPrivacySetting ps = null;
        if (element instanceof RGISPrivacySetting) {
            ps = (RGISPrivacySetting) element;
            
            if (ps.getIssues().isEmpty()) {
                return Images.INFO16;
            } else {
                return Images.ERROR16;
            }
            
        } else if (element instanceof EncapsulatedString) {
            EncapsulatedString es = (EncapsulatedString) element;
            ps = es.getPrivacySetting();
            
            // Show icon only if this field is invalid
            for (IIssue i : ps.getIssues()) {
                IssueType type = i.getType();
                if (es instanceof NameString
                        && (type == IssueType.RGIS_PS_NAME_ISSUES || type == IssueType.NAME_LOCALE_EN_MISSING)) {
                    return Images.ERROR16;
                }
                
                if (es instanceof DescriptionString
                        && (type == IssueType.RGIS_PS_DESCRIPTION_ISSUES || type == IssueType.DESCRIPTION_LOCALE_EN_MISSING)) {
                    return Images.ERROR16;
                }
                
                if (es instanceof ChangeDescriptionString
                        && (type == IssueType.RGIS_PS_CHANGE_DESCRIPTION_ISSUES || type == IssueType.CHANGE_DESCRIPTION_LOCALE_EN_MISSING)) {
                    return Images.ERROR16;
                }
                
            }
            return Images.INFO16;
        }
        
        return null;
    }
}
