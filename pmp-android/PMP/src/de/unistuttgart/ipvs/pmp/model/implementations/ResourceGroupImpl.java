package de.unistuttgart.ipvs.pmp.model.implementations;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;

/**
 * Implementation of the {@link IResourceGroup} interface.
 * 
 * @author Jakob Jarosch
 */
public class ResourceGroupImpl implements IResourceGroup {

    private String identifier;
    private String name;
    private String description;

    public ResourceGroupImpl(String identifier, String name, String description) {
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
    public IPrivacyLevel[] getPrivacyLevels() {
	List<IPrivacyLevel> list = new ArrayList<IPrivacyLevel>();

	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	Cursor cursor = db
		.rawQuery(
			"SELECT Identifier, Name_Cache, Description_Cache FROM PrivacyLevel WHERE ResourceGroup_Identifier = ?",
			new String[] { identifier });

	cursor.moveToNext();

	while (!cursor.isAfterLast()) {
	    String plIdentifier = cursor.getString(cursor
		    .getColumnIndex("Identifier"));
	    String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
	    String description = cursor.getString(cursor
		    .getColumnIndex("Description_Cache"));

	    list.add(new PrivacyLevelImpl(identifier, plIdentifier, name,
		    description));

	    cursor.moveToNext();
	}
	cursor.close();

	return list.toArray(new IPrivacyLevel[list.size()]);
    }
    
    @Override
    public IApp[] getAllAppsUsingThisResourceGroup() {
	SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper()
		.getReadableDatabase();

	List<IApp> list = new ArrayList<IApp>();
	
	Cursor cursor = db
		.rawQuery(
			"SELECT a.Identifier "
				+ "FROM App AS a, ServiceLevel_PrivacyLevels AS slpl "
				+ "WHERE a.ServiceLevel_Active = slpl.ServiceLevel_Level AND slpl.ResourceGroup_Identifier = ?",
			new String[] { identifier });
	
	cursor.moveToNext();
	
	while(!cursor.isAfterLast()) {
	    String appIdentifier = cursor.getString(cursor.getColumnIndex("Identifier"));
	    IApp app = ModelSingleton.getInstance().getModel().getApp(appIdentifier);
	    list.add(app);
	    cursor.moveToNext();
	}
	cursor.close();

	return list.toArray(new IApp[list.size()]);
    }

}
