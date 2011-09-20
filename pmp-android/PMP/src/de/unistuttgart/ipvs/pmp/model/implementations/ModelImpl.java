package de.unistuttgart.ipvs.pmp.model.implementations;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelConditions;
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

    /**
     * Constructor for {@link ModelImpl}.
     */
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

	Log.v("ModelImpl#getApps(): is returning " + list.size()
		+ " datasets");

	return list.toArray(new IApp[list.size()]);
    }

    @Override
    public IApp getApp(String identifier) {
	ModelConditions.assertStringNotNullOrEmpty("identifier", identifier);

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	IApp returnValue = null;

	Cursor cursor = db
		.rawQuery(
			"SELECT Name_Cache, Description_Cache FROM App WHERE Identifier = ?",
			new String[] { identifier });

	if (cursor.getCount() == 1) {
	    cursor.moveToNext();

	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description_Cache"));

	    returnValue = new AppImpl(identifier, name, description);
	} else {
	    Log.d("Model: There is no App with the identifier" + identifier
		    + " in the Database.");
	}

	Log.v("ModelImpl#getApp(): found "
		+ (returnValue == null ? "NO" : "a")
		+ " app with the identifier " + identifier);

	cursor.close();
	return returnValue;
    }

    @Override
    public void addApp(String identifier, byte[] publicKey) {
	ModelConditions.assertStringNotNullOrEmpty("identifier", identifier);
	ModelConditions.assertPublicKeyNotNullOrEmpty(publicKey);

	new AppRegistration(identifier, publicKey);
    }

    @Override
    public IResourceGroup[] getResourceGroups() {
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	List<IResourceGroup> list = new ArrayList<IResourceGroup>();

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

	Log.v("ModelImpl#getResourceGroups(): is returning "
		+ list.size() + " datasets");

	cursor.close();
	return list.toArray(new IResourceGroup[list.size()]);
    }

    @Override
    public IResourceGroup getResourceGroup(String identifier) {
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	IResourceGroup returnValue = null;

	Cursor cursor = db
		.rawQuery(
			"SELECT Name_Cache, Description_Cache FROM ResourceGroup WHERE Identifier = ?",
			new String[] { identifier });

	if (cursor.getCount() == 1) {
	    cursor.moveToNext();

	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description_Cache"));

	    returnValue = new ResourceGroupImpl(identifier, name, description);
	}

	Log.v("ModelImpl#getResourceGroup(): found  "
		+ (returnValue == null ? "NO" : "a")
		+ " resource group with the identifier " + identifier);

	cursor.close();
	return returnValue;
    }

    @Override
    public void addResourceGroup(String identifier, byte[] publicKey) {
	ModelConditions.assertStringNotNullOrEmpty("identifier", identifier);
	ModelConditions.assertPublicKeyNotNullOrEmpty(publicKey);

	new ResourceGroupRegistration(identifier, publicKey);
    }

    @Override
    public IPreset[] getPresets() {
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	List<IPreset> list = new ArrayList<IPreset>();

	Cursor cursor = db.rawQuery(
		"SELECT Name, Description, Type, Identifier FROM Preset", null);

	cursor.moveToNext();

	while (!cursor.isAfterLast()) {
	    String name = cursor.getString(cursor.getColumnIndex("Name"));
	    PMPComponentType type = PMPComponentType.NONE;
	    try {
		type = PMPComponentType.valueOf(cursor.getString(cursor
			.getColumnIndex("Type")));
	    } catch (IllegalArgumentException e) {
		Log.d("Model: Got a NULL PMPComponentType asume PMPComponentType.NONE was meant.");
	    }
	    String identifier = cursor.getString(cursor
		    .getColumnIndex("Identifier"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description"));

	    list.add(new PresetImpl(name, description, type, identifier));

	    cursor.moveToNext();
	}

	Log.v("ModelImpl#getPresets(): is returning " + list.size()
		+ " datasets");

	cursor.close();
	return list.toArray(new IPreset[list.size()]);
    }

    @Override
    public IPreset addPreset(String name, String description,
	    PMPComponentType type, String identifier) {
	ModelConditions.assertPMPComponentTypeNotNull(type);
	ModelConditions.assertStringNotNullOrEmpty("name", name);
	ModelConditions.assertPMPComponentTypeAndIdentiferMatch(type,
		identifier);

	if (description == null) {
	    Log.i("Model {ModelImpl#addPreset()}: description was NULL, emtpy string has been asinged.");
	    description = "";
	}
	if (identifier == null) {
	    Log.i("Model {ModelImpl#addPreset()}: identifier was NULL, emtpy string has been asinged.");
	    identifier = "";
	}

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
	} else {
	    Log.w("ModelImpl#addPreset(): found an exisiting Preset with that Name, Type and Identifier.");
	}

	Log.v("ModelImpl#addPreset(): returning the "
		+ (cursor.getCount() == 0 ? "new" : "existing") + " IPreset");

	cursor.close();
	return new PresetImpl(name, description, type, identifier);
    }
}