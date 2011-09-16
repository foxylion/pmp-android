package de.unistuttgart.ipvs.pmp.model.implementations;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.implementations.utils.AppRegistration;
import de.unistuttgart.ipvs.pmp.model.implementations.utils.ResourceGroupRegistration;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IModel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPreset;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;

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
	cursor.close();

	return list.toArray(new IApp[list.size()]);
    }

    @Override
    public IApp getApp(String identifier) {
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	Cursor cursor = db
		.rawQuery(
			"SELECT Name_Cache, Description_Cache FROM App WHERE Identifier = ?",
			new String[] { identifier });

	cursor.moveToNext();

	if (cursor.getCount() == 1) {
	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description_Cache"));

	    cursor.close();

	    return new AppImpl(identifier, name, description);
	} else {
	    cursor.close();
	    return null;
	}
    }

    @Override
    public void addApp(String identifier, byte[] publicKey) {
	new AppRegistration(identifier, publicKey);
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
	cursor.close();

	return list.toArray(new IResourceGroup[list.size()]);
    }

    @Override
    public IResourceGroup getResourceGroup(String identifier) {
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	Cursor cursor = db
		.rawQuery(
			"SELECT Name_Cache, Description_Cache FROM ResourceGroup WHERE Identifier = ?",
			new String[] { identifier });

	cursor.moveToNext();

	if (cursor.getCount() == 1) {
	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description_Cache"));

	    return new ResourceGroupImpl(identifier, name, description);
	}
	cursor.close();

	return null;
    }

    @Override
    public void addResourceGroup(String identifier, byte[] publicKey) {
	new ResourceGroupRegistration(identifier, publicKey);
    }

    @Override
    public IPreset[] getPresets() {
	List<IPreset> list = new ArrayList<IPreset>();

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	Cursor cursor = db.rawQuery(
		"SELECT Name, Description, Type, Identifier FROM Preset", null);

	cursor.moveToNext();

	while (!cursor.isAfterLast()) {
	    String name = cursor.getString(cursor.getColumnIndex("Name"));

	    PMPComponentType type;
	    try {
		type = PMPComponentType.valueOf(cursor.getString(cursor
			.getColumnIndex("Type")));
	    } catch (IllegalArgumentException e) {
		type = null;
	    }

	    String identifier = cursor.getString(cursor
		    .getColumnIndex("Identifier"));

	    String description = cursor.getString(cursor
		    .getColumnIndex("Description"));

	    list.add(new PresetImpl(name, description, type, identifier));

	    cursor.moveToNext();
	}
	cursor.close();

	return list.toArray(new IPreset[list.size()]);
    }

    @Override
    public IPreset addPreset(String name, String description,
	    PMPComponentType type, String identifier) {

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getWritableDatabase();

	Cursor cursor = db
		.rawQuery(
			"SELECT Name, Description, Type, Identifier FROM Preset WHERE Name = ? AND Type = ? AND Identifier = ?",
			new String[] { name, type.toString(), identifier });

	if (cursor.getCount() == 0) {
	    ContentValues cv = new ContentValues();
	    cv.put("Name", name);
	    cv.put("Description", description);
	    cv.put("Type", type.toString());
	    cv.put("Identifier", identifier);

	    db.insert("Preset", null, cv);
	}
	cursor.close();

	return new PresetImpl(name, description, type, identifier);
    }
}
