package de.unistuttgart.ipvs.pmp.xmlutil.ais;

import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.IIdentifierIS;

public interface IAISRequiredPrivacySetting extends IIdentifierIS {
    
    /**
     * Get the value
     * 
     * @return value
     */
    public abstract String getValue();
    
    
    /**
     * Set the value
     * 
     * @param value
     *            value to set
     */
    public abstract void setValue(String value);
    
}
