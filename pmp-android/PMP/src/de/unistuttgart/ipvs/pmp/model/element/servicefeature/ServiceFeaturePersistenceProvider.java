package de.unistuttgart.ipvs.pmp.model.element.servicefeature;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.model.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.ResourceGroup;

/**
 * The persistence provider for {@link ServiceFeature}s.
 * 
 * @author Tobias Kuhn
 * 
 */
public class ServiceFeaturePersistenceProvider extends ElementPersistenceProvider<ServiceFeature> {
    
    public ServiceFeaturePersistenceProvider(ServiceFeature element) {
        super(element);
    }
    
    
    @Override
    protected void loadElementData(SQLiteDatabase rdb, SQLiteQueryBuilder qb) {/*
        Cursor c = rdb
                .rawQuery(
                        "SELECT Name_Cache, Description_Cache FROM ServiceFeature WHERE App_Identifier = ? AND Level = ? LIMIT 1",
                        new String[] { this.element.getApp().getIdentifier(), String.valueOf(this.element.getLevel()) });
        
        if (!c.moveToFirst()) {
            throw new IllegalAccessError("The Identifier " + this.element.getLevel() + " of "
                    + this.element.getApp().getIdentifier() + " was not found in the database.");
        } else {
            this.element.name = c.getString(c.getColumnIndex("Name_Cache"));
            this.element.description = c.getString(c.getColumnIndex("Description_Cache"));
            
            this.element.privacyLevelValues = new HashMap<PrivacySetting, String>();
            // load privacy level values
            Cursor cpl = rdb.rawQuery(
                    "SELECT ResourceGroup_Identifier, PrivacyLevel_Identifier, Value FROM ServiceLevel_PrivacyLevels"
                            + " WHERE App_Identifier = ? AND ServiceLevel_Level = ?", new String[] {
                            this.element.getApp().getIdentifier(), String.valueOf(this.element.getLevel()) });
            cpl.moveToFirst();
            
            // load the required privacy level values
            this.element.privacyLevelValues = new HashMap<PrivacySetting, String>();
            this.element.available = true;
            while (!cpl.isAfterLast()) {
                String rgId = cpl.getString(cpl.getColumnIndex("ResourceGroup_Identifier"));
                String plId = cpl.getString(cpl.getColumnIndex("PrivacyLevel_Identifier"));
                String value = cpl.getString(cpl.getColumnIndex("Value"));
                
                ResourceGroup rg = getCache().getResourceGroups().get(rgId);
                if (rg == null) {
                    Log.w("Invalid service level cached (rg does not exist)");
                    this.element.available = false;
                    
                } else {
                    Map<String, PrivacySetting> pls = getCache().getPrivacyLevels().get(rg);
                    PrivacySetting pl = pls.get(plId);
                    
                    this.element.privacyLevelValues.put(pl, value);                    
                }
                cpl.moveToNext();
            }
            cpl.close();
            
        }
        
        c.close();*/
    }
    
    
    @Override
    protected void storeElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {/*
        ContentValues cv = new ContentValues();
        cv.put("Name_Cache", this.element.name);
        cv.put("Description_Cache", this.element.description);
        
        wdb.update("ServiceFeature", cv, "App_Identifier = ? AND Level = ?", new String[] {
                this.element.getApp().getIdentifier(), String.valueOf(this.element.getLevel()) });*/
    }


    @Override
    protected void deleteElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
        // TODO Auto-generated method stub
        
    }
    
}
