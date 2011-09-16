package de.unistuttgart.ipvs.pmp.model;

import de.unistuttgart.ipvs.pmp.model.implementations.ModelImpl;
import de.unistuttgart.ipvs.pmp.model.interfaces.IModel;

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
     * @return Returns the instance of the {@link ModelSingleton} class.
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
}