package de.unistuttgart.ipvs.pmp.model2.element.preset;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.model2.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model2.element.app.App;
import de.unistuttgart.ipvs.pmp.model2.element.privacylevel.PrivacyLevel;
import de.unistuttgart.ipvs.pmp.model2.element.resourcegroup.ResourceGroup;

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
    public void loadElementData(SQLiteDatabase rdb) {
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
            
            this.element.privacyLevelValues = new HashMap<PrivacyLevel, String>();
            while (!cpl.isAfterLast()) {
                String rgId = cpl.getString(cpl.getColumnIndex("ResourceGroup_Identifier"));
                String plId = cpl.getString(cpl.getColumnIndex("PrivacyLevel_Identifier"));
                String value = cpl.getString(cpl.getColumnIndex("Value"));
                
                ResourceGroup rg = getCache().getResourceGroups().get(rgId);
                if (rg == null) {
                    Log.w("Invalid preset cached (rg does not exist)");
                    // TODO set invalid flag here
                } else {
                    PrivacyLevel pl = getCache().getPrivacyLevels().get(rg).get(plId);
                    
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
            
            this.element.assignedApps = new ArrayList<App>();
            while (!capp.isAfterLast()) {
                String appId = capp.getString(capp.getColumnIndex("App_Identifier"));
                
                App app = getCache().getApps().get(appId);
                
                this.element.assignedApps.add(app);
                capp.moveToNext();
            }
            capp.close();
            
        }
        
        c.close();
    }
    
    
    @Override
    public void storeElementData(SQLiteDatabase wdb) {
        ContentValues cv = new ContentValues();
        cv.put("Name", this.element.name);
        cv.put("Description", this.element.description);
        
        wdb.update("Preset", cv, "Identifier = ? AND Type = ?", new String[] { this.element.getIdentifier(),
                this.element.getType().toString() });
    }
    
}
