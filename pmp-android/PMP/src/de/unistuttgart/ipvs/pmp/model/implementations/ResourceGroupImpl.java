package de.unistuttgart.ipvs.pmp.model.implementations;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelConditions;
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
        return this.identifier;
    }
    
    
    @Override
    public String getName() {
        return this.name;
    }
    
    
    @Override
    public String getDescription() {
        return this.description;
    }
    
    
    @Override
    public IPrivacyLevel[] getPrivacyLevels() {
        List<IPrivacyLevel> list = new ArrayList<IPrivacyLevel>();
        
        SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper().getReadableDatabase();
        
        Cursor cursor = db
                .rawQuery(
                        "SELECT Identifier, Name_Cache, Description_Cache FROM PrivacyLevel WHERE ResourceGroup_Identifier = ?",
                        new String[] { this.identifier });
        
        cursor.moveToNext();
        
        while (!cursor.isAfterLast()) {
            String plIdentifier = cursor.getString(cursor.getColumnIndex("Identifier"));
            String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
            String description = cursor.getString(cursor.getColumnIndex("Description_Cache"));
            
            list.add(new PrivacyLevelImpl(this.identifier, plIdentifier, name, description));
            
            cursor.moveToNext();
        }
        
        Log.v("ResourceGroupImpl#getPrivacyLevels(): is returning " + list.size() + " datasets for identifier "
                + getIdentifier());
        
        cursor.close();
        return list.toArray(new IPrivacyLevel[list.size()]);
    }
    
    
    @Override
    public IPrivacyLevel getPrivacyLevel(String privacyLevelIdentifier) {
        ModelConditions.assertStringNotNullOrEmpty("privacyLevelIdentifier", privacyLevelIdentifier);
        
        SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper().getReadableDatabase();
        
        PrivacyLevelImpl returnValue = null;
        
        Cursor cursor = db
                .rawQuery(
                        "SELECT Identifier, Name_Cache, Description_Cache FROM PrivacyLevel WHERE ResourceGroup_Identifier = ? AND Identifier = ?",
                        new String[] { this.identifier, privacyLevelIdentifier });
        
        if (cursor.getCount() == 1) {
            cursor.moveToNext();
            
            String plIdentifier = cursor.getString(cursor.getColumnIndex("Identifier"));
            String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
            String description = cursor.getString(cursor.getColumnIndex("Description_Cache"));
            
            returnValue = new PrivacyLevelImpl(this.identifier, plIdentifier, name, description);
        } else {
            Log.w("ResourceGroupImpl#getPrivacyLevel(): There is no PrivacyLevel " + privacyLevelIdentifier
                    + " in the ResourceGroup " + this.identifier);
        }
        
        cursor.close();
        return returnValue;
    }
    
    
    @Override
    public IApp[] getAllAppsUsingThisResourceGroup() {
        SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper().getReadableDatabase();
        
        List<IApp> list = new ArrayList<IApp>();
        
        Cursor cursor = db.rawQuery("SELECT a.Identifier " + "FROM App AS a, ServiceLevel_PrivacyLevels AS slpl "
                + "WHERE a.ServiceLevel_Active = slpl.ServiceLevel_Level AND slpl.ResourceGroup_Identifier = ? "
                + "GROUP BY a.Identifier", new String[] { this.identifier });
        
        cursor.moveToNext();
        
        while (!cursor.isAfterLast()) {
            String appIdentifier = cursor.getString(0);
            IApp app = ModelSingleton.getInstance().getModel().getApp(appIdentifier);
            list.add(app);
            cursor.moveToNext();
        }
        
        Log.v("ResourceGroupImpl#getAllAppsUsingThisResourceGroup(): is returning " + list.size()
                + " datasets for identifier " + getIdentifier());
        
        cursor.close();
        return list.toArray(new IApp[list.size()]);
    }
    
}
