package de.unistuttgart.ipvs.pmp.model2.element.app;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.unistuttgart.ipvs.pmp.model2.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model2.element.preset.Preset;

/**
 * The persistence provider for {@link App}s.
 * 
 * @author Tobias Kuhn
 * 
 */
public class AppPersistenceProvider extends ElementPersistenceProvider<App> {
    
    public AppPersistenceProvider(App element) {
        super(element);
    }
    
    
    @Override
    protected void loadElementData(SQLiteDatabase rdb) {
        Cursor c = rdb.rawQuery(
                "SELECT Name_Cache, Description_Cache, ServiceLevel_Active FROM App WHERE Identifier = ? LIMIT 1",
                new String[] { this.element.getIdentifier() });
        
        if (!c.moveToFirst()) {
            throw new IllegalAccessError("The Identifier " + this.element.getIdentifier()
                    + " was not found in the database.");
        } else {
            this.element.name = c.getString(c.getColumnIndex("Name_Cache"));
            this.element.description = c.getString(c.getColumnIndex("Description_Cache"));
            this.element.activeServiceLevel = c.getInt(c.getColumnIndex("ServiceLevel_Active"));
            
            this.element.serviceLevels = getCache().getServiceLevels().get(this.element);
            
            this.element.assignedPresets = new ArrayList<Preset>();
            for (Preset p : getCache().getPresets()) {
                if (p.isAppAssigned(this.element)) {
                    this.element.assignedPresets.add(p);
                }
            }
        }
        
        c.close();
    }
    
    
    @Override
    protected void storeElementData(SQLiteDatabase wdb) {
        ContentValues cv = new ContentValues();
        cv.put("Name_Cache", this.element.name);
        cv.put("Description_Cache", this.element.description);
        cv.put("ServiceLevel_Active", this.element.activeServiceLevel);
        
        wdb.update("App", cv, "Identifier = ?", new String[] { this.element.getIdentifier() });
    }
    
    
    @Override
    protected void deleteElementData(SQLiteDatabase wdb) {
        // TODO Auto-generated method stub
        
    }
    
    
    /**
     * Creates the data <b>in the persistence</b> for the {@link App} specified with the parameters.
     * 
     * @return an {@link App} object that is linked to the newly created persistence data.
     */
    protected static App createElementData(String identifier) {
        
        // TODO store in db
        
        // create associated object
        App result = new App(identifier);
        result.setPersistenceProvider(new AppPersistenceProvider(result));
        
        return result;
        
    }
    
}
