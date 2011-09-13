package de.unistuttgart.ipvs.pmp.model.implementations;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPreset;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;

/**
 * Implementation of the {@link IApp} interface.
 * 
 * @author Jakob Jarosch
 */
public class AppImpl implements IApp {

    private String identifier;
    private String name;
    private String description;

    public AppImpl(String identifier, String name, String description) {
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
    public IServiceLevel[] getServiceLevels() {
	List<IServiceLevel> list = new ArrayList<IServiceLevel>();

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	Cursor cursor = db
		.rawQuery(
			"SELECT Level, Name_Cache, Description_Cache FROM ServiceLevel WHERE App_Identifier = '"
				+ identifier + "';", null);

	cursor.moveToNext();
	
	while (!cursor.isAfterLast()) {
	    int level = cursor.getInt(cursor.getColumnIndex("Level"));
	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description_Cache"));

	    list.add(new ServiceLevelImpl(identifier, level, name, description));

	    cursor.moveToNext();
	}

	return list.toArray(new IServiceLevel[list.size()]);
    }

    @Override
    public IServiceLevel getServiceLevel(int level) {

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	Cursor cursor = db
		.rawQuery(
			"SELECT Level, Name_Cache, Description_Cache FROM ServiceLevel WHERE App_Identifier = ? AND Level = "
				+ level + " LIMIT 1;",
			new String[] { identifier });

	if (cursor != null && cursor.getCount() == 1) {
	    cursor.moveToNext();
	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description_Cache"));

	    return new ServiceLevelImpl(identifier, level, name, description);
	} else {
	    return null;
	}
    }

    @Override
    public int getActiveServiceLevel() {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public void setActiveServiceLevel(int serviceLevel) {
	// TODO Auto-generated method stub
	throw new UnsupportedOperationException();
    }

    @Override
    public IPreset[] getAssignedPresets() {
	// TODO Auto-generated method stub
	return null;
    }

}
