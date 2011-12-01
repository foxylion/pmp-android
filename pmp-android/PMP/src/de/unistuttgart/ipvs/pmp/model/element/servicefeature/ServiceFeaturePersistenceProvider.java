package de.unistuttgart.ipvs.pmp.model.element.servicefeature;

import java.util.HashMap;
import java.util.Map;

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
    protected void loadElementData(SQLiteDatabase rdb, SQLiteQueryBuilder qb) {
        qb.setTables(TBL_SFReqPSValue);
        
        // load privacy level values
        Cursor c = qb.query(rdb, new String[] { REQUIREDVALUE, PRIVACYSETTING_RESOURCEGROUP_PACKAGE,
                PRIVACYSETTING_IDENTIFIER }, SERVICEFEATURE_APP_PACKAGE + " = ? AND " + SERVICEFEATURE_IDENTIFIER
                + " = ?", new String[] { this.element.app.getIdentifier(), this.element.getLocalIdentifier() }, null,
                null, null);
        
        this.element.privacyLevelValues = new HashMap<PrivacySetting, String>();
        this.element.containsUnknownPrivacySettings = false;
        
        while (!c.isAfterLast()) {
            String rgPackage = c.getString(c.getColumnIndex(PRIVACYSETTING_RESOURCEGROUP_PACKAGE));
            String psIdentifier = c.getString(c.getColumnIndex(PRIVACYSETTING_IDENTIFIER));
            String reqValue = c.getString(c.getColumnIndex(REQUIREDVALUE));
            
            ResourceGroup rg = getCache().getResourceGroups().get(rgPackage);
            if (rg == null) {
                Log.w("Unavailable service feature cached (RG not present).");
                this.element.containsUnknownPrivacySettings = true;
                
            } else {
                Map<String, PrivacySetting> pss = getCache().getPrivacyLevels().get(rg);
                PrivacySetting ps = pss.get(psIdentifier);
                
                if (ps == null) {
                    Log.w("Unavailable service feature cached (PS not found in RG).");
                    this.element.containsUnknownPrivacySettings = true;
                } else {
                    this.element.privacyLevelValues.put(ps, reqValue);
                }
            }
            c.moveToNext();
        }
        c.close();
        
    }
    
    
    @Override
    protected void storeElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
        // this method should never be called
        throw new UnsupportedOperationException();
    }
    
    
    @Override
    protected void deleteElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
        // delete service feature required privacy setting values references
        wdb.rawQuery("DELETE FROM " + TBL_SFReqPSValue + " WHERE " + SERVICEFEATURE_APP_PACKAGE + " = ? AND "
                + SERVICEFEATURE_IDENTIFIER + " = ?", new String[] { this.element.getApp().getIdentifier(),
                this.element.getLocalIdentifier() });
        
        // delete service feature
        wdb.rawQuery("DELETE FROM " + TBL_SERVICEFEATURE + " WHERE " + SERVICEFEATURE_APP_PACKAGE + " = ? AND "
                + SERVICEFEATURE_IDENTIFIER + " = ?", new String[] { this.element.getApp().getIdentifier(),
                this.element.getLocalIdentifier() });
        
    }
    
}
