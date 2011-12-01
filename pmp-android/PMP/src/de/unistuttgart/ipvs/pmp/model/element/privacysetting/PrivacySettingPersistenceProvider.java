package de.unistuttgart.ipvs.pmp.model.element.privacysetting;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import de.unistuttgart.ipvs.pmp.model.element.ElementPersistenceProvider;

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
    
}
