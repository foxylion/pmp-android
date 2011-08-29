package de.unistuttgart.ipvs.pmp.model.interfaces;

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
	 * @return Returns the description of the preset.
	 */
	public String getDescription();

	/**
	 * @return Returns the assigned {@link IApp}s to this preset.
	 */
	public IApp[] getAssignedApps();

	/**
	 * @return Returns the used {@link IPrivacyLevel}s by this preset.
	 */
	public IPrivacyLevel[] getUsedPrivacyLevels();
}
