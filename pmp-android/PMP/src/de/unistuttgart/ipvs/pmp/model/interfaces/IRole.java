package de.unistuttgart.ipvs.pmp.model.interfaces;

/**
 * {@link IRole} is the chain link between the {@link IResourceGroup} and its
 * {@link IPrivacyLevel}s and the {@link IApp}. It gives the {@link IApp} access
 * to the defined {@link IPrivacyLevel}s.
 * 
 * @author Jakob Jarosch
 */
public interface IRole {

	/**
	 * @return Returns the name of the role.
	 */
	public String getName();

	/**
	 * @return Returns the description of the role.
	 */
	public String getDescription();

	/**
	 * @return Returns the assigned {@link IApp}s to this role.
	 */
	public IApp[] getAssignedApps();

	/**
	 * @return Returns the used {@link IPrivacyLevel}s by this role.
	 */
	public IPrivacyLevel[] getUsedPrivacyLevels();
}
