package de.unistuttgart.ipvs.pmp.model.interfaces;

/**
 * The {@link IModel} provides all {@link IApp}s, {@link IPreset}s and
 * {@link IResourceGroup}s known by PMP.
 * 
 * @author Jakob Jarosch
 */
public interface IModel {

    /**
     * @return Returns all {@link IApp}s known by PMP.
     */
    public IApp[] getApps();

    /**
     * Register a new App at PMP.
     * 
     * @param identifier 
     * @param publicKey 
     */
    public boolean addApp(String identifier, String publicKey);
    
    /**
     * @return Returns all {@link IPreset}s known by PMP.
     */
    public IPreset[] getRoles();

    /**
     * @return Returns all {@link IResourceGroup}s known by PMP.
     */
    public IResourceGroup[] getResourceGroups();
    
    /**
     * Register a new ResourceGroup at PMP.
     * 
     * @param identifier
     * @param publicKey
     */
    public boolean addResourceGroup(String identifier, String publicKey);
}