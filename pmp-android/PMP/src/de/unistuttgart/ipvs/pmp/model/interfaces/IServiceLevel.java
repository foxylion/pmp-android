package de.unistuttgart.ipvs.pmp.model.interfaces;

public interface IServiceLevel {
	
	public String getOrdering();
	
	public String getName();
	
	public String getDescription();
	
	public IPrivacyLevel[] getPrivacyLevels();
}
