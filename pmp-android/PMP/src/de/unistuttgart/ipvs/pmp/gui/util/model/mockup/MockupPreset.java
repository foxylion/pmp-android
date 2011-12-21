package de.unistuttgart.ipvs.pmp.gui.util.model.mockup;

import java.util.ArrayList;
import java.util.HashMap;

import de.unistuttgart.ipvs.pmp.model.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.IModelElement;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.preset.Preset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;

/**
 * 
 * @author Tobias Kuhn
 * 
 */
public class MockupPreset extends Preset {
    
    public MockupPreset(IModelElement creator, String identifier, String name, String description) {
        super(creator, identifier);
        this.name = name;
        this.description = description;
        this.deleted = false;
        this.assignedApps = new ArrayList<IApp>();
        this.containsUnknownElements = false;
        this.privacySettingValues = new HashMap<IPrivacySetting, String>();
    }
    
    
    @Override
    public void setPersistenceProvider(ElementPersistenceProvider<? extends ModelElement> persistenceProvider) {
        super.setPersistenceProvider(null);
    }
    
    
    public void setAvailable(boolean available) {
        this.containsUnknownElements = !available;
    }
    
    
    @Override
    public void assignApp(IApp app) {
        try {
            super.assignApp(app);
        } catch (Throwable t) {
        }
    }
    
    
    @Override
    public void removeApp(IApp app) {
        try {
            super.removeApp(app);
        } catch (Throwable t) {
        }
    }
}
