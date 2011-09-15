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
    private int level;
    private String name;
    private String description;

    public ServiceLevelImpl(String appIdentifier, int level, String name,
	    String description) {
	this.appIdentifier = appIdentifier;
	this.level = level;
	this.name = name;
	this.description = description;
    }

    public String getAppIdentifier() {
	return appIdentifier;
    }

    @Override
    public int getLevel() {
	return level;
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
			"SELECT pl.ResourceGroup_Identifier, "
				+ "pl.Identifier, "
				+ "pl.Name_Cache, "
				+ "pl.Description_Cache, "
				+ "slpl.Value "
				+ "FROM ServiceLevel_PrivacyLevels AS slpl, PrivacyLevel AS pl "
				+ "WHERE slpl.ResourceGroup_Identifier = pl.ResourceGroup_Identifier "
				+ "AND slpl.PrivacyLevel_Identifier = pl.Identifier "
				+ "AND slpl.App_Identifier = ? "
				+ "AND slpl.ServiceLevel_Level = " + level,
			new String[] { appIdentifier });

	cursor.moveToNext();

	while (!cursor.isAfterLast()) {
	    String resourceGroup_Identifier = cursor.getString(cursor
		    .getColumnIndex("ResourceGroup_Identifier"));
	    String identifier = cursor.getString(cursor
		    .getColumnIndex("Identifier"));
	    String value = cursor.getString(cursor.getColumnIndex("Value"));
	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description_Cache"));

	    list.add(new PrivacyLevelImpl(resourceGroup_Identifier, identifier,
		    name, description, value));

	    cursor.moveToNext();
	}

	return list.toArray(new IPrivacyLevel[list.size()]);
    }
}
