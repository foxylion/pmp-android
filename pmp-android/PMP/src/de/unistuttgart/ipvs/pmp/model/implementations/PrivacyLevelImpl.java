package de.unistuttgart.ipvs.pmp.model.implementations;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;

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

    public String getRessourceGroupIdentifier() {
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

	if (cursor != null && cursor.getCount() == 1) {
	    cursor.moveToNext();
	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description_Cache"));

	    return new ResourceGroupImpl(resourceGroupIdentifier, name,
		    description);
	} else {
	    return null;
	}
    }

    @Override
    public String getHumanReadableValue(String value) {
	/*
	 * TODO Implement the humand readable request
	 * 
	 * The request of the human readable value should be done in a non
	 * connection blocking way and return the human readable value (...)
	 */

	return value;
    }

    @Override
    public boolean satisfies(String reference, String value) {
	// TODO Auto-generated method stub
	return false;
    }

}
