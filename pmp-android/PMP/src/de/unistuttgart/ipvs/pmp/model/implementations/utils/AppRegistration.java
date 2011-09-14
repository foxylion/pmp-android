package de.unistuttgart.ipvs.pmp.model.implementations.utils;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.app.appUtil.xmlParser.AppInformationSet;
import de.unistuttgart.ipvs.pmp.app.appUtil.xmlParser.RequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.app.appUtil.xmlParser.ServiceLevel;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.service.RegistrationState;
import de.unistuttgart.ipvs.pmp.service.app.IAppService;
import de.unistuttgart.ipvs.pmp.service.utils.AppServiceConnector;
import de.unistuttgart.ipvs.pmp.service.utils.IConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

public class AppRegistration {

    private String identifier;

    private byte[] publicKey;

    private AppInformationSet ais = null;

    private final AppServiceConnector asp = new AppServiceConnector(
	    PMPApplication.getContext(), PMPApplication.getSignee(), identifier);

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

	connect();
    }

    /**
     * Connect to the Service.
     */
    private void connect() {
	asp.addCallbackHandler(new IConnectorCallback() {

	    @Override
	    public void disconnected() {
		/* ignore */
	    }

	    @Override
	    public void connected() {
		asp.removeCallbackHandler(this);

		new Thread(new Runnable() {

		    @Override
		    public void run() {
			if (asp.getAppService() == null) {
			    Log.e("Registration failed: Binding to the AppService failed, only got a NULL IBinder.");
			} else {
			    loadAppInformationSet(asp.getAppService());
			}
		    }
		}).start();
	    }

	    @Override
	    public void bindingFailed() {
		Log.e("Registration failed: onnection to the AppService failed. More details can be found in the log.");
	    }
	});
	asp.bind();
    }

    /**
     * Load the {@link AppInformationSet} from the Service.
     * 
     * @param appService
     *            service which provides the {@link AppInformationSet}
     */
    private void loadAppInformationSet(IAppService appService) {
	try {
	    appService.getAppInformationSet();
	} catch (RemoteException e) {
	    Log.e("Registration failed: getAppInformationSet() produced an RemoteException.",
		    e);
	}

	if (ais == null) {
	    Log.e("Registration failed: AppInformationSet is NULL.");
	    informAppAboutRegistration(false, "AppInformationSet is NULL.");
	} else {
	    checkAppInformationSet();
	}
    }

    /**
     * Check if the {@link AppInformationSet} is correct and the application is
     * not already registered.
     */
    private void checkAppInformationSet() {
	/* Check if the app is already in the PMP-Database. */
	for (IApp app : ModelSingleton.getInstance().getModel().getApps()) {
	    if (app.getIdentifier().equals(identifier)) {
		informAppAboutRegistration(false, "Already registered");
		return;
	    }
	}

	registerAppInDatabase();
    }

    /**
     * Write the {@link AppInformationSet} to the Database.
     */
    private void registerAppInDatabase() {
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getWritableDatabase();

	db.rawQuery(
		"INSERT INTO App (Identifier, Name_Cache, Description_Cache, ServiceLevel_Active) VALUES (?, ?, ?, 0)",
		new String[] { identifier, getLocalized(ais.getNames()),
			getLocalized(ais.getDescriptions()) });

	for (Entry<Integer, ServiceLevel> sl : ais.getServiceLevels()
		.entrySet()) {
	    db.rawQuery(
		    "INSERT INTO ServiceLevel (App_Identifier, Level, Name_Cache, Description_Cache) VALUES (?, "
			    + sl.getKey() + ", ?, ?)", new String[] {
			    identifier, getLocalized(sl.getValue().getNames()),
			    getLocalized(sl.getValue().getDescriptions()) });

	    for (RequiredResourceGroup rrg : sl.getValue()
		    .getRequiredResourceGroups()) {

		for (Entry<String, String> pl : rrg.getPrivacyLevelMap()
			.entrySet()) {
		    db.rawQuery(
			    "INSERT INTO ServiceLevel_PrivacyLevels (App_Identifier, Level, ResourceGroup_Identifier, PrivacyLevel_Identifier, Value) VALUES (?, "
				    + sl.getKey() + ", ?, ?, ?)",
			    new String[] { identifier, rrg.getRgIdentifier(),
				    pl.getKey(), pl.getValue() });
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
	PMPApplication.getSignee().setRemotePublicKey(PMPComponentType.APP,
		identifier, publicKey);

	informAppAboutRegistration(true, null);
    }

    /**
     * Inform the AppService about the registration state.
     * 
     * @param state
     *            true means successful, false means unsuccessful
     * @param message
     *            a message with optional information provided.
     */
    private void informAppAboutRegistration(final boolean state,
	    final String message) {
	if (!asp.isBound()) {
	    asp.addCallbackHandler(new IConnectorCallback() {

		@Override
		public void disconnected() {
		}

		@Override
		public void connected() {
		    asp.removeCallbackHandler(this);
		    informAppAboutRegistration(state, message);
		}

		@Override
		public void bindingFailed() {
		    Log.e("Registration Failed: Could not reconnect to AppService for setting the registration state");
		}
	    });
	    asp.bind();
	} else {
	    try {
		IAppService appService = asp.getAppService();
		appService.setRegistrationSuccessful(new RegistrationState(
			state, message));
	    } catch (RemoteException e) {
		Log.e("Registration Failed: setRegistrationSuccessful() produced an RemoteException.", e);
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
