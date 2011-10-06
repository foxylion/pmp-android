/*
 * Copyright 2011 pmp-android development team
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
package de.unistuttgart.ipvs.pmp.model.implementations;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;

/**
 * Implementation of the {@link IServiceLevel} interface.
 * 
 * @author Jakob Jarosch
 */
public class ServiceLevelImpl implements IServiceLevel {
    
    private String appIdentifier;
    private int level;
    private String name;
    private String description;
    
    
    public ServiceLevelImpl(String appIdentifier, int level, String name, String description) {
        this.appIdentifier = appIdentifier;
        this.level = level;
        this.name = name;
        this.description = description;
    }
    
    
    public String getAppIdentifier() {
        return this.appIdentifier;
    }
    
    
    @Override
    public int getLevel() {
        return this.level;
    }
    
    
    @Override
    public String getName() {
        return this.name;
    }
    
    
    @Override
    public String getDescription() {
        return this.description;
    }
    
    
    @Override
    public IPrivacyLevel[] getPrivacyLevels() {
        List<IPrivacyLevel> list = new ArrayList<IPrivacyLevel>();
        
        SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper().getReadableDatabase();
        
        Cursor cursor = db.rawQuery("SELECT pl.ResourceGroup_Identifier, " + "pl.Identifier, " + "pl.Name_Cache, "
                + "pl.Description_Cache, " + "slpl.Value "
                + "FROM ServiceLevel_PrivacyLevels AS slpl, PrivacyLevel AS pl "
                + "WHERE slpl.ResourceGroup_Identifier = pl.ResourceGroup_Identifier "
                + "AND slpl.PrivacyLevel_Identifier = pl.Identifier " + "AND slpl.App_Identifier = ? "
                + "AND slpl.ServiceLevel_Level = " + this.level, new String[] { this.appIdentifier });
        
        cursor.moveToNext();
        
        while (!cursor.isAfterLast()) {
            String resourceGroup_Identifier = cursor.getString(cursor.getColumnIndex("ResourceGroup_Identifier"));
            String identifier = cursor.getString(cursor.getColumnIndex("Identifier"));
            String value = cursor.getString(cursor.getColumnIndex("Value"));
            String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
            String description = cursor.getString(cursor.getColumnIndex("Description_Cache"));
            
            list.add(new PrivacyLevelImpl(resourceGroup_Identifier, identifier, name, description, value));
            
            cursor.moveToNext();
        }
        
        Log.v("ServiceLevelImpl#getPrivacyLevels(): is returning " + list.size() + " datasets for identifier:level "
                + getAppIdentifier() + ":" + getLevel());
        
        cursor.close();
        return list.toArray(new IPrivacyLevel[list.size()]);
    }
    
    
    @Override
    public boolean isAvailable() {
        SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper().getReadableDatabase();
        
        Cursor cursor = db.rawQuery("SELECT ResourceGroup_Identifier, PrivacyLevel_Identifier "
                + "FROM ServiceLevel_PrivacyLevels " + "WHERE App_Identifier = ? AND ServiceLevel_Level = "
                + this.level, new String[] { this.appIdentifier });
        
        cursor.moveToNext();
        
        while (!cursor.isAfterLast()) {
            String resourceGroupIdentifier = cursor.getString(cursor.getColumnIndex("ResourceGroup_Identifier"));
            String privacyLevelIdentifier = cursor.getString(cursor.getColumnIndex("PrivacyLevel_Identifier"));
            
            /* check if there is any IResourceGroup with that identifier. */
            if (ModelSingleton.getInstance().getModel().getResourceGroup(resourceGroupIdentifier) == null) {
                cursor.close();
                return false;
            } else {
                /*
                 * check if there is any IPrivacyLevel in this IResourceGroup
                 * with that identifier
                 */
                if (ModelSingleton.getInstance().getModel().getResourceGroup(resourceGroupIdentifier)
                        .getPrivacyLevel(privacyLevelIdentifier) == null) {
                    cursor.close();
                    return false;
                }
            }
            
            cursor.moveToNext();
        }
        
        cursor.close();
        return true;
    }
}
