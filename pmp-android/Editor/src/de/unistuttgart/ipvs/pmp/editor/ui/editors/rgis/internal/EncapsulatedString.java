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

import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.common.ILocalizedString;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue;

public abstract class EncapsulatedString {
    
    protected final RGISPrivacySetting ps;
    private final String value;
    
    
    public EncapsulatedString(String value, RGISPrivacySetting ps) {
        this.value = value;
        this.ps = ps;
    }
    
    
    public String getString() {
        return this.value;
    }
    
    
    public RGISPrivacySetting getPrivacySetting() {
        return this.ps;
    }
    
    
    public abstract List<ILocalizedString> getStringList();
    
    
    public List<IIssue> getIssues() {
        List<IIssue> issues = new ArrayList<IIssue>();
        for (ILocalizedString ls : getStringList()) {
            issues.addAll(ls.getIssues());
        }
        return issues;
    }
}
