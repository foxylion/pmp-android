package de.unistuttgart.ipvs.pmp.model.implementations;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
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
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;
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
    public IServiceLevel getActiveServiceLevel() {
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

	    return getServiceLevel(serviceLevel);
	} else {
	    Log.e("App was not found in Database, Model seems to be out of sync with Database.");
	    return null;
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

	setActiveServiceLevel(serviceLevel, false);

	/* Set the new ServiceLevel */

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
		} catch (RemoteException e) {
		    serviceLevel = 0;
		    Log.e("Could not calculate service level", e);
		}

		if (serviceLevel != getActiveServiceLevel().getLevel()) {
		    setActiveServiceLevel(serviceLevel, true);
		}

	    }
	}).start();
    }

    @Override
    public IPreset[] getAssignedPresets() {
	List<IPreset> list = new ArrayList<IPreset>();

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	Cursor cursor = db
		.rawQuery(
			"SELECT p.Name, p.Description, p.Type, p.Identifier "
				+ "FROM Preset as p, Preset_Apps AS pa "
				+ "WHERE pa.Preset_Name = p.Name AND pa.Preset_Type = p.Type AND pa.Preset_Identifier = p.Identifier AND pa.App_Identifier = ?",
			new String[] { identifier });

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

    @Override
    public IResourceGroup[] getAllResourceGroupsUsedByServiceLevels() {
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	List<IResourceGroup> list = new ArrayList<IResourceGroup>();

	Cursor cursor = db
		.rawQuery(
			"SELECT ResourceGroup_Identifier FROM ServiceLevel_PrivacyLevels WHERE App_Identifier = ?",
			new String[] { identifier });

	cursor.moveToNext();

	while (!cursor.isAfterLast()) {
	    String rgIdentifier = cursor.getString(cursor
		    .getColumnIndex("ResourceGroup_Identifier"));
	    IResourceGroup resourceGroup = ModelSingleton.getInstance()
		    .getModel().getResourceGroup(rgIdentifier);
	    list.add(resourceGroup);
	}

	return list.toArray(new IResourceGroup[list.size()]);
    }

    @Override
    public IPrivacyLevel[] getAllPrivacyLevelsUsedByActiveServiceLevel(
	    IResourceGroup resourceGroup) {
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	List<IPrivacyLevel> list = new ArrayList<IPrivacyLevel>();

	Cursor cursor = db
		.rawQuery(
			"SELECT slpl.PrivacyLevel_Identifier, slpl.Value, pl.Name_Cache, pl.Description_Cache "
				+ "FROM ServiceLevel_PrivacyLevels AS slpl, PrivacyLevel AS pl "
				+ "WHERE slpl.ResourceGroup_Identifier = pl.ResourceGroup_Identifier AND slpl.PrivacyLevel_Identifier = pl.Identifier "
				+ "AND slpl.App_Identifier = ? AND slpl.ResourceGroup_Identifier = ? AND slpl.ServiceLevel_Level = "
				+ getActiveServiceLevel().getLevel(),
			new String[] { identifier });

	cursor.moveToNext();

	while (!cursor.isAfterLast()) {
	    String plIdentifier = cursor.getString(cursor
		    .getColumnIndex("PrivacyLevel_Identifier"));
	    String value = cursor.getString(cursor.getColumnIndex("Value"));
	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor.getColumnIndex("Description_Cache"));

	    IPrivacyLevel pl = new PrivacyLevelImpl(
		    resourceGroup.getIdentifier(), plIdentifier, name,
		    description, value);
	    list.add(pl);
	}

	return list.toArray(new IPrivacyLevel[list.size()]);
    }

    private void setActiveServiceLevel(int serviceLevel, boolean asynchronously) {
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getWritableDatabase();

	IServiceLevel oldServiceLevel = getActiveServiceLevel();

	ContentValues cv = new ContentValues();
	cv.put("ServiceLevel_Active", serviceLevel);

	db.update("App", cv, "Identifier = ?", new String[] { identifier });

	Log.d("Setting for " + identifier + " the new service level "
		+ serviceLevel + ".");

	ServiceLevelPublisher slp = new ServiceLevelPublisher(this,
		oldServiceLevel);
	slp.publish(asynchronously);
    }
}
