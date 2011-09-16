package de.unistuttgart.ipvs.pmp.model.implementations;

import java.util.Locale;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;
import de.unistuttgart.ipvs.pmp.service.utils.ResourceGroupServiceConnector;

/**
 * Implementation of the {@link PrivacyLevelImpl} interface.
 * 
 * @author Jakob Jarosch
 */
public class PrivacyLevelImpl implements IPrivacyLevel {

    private String resourceGroupIdentifier;
    private String identifier;
    private String name;
    private String description;
    private String value;

    public PrivacyLevelImpl(String resourceGroupIdentifier, String identifier,
	    String name, String description) {
	this(resourceGroupIdentifier, identifier, name, description, null);
    }

    public PrivacyLevelImpl(String resourceGroupIdentifier, String identifier,
	    String name, String description, String value) {
	this.resourceGroupIdentifier = resourceGroupIdentifier;
	this.identifier = identifier;
	this.name = name;
	this.description = description;
	this.value = value;
    }

    private String getRessourceGroupIdentifier() {
	return resourceGroupIdentifier;
    }

    @Override
    public String getIdentifier() {
	return identifier;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public String getDescription() {
	return description;
    }

    @Override
    public String getValue() {
	return value;
    }

    @Override
    public IResourceGroup getResourceGroup() {
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	Cursor cursor = db
		.rawQuery(
			"SELECT Name_Cache, Description_Cache FROM ResourceGroup WHERE Identifier = ? LIMIT 1",
			new String[] { resourceGroupIdentifier });

	cursor.moveToNext();
	
	if (cursor.getCount() == 1) {
	    cursor.moveToNext();
	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description_Cache"));

	    cursor.close();

	    return new ResourceGroupImpl(resourceGroupIdentifier, name,
		    description);
	} else {
	    cursor.close();
	    return null;
	}
    }

    @Override
    public String getHumanReadableValue(String value) throws RemoteException {
	if (value == null) {
	    throw new IllegalArgumentException("Value must not be NULL");
	}

	ResourceGroupServiceConnector rgsc = new ResourceGroupServiceConnector(
		PMPApplication.getContext(), PMPApplication.getSignee(),
		getRessourceGroupIdentifier());

	rgsc.bind(true);

	if (!rgsc.isBound() || rgsc.getPMPService() == null) {
	    Log.e("Binding of ResourceGroupService "
		    + getRessourceGroupIdentifier()
		    + " failed, can't do satisfies");
	    RemoteException re = new RemoteException();
	    re.initCause(new Throwable("Binding of ResourceGroupService "
		    + getRessourceGroupIdentifier()
		    + " failed, can't do satisfies"));
	    throw re;
	} else {
	    return rgsc.getPMPService().getHumanReadablePrivacyLevelValue(
		    Locale.getDefault().getLanguage(), identifier, value);
	}
    }

    @Override
    public boolean satisfies(String reference, String value)
	    throws RemoteException {
	if (reference == null) {
	    throw new IllegalArgumentException("reference must not be NULL");
	}
	if (value == null) {
	    throw new IllegalArgumentException("value must not be NULL");
	}

	ResourceGroupServiceConnector rgsc = new ResourceGroupServiceConnector(
		PMPApplication.getContext(), PMPApplication.getSignee(),
		getRessourceGroupIdentifier());

	rgsc.bind(true);

	if (!rgsc.isBound() || rgsc.getPMPService() == null) {
	    Log.e("Binding of ResourceGroupService "
		    + getRessourceGroupIdentifier()
		    + " failed, can't do satisfies");
	    RemoteException re = new RemoteException();
	    re.initCause(new Throwable("Binding of ResourceGroupService "
		    + getRessourceGroupIdentifier()
		    + " failed, can't do satisfies"));
	    throw re;
	} else {
	    return rgsc.getPMPService().satisfiesPrivacyLevel(identifier,
		    reference, value);
	}
    }

}
