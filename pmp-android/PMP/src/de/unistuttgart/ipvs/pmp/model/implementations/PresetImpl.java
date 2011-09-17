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
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPreset;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;

/**
 * Implementation of the {@link IPreset} interface.
 * 
 * @author Jakob Jarosch
 */
public class PresetImpl implements IPreset {

    private String name;
    private PMPComponentType type;
    private String identifier;
    private String description;

    public PresetImpl(String name, String description, PMPComponentType type,
	    String identifier) {
	this.name = name;
	this.description = description;
	this.type = type;
	this.identifier = identifier;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public PMPComponentType getType() {
	return type;
    }

    @Override
    public String getIdentifier() {
	if (identifier != null && identifier.length() == 0) {
	    return null;
	} else {
	    return identifier;
	}
    }

    @Override
    public String getDescription() {
	return description;
    }

    @Override
    public IApp[] getAssignedApps() {
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	List<IApp> list = new ArrayList<IApp>();

	Cursor cursor = db
		.rawQuery(
			"SELECT a.App_Identifier, a.Name_Cache, a.Description_Cache FROM App as a, Preset_Apps AS pa"
				+ "WHERE pa.App_Identifier = a.Identifier AND pa.Name = ? AND pa.Type = ? AND pa.Identifier = ?",
			new String[] { name, type.toString(), identifier });

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

	Log.v("PresetImpl#getAssignedApps(): is returning " + list.size()
		+ " datasets for Preset-Name " + getName());

	return list.toArray(new IApp[list.size()]);
    }

    @Override
    public boolean isAppAssigned(IApp app) {
	ModelConditions.assertNotNull("app", app);

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	Cursor cursor = db
		.rawQuery(
			"SELECT Preset_Name FROM Preset_Apps WHERE Preset_Name = ? AND Preset_Type = ? AND Preset_Identifier = ? AND App_Identifier = ?",
			new String[] { name, type.toString(), identifier,
				app.getIdentifier() });

	boolean result = (cursor.getCount() > 0);

	Log.v("PresetImpl#isAppAssigned(): The App " + app.getIdentifier()
		+ " is " + (result ? "" : "not ") + " assigned to the Preset "
		+ getName());

	cursor.close();
	return result;
    }

    @Override
    public void addApp(IApp app) {
	addApp(app, false);
    }

    @Override
    public void addApp(IApp app, boolean hidden) {
	ModelConditions.assertNotNull("app", app);

	Log.v("PresetImpl#addApp(): The App " + app.getIdentifier()
		+ " is beeing added to the Preset " + getName());

	if (!isAppAssigned(app)) {
	    SQLiteDatabase db = DatabaseSingleton.getInstance()
		    .getDatabaseHelper().getWritableDatabase();

	    ContentValues cv = new ContentValues();
	    cv.put("Preset_Name", name);
	    cv.put("Preset_Type", type.toString());
	    cv.put("Preset_Identifier", identifier);
	    cv.put("App_Identifier", app.getIdentifier());

	    db.insert("Preset_Apps", null, cv);

	    if (!hidden) {
		app.verifyServiceLevel();
	    }
	} else {
	    Log.v("PresetImpl#addApp(): The App " + app.getIdentifier()
		    + " was already assigned to the Preset " + getName());
	}
    }

    @Override
    public void removeApp(IApp app) {
	removeApp(app, false);
    }

    @Override
    public void removeApp(IApp app, boolean hidden) {
	ModelConditions.assertNotNull("app", app);

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getWritableDatabase();

	db.delete(
		"Preset_Apps",
		"Preset_Name = ? AND Preset_Type = ? AND Preset_Identifier = ? AND App_Identifier = ?",
		new String[] { name, type.toString(), identifier,
			app.getIdentifier() });

	Log.v("PresetImpl#removeApp(): Removed the App " + app.getIdentifier()
		+ " from the Preset " + getName());

	if (!hidden) {
	    app.verifyServiceLevel();
	}
    }

    @Override
    public IPrivacyLevel[] getUsedPrivacyLevels() {
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	List<IPrivacyLevel> list = new ArrayList<IPrivacyLevel>();

	Cursor cursor = db
		.rawQuery(
			"SELECT pl.ResourceGroup_Identifier, pl.Identifier, pl.Name_Cache, pl.Description_Cache, pp.Value FROM PrivacyLevel as pl, Preset_PrivacyLevels AS pp"
				+ "WHERE pp.Name = ? AND pp.Type = ? AND pp.Identifier = ? AND pp.ResourceGroup_Identifier = pl.ResourceGroupIdentifier AND "
				+ "pp.PrivacyLevel_Identifier = pl.Identifier",
			new String[] { name, type.toString(), identifier });

	cursor.moveToNext();

	while (!cursor.isAfterLast()) {
	    String resourceGroupIdentifier = cursor.getString(cursor
		    .getColumnIndex("ResourceGroup_Identifier"));
	    String identifier = cursor.getString(cursor
		    .getColumnIndex("Identifier"));
	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description_Cache"));
	    String value = cursor.getString(cursor.getColumnIndex("Value"));

	    list.add(new PrivacyLevelImpl(resourceGroupIdentifier, identifier,
		    name, description, value));

	    cursor.moveToNext();
	}

	Log.v("PresetImpl#getUsedPrivacyLevels(): is returning " + list.size()
		+ " datasets for Preset-Name " + getName());

	cursor.close();
	return list.toArray(new IPrivacyLevel[list.size()]);
    }

    @Override
    public void setPrivacyLevel(IPrivacyLevel privacyLevel) {
	setPrivacyLevel(privacyLevel, false);
    }

    @Override
    public void setPrivacyLevel(IPrivacyLevel privacyLevel, boolean hidden) {
	ModelConditions.assertNotNull("privacyLevel", privacyLevel);

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getWritableDatabase();

	Log.v("PresetImpl#setPrivacyLevel(): First remove the "
		+ privacyLevel.getIdentifier() + " from the Preset "
		+ getName());

	/*
	 * Remove the privacy level from preset (either it is inside or not),
	 * will prevent that the privacy level is doubled inside.
	 */
	removePrivacyLevel(privacyLevel, true);

	/* add the new (or changed) privacy level */
	ContentValues cv = new ContentValues();
	cv.put("Preset_Name", name);
	cv.put("Preset_Type", type.toString());
	cv.put("Preset_Identifier", identifier);
	cv.put("ResourceGroup_Identifier", privacyLevel.getResourceGroup()
		.getIdentifier());
	cv.put("PrivacyLevel_Identifier", privacyLevel.getIdentifier());
	cv.put("Value", privacyLevel.getValue());

	db.insert("Preset_PrivacyLevels", null, cv);

	Log.v("PresetImpl#setPrivacyLevel(): Added the PrivacyLevel "
		+ privacyLevel.getIdentifier() + " to the Preset " + getName());

	if (!hidden) {
	    for (IApp app : getAssignedApps()) {
		app.verifyServiceLevel();
	    }
	}
    }

    @Override
    public void removePrivacyLevel(IPrivacyLevel privacyLevel) {
	removePrivacyLevel(privacyLevel, false);
    }

    @Override
    public void removePrivacyLevel(IPrivacyLevel privacyLevel, boolean hidden) {
	ModelConditions.assertNotNull("privacyLevel", privacyLevel);

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getWritableDatabase();

	db.delete(
		"Preset_PrivacyLevels",
		"Preset_Name = ? AND Preset_Type = ? AND Preset_Identifier = ? AND ResourceGroup_Identifier = ? AND PrivacyLevel_Identifier = ?",
		new String[] { name, type.toString(), identifier,
			privacyLevel.getResourceGroup().getIdentifier(),
			privacyLevel.getIdentifier() });

	Log.v("PresetImpl#removePrivacyLevel(): Removed the PrivacyLevel "
		+ privacyLevel.getIdentifier() + " from the Preset "
		+ getName());

	if (!hidden) {
	    for (IApp app : getAssignedApps()) {
		app.verifyServiceLevel();
	    }
	}
    }
}
