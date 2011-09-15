package de.unistuttgart.ipvs.pmp.app.DEPRECATED;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.service.app.AppService;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

/**
 * This acts like an internal provider for the {@link PMPSignee} of the App.
 * 
 * @author Thorsten Berberich
 * 
 */
public class ApplicationApp {

    /**
     * Singleton instance of the class
     */
    private static ApplicationApp instance = null;

    /**
     * The {@link Context} of the app
     */
    private Context context;

    /**
     * The identifier of the app service
     */
    private String appServiceIdentifier;

    /**
     * The {@link PMPServiceConnector} of the app
     */
    private PMPServiceConnector serviceConnector;

    /**
     * Private constructor because of the singleton
     */
    private ApplicationApp() {
    }

    /**
     * Method to get the singleton instance
     * 
     * @return instance of this method
     */
    public static ApplicationApp getInstance() {
	if (instance == null) {
	    instance = new ApplicationApp();
	}
	return instance;
    }

    /**
     * Returns the {@link PMPSignee} of the app
     * 
     * @return the {@link PMPSignee}
     */
    public PMPSignee getSignee() {
	PMPSignee signee = new PMPSignee(PMPComponentType.APP,
		AppService.class, getContext());
	signee.setIdentifier(appServiceIdentifier);
	return signee;
    }

    /**
     * Returns the {@link PMPServiceConnector} of the app
     * 
     * @return the {@link PMPServiceConnector}
     */
    public PMPServiceConnector getServiceConnector() {
	return serviceConnector;
    }

    /**
     * Sets the {@link PMPServiceConnector} of the app
     * 
     * @param serviceConnector
     *            {@link PMPServiceConnector} to set
     */
    public void setServiceConnector(PMPServiceConnector serviceConnector) {
	this.serviceConnector = serviceConnector;
    }

    /**
     * Returns the context of the app
     * 
     * @return the {@link Context}
     */
    public Context getContext() {
	return context;
    }

    /**
     * Sets the {@link Context} of the app
     * 
     * @param context
     *            {@link Context} of the app to set
     */
    public void setContext(Context context) {
	this.context = context;
    }

    /**
     * Returns the identifier of the app service
     * 
     * @return app service identifier
     */
    public String getAppServiceIdentifier() {
	return appServiceIdentifier;
    }

    /**
     * Sets the app service identifer
     * 
     * @param appServiceIdentifier
     */
    public void setAppServiceIdentifier(String appServiceIdentifier) {
	this.appServiceIdentifier = appServiceIdentifier;
    }

}
