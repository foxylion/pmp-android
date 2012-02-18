package de.unistuttgart.ipvs.pmp.model.element.resourcegroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.graphics.drawable.Drawable;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.model.assertion.Assert;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelMisuseError;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySetting;
import de.unistuttgart.ipvs.pmp.resource.Resource;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * @see IResourceGroup
 * @author Tobias Kuhn
 * 
 */
public class ResourceGroup extends ModelElement implements IResourceGroup {
    
    /**
     * localized values
     */
    protected RGIS rgis;
    
    /**
     * internal data & links
     */
    protected Map<String, PrivacySetting> privacySettings;
    protected Drawable icon;
    protected de.unistuttgart.ipvs.pmp.resource.ResourceGroup link;
    protected long revision;
    
    /**
     * will be used when the linked resource fails somehow.
     */
    protected Throwable unexpectedThrowable;
    
    
    /* organizational */
    
    public ResourceGroup(String rgPackage) {
        super(rgPackage);
        this.unexpectedThrowable = null;
    }
    
    
    @Override
    public String toString() {
        return super.toString()
                + String.format(" [rgis = %s, ps = %s, link = %s, rev = %d, ut = %s]", this.rgis,
                        ModelElement.collapseMapToString(this.privacySettings), this.link, this.revision,
                        this.unexpectedThrowable);
    }
    
    
    /* interface */
    
    @Override
    public String getName() {
        checkCached();
        String name = this.rgis.getNameForLocale(Locale.getDefault());
        if (name == null) {
            name = this.rgis.getNameForLocale(Locale.ENGLISH);
        }
        return name;
    }
    
    
    @Override
    public String getDescription() {
        checkCached();
        String description = this.rgis.getDescriptionForLocale(Locale.getDefault());
        if (description == null) {
            description = this.rgis.getDescriptionForLocale(Locale.ENGLISH);
        }
        return description;
    }
    
    
    @Override
    public Drawable getIcon() {
        checkCached();
        return this.icon;
    }
    
    
    @Override
    public long getRevision() {
        checkCached();
        return this.revision;
    }
    
    
    @Override
    public List<IPrivacySetting> getPrivacySettings() {
        checkCached();
        return new ArrayList<IPrivacySetting>(this.privacySettings.values());
    }
    
    
    @Override
    public IPrivacySetting getPrivacySetting(String privacySettingIdentifier) {
        checkCached();
        Assert.nonNull(privacySettingIdentifier, ModelMisuseError.class, Assert.ILLEGAL_NULL,
                "privacySettingIdentifier", privacySettingIdentifier);
        return this.privacySettings.get(privacySettingIdentifier);
    }
    
    
    @Override
    public IBinder getResource(String appPackage, String resource) {
        checkCached();
        Assert.nonNull(appPackage, ModelMisuseError.class, Assert.ILLEGAL_NULL, "appPackage", appPackage);
        Assert.nonNull(resource, ModelMisuseError.class, Assert.ILLEGAL_NULL, "resource", resource);
        
        Resource res = null;
        try {
            res = this.link.getResource(resource);
        } catch (Throwable t) {
            deactivate(t);
        }
        
        if (res != null) {
            return res.getAndroidInterface(appPackage);
        } else {
            return null;
        }
    }
    
    
    @Override
    public boolean isDeactivated() {
        return this.unexpectedThrowable != null;
    }
    
    
    @Override
    public Throwable getReasonForDeactivation() {
        return this.unexpectedThrowable;
    }
    
    
    /* inter-model communication */
    
    public RGIS getRgis() {
        checkCached();
        return this.rgis;
    }
    
    
    public void deactivate(Throwable t) {
        this.unexpectedThrowable = t;
    }
    
    
    @Override
    public boolean checkCached() {
        if (this.unexpectedThrowable != null) {
            throw new IllegalStateException("ResourceGroup is deactivated.");
        }
        return super.checkCached();
    }
    
}
