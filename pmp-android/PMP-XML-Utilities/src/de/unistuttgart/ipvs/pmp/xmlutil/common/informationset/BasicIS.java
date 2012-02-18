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
package de.unistuttgart.ipvs.pmp.xmlutil.common.informationset;

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
     * This list contains all names.
     */
    protected List<LocalizedString> names = new ArrayList<LocalizedString>();
    
    /**
     * This list contains all descriptions.
     */
    protected List<LocalizedString> descriptions = new ArrayList<LocalizedString>();
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IBasicIS#getNames()
     */
    @Override
    public List<LocalizedString> getNames() {
        return this.names;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IBasicIS#getNameForLocale(java.util.Locale)
     */
    @Override
    public String getNameForLocale(Locale locale) {
        for (ILocalizedString name : this.names) {
            if (name.getLocale().getLanguage().equals(locale.getLanguage())) {
                return name.getString();
            }
        }
        return null;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IBasicIS#addName(de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.LocalizedString)
     */
    @Override
    public void addName(LocalizedString name) {
        this.names.add(name);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IBasicIS#removeName(de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.LocalizedString)
     */
    @Override
    public void removeName(ILocalizedString name) {
        this.names.remove(name);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IBasicIS#getDescriptions()
     */
    @Override
    public List<LocalizedString> getDescriptions() {
        return this.descriptions;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IBasicIS#getDescriptionForLocale(java.util.Locale)
     */
    @Override
    public String getDescriptionForLocale(Locale locale) {
        for (ILocalizedString descr : this.descriptions) {
            if (descr.getLocale().getLanguage().equals(locale.getLanguage())) {
                return descr.getString();
            }
        }
        return null;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IBasicIS#addDescription(de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.LocalizedString)
     */
    @Override
    public void addDescription(LocalizedString description) {
        this.descriptions.add(description);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IBasicIS#removeDescription(de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.LocalizedString)
     */
    @Override
    public void removeDescription(ILocalizedString description) {
        this.descriptions.remove(description);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IBasicIS#clearNameIssues()
     */
    @Override
    public void clearNameIssues() {
        for (LocalizedString name : this.getNames()) {
            name.clearIssuesAndPropagate();
        }
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IBasicIS#clearDescriptionIssues()
     */
    @Override
    public void clearDescriptionIssues() {
        for (LocalizedString descr : this.getDescriptions()) {
            descr.clearIssuesAndPropagate();
        }
    }
    
}
