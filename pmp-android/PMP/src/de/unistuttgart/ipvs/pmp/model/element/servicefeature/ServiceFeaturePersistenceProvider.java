/*
 * Copyright 2012 pmp-android development team
 * Project: PMP
 * Project-Site: http://code.google.com/p/pmp-android/
 * 
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.model.element.servicefeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.model.assertion.Assert;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelIntegrityError;
import de.unistuttgart.ipvs.pmp.model.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.app.App;
import de.unistuttgart.ipvs.pmp.model.element.missing.MissingPrivacySettingValue;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.ResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredResourceGroup;

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
        
        // load privacy setting values
        Cursor c = qb.query(rdb, new String[] { REQUIREDVALUE, PRIVACYSETTING_RESOURCEGROUP_PACKAGE,
                PRIVACYSETTING_IDENTIFIER }, SERVICEFEATURE_APP_PACKAGE + " = ? AND " + SERVICEFEATURE_IDENTIFIER
                + " = ?", new String[] { this.element.app.getIdentifier(), this.element.getLocalIdentifier() }, null,
                null, null);
        
        this.element.privacySettingValues = new HashMap<PrivacySetting, String>();
        this.element.missingPrivacySettings = new ArrayList<MissingPrivacySettingValue>();
        
        if (c.moveToFirst()) {
            do {
                String rgPackage = c.getString(c.getColumnIndex(PRIVACYSETTING_RESOURCEGROUP_PACKAGE));
                String psIdentifier = c.getString(c.getColumnIndex(PRIVACYSETTING_IDENTIFIER));
                String reqValue = c.getString(c.getColumnIndex(REQUIREDVALUE));
                
                ResourceGroup rg = getCache().getResourceGroups().get(rgPackage);
                if (rg == null) {
                    Log.w(this, "Unavailable service feature cached (RG not present).");
                    this.element.missingPrivacySettings.add(new MissingPrivacySettingValue(rgPackage, psIdentifier,
                            reqValue));
                    
                } else {
                    Map<String, PrivacySetting> pss = getCache().getPrivacySettings().get(rg);
                    PrivacySetting ps = pss.get(psIdentifier);
                    
                    if (ps == null) {
                        Log.w(this, "Unavailable service feature cached (PS not found in RG).");
                        this.element.missingPrivacySettings.add(new MissingPrivacySettingValue(rgPackage, psIdentifier,
                                reqValue));
                        
                    } else {
                        this.element.privacySettingValues.put(ps, reqValue);
                    }
                }
            } while (c.moveToNext());
        }
        c.close();
        
    }
    
    
    @Override
    protected void storeElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
        // this method should never be called
        throw new ModelIntegrityError(Assert.format(Assert.ILLEGAL_METHOD, "storeElementData", this));
    }
    
    
    @Override
    protected void deleteElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
        // delete service feature required privacy setting values references
        wdb.execSQL("DELETE FROM " + TBL_SFReqPSValue + " WHERE " + SERVICEFEATURE_APP_PACKAGE + " = ? AND "
                + SERVICEFEATURE_IDENTIFIER + " = ?", new String[] { this.element.getApp().getIdentifier(),
                this.element.getLocalIdentifier() });
        
        // delete service feature
        wdb.execSQL("DELETE FROM " + TBL_SERVICEFEATURE + " WHERE " + APP_PACKAGE + " = ? AND " + IDENTIFIER + " = ?",
                new String[] { this.element.getApp().getIdentifier(), this.element.getLocalIdentifier() });
        
    }
    
    
    /**
     * Creates the data <b>in the persistence</b> for the {@link ServiceFeature} specified with the parameters. Links
     * this {@link ServiceFeaturePersistenceProvider} to the newly created object.
     * 
     * @param app
     *            the app whom this service feature belongs to
     * @param identifier
     *            the identifier of this service feature
     * @return an {@link ServiceFeature} object that is linked to the newly created persistence data and this
     *         {@link ServiceFeaturePersistenceProvider}, or null, if the creation was not possible
     */
    public ServiceFeature createElementData(App app, String identifier,
            List<IAISRequiredResourceGroup> requiredResourceGroups) {
        // store in db
        SQLiteDatabase sqldb = getDoh().getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(APP_PACKAGE, app.getIdentifier());
            cv.put(IDENTIFIER, identifier);
            
            if (sqldb.insert(TBL_SERVICEFEATURE, null, cv) == -1) {
                Log.e(this, "Could not write service feature.");
                return null;
            }
            
            // refer to all the required resource groups
            for (IAISRequiredResourceGroup rrg : requiredResourceGroups) {
                for (IAISRequiredPrivacySetting ps : rrg.getRequiredPrivacySettings()) {
                    cv = new ContentValues();
                    cv.put(PRIVACYSETTING_RESOURCEGROUP_PACKAGE, rrg.getIdentifier());
                    cv.put(PRIVACYSETTING_IDENTIFIER, ps.getIdentifier());
                    cv.put(SERVICEFEATURE_APP_PACKAGE, app.getIdentifier());
                    cv.put(SERVICEFEATURE_IDENTIFIER, identifier);
                    cv.put(REQUIREDVALUE, ps.getValue());
                    if (sqldb.insert(TBL_SFReqPSValue, null, cv) == -1) {
                        Log.e(this,
                                "Could not write required privacy setting for service feature. Corruption of database very likely.");
                        return null;
                    }
                }
            }
        } finally {
            sqldb.close();
        }
        
        // create associated object
        ServiceFeature result = new ServiceFeature(app, identifier);
        this.element = result;
        result.setPersistenceProvider(this);
        
        return result;
    }
}
