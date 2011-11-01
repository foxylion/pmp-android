package de.unistuttgart.ipvs.pmp.model2.element.resourcegroup;

import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.unistuttgart.ipvs.pmp.model2.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model2.element.privacylevel.PrivacyLevel;

/**
 * The persistence provider for {@link ResourceGroup}s.
 * 
 * @author Tobias Kuhn
 * 
 */
public class ResourceGroupPersistenceProvider extends ElementPersistenceProvider<ResourceGroup> {
    
    public ResourceGroupPersistenceProvider(ResourceGroup element) {
        super(element);
    }
    
    
    @Override
    public void loadElementData(SQLiteDatabase rdb) {
        Cursor c = rdb.rawQuery("SELECT Name_Cache, Description_Cache FROM ResourceGroup WHERE Identifier = ? LIMIT 1",
                new String[] { this.element.getIdentifier() });
        
        if (!c.moveToFirst()) {
            throw new IllegalAccessError("The Identifier " + this.element.getIdentifier()
                    + " was not found in the database.");
        } else {
            this.element.name = c.getString(c.getColumnIndex("Name_Cache"));
            this.element.description = c.getString(c.getColumnIndex("Description_Cache"));
            
            this.element.privacyLevels = new HashMap<String, PrivacyLevel>();
            for (PrivacyLevel pl : getCache().getPrivacyLevels().get(this.element).values()) {
                this.element.privacyLevels.put(pl.getIdentifier(), pl);
            }
        }
        
        c.close();
    }
    
    
    @Override
    public void storeElementData(SQLiteDatabase wdb) {
        ContentValues cv = new ContentValues();
        cv.put("Name_Cache", this.element.name);
        cv.put("Description_Cache", this.element.description);
        
        wdb.update("ResourceGroup", cv, "Identifier = ?", new String[] { this.element.getIdentifier() });
    }
    
}
