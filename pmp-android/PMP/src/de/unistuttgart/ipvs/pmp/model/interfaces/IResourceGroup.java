package de.unistuttgart.ipvs.pmp.model.interfaces;

public interface IResourceGroup {

	public String getIdentifier();
	
	public String getName();
	
	public String getDescription();
	
	public IPrivacyLevel[] getPrivacyLevels();
}
