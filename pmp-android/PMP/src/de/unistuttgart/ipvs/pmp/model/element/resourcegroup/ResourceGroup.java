package de.unistuttgart.ipvs.pmp.model.element.resourcegroup;

import java.util.Locale;
import java.util.Map;

import android.graphics.drawable.Drawable;
import android.os.IBinder;
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
    protected de.unistuttgart.ipvs.pmp.resource.ResourceGroup link;
    
    
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
    public Drawable getIcon() {
        // TODO
        return null;
    }
    
    
    @Override
    public IPrivacySetting[] getPrivacySettings() {
        checkCached();
        return this.privacySettings.values().toArray(new IPrivacySetting[0]);
    }
    
    
    @Override
    public IPrivacySetting getPrivacySetting(String privacyLevelIdentifier) {
        checkCached();
        return this.privacySettings.get(privacyLevelIdentifier);
    }
    
    
    @Override
    public IBinder getResource(String appPackage, String resource) {
        return this.link.getResource(resource).getAndroidInterface(appPackage);
    }
    
    
    /* inter-model communication */
    
    public RgInformationSet getRgis() {
        checkCached();
        return this.rgis;
    }
    
}
