package de.unistuttgart.ipvs.pmp.model.interfaces;

public interface IApp {
	
	public String getIdentifier();

	public String getName();
	
	public String getDescription();
	
	public IServiceLevel[] getServiceLevels();
	
	public int getActiveServiceLevel();
	
	public void setServiceLevel(int serviceLevel);
}
