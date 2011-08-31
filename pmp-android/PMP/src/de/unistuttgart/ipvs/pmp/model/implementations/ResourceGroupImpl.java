package de.unistuttgart.ipvs.pmp.model.implementations;

import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;

/**
 * Implementation of the {@link IResourceGroup} interface.
 * 
 * @author Jakob Jarosch
 */
public class ResourceGroupImpl implements IResourceGroup {

    private String identifier;
    private String name;
    private String description;

    public ResourceGroupImpl(String identifier, String name, String description) {
	this.identifier = identifier;
	this.name = name;
	this.description = description;
    }

    @Override
    public String getIdentifier() {
	return identifier;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public String getDescription() {
	return description;
    }

    @Override
    public IPrivacyLevel[] getPrivacyLevels() {
	// TODO Auto-generated method stub
	return null;
    }

}
