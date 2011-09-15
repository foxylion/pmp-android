package de.unistuttgart.ipvs.pmp.app;

import java.io.InputStream;

import android.app.Application;
import android.content.Context;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.app.xmlparser.AppInformationSet;
import de.unistuttgart.ipvs.pmp.app.xmlparser.AppInformationSetParser;
import de.unistuttgart.ipvs.pmp.service.pmp.IPMPServiceRegistration;
import de.unistuttgart.ipvs.pmp.service.utils.IConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

/**
 * A PMP-compatible App that uses the parsed service levels.
 * 
 * @author Tobias Kuhn
 * 
 */
public abstract class App extends Application {

    /**
     * Stores the associated signee.
     */
    private PMPSignee signee;

    /**
     * All the basic informations of an App.
     */
    private AppInformationSet infoSet;

    @Override
    public void onCreate() {
	signee = new PMPSignee(PMPComponentType.APP, getServiceAndroidName(),
		getApplicationContext());

	InputStream is = getXMLInputStream();
	if (is != null) {
	    infoSet = AppInformationSetParser.createAppInformationSet(is);
	} else {
	    infoSet = null;
	    // maybe it is required to have a specific NullInfoSet object?
	}
    }

    /**
     * Overwrite this method to return the <b>exact same</b> identifier you have
     * put in the manifest file for the service for this Resource Group:
     * &lt;service>...&lt;intent-filter>...&lt;action
     * android:name="<b>HERE</b>">. If the identifier differ, the service will
     * not work.
     * 
     * @return the specified identifier
     */
    protected abstract String getServiceAndroidName();

    /**
     * 
     * @return the signee
     */
    public PMPSignee getSignee() {
	return this.signee;
    }

    /**
     * The {@link AppInformationSet} parsed by this {@link App}. See
     * {@link App#getXMLInputStream()}.
     * 
     * @return The {@link AppInformationSet} parsed by this {@link App}.
     */
    public AppInformationSet getInfoSet() {
	return this.infoSet;
    }

    /**
     * Overwrite this method to react whenever PMP changes your active service
     * level. When not received any service level yet, assume you are on level
     * 0.
     * 
     * @param level
     *            the new service level level according to your specification in
     *            {@link App#getXMLInputStream()}.
     */
    public abstract void setActiveServiceLevel(int level);

    /**
     * Overwrite this method to return an input stream for the XML file that you
     * use to describe your application.
     * 
     * @return an {@link InputStream} for the XML file that describes this app
     *         according to assets/xml/App.xsd
     */
    protected abstract InputStream getXMLInputStream();

    /**
     * Effectively starts this app and registers it with PMP. You can implement
     * reacting to the result of this operation by implementing
     * onRegistrationSuccess() or onRegistrationFailed()
     * 
     * @param context
     *            {@link Context} to use for the connection
     * 
     */
    public void start(Context context) {

	// connect to PMP
	final PMPServiceConnector pmpsc = new PMPServiceConnector(context,
		signee);

	pmpsc.addCallbackHandler(new IConnectorCallback() {

	    @Override
	    public void disconnected() {
	    }

	    @Override
	    public void connected() {
		if (!pmpsc.isRegistered()) {
		    // register with PMP
		    IPMPServiceRegistration ipmpsr = pmpsc
			    .getRegistrationService();
		    try {
			byte[] pmpPublicKey = ipmpsr
				.registerResourceGroup(signee
					.getLocalPublicKey());

			// save the returned public key to be PMP's
			signee.setRemotePublicKey(PMPComponentType.PMP,
				Constants.PMP_IDENTIFIER, pmpPublicKey);

		    } catch (RemoteException e) {
			Log.e("RemoteException during registering app", e);
		    }
		}
	    }

	    @Override
	    public void bindingFailed() {
		Log.e("Binding failed during registering resource group.");
	    }
	});
	pmpsc.bind();
    }

    /**
     * Callback called when the preceding call to start() registered this app
     * successfully with PMP.
     */
    public abstract void onRegistrationSuccess();

    /**
     * Callback called when the preceding call to start() could not register
     * this app with PMP due to errors.
     * 
     * @param message
     *            returned message from the PMP service
     */
    public abstract void onRegistrationFailed(String message);

}