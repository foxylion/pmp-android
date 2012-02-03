package de.unistuttgart.ipvs.pmp.jpmpps.model;

import java.util.HashMap;
import java.util.Map;

public class Model {

	private static Model instance = null;
	
	private Model() {
		
	}
	
	public static Model get() {
		if(instance == null) {
			instance = new Model();
		}
		
		return instance;
	}
	

	private Map<String, ResourceGroup> resourceGroups = new HashMap<String, ResourceGroup>();
	
	public Map<String, ResourceGroup> getResourceGroups() {
		return resourceGroups;
	}
}
