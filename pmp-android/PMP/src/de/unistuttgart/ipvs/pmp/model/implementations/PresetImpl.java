package de.unistuttgart.ipvs.pmp.model.implementations;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
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

	if (identifier != null && identifier.length() == 0) {
	    this.identifier = null;
	}
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
	return identifier;
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

	return list.toArray(new IApp[list.size()]);
    }

    @Override
    public boolean isAppAssigned(IApp app) {
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	Cursor cursor = db
		.rawQuery(
			"SELECT Name FROM Preset_Apps WHERE Name = ? AND Type = ? AND Identifier = ? AND App_Identifier = ?",
			new String[] { name, type.toString(), identifier,
				app.getIdentifier() });

	return (cursor.getCount() > 0);
    }

    @Override
    public void addApp(IApp app) {
	addApp(app, false);
    }

    @Override
    public void addApp(IApp app, boolean hidden) {
	if (!isAppAssigned(app)) {
	    SQLiteDatabase db = DatabaseSingleton.getInstance()
		    .getDatabaseHelper().getWritableDatabase();

	    db.rawQuery(
		    "INSERT INTO Preset_Apps (Name, Type, Identifier, App_Identifier) VALUES (?, ?, ?, ?)",
		    new String[] { name, type.toString(), identifier,
			    app.getIdentifier() });

	    if (!hidden) {
		app.verifyServiceLevel();
	    }
	}
    }

    @Override
    public void removeApp(IApp app) {
	removeApp(app, false);
    }

    @Override
    public void removeApp(IApp app, boolean hidden) {
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getWritableDatabase();

	db.rawQuery(
		"DELETE FROM Preset_Apps WHERE Name = ? AND Type = ? AND Identifier = ? AND App_Identifier = ?",
		new String[] { name, type.toString(), identifier,
			app.getIdentifier() });

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

	return list.toArray(new IPrivacyLevel[list.size()]);
    }

    @Override
    public void setPrivacyLevel(IPrivacyLevel privacyLevel) {
	setPrivacyLevel(privacyLevel, false);
    }

    @Override
    public void setPrivacyLevel(IPrivacyLevel privacyLevel, boolean hidden) {
	removePrivacyLevel(privacyLevel, true);

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getWritableDatabase();

	db.rawQuery(
		"INSERT INTO Preset_PrivacyLevels (Name, Type, Identifier, ResourceGroup_Identifier, PrivacyLevel_Identifier, Value) VALUES (?, ?, ?, ?, ?, ?)",
		new String[] { name, type.toString(), identifier,
			privacyLevel.getResourceGroup().getIdentifier(),
			privacyLevel.getIdentifier(), privacyLevel.getValue() });
	
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
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getWritableDatabase();

	db.rawQuery(
		"DELETE FROM Preset_PrivacyLevels Name = ? AND Type = ? AND Identifier = ? AND ResourceGroup_Identifier = ? AND PrivacyLevel_Identifier = ?",
		new String[] { name, type.toString(), identifier,
			privacyLevel.getResourceGroup().getIdentifier(),
			privacyLevel.getIdentifier() });

	if (!hidden) {
	    for (IApp app : getAssignedApps()) {
		app.verifyServiceLevel();
	    }
	}
    }

}
