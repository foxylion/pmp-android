package de.unistuttgart.ipvs.pmp.model.interfaces;

import de.unistuttgart.ipvs.pmp.PMPComponentType;

/**
 * {@link IPreset} is the chain link between the {@link IResourceGroup} and its
 * {@link IPrivacyLevel}s and the {@link IApp}. It gives the {@link IApp} access
 * to the defined {@link IPrivacyLevel}s.
 * 
 * @author Jakob Jarosch
 */
public interface IPreset {

    /**
     * @return Returns the name of the preset.
     */
    public String getName();

    /**
     * @return Returns the type of the identifier which may be assigned.
     */
    PMPComponentType getType();
    
    /**
     * @return Returns the identifier which created this
     *         preset, or NULL if this preset was generated manually.
     */
    public String getIdentifier();

    /**
     * @return Returns the description of the preset.
     */
    public String getDescription();

    /**
     * @return Returns the assigned {@link IApp}s to this preset.
     */
    public IApp[] getAssignedApps();
    
    /**
     * @return Returns true if the {@link IApp} is assigned, otherwise false.
     */
    public boolean isAppAssigned(IApp app);
    
    /**
     * Adds an {@link IApp} to the {@link IPreset}.
     * Inside the {@link IApp#verifyServiceLevel()) will be called.
     * 
     * @param app {@link IApp} which should be added.
     */
    public void addApp(IApp app);
    
    public void addApp(IApp app, boolean hidden);
    
    /**
     * Removes an {@link IApp} from the {@link IPreset}.
     * Inside the {@link IApp#verifyServiceLevel()} whill be called, to prevent this, call {@link IPreset#removeApp(IApp, boolean)}.
     * 
     * @param app {@link IApp} which should be removed
     */
    public void removeApp(IApp app);
    
    /**
     * Removes an {@link IApp} from the {@link IPreset}.
     * 
     * @param app {@link IApp} which should be removed
     * @param hidden If set to true, the verification described in {@link IPreset#removeApp(IApp)} will not be called.
     */
    public void removeApp(IApp app, boolean hidden);
    


    public void setPrivacyLevel(IPrivacyLevel privacyLevel);
    
    public void setPrivacyLevel(IPrivacyLevel privacyLevel, boolean hidden);
    
    public void removePrivacyLevel(IPrivacyLevel privacyLevel);
    
    public void removePrivacyLevel(IPrivacyLevel privacyLevel, boolean hidden);
    
    /**
     * @return Returns the used {@link IPrivacyLevel}s by this preset.
     */
    public IPrivacyLevel[] getUsedPrivacyLevels();
}
