package de.unistuttgart.ipvs.pmp.model.implementations;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IModel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPreset;

/**
 * Implementation of the {@link IModel} interface.
 * 
 * @author Jakob Jarosch
 */
public class ModelImpl implements IModel {

    public ModelImpl() {

    }

    @Override
    public IApp[] getApps() {
	List<IApp> list = new ArrayList<IApp>();

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	Cursor cursor = db.rawQuery(
		"SELECT Identifier, Name_Cache, Description_Cache FROM App",
		null);
	
	cursor.moveToNext();

	while (!cursor.isAfterLast()) {
	    String identifier = cursor.getString(cursor
		    .getColumnIndex("Identifier"));
	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description_Cache"));

	    list.add(new AppImpl(identifier, name, description));

	    cursor.moveToNext();
	}

	return list.toArray(new IApp[list.size()]);
    }

    @Override
    public void addApp(String identifier, String publicKey) {
	// TODO Auto-generated method stub
    }

    @Override
    public IResourceGroup[] getResourceGroups() {
	List<IResourceGroup> list = new ArrayList<IResourceGroup>();

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	Cursor cursor = db
		.rawQuery(
			"SELECT Identifier, Name_Cache, Description_Cache FROM ResourceGroup",
			null);

	cursor.moveToNext();
	
	while (!cursor.isAfterLast()) {
	    String identifier = cursor.getString(cursor
		    .getColumnIndex("Identifier"));
	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description_Cache"));

	    list.add(new ResourceGroupImpl(identifier, name, description));

	    cursor.moveToNext();
	}

	return list.toArray(new IResourceGroup[list.size()]);
    }

    @Override
    public void addResourceGroup(String identifier, String publicKey) {
	// TODO Auto-generated method stub
    }

    @Override
    public IPreset[] getPresets() {
	List<IPreset> list = new ArrayList<IPreset>();

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	Cursor cursor = db
		.rawQuery(
			"SELECT Name, ResourceGroup_Identifier, Description FROM Preset",
			null);

	cursor.moveToNext();
	
	while (!cursor.isAfterLast()) {
	    String name = cursor.getString(cursor.getColumnIndex("Name"));
	    String rgIdentifier = cursor.getString(cursor
		    .getColumnIndex("ResourceGroup_Identifier"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description"));

	    list.add(new PresetImpl(name, rgIdentifier, description));

	    cursor.moveToNext();
	}

	return list.toArray(new IPreset[list.size()]);
    }
}
