package de.unistuttgart.ipvs.pmp.model.implementations;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.implementations.utils.ServiceLevelCalculator;
import de.unistuttgart.ipvs.pmp.model.implementations.utils.ServiceLevelPublisher;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPreset;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
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
			"SELECT Level, Name_Cache, Description_Cache FROM ServiceLevel WHERE App_Identifier = ? ORDER BY Level ASC",
			new String[] { identifier });

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
				+ level + " LIMIT 1",
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
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	Cursor cursor = db
		.rawQuery(
			"SELECT ServiceLevel_Active FROM App WHERE Identifier = ? LIMIT 1",
			new String[] { identifier });

	if (cursor != null && cursor.getCount() == 1) {
	    cursor.moveToNext();
	    int serviceLevel = cursor.getInt(cursor
		    .getColumnIndex("ServiceLevel_Active"));

	    return serviceLevel;
	} else {
	    Log.e("App was not found in Database, Model seems to be out of sync with Database.");
	    return 0;
	}

    }

    @Override
    public void setActiveServiceLevelAsPreset(int serviceLevel) {
	/* Remove App from all Presets */
	for (IPreset preset : getAssignedPresets()) {
	    preset.removeApp(this, true);
	}

	/* Create a new Preset (if not already exists) for this ServiceLevel */
	IPreset preset = ModelSingleton
		.getInstance()
		.getModel()
		.addPreset("AutoServiceLevelPreset", "", PMPComponentType.APP,
			identifier);
	preset.addApp(this, true);

	IServiceLevel sl = getServiceLevel(serviceLevel);
	for (IPrivacyLevel pl : sl.getPrivacyLevels()) {
	    preset.setPrivacyLevel(pl, true);
	}

	/* Set the new ServiceLevel */
	new ServiceLevelPublisher(this, false);
    }

    @Override
    public void verifyServiceLevel() {
	final IApp app = this;
	new Thread(new Runnable() {

	    @Override
	    public void run() {
		ServiceLevelCalculator slc = new ServiceLevelCalculator(app);
		int serviceLevel;
		try {
		    serviceLevel = slc.calculate();

		    if (serviceLevel != getActiveServiceLevel()) {
			setActiveServiceLevel(serviceLevel);
		    }
		} catch (RemoteException e) {
		    Log.e("Could not calculate the ServiceLevel, got RemoteException",
			    e);
		}

	    }
	}).start();
    }

    @Override
    public IPreset[] getAssignedPresets() {
	List<IPreset> list = new ArrayList<IPreset>();

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	Cursor cursor = db.rawQuery("SELECT p.Name, " + "p.Description, "
		+ "p.Type, " + "p.Identifier " + "FROM Preset as p, "
		+ "Preset_Apps AS pa " + "WHERE pa.Name = p.Name "
		+ "AND pa.Type = p.Type " + "AND pa.Identifier = p.Identifier"
		+ "AND p.App_Identifier = ?", new String[] { identifier });

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

	return list.toArray(new IPreset[list.size()]);
    }

    private void setActiveServiceLevel(int serviceLevel) {
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getWritableDatabase();

	db.rawQuery("UPDATE App SET ServiceLevel_Active = " + serviceLevel
		+ " WHERE Identifier = ?", new String[] { identifier });

	new ServiceLevelPublisher(this, false);
    }
}
