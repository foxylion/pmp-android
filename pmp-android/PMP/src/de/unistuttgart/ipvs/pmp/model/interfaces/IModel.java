package de.unistuttgart.ipvs.pmp.model.interfaces;

import de.unistuttgart.ipvs.pmp.PMPComponentType;

/**
 * The {@link IModel} provides all {@link IApp}s, {@link IPreset}s and {@link IResourceGroup}s known by PMP.
 * 
 * That interface can got by calling <code>ModelSingleton.getInstance().getModel();</code>.
 * 
 * @author Jakob Jarosch
 */
public interface IModel {
    
    /**
     * @return Returns all {@link IApp}s known by PMP.
     */
    public IApp[] getApps();
    
    
    /**
     * Returns the corresponding {@link IApp} to an identifier of an {@link IApp}.<br>
     * This method can be used when you have only a identifier for the {@link IApp}.
     * 
     * @param identifier
     *            Corresponding {@link IApp} identifier
     * @return Returns the requested {@link IApp} or NULL if the {@link IApp} does not exists in PMP.
     */
    public IApp getApp(String identifier);
    
    
    /**
     * Registers a new {@link IApp} at PMP.
     * 
     * <p>
     * <b>This method is executed asynchronously so the termination of this method will not mean the {@link IApp}
     * registration has succeeded.</b>
     * </p>
     * 
     * @param identifier
     *            The identifier for the {@link IApp} which should be registered.
     * @param publicKey
     *            The publicKey for the {@link IApp} which should be registered.
     */
    public void addApp(String identifier, byte[] publicKey);
    
    
    /**
     * @return Returns all {@link IResourceGroup}s known by PMP.
     */
    public IResourceGroup[] getResourceGroups();
    
    
    /**
     * Returns the corresponding {@link IResourceGroup} to an identifier of a {@link IResourceGroup}.<br>
     * This method can be used when you have only a identifier for the {@link IResourceGroup}.
     * 
     * @param identifier
     *            Corresponding {@link IResourceGroup} identifier
     * @return Returns the requested {@link IResourceGroup} or NULL if the {@link IResourceGroup} does not exists in
     *         PMP.
     */
    public IResourceGroup getResourceGroup(String identifier);
    
    
    /**
     * Registers a new {@link IResourceGroup} at PMP.
     * 
     * <p>
     * <b>This method is executed asynchronously so the termination of this method will not mean the
     * {@link IResourceGroup} registration has succeeded.</b>
     * </p>
     * 
     * @param identifier
     *            The identifier for the {@link IResourceGroup} which should be registered.
     * @param publicKey
     *            The publicKey for the {@link IResourceGroup} which should be registered.
     */
    public void addResourceGroup(String identifier, byte[] publicKey);
    
    
    /**
     * @return Returns all {@link IPreset}s known by PMP.
     */
    public IPreset[] getPresets();
    
    
    /**
     * Adds a new {@link IPreset} to PMP.
     * 
     * @param name
     *            The name of the {@link IPreset}.
     * @param description
     *            The description of the {@link IPreset}.
     * @param type
     *            The {@link PMPComponentType} of the {@link IPreset}. Use {@link PMPComponentType#NONE} if you want to
     *            create a {@link IPreset} with no reference to an {@link IApp} or {@link IResourceGroup}.
     * @param identifier
     *            The identifier to the corresponding {@link PMPComponentType}. Use NULL if the
     *            {@link PMPComponentType#NULL} was set.
     * @return
     */
    public IPreset addPreset(String name, String description, PMPComponentType type, String identifier);
    
}
