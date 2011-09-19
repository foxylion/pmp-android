package de.unistuttgart.ipvs.pmp.model.implementations.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelConditions;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.service.RegistrationState;
import de.unistuttgart.ipvs.pmp.service.resource.IResourceGroupServicePMP;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;
import de.unistuttgart.ipvs.pmp.service.utils.ResourceGroupServiceConnector;

public class ResourceGroupRegistration {

    private String identifier;

    private byte[] publicKey;

    private final ResourceGroupServiceConnector rgsc;

    /**
     * Executes an asynchronous ResourceGroup registration.
     * 
     * @param identifier
     *            Identifier of the ResourceGroup
     * @param publicKey
     *            Public key of the ResourceGroup
     */
    public ResourceGroupRegistration(String identifier, byte[] publicKey) {
	ModelConditions.assertStringNotNullOrEmpty("identifier", identifier);
	ModelConditions.assertPublicKeyNotNullOrEmpty(publicKey);
	
	this.identifier = identifier;
	this.publicKey = publicKey.clone();
	this.rgsc = new ResourceGroupServiceConnector(
		PMPApplication.getContext(), PMPApplication.getSignee(),
		identifier);

	Log.v("Registration (" + identifier
		+ "): Trying to connect to the ResourceGroupService");

	connect();
    }

    /**
     * Connect to the Service.
     */
    private void connect() {
	if (!rgsc.bind(true)) {
	    Log.e("Registration ("
		    + identifier
		    + "): FAILED - Connection to the ResourceGroupService failed. More details can be found in the log.");
	} else {
	    Log.d("Registration (" + identifier + "): Successfully bound.");

	    if (rgsc.getPMPService() == null) {
		Log.e("Registration ("
			+ identifier
			+ "): FAILED - Binding to the ResourceGroupService failed, only got a NULL IBinder.");
	    } else {
		Log.d("Registration ("
			+ identifier
			+ "): Successfully connected, got IResourceGroupServicePMP.");
		loadResourceGroupData(rgsc.getPMPService());
	    }
	}
    }

    /**
     * Load the informations from the ResourceGroup-Service.
     * 
     * @param rgService
     *            service which provides the {@link ResourceGroup} informations
     */
    private void loadResourceGroupData(IResourceGroupServicePMP rgService) {
	Log.v("Registration (" + identifier
		+ "): loading resource group data...");

	try {
	    String name = rgService.getName(Locale.getDefault().getLanguage());
	    String description = rgService.getDescription(Locale.getDefault()
		    .getLanguage());

	    ResourceGroupDS rgDS = new ResourceGroupDS(name, description);

	    @SuppressWarnings("unchecked")
	    List<String> privacyLevels = (List<String>) rgService
		    .getPrivacyLevelIdentifiers();

	    for (String identifier : privacyLevels) {
		name = rgService.getPrivacyLevelName(Locale.getDefault()
			.getLanguage(), identifier);
		description = rgService.getPrivacyLevelDescription(Locale
			.getDefault().getLanguage(), identifier);

		rgDS.getPrivacyLevels().add(
			new PrivacyLevelDS(identifier, name, description));
	    }

	    checkResourceGroup(rgDS);
	} catch (RemoteException e) {
	    Log.e("Registration ("
		    + identifier
		    + "): FAILED - while loading resource group data a RemoteException was produced.",
		    e);
	    informAboutRegistration(
		    false,
		    "IResourceGroupServicePMP produced a RemoteException, during information collection.");
	}
    }

    /**
     * Check if the informations provided by the {@link ResourceGroup} and if
     * the {@link ResourceGroup} is not already registered.
     */
    private void checkResourceGroup(ResourceGroupDS rgDS) {
	if (!rgDS.validate()) {
	    Log.e("Registration ("
		    + identifier
		    + "): FAILED - ResourceGroup informations are missing, details above.");
	    informAboutRegistration(false,
		    "ResourceGroup informations are missing, details in LogCat.");
	    return;
	}

	if (ModelSingleton.getInstance().getModel()
		.getResourceGroup(identifier) != null) {
	    Log.e("Registration (" + identifier
		    + "): FAILED - ResourceGroup already registred in PMP-Database, maybe lost your key?.");
	    informAboutRegistration(false, "ResourceGroup already registred, maybe lost your key?");
	    return;
	}

	registerResourceGroupInDatabase(rgDS);
    }

