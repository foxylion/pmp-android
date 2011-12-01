package de.unistuttgart.ipvs.pmp.model.element.resourcegroup;

import java.util.HashMap;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import de.unistuttgart.ipvs.pmp.model.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySetting;

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
    protected void loadElementData(SQLiteDatabase rdb, SQLiteQueryBuilder qb) {
        
        // TODO set RGIS via XML file somewhere
        
        this.element.privacySettings = new HashMap<String, PrivacySetting>();
        for (PrivacySetting pl : getCache().getPrivacyLevels().get(this.element).values()) {
            this.element.privacySettings.put(pl.getIdentifier(), pl);
        }
    }
    
    
    @Override
    protected void storeElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
        // this method should never be called
        throw new UnsupportedOperationException();
    }
    
    
    @Override
    protected void deleteElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
        // delete privacy settings
        for (PrivacySetting ps : this.element.privacySettings.values()) {
            ps.delete();
        }
        
        // delete resource group
        wdb.rawQuery("DELETE FROM " + TBL_RESOURCEGROUP + " WHERE " + PACKAGE + " = ?",
                new String[] { this.element.getIdentifier() });
        
    }
    
    
    /**
     * Creates the data <b>in the persistence</b> for the {@link ResourceGroup} specified with the parameters.
     * 
     * @param rgPackage
     *            package of the resource group.
     * @return a {@link ResourceGroup} object that is linked to the newly created persistence data and this
     *         {@link ResourceGroupPersistenceProvider}, or null, if the creation was not possible
     */
    public ResourceGroup createElementData(String rgPackage) {
        
        // TODO store in db
        
        // create associated object
        ResourceGroup result = new ResourceGroup(rgPackage);
        result.setPersistenceProvider(this);
        
        return result;
        
    }
    
}
