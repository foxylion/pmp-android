package de.unistuttgart.ipvs.pmp.model.element.privacysetting;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.model.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.ResourceGroup;

/**
 * The persistence provider for {@link PrivacySetting}s.
 * 
 * @author Tobias Kuhn
 * 
 */
public class PrivacySettingPersistenceProvider extends ElementPersistenceProvider<PrivacySetting> {
    
    public PrivacySettingPersistenceProvider(PrivacySetting element) {
        super(element);
    }
    
    
    @Override
    protected void loadElementData(SQLiteDatabase rdb, SQLiteQueryBuilder qb) {
        // TODO set the pointer to element.link
    }
    
    
    @Override
    protected void storeElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
        // this method should never be called
        throw new UnsupportedOperationException();
    }
    
    
    @Override
    protected void deleteElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
        // delete service feature required privacy setting values references
        wdb.rawQuery("DELETE FROM " + TBL_SFReqPSValue + " WHERE " + PRIVACYSETTING_RESOURCEGROUP_PACKAGE + " = ? AND "
                + PRIVACYSETTING_IDENTIFIER + " = ?", new String[] { this.element.getResourceGroup().getIdentifier(),
                this.element.getLocalIdentifier() });
        
        // delete preset granted privacy setting values references
        wdb.rawQuery("DELETE FROM " + TBL_GrantPSValue + " WHERE " + PRIVACYSETTING_RESOURCEGROUP_PACKAGE + " = ? AND "
                + PRIVACYSETTING_IDENTIFIER + " = ?", new String[] { this.element.getResourceGroup().getIdentifier(),
                this.element.getLocalIdentifier() });
        
        // delete privacy settings
        wdb.rawQuery("DELETE FROM " + TBL_PRIVACYSETTING + " WHERE " + PRIVACYSETTING_RESOURCEGROUP_PACKAGE
                + " = ? AND " + PRIVACYSETTING_IDENTIFIER + " = ?", new String[] {
                this.element.getResourceGroup().getIdentifier(), this.element.getLocalIdentifier() });
        
    }
    
    
    /**
     * Creates the data <b>in the persistence</b> for the {@link PrivacySetting} specified with the parameters. Links
     * this {@link PrivacySettingPersistenceProvider} to the newly created object.
     * 
     * @param rg
     *            the rg whom this privacy setting belongs to
     * @param identifier
     *            the identifier of this privacy setting
     * @return an {@link PrivacySetting} object that is linked to the newly created persistence data and this
     *         {@link PrivacySettingPersistenceProvider}, or null, if the creation was not possible
     */
    public PrivacySetting createElementData(ResourceGroup rg, String identifier) {
        // store in db
        ContentValues cv = new ContentValues();
        cv.put(RESOURCEGROUP_PACKAGE, rg.getIdentifier());
        cv.put(IDENTIFIER, identifier);
        if (getDoh().getWritableDatabase().insert(TBL_PRIVACYSETTING, null, cv) == -1) {
            Log.e("Could not write service feature.");
            return null;
        }
        
        // create associated object
        PrivacySetting result = new PrivacySetting(rg, identifier);
        this.element = result;
        result.setPersistenceProvider(this);
        
        return result;
    }
}
