package de.unistuttgart.ipvs.pmp.service.pmp;

/**
 * The Service of PMP provided for an  App.
 *
 * @author Jakob Jarosch
 */
interface IPMPServiceApp {

	/**
	 * A authenticated App can call this method to gain the first initial {@link IServiceLevel}.<br>
	 * The {@link PMPService} will answer this method call asynchronously {@link IAppServicePMP#setServiceLevel(Integer)}.
	 */
	void getInitialServiceLevel();
	
	/** 
	 * Method for registering a new App at PMP.
	 */
	void registerApp();
	
	/**
     * 
     * @return true, if the class connecting to PMP is already registered and does not require any registration action
     *         via the {@link PMPServiceConnector#getRegistrationService()} interface.
     */
    boolean isRegistered();
}