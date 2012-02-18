package de.unistuttgart.ipvs.pmp.xmlutil.rgis;

import java.util.List;
import java.util.Locale;

import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IBasicIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IIdentifierIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.ILocalizedString;

public interface IRGISPrivacySetting extends IBasicIS, IIdentifierIS {
    
    /**
     * Get the description for valid values
     * 
     * @return description for valid values
     */
    public abstract String getValidValueDescription();
    
    
    /**
     * Set the description for valid values
     * 
     * @param validValueDescription
     *            description for valid values
     */
    public abstract void setValidValueDescription(String validValueDescription);
    
    
    /**
     * Add a change descriptions to the privacy setting
     * 
     * @param changeDescription
     *            change description to add
     */
    public abstract void addChangeDescription(ILocalizedString changeDescription);
    
    
    /**
     * Get the list which contains all change descriptions
     * 
     * @return list with change descriptions
     */
    public abstract List<ILocalizedString> getChangeDescriptions();
    
    
    /**
     * Remove a change description from the privacy setting
     * 
     * @param changeDescription
     *            change description to remove
     */
    public abstract void removeChangeDescription(ILocalizedString changeDescription);
    
    
    /**
     * Get a change description-string for a specific locale.
     * 
     * @param locale
     *            locale
     * @return the change description-string for the given locale. Null, if no change description for the
     *         given locale exists.
     */
    public abstract String getChangeDescriptionForLocale(Locale locale);
    
}
