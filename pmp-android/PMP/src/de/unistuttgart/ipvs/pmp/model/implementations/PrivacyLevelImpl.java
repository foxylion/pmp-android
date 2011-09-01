package de.unistuttgart.ipvs.pmp.model.implementations;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;

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

    public PrivacyLevelImpl(String resourceGroupIdentifier, String identifier,
	    String name, String description) {
	this.resourceGroupIdentifier = resourceGroupIdentifier;
	this.identifier = identifier;
	this.name = name;
	this.description = description;
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
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public IResourceGroup[] getResourceGroup() {
	List<IResourceGroup> list = new ArrayList<IResourceGroup>();

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	Cursor cursor = db
		.rawQuery(
			"SELECT Identifier, Name_Cache, Description_Cache FROM ResourceGroup;",
			null);

	while (!cursor.isAfterLast()) {
	    String identifier = cursor.getString(cursor
		    .getColumnIndex("Identifier"));
	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description_Cache"));

	    list.add(new ResourceGroupImpl(identifier, name,
		    description));

	    cursor.moveToNext();
	}

	return list.toArray(new IResourceGroup[list.size()]);
    }

    @Override
    public String getHumanReadableValue(String value) {
	// TODO Auto-generated method stub
	return null;
    }

}
