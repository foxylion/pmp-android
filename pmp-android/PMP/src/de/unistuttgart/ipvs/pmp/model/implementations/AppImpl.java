package de.unistuttgart.ipvs.pmp.model.implementations;

import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPreset;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;

/**
 * Implementation of the {@link IApp} interface.
 * 
 * @author Jakob Jarosch
 */
public class AppImpl implements IApp {

    private String identifier;
    private String name;
    private String description;
    
    public AppImpl(String identifier, String name, String description) {
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
    public IServiceLevel[] getServiceLevels() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public IServiceLevel getServiceLevel(int ordering) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public int getActiveServiceLevel() {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public void setActiveServiceLevel(int serviceLevel) {
	// TODO Auto-generated method stub
	throw new UnsupportedOperationException();
    }

    @Override
    public IPreset[] getAssignedPresets() {
	// TODO Auto-generated method stub
	return null;
    }

}
