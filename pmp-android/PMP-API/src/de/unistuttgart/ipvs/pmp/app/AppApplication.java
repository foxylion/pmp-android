package de.unistuttgart.ipvs.pmp.app;

import android.content.Context;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

/**
 * This acts like an internal provider for the {@link PMPSignee} of the App.
 * 
 * @author Thorsten Berberich
 * 
 */
public class AppApplication {

    /**
     * Singleton instance of the class
     */
    private static AppApplication instance = null;

    /**
     * The {@link PMPSignee} of the app
     */
    private PMPSignee signee;

    /**
     * The {@link Context} of the app
     */
    private Context context;

    /**
     * The {@link PMPServiceConnector} of the app
     */
    private PMPServiceConnector serviceConnector;

    /**
     * Private constructor because of the singleton
     */
    private AppApplication() {
    }

    /**
     * Method to get the singleton instance
     * 
     * @return instance of this method
     */
    public static AppApplication getInstance() {
	if (instance == null) {
	    instance = new AppApplication();
	}
	return instance;
    }

    /**
     * Returns the {@link PMPSignee} of the app
     * 
     * @return the {@link PMPSignee}
     */
    public PMPSignee getSignee() {
	return signee;
    }

    /**
     * Sets the {@link PMPSignee} of the app
     * 
     * @param signee
     *            {@link PMPSignee} to set
     */
    public void setSignee(PMPSignee signee) {
	this.signee = signee;
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

}