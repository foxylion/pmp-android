package de.unistuttgart.ipvs.pmp.model.implementations;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;

/**
 * Implementation of the {@link IServiceLevel} interface.
 * 
 * @author Jakob Jarosch
 */
public class ServiceLevelImpl implements IServiceLevel {

    private String appIdentifier;
    private int ordering;
    private String name;
    private String description;

    public ServiceLevelImpl(String appIdentifier, int ordering, String name,
	    String description) {
	this.appIdentifier = appIdentifier;
	this.ordering = ordering;
	this.name = name;
	this.description = description;
    }

    public String getAppIdentifier() {
	return appIdentifier;
    }

    @Override
    public int getOrdering() {
	return ordering;
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
			"SELECT ResourceGroup_Identifier, Identifier, Name_Cache, Description_Cache FROM PrivacyLevel WHERE ResourceGroup_Identifier ="
				+ appIdentifier + ";", null);

	while (!cursor.isAfterLast()) {
	    String resourceGroup_Identifier = cursor.getString(cursor
		    .getColumnIndex("ResourceGroup_Identifier"));
	    String identifier = cursor.getString(cursor
		    .getColumnIndex("Identifier"));
	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description_Cache"));

	    list.add(new PrivacyLevelImpl(resourceGroup_Identifier, identifier,
		    name, description));

	    cursor.moveToNext();
	}

	return list.toArray(new IPrivacyLevel[list.size()]);
    }

}
