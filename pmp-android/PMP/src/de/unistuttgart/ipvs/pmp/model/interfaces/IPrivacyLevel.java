package de.unistuttgart.ipvs.pmp.model.interfaces;

public interface IPrivacyLevel {
	
	public String getIdentifier();

	public String getName();
	
	public String getDescription();
	
	public String getValue();
	
	public IResourceGroup getResourceGroup();
	
	public String getHumanReadableValue(String value);
}
