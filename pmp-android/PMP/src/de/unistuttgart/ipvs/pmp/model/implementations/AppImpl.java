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

    @Override
    public String getIdentifier() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getName() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getDescription() {
	// TODO Auto-generated method stub
	return null;
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
