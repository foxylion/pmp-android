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
package de.unistuttgart.ipvs.pmp.model.element.resourcegroup;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import de.unistuttgart.ipvs.pmp.model.assertion.Assert;
import de.unistuttgart.ipvs.pmp.model.assertion.ModelIntegrityError;
import de.unistuttgart.ipvs.pmp.model.element.ElementPersistenceProvider;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.PrivacySetting;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidPluginException;
import de.unistuttgart.ipvs.pmp.model.plugin.PluginProvider;

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
        
        // set RGIS via XML file        
        try {
            this.element.rgis = PluginProvider.getInstance().getRGIS(this.element.getIdentifier());
            this.element.link = PluginProvider.getInstance().getResourceGroupObject(this.element.getIdentifier());
            this.element.icon = PluginProvider.getInstance().getIcon(this.element.getIdentifier());
            this.element.revision = PluginProvider.getInstance().getRevision(this.element.getIdentifier());
        } catch (InvalidPluginException ipe) {
            this.element.deactivate(ipe);
        }
        
        this.element.privacySettings = getCache().getPrivacySettings().get(this.element);
    }
    
    
    @Override
    protected void storeElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
        // this method should never be called
        throw new ModelIntegrityError(Assert.format(Assert.ILLEGAL_METHOD, "storeElementData", this));
    }
    
    
    @Override
    protected void deleteElementData(SQLiteDatabase wdb, SQLiteQueryBuilder qb) {
        // delete resource group
        wdb.execSQL("DELETE FROM " + TBL_RESOURCEGROUP + " WHERE " + PACKAGE + " = ?",
                new String[] { this.element.getIdentifier() });
        
        // delete privacy settings
        for (PrivacySetting ps : this.element.privacySettings.values()) {
            ps.delete();
        }
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
        // store in db
        SQLiteDatabase sqldb = getDoh().getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(PACKAGE, rgPackage);
            if (sqldb.insert(TBL_RESOURCEGROUP, null, cv) == -1) {
                return null;
            }
        } finally {
            sqldb.close();
        }
        
        // create associated object
        ResourceGroup result = new ResourceGroup(rgPackage);
        this.element = result;
        result.setPersistenceProvider(this);
        
        return result;
    }
    
}