    /**
     * Write the {@link ResourceGroupDS} to the Database.
     */
    private void registerResourceGroupInDatabase(ResourceGroupDS rgDS) {
	Log.v("Registration (" + identifier
		+ "): Adding the ResourceGroup to the PMP-Database");

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getWritableDatabase();

	ContentValues cv = new ContentValues();
	cv.put("Identifier", identifier);
	cv.put("Name_Cache", rgDS.getName());
	cv.put("Description_Cache", rgDS.getDescription());

	db.insert("ResourceGroup", null, cv);

	for (PrivacyLevelDS pl : rgDS.getPrivacyLevels()) {
	    cv = new ContentValues();
	    cv.put("ResourceGroup_Identifier", identifier);
	    cv.put("Identifier", pl.getIdentifier());
	    cv.put("Name_Cache", pl.getName());
	    cv.put("Description_Cache", pl.getDescription());

	    db.insert("PrivacyLevel", null, cv);
	}

	publishPublicKey();
    }

    /**
     * Publish the public key of the to the {@link PMPSignee} of
     * {@link PMPApplication}.
     */
    private void publishPublicKey() {
	Log.v("Loading ResourceGroups public key into PMPSignee");

	PMPApplication.getSignee().setRemotePublicKey(
		PMPComponentType.RESOURCE_GROUP, identifier, publicKey);

	informAboutRegistration(true, null);
    }

    /**
     * Inform the ResourceGroupService about the registration state.
     * 
     * @param state
     *            true means successful, false means unsuccessful
     * @param message
     *            a message with optional information provided.
     */
    private void informAboutRegistration(final boolean state,
	    final String message) {
	Log.d("Registration (" + identifier
		+ "): Informing ResourceGroup about registrationState ("
		+ state + ")");

	if (!rgsc.isBound()) {
	    if (!rgsc.bind(true)) {
		Log.e("Registration ("
			+ identifier
			+ "): FAILED - Connection to the ResourceGroupService failed. More details can be found in the log.");
	    } else {
		Log.d("Registration (" + identifier + "): Successfully bound.");

		if (rgsc.getPMPService() == null) {
		    Log.e("Registration ("
			    + identifier
			    + "): FAILED - Binding to the ResourceGroupService failed, only got a NULL IBinder.");
		} else {
		    Log.d("Registration ("
			    + identifier
			    + "): Successfully connected got IResourceGroupServicePMP.");
		    informAboutRegistration(state, message);
		}
	    }
	} else {
	    try {
		Log.v("Registration (" + identifier
			+ "): Calling setRegistrationState");
		IResourceGroupServicePMP rgService = rgsc.getPMPService();
		rgService.setRegistrationState(new RegistrationState(state,
			message));
		rgsc.unbind();

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
}

class ResourceGroupDS {
    private String name;
    private String description;

    private List<PrivacyLevelDS> privacyLevels = new ArrayList<PrivacyLevelDS>();

    public ResourceGroupDS(String name, String description) {
	this.name = name;
	this.description = description;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public List<PrivacyLevelDS> getPrivacyLevels() {
	return privacyLevels;
    }

    public boolean validate() {
	boolean state = true;

	if (name == null || description == null) {
	    Log.e("ResourceGroup validation failed: missing name or description (Locale: "
		    + Locale.getDefault().getLanguage() + ")");
	    state = false;
	}

	for (PrivacyLevelDS plDS : privacyLevels) {
	    if (!plDS.validate()) {
		state = false;
	    }
	}
	return state;
    }
}

class PrivacyLevelDS {
    private String identifier;
    private String name;
    private String description;

    public PrivacyLevelDS(String identifier, String name, String description) {
	this.identifier = identifier;
	this.name = name;
	this.description = description;
    }

    public String getIdentifier() {
	return identifier;
    }

    public void setIdentifier(String identifier) {
	this.identifier = identifier;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public boolean validate() {
	if (identifier == null || name == null || description == null) {
	    Log.e("PrivacyLevel validation failed: missing identifier, name or description (PrivacyLevel: "
		    + identifier
		    + ", Locale: "
		    + Locale.getDefault().getLanguage() + ")");
	    return false;
	}
	return true;
    }
}