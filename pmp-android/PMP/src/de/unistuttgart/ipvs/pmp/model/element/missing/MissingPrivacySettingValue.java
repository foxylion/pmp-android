package de.unistuttgart.ipvs.pmp.model.element.missing;

import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySetting;

/**
 * Object to inform about missing {@link PrivacySetting} values.
 * 
 * @author Tobias Kuhn
 * 
 */
public class MissingPrivacySettingValue {
    
    private final String rg, ps, value;
    
    
    public MissingPrivacySettingValue(String rg, String ps, String value) {
        this.rg = rg;
        this.ps = ps;
        this.value = value;
    }
    
    
    public String getResourceGroup() {
        return this.rg;
    }
    
    
    public String getPrivacySetting() {
        return this.ps;
    }
    
    
    public String getValue() {
        return this.value;
    }
    
    
    @Override
    public String toString() {
        return String.format("%s [%s,%s,'%s']", super.toString(), this.rg, this.ps, this.value);
    }
    
}
