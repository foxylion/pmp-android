package de.unistuttgart.ipvs.pmp.model;

import de.unistuttgart.ipvs.pmp.model.implementations.ModelImpl;
import de.unistuttgart.ipvs.pmp.model.interfaces.IModel;
import de.unistuttgart.ipvs.pmp.service.PMPService;

public class ModelSingleton {

	private static final ModelSingleton instance = new ModelSingleton();
	
	private ModelSingleton() {
		
	}
	
	public static ModelSingleton getInstance() {
		return instance;
	}
	
	public IModel getModel() {
		return new ModelImpl();
	}

	/**
	 * Is used to check authentification a {@link PMPService}.
	 * 
	 * @param identifier of the App
	 * @param token Token the App uses to identify
	 * @return true when authentification succeeded, otherwise false
	 */
	public boolean checkAppToken(String identifier, String token) {
		
		// TODO Implement the asymetric authentification system
		return true;
	}
	
	/**
	 * Is used to check authentification a {@link PMPService}.
	 * 
	 * @param identifier of the ResourceGroup
	 * @param token Token the ResourceGroup uses to identify
	 * @return true when authentification succeeded, otherwise false
	 */
	public boolean checkResourceGroupToken(String identifier, String token) {
		
		// TODO Implement the asymetric authentification system
		return true;
	}
}