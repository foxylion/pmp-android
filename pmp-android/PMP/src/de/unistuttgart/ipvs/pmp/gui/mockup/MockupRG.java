package de.unistuttgart.ipvs.pmp.gui.mockup;

import java.util.HashMap;

import android.graphics.drawable.Drawable;
import android.os.IBinder;
import de.unistuttgart.ipvs.pmp.model.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.ResourceGroup;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSet;

/**
 * 
 * @author Tobias Kuhn
 * 
 */
public class MockupRG extends ResourceGroup {
    
    private Drawable icon;
    
    
    public MockupRG(String rgPackage, Drawable icon, RgInformationSet rgis) {
        super(rgPackage);
        this.privacySettings = new HashMap<String, PrivacySetting>();
        this.icon = icon;
        this.rgis = rgis;
        this.link = null;
    }
    
    
    @Override
    public void setPersistenceProvider(ElementPersistenceProvider<? extends ModelElement> persistenceProvider) {
        super.setPersistenceProvider(null);
    }
    
    
    @Override
    public Drawable getIcon() {
        return this.icon;
    }
    
    
    public void addPS(String name, MockupPrivacySetting ps) {
        this.privacySettings.put(name, ps);
        
    }
    
    
    @Override
    public IBinder getResource(String appPackage, String resource) {
        return null;
    }
    
}
