package de.unistuttgart.ipvs.pmp.model.implementations.utils;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.app.xmlparser.AppInformationSet;
import de.unistuttgart.ipvs.pmp.app.xmlparser.RequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.app.xmlparser.ServiceLevel;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.service.RegistrationState;
import de.unistuttgart.ipvs.pmp.service.app.IAppServicePMP;
import de.unistuttgart.ipvs.pmp.service.utils.AppServiceConnector;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

public class AppRegistration {

    private String identifier;

    private byte[] publicKey;

    private AppInformationSet ais = null;

    private final AppServiceConnector asp;

    /**
     * Executes an asynchronous App registration.
     * 
     * @param identifier
     *            Identifier of the App
     * @param publicKey
     *            Public key of the App
     */
    public AppRegistration(String identifier, byte[] publicKey) {
	this.identifier = identifier;
	this.publicKey = publicKey;
	this.asp = new AppServiceConnector(PMPApplication.getContext(),
		PMPApplication.getSignee(), identifier);

	Log.v("Registration (" + identifier
		+ "): Trying to connect to the AppService");

	connect();
    }

    /**
     * Connect to the Service.
     */
    private void connect() {
	if (!asp.bind(true)) {
	    Log.e("Registration ("
		    + identifier
		    + "): FAILED - Connection to the AppService failed. More details can be found in the log.");
	} else {
	    Log.d("Registration (" + identifier + "): Successfully bound.");

	    if (asp.getAppService() == null) {
		Log.e("Registration ("
			+ identifier
			+ "): FAILED - Binding to the AppService failed, only got a NULL IBinder.");
	    } else {
		Log.d("Registration (" + identifier
			+ "): Successfully connected, got IAppServicePMP.");
		loadAppInformationSet(asp.getAppService());
	    }
	}
    }

    /**
     * Load the {@link AppInformationSet} from the Service.
     * 
     * @param appService
     *            service which provides the {@link AppInformationSet}
     */
    private void loadAppInformationSet(IAppServicePMP appService) {
	Log.v("Registration (" + identifier + "): loading AppInformationSet...");

	try {
	    ais = appService.getAppInformationSet().getAppInformationSet();
	    
	    checkAppInformationSet();
	} catch (RemoteException e) {
	    Log.e("Registration ("
		    + identifier
		    + "): FAILED - getAppInformationSet() produced an RemoteException.",
		    e);
	    informAboutRegistration(false,
		    "getAppInformationSet() produced an RemoteException.");
	}

    }

    /**
     * Check if the {@link AppInformationSet} is correct and the application is
     * not already registered.
     */
    private void checkAppInformationSet() {
	if (ais == null) {
	    Log.e("Registration (" + identifier
		    + "): FAILED - AppInformationSet is NULL.");
	    informAboutRegistration(false, "AppInformationSet is NULL.");
	    return;
	}

	/* Check if the app is already in the PMP-Database. */
	if (ModelSingleton.getInstance().getModel().getApp(identifier) != null) {
	    Log.e("Registration (" + identifier
		    + "): FAILED - App already registred, maybe lost your key?");
	    informAboutRegistration(false, "Already registered, maybe lost your key?");
	    return;
	}

	registerAppInDatabase();
    }

    /**
     * Write the {@link AppInformationSet} to the Database.
     */
    private void registerAppInDatabase() {
	Log.v("Registration (" + identifier
		+ "): Adding the App to the PMP-Database");

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getWritableDatabase();

	ContentValues cv = new ContentValues();
	cv.put("Identifier", identifier);
	cv.put("Name_Cache", getLocalized(ais.getNames()));
	cv.put("Description_Cache", getLocalized(ais.getDescriptions()));

	db.insert("App", null, cv);

	for (Entry<Integer, ServiceLevel> sl : ais.getServiceLevels()
		.entrySet()) {

	    cv = new ContentValues();
	    cv.put("App_Identifier", identifier);
	    cv.put("Level", sl.getKey());
	    cv.put("Name_Cache", getLocalized(sl.getValue().getNames()));
	    cv.put("Description_Cache", getLocalized(sl.getValue()
		    .getDescriptions()));

	    db.insert("ServiceLevel", null, cv);

	    for (RequiredResourceGroup rrg : sl.getValue()
		    .getRequiredResourceGroups()) {

		for (Entry<String, String> pl : rrg.getPrivacyLevelMap()
			.entrySet()) {

		    cv = new ContentValues();
		    cv.put("App_Identifier", identifier);
		    cv.put("ServiceLevel_Level", sl.getKey());
		    cv.put("ResourceGroup_Identifier", rrg.getRgIdentifier());
		    cv.put("PrivacyLevel_Identifier", pl.getKey());
		    cv.put("Value", pl.getValue());

		    db.insert("ServiceLevel_PrivacyLevels", null, cv);
		}
	    }
	}

	publishPublicKey();
    }

    /**
     * Publish the public key of the to the {@link PMPSignee} of
     * {@link PMPApplication}.
     */
    private void publishPublicKey() {
	Log.v("Loading Apps public key into PMPSignee");

	PMPApplication.getSignee().setRemotePublicKey(PMPComponentType.APP,
		identifier, publicKey);

	informAboutRegistration(true, null);
    }

    /**
     * Inform the AppService about the registration state.
     * 
     * @param state
     *            true means successful, false means unsuccessful
     * @param message
     *            a message with optional information provided.
     */
    private void informAboutRegistration(final boolean state,
	    final String message) {
	Log.d("Registration (" + identifier
		+ "): Informing App about registrationState (" + state + ")");

	if (!asp.isBound()) {
	    if (!asp.bind(true)) {
		Log.e("Registration ("
			+ identifier
			+ "): FAILED - Connection to the AppService failed. More details can be found in the log.");
	    } else {
		Log.d("Registration (" + identifier + "): Successfully bound.");

		if (asp.getAppService() == null) {
		    Log.e("Registration ("
			    + identifier
			    + "): FAILED - Binding to the AppService failed, only got a NULL IBinder.");
		} else {
		    Log.d("Registration (" + identifier
			    + "): Successfully connected got IAppServicePMP");
		    informAboutRegistration(state, message);
		}
	    }
	} else {
	    try {
		Log.v("Registration (" + identifier
			+ "): Calling setRegistrationState");
		IAppServicePMP appService = asp.getAppService();
		appService.setRegistrationState(new RegistrationState(state,
			message));
		
		Log.d("Registration (" + identifier + "): Registration "
			+ (state ? "succeed" : "FAILED"));
	    } catch (RemoteException e) {
		Log.e("Registration ("
			+ identifier
			+ "): "
			+ (state ? "succeed" : "FAILED")
			+ ", but setRegistrationState() produced an RemoteException.",
			e);
	    }
	}
    }

    private String getLocalized(Map<Locale, String> map) {
	if (map.containsKey(Locale.getDefault())) {
	    // TODO check if that really works when the default is de_DE.utf-8
	    // or something like that...
	    return map.get(Locale.getDefault());
	} else {
	    return map.get(new Locale("en"));
	}
    }
}
