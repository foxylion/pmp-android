package de.unistuttgart.ipvs.pmp.service.app;

import de.unistuttgart.ipvs.pmp.service.RegistrationState;
import de.unistuttgart.ipvs.pmp.app.DEPRECATED.AppInformationSetParcelable;

/**
 * The IAppService is provided by an application, over the interface
 * it's possible to get name, description and ServiceLevels from the App.
 *
 * @author Jakob Jarosch
 */
interface IAppService {
	
	/**
	 * @return Returns all Informations about the App.
	 */
	AppInformationSetParcelable getAppInformationSet();
	
	/**
	 * Sets the current active ServiceLevel for the App.
	 *
	 * @param level the current active level for the App
	 */
	void setActiveServiceLevel(int level);
	
	/**
	 * Is called when the PMP will inform the app about a successful (or not) registration.
	 *
	 * @param state State of the registration
	 */
	void setRegistrationSuccessful(in RegistrationState state);
}