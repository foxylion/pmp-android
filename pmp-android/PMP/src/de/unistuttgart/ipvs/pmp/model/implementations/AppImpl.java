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

    /**
     * The identifier of the {@link IApp} should be unique.
     */
    private String identifier;

    /**
     * The localized name of the {@link IApp}.
     */
    private String name;

    /**
     * The localized description of the {@link IApp}.
     */
    private String description;

    /**
     * Constructor for the {@link AppImpl}.
     * 
     * @param identifier
     *            The identifier of the {@link IApp}.
     * @param name
     *            The name of the {@link IApp}.
     * @param description
     *            The description of the {@link IApp}.
     */
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
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	List<IServiceLevel> list = new ArrayList<IServiceLevel>();

	Cursor cursor = db
		.rawQuery(
			"SELECT Level, Name_Cache, Description_Cache FROM ServiceLevel WHERE App_Identifier = ? ORDER BY Level ASC",
			new String[] { getIdentifier() });

	cursor.moveToNext();

	/* iterating over the entries of service levels */
	while (!cursor.isAfterLast()) {
	    int level = cursor.getInt(cursor.getColumnIndex("Level"));
	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description_Cache"));

	    list.add(new ServiceLevelImpl(getIdentifier(), level, name,
		    description));

	    cursor.moveToNext();
	}
	cursor.close();

	return list.toArray(new IServiceLevel[list.size()]);
    }

    @Override
    public IServiceLevel getServiceLevel(int level) {
	ModelConditions.assertServiceLevelIdNotNegative(level);

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	IServiceLevel returnValue = null;

	Cursor cursor = db
		.rawQuery(
			"SELECT Level, Name_Cache, Description_Cache FROM ServiceLevel WHERE App_Identifier = ? AND Level = "
				+ level + " LIMIT 1",
			new String[] { getIdentifier() });

	if (cursor.getCount() == 1) {
	    cursor.moveToNext();
	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description_Cache"));

	    returnValue = new ServiceLevelImpl(getIdentifier(), level, name,
		    description);
	} else {
	    Log.d("ServiceLevel " + level + " for " + getIdentifier()
		    + " was not found in Database.");
	}

	cursor.close();
	return returnValue;
    }

    @Override
    public IServiceLevel getActiveServiceLevel() {
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	IServiceLevel returnValue = null;

	Cursor cursor = db
		.rawQuery(
			"SELECT ServiceLevel_Active FROM App WHERE Identifier = ? LIMIT 1",
			new String[] { getIdentifier() });

	if (cursor.getCount() == 1) {
	    cursor.moveToNext();
	    int serviceLevel = cursor.getInt(cursor
		    .getColumnIndex("ServiceLevel_Active"));

	    returnValue = getServiceLevel(serviceLevel);
	} else {
	    Log.e("Model: App "
		    + getIdentifier()
		    + " was not found in Database, Model seems to be out of sync with Database.");
	}

	cursor.close();
	return returnValue;
    }

    @Override
    public boolean setActiveServiceLevelAsPreset(int level) {
	ModelConditions.assertServiceLevelIdNotNegative(level);

	if (getServiceLevel(level).isAvailable()) {
	    /* Remove App from all Presets */
	    for (IPreset preset : getAssignedPresets()) {
		preset.removeApp(this, true);
	    }

	    /* Create a new Preset (if not already exists) for this ServiceLevel */
	    IPreset preset = ModelSingleton
		    .getInstance()
		    .getModel()
		    .addPreset("AutoServiceLevelPreset", "",
			    PMPComponentType.APP, getIdentifier());
	    preset.addApp(this, true);

	    IServiceLevel sl = getServiceLevel(level);
	    for (IPrivacyLevel pl : sl.getPrivacyLevels()) {
		preset.setPrivacyLevel(pl, true);
	    }

	    return setActiveServiceLevel(level, false);
	} else {
	    Log.w("Model: Tried to set the ServiceLevel " + level
		    + " which is currently not available for "
		    + getIdentifier());
	    return false;
	}
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
		    Log.e("Model: Could not calculate ServiceLevel for "
			    + app.getIdentifier() + ", got RemoteException", e);
		}

		if (serviceLevel != getActiveServiceLevel().getLevel()) {
		    setActiveServiceLevel(serviceLevel, true);
		}

	    }
	}).start();
    }

    @Override
    public IPreset[] getAssignedPresets() {
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	List<IPreset> list = new ArrayList<IPreset>();

	Cursor cursor = db
		.rawQuery(
			"SELECT p.Name, p.Description, p.Type, p.Identifier "
				+ "FROM Preset as p, Preset_Apps AS pa "
				+ "WHERE pa.Preset_Name = p.Name AND pa.Preset_Type = p.Type AND pa.Preset_Identifier = p.Identifier AND pa.App_Identifier = ?",
			new String[] { getIdentifier() });

	cursor.moveToNext();

	while (!cursor.isAfterLast()) {
	    String name = cursor.getString(cursor.getColumnIndex("Name"));
	    PMPComponentType type = PMPComponentType.valueOf(cursor
		    .getString(cursor.getColumnIndex("Type")));
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
    public IResourceGroup[] getAllResourceGroupsUsedByServiceLevels() {
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	List<IResourceGroup> list = new ArrayList<IResourceGroup>();

	Cursor cursor = db
		.rawQuery(
			"SELECT ResourceGroup_Identifier FROM ServiceLevel_PrivacyLevels WHERE App_Identifier = ?",
			new String[] { getIdentifier() });

	cursor.moveToNext();

	while (!cursor.isAfterLast()) {
	    String rgIdentifier = cursor.getString(cursor
		    .getColumnIndex("ResourceGroup_Identifier"));
	    IResourceGroup resourceGroup = ModelSingleton.getInstance()
		    .getModel().getResourceGroup(rgIdentifier);

	    list.add(resourceGroup);

	    cursor.moveToNext();
	}

	cursor.close();
	return list.toArray(new IResourceGroup[list.size()]);
    }

    @Override
    public IPrivacyLevel[] getAllPrivacyLevelsUsedByActiveServiceLevel(
	    IResourceGroup resourceGroup) {
	ModelConditions.assertNotNull("resourceGroup", resourceGroup);

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
			new String[] { getIdentifier() });

	cursor.moveToNext();

	while (!cursor.isAfterLast()) {
	    String plIdentifier = cursor.getString(cursor
		    .getColumnIndex("PrivacyLevel_Identifier"));
	    String value = cursor.getString(cursor.getColumnIndex("Value"));
	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description_Cache"));

	    IPrivacyLevel pl = new PrivacyLevelImpl(
		    resourceGroup.getIdentifier(), plIdentifier, name,
		    description, value);

	    list.add(pl);
	}

	cursor.close();
	return list.toArray(new IPrivacyLevel[list.size()]);
    }

    private boolean setActiveServiceLevel(int level, boolean asynchronously) {
	ModelConditions.assertServiceLevelIdNotNegative(level);

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getWritableDatabase();

	IServiceLevel oldServiceLevel = getActiveServiceLevel();

	if (!getServiceLevel(level).isAvailable()) {
	    return false;
	}

	ContentValues cv = new ContentValues();
	cv.put("ServiceLevel_Active", level);

	db.update("App", cv, "Identifier = ?", new String[] { getIdentifier() });

	Log.d("Model: Set for App " + getIdentifier()
		+ " the new service level " + level + ".");

	ServiceLevelPublisher slp = new ServiceLevelPublisher(this,
		oldServiceLevel);
	slp.publish(asynchronously);

	return true;
    }
}