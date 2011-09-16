package de.unistuttgart.ipvs.pmp.service.pmp;

/**
 * The Service of PMP provided for an authenticated App.
 *
 * @author Jakob Jarosch
 */
interface IPMPServiceApp {

	/**
	 * A authenticated App can call this method to gain the first initial {@link IServiceLevel}.<br>
	 * The {@link PMPService} will answer this method call asynchronously {@link IAppServicePMP#setServiceLevel(Integer)}.
	 */
	void getInitialServiceLevel();
}