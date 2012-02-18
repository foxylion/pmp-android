package de.unistuttgart.ipvs.pmp.xmlutil.common;

import java.util.Locale;

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssueLocation;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public interface ILocalizedString extends IIssueLocation {
    
    /**
     * Get the name
     * 
     * @return name
     */
    public abstract String getString();
    
    
    /**
     * Set the name
     * 
     * @param name
     *            name to set
     */
    public abstract void setString(String name);
    
    
    /**
     * Get the locale
     * 
     * @return locale
     */
    public abstract Locale getLocale();
    
    
    /**
     * Set the locale
     * 
     * @param locale
     *            locale to set
     */
    public abstract void setLocale(Locale locale);
    
}
