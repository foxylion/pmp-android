package de.unistuttgart.ipvs.pmp.model.element.privacysetting;

import java.util.Locale;

import android.view.View;
import de.unistuttgart.ipvs.pmp.model.PersistenceConstants;
import de.unistuttgart.ipvs.pmp.model.element.ModelElement;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.ResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.PrivacyLevel;
import de.unistuttgart.ipvs.pmp.resource.privacylevel.PrivacyLevelValueException;

/**
 * @see IPrivacySetting
 * @author Tobias Kuhn
 * 
 */
public class PrivacySetting extends ModelElement implements IPrivacySetting {
    
    /**
     * identifying attributes
     */
    protected ResourceGroup resourceGroup;
    protected String localIdentifier;
    
    /**
     * internal data & links
     */
    protected PrivacyLevel<?> link;
    
    
    /* organizational */
    
    public PrivacySetting(ResourceGroup resourceGroup, String identifier) {
        super(resourceGroup.getIdentifier() + PersistenceConstants.PACKAGE_SEPARATOR + identifier);
        this.resourceGroup = resourceGroup;
        this.localIdentifier = identifier;
    }
    
    
    /* interface */
    
    @Override
    public IResourceGroup getResourceGroup() {
        return this.resourceGroup;
    }
    
    
    @Override
    public String getName() {
        String name = this.resourceGroup.getRgis().getPrivacySettingsMap().get(getIdentifier()).getNames()
                .get(Locale.getDefault());
        if (name == null) {
            name = this.resourceGroup.getRgis().getPrivacySettingsMap().get(getIdentifier()).getNames().get(Locale.US);
        }
        return name;
    }
    
    
    @Override
    public String getDescription() {
        String description = this.resourceGroup.getRgis().getPrivacySettingsMap().get(getIdentifier())
                .getDescriptions().get(Locale.getDefault());
        if (description == null) {
            description = this.resourceGroup.getRgis().getPrivacySettingsMap().get(getIdentifier()).getDescriptions()
                    .get(Locale.US);
        }
        return description;
    }
    
    
    @Override
    public String getLocalIdentifier() {
        return this.localIdentifier;
    }
    
    
    @Override
    public boolean isValueValid(String value) {
        checkCached();
        try {
            this.link.parseValue(value);
            return true;
        } catch (PrivacyLevelValueException plve) {
            return false;
        }
    }
    
    
    @Override
    public String getHumanReadableValue(String value) throws PrivacyLevelValueException {
        checkCached();
        return this.link.getHumanReadableValue(value);
    }
    
    
    @Override
    public boolean permits(String reference, String value) throws PrivacyLevelValueException {
        checkCached();
        return this.link.permits(value, reference);
    }
    
    
    @Override
    public View getView() {
        checkCached();
        return this.link.getView();
    }
    
    
    @Override
    public String getViewValue(View view) {
        checkCached();
        return this.link.getViewValue(view);
    }
    
    
    @Override
    public void setViewValue(View view, String value) throws PrivacyLevelValueException {
        checkCached();
        this.link.setViewValue(view, value);
    }
    
}
