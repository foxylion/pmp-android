package de.unistuttgart.ipvs.pmp.model.element.resourcegroup;

import java.util.Locale;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySetting;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSet;

/**
 * @see IResourceGroup
 * @author Tobias Kuhn
 * 
 */
public class ResourceGroup extends ModelElement implements IResourceGroup {
    
    /**
     * localized values
     */
    protected RgInformationSet rgis;
    
    /**
     * internal data & links
     */
    protected Map<String, PrivacySetting> privacySettings;
    
    
    /* organizational */
    
    public ResourceGroup(String rgPackage) {
        super(rgPackage);
    }
    
    
    /* interface */
    
    @Override
    public String getName() {
        checkCached();
        String name = this.rgis.getNames().get(Locale.getDefault());
        if (name == null) {
            name = this.rgis.getNames().get(Locale.US);
        }
        return name;
    }
    
    
    @Override
    public String getDescription() {
        checkCached();
        String description = this.rgis.getDescriptions().get(Locale.getDefault());
        if (description == null) {
            description = this.rgis.getDescriptions().get(Locale.US);
        }
        return description;
    }
    
    
    @Override
    public IPrivacySetting[] getPrivacySettings() {
        checkCached();
        return this.privacySettings.values().toArray(new IPrivacySetting[0]);
    }
    
    
    @Override
    public IPrivacySetting getPrivacySetting(String privacyLevelIdentifier) {
        return this.privacySettings.get(privacyLevelIdentifier);
    }
    
    
    /* inter-model communication */
    
    public RgInformationSet getRgis() {
        return this.rgis;
    }
    
}
