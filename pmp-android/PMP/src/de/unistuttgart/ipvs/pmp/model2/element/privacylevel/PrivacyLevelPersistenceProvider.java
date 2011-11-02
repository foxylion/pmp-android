package de.unistuttgart.ipvs.pmp.model2.element.privacylevel;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.unistuttgart.ipvs.pmp.model2.element.ElementPersistenceProvider;

/**
 * The persistence provider for {@link PrivacyLevel}s.
 * 
 * @author Tobias Kuhn
 * 
 */
public class PrivacyLevelPersistenceProvider extends ElementPersistenceProvider<PrivacyLevel> {
    
    public PrivacyLevelPersistenceProvider(PrivacyLevel element) {
        super(element);
    }
    
    
    @Override
    protected void loadElementData(SQLiteDatabase rdb) {
        Cursor c = rdb
                .rawQuery(
                        "SELECT Name_Cache, Description_Cache FROM PrivacyLevel WHERE Identifier = ? AND ResourceGroup_Identifier = ? LIMIT 1",
                        new String[] { this.element.getIdentifier(), this.element.getResourceGroup().getIdentifier() });
        
        if (!c.moveToFirst()) {
            throw new IllegalAccessError("The Identifier " + this.element.getIdentifier() + "of "
                    + this.element.getResourceGroup().getIdentifier() + " was not found in the database.");
        } else {
            this.element.name = c.getString(c.getColumnIndex("Name_Cache"));
            this.element.description = c.getString(c.getColumnIndex("Description_Cache"));
            
        }
        
        c.close();
    }
    
    
    @Override
    protected void storeElementData(SQLiteDatabase wdb) {
        ContentValues cv = new ContentValues();
        cv.put("Name_Cache", this.element.name);
        cv.put("Description_Cache", this.element.description);
        
        wdb.update("PrivacyLevel", cv, "Identifier = ? AND ResourceGroup_Identifier",
                new String[] { this.element.getIdentifier(), this.element.getResourceGroup().getIdentifier() });
    }


    @Override
    protected void deleteElementData(SQLiteDatabase wdb) {
        // TODO Auto-generated method stub
        
    }
    
}
