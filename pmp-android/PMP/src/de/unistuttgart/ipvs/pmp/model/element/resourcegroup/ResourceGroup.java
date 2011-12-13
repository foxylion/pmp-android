package de.unistuttgart.ipvs.pmp.model.element.resourcegroup;

import java.util.Locale;
import java.util.Map;

import android.graphics.drawable.Drawable;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.model.assertion.Assert;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelMisuseError;
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
    
    protected int version;
    // FIXME: Warum gibt es dieses Attribut? Alle im Model registrierten Komponenten müssen installiert sein.
    protected boolean isInstalled;
    
    
    /* organizational */
    
    public ResourceGroup(String rgPackage) {
        super(rgPackage);
        this.version = 0;
        this.isInstalled = false;
    }
    
    @Override
    public String toString() {
        return super.toString()
                + String.format(" [rgis = %s, ps = %s, link = %s]", this.rgis.toString(),
                        ModelElement.collapseMapToString(this.privacySettings), this.link.toString());
    }
    
    
    /* interface */
    
    @Override
    public String getName() {
        checkCached();
        String name = this.rgis.getNames().get(Locale.getDefault());
        if (name == null) {
            name = this.rgis.getNames().get(Locale.ENGLISH);
        }
        return name;
    }
    
    
    @Override
    public String getDescription() {
        checkCached();
        String description = this.rgis.getDescriptions().get(Locale.getDefault());
        if (description == null) {
            description = this.rgis.getDescriptions().get(Locale.ENGLISH);
        }
        return description;
    }
    
    
    @Override
    public Drawable getIcon() {
        // TODO: Resourcegroup contains icon in root folder of the jar-file with name "icon.png"
        return null;
    }
    
    @Override
    public int getVersion() {
        checkCached();
        return this.version;
    }
    
    @Override
    public boolean isInstalled() {
        // FIXME: Warum gibt es diese Funktion? Alle im Model registrierten Komponenten müssen installiert sein.
        checkCached();
        return this.isInstalled;
    }
    
    @Override
    public void setInstalled(boolean flag) {
       // FIXME: Warum gibt es diese Funktion? Alle im Model registrierten Komponenten müssen installiert sein.
       this.isInstalled = flag;
    }
    
    @Override
    public IPrivacySetting[] getPrivacySettings() {
        checkCached();
        return this.privacySettings.values().toArray(new IPrivacySetting[0]);
    }
    
    
    @Override
    public IPrivacySetting getPrivacySetting(String privacySettingIdentifier) {
        checkCached();
        Assert.nonNull(privacySettingIdentifier, new ModelMisuseError(Assert.ILLEGAL_NULL, "privacySettingIdentifier",
                privacySettingIdentifier));
        return this.privacySettings.get(privacySettingIdentifier);
    }
    
    
    @Override
    public IBinder getResource(String appPackage, String resource) {
        checkCached();
        Assert.nonNull(appPackage, new ModelMisuseError(Assert.ILLEGAL_NULL, "appPackage", appPackage));
        Assert.nonNull(resource, new ModelMisuseError(Assert.ILLEGAL_NULL, "resource", resource));
        return this.link.getResource(resource).getAndroidInterface(appPackage);
    }
    
    
    /* inter-model communication */
    public RgInformationSet getRgis() {
        checkCached();
        return this.rgis;
    }
    
}
