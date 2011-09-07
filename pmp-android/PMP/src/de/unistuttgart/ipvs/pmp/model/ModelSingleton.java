package de.unistuttgart.ipvs.pmp.model;

import de.unistuttgart.ipvs.pmp.model.implementations.ModelImpl;
import de.unistuttgart.ipvs.pmp.model.interfaces.IModel;
import de.unistuttgart.ipvs.pmp.service.PMPService;

/**
 * {@link ModelSingleton} provides an instance of the {@link IModel} for direct
 * access to the model data.
 * 
 * @author Jakob Jarosch
 */
public class ModelSingleton {

    /**
     * The instance.
     */
    private static final ModelSingleton instance = new ModelSingleton();

    /**
     * Private constructor, prevents direct object creation.
     */
    private ModelSingleton() {

    }

    /**
     * @return Returns an instance of the {@link ModelSingleton} class.
     */
    public static ModelSingleton getInstance() {
	return instance;
    }

    /**
     * @return Returns an instance of the {@link IModel} interface.
     */
    public IModel getModel() {
	return new ModelImpl();
    }

    /**
     * Is used to check authentication a {@link PMPService}.
     * 
     * @param identifier
     *            of the App
     * @param token
     *            Token the App uses to identify
     * @return true when authentication succeeded, otherwise false
     */
    public boolean checkAppToken(String identifier, byte[] token) {

	// TODO Implement the asymmetric authentication system
	return true;
    }

    /**
     * Is used to check authentication a {@link PMPService}.
     * 
     * @param identifier
     *            of the ResourceGroup
     * @param token
     *            Token the ResourceGroup uses to identify
     * @return true when authentication succeeded, otherwise false
     */
    public boolean checkResourceGroupToken(String identifier, byte[] token) {

	// TODO Implement the asymmetric authentication system
	return true;
    }
}