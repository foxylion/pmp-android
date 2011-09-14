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
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void setPrivacyLevel(IPrivacyLevel privacyLevel) {
	setPrivacyLevel(privacyLevel, false);
    }

    @Override
    public void setPrivacyLevel(IPrivacyLevel privacyLevel, boolean hidden) {
	// TODO Auto-generated method stub

    }

    @Override
    public void removePrivacyLevel(IPrivacyLevel privacyLevel) {
	removePrivacyLevel(privacyLevel, false);
    }

    @Override
    public void removePrivacyLevel(IPrivacyLevel privacyLevel, boolean hidden) {
	// TODO Auto-generated method stub

	if (!hidden) {
	    for (IApp app : getAssignedApps()) {
		app.verifyServiceLevel();
	    }
	}
    }

}
