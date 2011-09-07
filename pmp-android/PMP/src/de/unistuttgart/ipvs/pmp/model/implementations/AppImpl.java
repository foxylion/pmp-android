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
			"SELECT App_Identifier, Ordering, Name_Cache, Description_Cache FROM ServiceLevel WHERE App_Identifier = "
				+ identifier + ";", null);

	while (!cursor.isAfterLast()) {
	    String app_Identifier = cursor.getString(cursor
		    .getColumnIndex("App_Identifier"));
	    int ordering = cursor.getInt(cursor
		    .getColumnIndex("Ordering"));
	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description_Cache"));

	    list.add(new ServiceLevelImpl(app_Identifier, ordering, name,
		    description));

	    cursor.moveToNext();
	}

	return list.toArray(new IServiceLevel[list.size()]);
    }

    @Override
    public IServiceLevel getServiceLevel(int ordering) {

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	Cursor cursor = db
		.rawQuery(
			"SELECT App_Identifier, Ordering, Name_Cache, Description_Cache FROM ServiceLevel WHERE Ordering = "
				+ ordering + ";", null);

	String app_Identifier = cursor.getString(cursor
		.getColumnIndex("App_Identifier"));
	ordering = cursor.getInt(cursor
		    .getColumnIndex("Ordering"));
	String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	String description = cursor.getString(cursor
		.getColumnIndex("Description_Cache"));

	cursor.moveToNext();

	return new ServiceLevelImpl(app_Identifier, ordering, name, description);
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
