package de.unistuttgart.ipvs.pmp.resourcegroups.location;

public enum UseLocationDescriptionEnum {
	
	NONE("None"), COUNTRY("Country"), CITY("City"), STREET("Street");
	
	private String name;
	
	
	UseLocationDescriptionEnum(String humanReadableName) {
		this.name = humanReadableName;
	}
	
	
	@Override
	public String toString() {
		return this.name;
	}
}
