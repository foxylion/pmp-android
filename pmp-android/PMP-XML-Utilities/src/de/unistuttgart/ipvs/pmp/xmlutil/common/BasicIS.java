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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueLocation;

/**
 * This class abstracts common used fields and methods for information sets
 * 
 * @author Marcus Vetter
 * 
 */
public abstract class BasicIS extends IssueLocation implements IBasicIS {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = -7705624993277295194L;
    
    /**
     * This list contains all names as {@link ILocalizedString}s.
     */
    protected List<ILocalizedString> names = new ArrayList<ILocalizedString>();
    
    /**
     * This list contains all descriptions as {@link ILocalizedString}s.
     */
    protected List<ILocalizedString> descriptions = new ArrayList<ILocalizedString>();
    
    
    @Override
    public List<ILocalizedString> getNames() {
        return this.names;
    }
    
    
    @Override
    public String getNameForLocale(Locale locale) {
        for (ILocalizedString name : this.names) {
            if (name.getLocale() == null) {
                continue;
            }
            if (name.getLocale().getLanguage().equals(locale.getLanguage())) {
                return name.getString();
            }
        }
        return null;
    }
    
    
    @Override
    public void addName(ILocalizedString name) {
        this.names.add(name);
    }
    
    
    @Override
    public void removeName(ILocalizedString name) {
        this.names.remove(name);
    }
    
    
    @Override
    public List<ILocalizedString> getDescriptions() {
        return this.descriptions;
    }
    
    
    @Override
    public String getDescriptionForLocale(Locale locale) {
        for (ILocalizedString descr : this.descriptions) {
            if (descr.getLocale() == null) {
                continue;
            }
            if (descr.getLocale().getLanguage().equals(locale.getLanguage())) {
                return descr.getString();
            }
        }
        return null;
    }
    
    
    @Override
    public void addDescription(ILocalizedString description) {
        this.descriptions.add(description);
    }
    
    
    @Override
    public void removeDescription(ILocalizedString description) {
        this.descriptions.remove(description);
    }
    
    
    @Override
    public void clearNameIssues() {
        for (ILocalizedString name : getNames()) {
            name.clearIssues();
        }
    }
    
    
    @Override
    public void clearDescriptionIssues() {
        for (ILocalizedString descr : getDescriptions()) {
            descr.clearIssues();
        }
    }
    
}
