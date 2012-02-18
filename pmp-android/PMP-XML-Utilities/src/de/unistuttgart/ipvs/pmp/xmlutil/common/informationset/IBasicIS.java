package de.unistuttgart.ipvs.pmp.xmlutil.common.informationset;

import java.util.List;
import java.util.Locale;

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssueLocation;

public interface IBasicIS extends IIssueLocation {
    
    /**
     * Get all names.
     * 
     * @return list with names
     */
    public abstract List<LocalizedString> getNames();
    
    
    /**
     * Get a name-string for a specific locale.
     * 
     * @param locale
     *            locale
     * @return the name-string for the given locale. Null, if no name for the
     *         given locale exists.
     */
    public abstract String getNameForLocale(Locale locale);
    
    
    /**
     * Add a name.
     * 
     * @param name
     *            name to add
     */
    public abstract void addName(LocalizedString name);
    
    
    /**
     * Remove a name.
     * 
     * @param name
     *            name to remove
     */
    public abstract void removeName(ILocalizedString name);
    
    
    /**
     * Get all descriptions.
     * 
     * @return list with descriptions
     */
    public abstract List<LocalizedString> getDescriptions();
    
    
    /**
     * Get a description-string for a specific locale.
     * 
     * @param locale
     *            locale
     * @return the description-string for the given locale. Null, if no description for the
     *         given locale exists.
     */
    public abstract String getDescriptionForLocale(Locale locale);
    
    
    /**
     * Add a description
     * 
     * @param description
     *            description to add
     */
    public abstract void addDescription(LocalizedString description);
    
    
    /**
     * Remove a description
     * 
     * @param description
     *            description to remove
     */
    public abstract void removeDescription(ILocalizedString description);
    
    
    /**
     * Clear all issues of the names
     */
    public abstract void clearNameIssues();
    
    
    /**
     * Clear all issues of the descriptions
     */
    public abstract void clearDescriptionIssues();
    
}
