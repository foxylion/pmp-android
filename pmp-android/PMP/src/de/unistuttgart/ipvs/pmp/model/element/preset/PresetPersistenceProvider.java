package de.unistuttgart.ipvs.pmp.model.element.preset;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.model.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.app.App;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.ResourceGroup;

/**
 * The persistence provider for {@link Preset}s.
 * 
 * @author Tobias Kuhn
 * 
 */
public class PresetPersistenceProvider extends ElementPersistenceProvider<Preset> {
    
    public PresetPersistenceProvider(Preset element) {
        super(element);
    }
    
    
    @Override
    protected void loadElementData(SQLiteDatabase rdb) {/*
        Cursor c = rdb.rawQuery("SELECT Name, Description FROM Preset WHERE Identifier = ? AND Type = ? LIMIT 1",
                new String[] { this.element.getIdentifier(), this.element.getType().toString() });
        
        if (!c.moveToFirst()) {
            throw new IllegalAccessError("The Identifier " + this.element.getIdentifier()
                    + " was not found in the database.");
        } else {
            this.element.name = c.getString(c.getColumnIndex("Name"));
            this.element.description = c.getString(c.getColumnIndex("Description"));
            
            // load privacy level values
            Cursor cpl = rdb.rawQuery(
                    "SELECT ResourceGroup_Identifier, PrivacyLevel_Identifier, Value FROM Preset_PrivacyLevels"
                            + " WHERE Preset_Identifier = ? AND Preset_Type = ?",
                    new String[] { this.element.getIdentifier(), this.element.getType().toString() });
            cpl.moveToFirst();
            
            this.element.privacyLevelValues = new HashMap<IPrivacySetting, String>();
            while (!cpl.isAfterLast()) {
                String rgId = cpl.getString(cpl.getColumnIndex("ResourceGroup_Identifier"));
                String plId = cpl.getString(cpl.getColumnIndex("PrivacyLevel_Identifier"));
                String value = cpl.getString(cpl.getColumnIndex("Value"));
                
                ResourceGroup rg = getCache().getResourceGroups().get(rgId);
                this.element.available = true;
                if (rg == null) {
                    Log.w("Invalid preset cached (rg does not exist)");
                    
                    this.element.available = false;
                } else {
                    PrivacySetting pl = getCache().getPrivacyLevels().get(rg).get(plId);
                    
                    this.element.privacyLevelValues.put(pl, value);
                }
                cpl.moveToNext();
            }
            cpl.close();
            
            // load assigned apps
            Cursor capp = rdb.rawQuery("SELECT App_Identifier FROM Preset_Apps"
                    + " WHERE Preset_Identifier = ? AND Preset_Type = ?", new String[] { this.element.getIdentifier(),
                    this.element.getType().toString() });
            capp.moveToFirst();
            
            this.element.assignedApps = new ArrayList<IApp>();
            while (!capp.isAfterLast()) {
                String appId = capp.getString(capp.getColumnIndex("App_Identifier"));
                
                App app = getCache().getApps().get(appId);
                
                this.element.assignedApps.add(app);
                capp.moveToNext();
            }
            capp.close();
            
        }
        
        c.close();*/
    }
    
    
    @Override
    protected void storeElementData(SQLiteDatabase wdb) {/*
        ContentValues cv = new ContentValues();
        cv.put("Name", this.element.name);
        cv.put("Description", this.element.description);
        
        wdb.update("Preset", cv, "Identifier = ? AND Type = ?", new String[] { this.element.getIdentifier(),
                this.element.getType().toString() });*/
    }
    
    
    @Override
    protected void deleteElementData(SQLiteDatabase wdb) {
        // TODO Auto-generated method stub
        
    }
    
    
    protected void assignApp(IApp app) {/*
        SQLiteDatabase wdb = getDoh().getWritableDatabase();
        
        ContentValues cv = new ContentValues();
        cv.put("Preset_Name", this.element.getName());
        cv.put("Preset_Type", this.element.getType().toString());
        cv.put("Preset_Identifier", this.element.getIdentifier());
        cv.put("App_Identifier", app.getIdentifier());
        
        wdb.insert("Preset_Apps", null, cv);*/
    }
    
    
    protected void removeApp(IApp app) {/*
        SQLiteDatabase wdb = getDoh().getWritableDatabase();
        
        wdb.rawQuery(
                "DELETE FROM Preset_Apps WHERE Preset_Name = ? AND Preset_Type = ? AND Preset_Identifier = ? AND App_Identifier = ?",
                new String[] { this.element.getName(), this.element.getType().toString(), this.element.getIdentifier(),
                        app.getIdentifier() });
        */
    }
    
    
    protected void assignPrivacyLevel(IPrivacySetting pl, String value) {/*
        SQLiteDatabase wdb = getDoh().getWritableDatabase();
        
        ContentValues cv = new ContentValues();
        cv.put("Preset_Name", this.element.getName());
        cv.put("Preset_Type", this.element.getType().toString());
        cv.put("Preset_Identifier", this.element.getIdentifier());
        cv.put("ResourceGroup_Identifier", pl.getResourceGroup().getIdentifier());
        cv.put("PrivacyLevel_Identifier", pl.getIdentifier());
        cv.put("Value", value);
        
        if (wdb.insert("Preset_PrivacyLevels", null, cv) == -1) {
            
            wdb.update(
                    "Preset_PrivacyLevels",
                    cv,
                    "Preset_Name = ? AND Preset_Type = ? AND Preset_Identifier = ? AND ResourceGroup_Identifier = ? AND PrivacyLevel_Identifier = ?",
                    new String[] { this.element.getName(), this.element.getType().toString(),
                            this.element.getIdentifier(), pl.getResourceGroup().getIdentifier(), pl.getIdentifier() });
        }*/
    }
    
    
    protected void removePrivacyLevel(IPrivacySetting pl) {/*
        SQLiteDatabase wdb = getDoh().getWritableDatabase();
        
        wdb.rawQuery(
                "DELETE FROM Preset_PrivacyLevels WHERE Preset_Name = ? AND Preset_Type = ? AND Preset_Identifier = ? AND ResourceGroup_Identifier = ? AND PrivacyLevel_Identifier = ?",
                new String[] { this.element.getName(), this.element.getType().toString(), this.element.getIdentifier(),
                        pl.getResourceGroup().getIdentifier(), pl.getIdentifier() });*/
    }
    
}
