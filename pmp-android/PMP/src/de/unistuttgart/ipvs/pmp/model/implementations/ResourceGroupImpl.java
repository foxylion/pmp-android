package de.unistuttgart.ipvs.pmp.model.implementations;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;

/**
 * Implementation of the {@link IResourceGroup} interface.
 * 
 * @author Jakob Jarosch
 */
public class ResourceGroupImpl implements IResourceGroup {

    private String identifier;
    private String name;
    private String description;

    public ResourceGroupImpl(String identifier, String name, String description) {
	this.identifier = identifier;
	this.name = name;
	this.description = description;
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
    public IPrivacyLevel[] getPrivacyLevels() {
	List<IPrivacyLevel> list = new ArrayList<IPrivacyLevel>();

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	Cursor cursor = db
		.rawQuery(
			"SELECT ResourceGroup_Identifier, Identifier, Name_Cache, Description_Cache FROM PrivacyLevel WHERE ResourceGroup_Identifier = "
				+ identifier + ";", null);

	while (!cursor.isAfterLast()) {
	    String resourceIdentifier = cursor.getString(cursor
		    .getColumnIndex("Resource_Identifier"));
	    String identifier = cursor.getString(cursor
		    .getColumnIndex("Identifier"));
	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description_Cache"));

	    list.add(new PrivacyLevelImpl(resourceIdentifier, identifier, name,
		    description));

	    cursor.moveToNext();
	}

	return list.toArray(new IPrivacyLevel[list.size()]);

    }

}
