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
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.service.RegistrationState;
import de.unistuttgart.ipvs.pmp.service.resource.IResourceGroupServicePMP;
import de.unistuttgart.ipvs.pmp.service.utils.IConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;
import de.unistuttgart.ipvs.pmp.service.utils.ResourceGroupServiceConnector;

public class ResourceGroupRegistration {

    private String identifier;

    private byte[] publicKey;

    private final ResourceGroupServiceConnector rgsc = new ResourceGroupServiceConnector(
	    PMPApplication.getContext(), PMPApplication.getSignee(), identifier);

    /**
     * Executes an asynchronous ResourceGroup registration.
     * 
     * @param identifier
     *            Identifier of the ResourceGroup
     * @param publicKey
     *            Public key of the ResourceGroup
     */
    public ResourceGroupRegistration(String identifier, byte[] publicKey) {
	this.identifier = identifier;
	this.publicKey = publicKey;

	connect();
    }

    /**
     * Connect to the Service.
     */
    private void connect() {
	
    }

    /**
     * Load the informations from the ResourceGroup-Service.
     * 
     * @param rgService
     *            service which provides the {@link ResourceGroup} informations
     */
    private void loadResourceGroupData(IResourceGroupServicePMP rgService) {
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
	    Log.e("Registration failed: while getting informations it produced an RemoteException.",
		    e);
	}
    }

    /**
     * Check if the informations provided by the {@link ResourceGroup} and if
     * the {@link ResourceGroup} is not already registered.
     */
    private void checkResourceGroup(ResourceGroupDS rgDS) {
	if (!rgDS.validate()) {
	    Log.e("Registration failed: ResourceGroup informations are missing, details above.");
	    informAppAboutRegistration(false,
		    "ResourceGroup informations are missing, details in LogCat.");
	    return;
	}

	for (IResourceGroup rg : ModelSingleton.getInstance().getModel()
		.getResourceGroups()) {
	    if (rg.getIdentifier().equals(identifier)) {
		Log.e("Registration failed: ResourceGroup already registred.");
		informAppAboutRegistration(false,
			"ResourceGroup already registred.");
		return;
	    }
	}

	registerResourceGroupInDatabase(rgDS);
    }

    /**
     * Write the {@link ResourceGroupDS} to the Database.
     */
    private void registerResourceGroupInDatabase(ResourceGroupDS rgDS) {
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getWritableDatabase();

	ContentValues cv = new ContentValues();
	cv.put("Identifier", identifier);
	cv.put("Name_Cache", rgDS.getName());
	cv.put("Description_Cache", rgDS.getDescription());

	db.insert("ResourceGroup", null, cv);

	for (PrivacyLevelDS pl : rgDS.getPrivacyLevels()) {
	    db.rawQuery(
		    "INSERT INTO PrivacyLevel (ResourceGroup_Identifier, Identifier, Name_Cache, Description_Cache) VALUES (?, ?, ?, ?)",
		    new String[] { identifier, pl.getIdentifier(),
			    pl.getName(), pl.getDescription() });

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
	PMPApplication.getSignee().setRemotePublicKey(
		PMPComponentType.RESOURCE_GROUP, identifier, publicKey);

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
	if (!rgsc.isBound()) {
	    
	} else {
	    try {
		IResourceGroupServicePMP appService = rgsc.getPMPService();
		appService.setRegistrationSuccessful(new RegistrationState(
			state, message));
	    } catch (RemoteException e) {
		Log.e("Registration Failed: setRegistrationSuccessful() produced an RemoteException.",
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