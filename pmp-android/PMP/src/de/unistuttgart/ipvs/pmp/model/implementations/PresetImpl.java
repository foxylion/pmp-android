package de.unistuttgart.ipvs.pmp.model.implementations;

import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPreset;

/**
 * Implementation of the {@link IPreset} interface.
 * 
 * @author Jakob Jarosch
 */
public class PresetImpl implements IPreset {

    private String name;
    private String resourceGroupIdentifier;
    private String description;

    public PresetImpl(String name, String resourceGroupIdentifier,
	    String description) {
	this.name = name;
	this.resourceGroupIdentifier = resourceGroupIdentifier;
	this.description = description;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public String getResourceGroupIdentifier() {
	return resourceGroupIdentifier;
    }

    @Override
    public String getDescription() {
	return description;
    }

    @Override
    public IApp[] getAssignedApps() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public IPrivacyLevel[] getUsedPrivacyLevels() {
	// TODO Auto-generated method stub
	return null;
    }
}
