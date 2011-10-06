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

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelConditions;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPreset;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;

/**
 * Implementation of the {@link IPreset} interface.
 * 
 * @author Jakob Jarosch
 */
public class PresetImpl implements IPreset {
    
    private String name;
    private PMPComponentType type;
    private String identifier;
    private String description;
    
    
    public PresetImpl(String name, String description, PMPComponentType type, String identifier) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.identifier = identifier;
    }
    
    
    @Override
    public String getName() {
        return this.name;
    }
    
    
    @Override
    public PMPComponentType getType() {
        return this.type;
    }
    
    
    @Override
    public String getIdentifier() {
        if (this.identifier != null && this.identifier.length() == 0) {
            return null;
        } else {
            return this.identifier;
        }
    }
    
    
    @Override
    public String getDescription() {
        return this.description;
    }
    
    
    @Override
    public IApp[] getAssignedApps() {
        SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper().getReadableDatabase();
        
        List<IApp> list = new ArrayList<IApp>();
        
        Cursor cursor = db
                .rawQuery(
                        "SELECT a.Identifier, a.Name_Cache, a.Description_Cache FROM App as a, Preset_Apps AS pa "
                                + "WHERE pa.App_Identifier = a.Identifier AND pa.Preset_Name = ? AND pa.Preset_Type = ? AND pa.Preset_Identifier = ?",
                        new String[] { this.name, this.type.toString(), this.identifier });
        
        cursor.moveToNext();
        
        while (!cursor.isAfterLast()) {
            String identifier = cursor.getString(cursor.getColumnIndex("Identifier"));
            String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
            String description = cursor.getString(cursor.getColumnIndex("Description_Cache"));
            
            list.add(new AppImpl(identifier, name, description));
            
            cursor.moveToNext();
        }
        cursor.close();
        
        Log.v("PresetImpl#getAssignedApps(): is returning " + list.size() + " datasets for Preset-Name " + getName());
        
        return list.toArray(new IApp[list.size()]);
    }
    
    
    @Override
    public boolean isAppAssigned(IApp app) {
        ModelConditions.assertNotNull("app", app);
        
        SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper().getReadableDatabase();
        
        Cursor cursor = db
                .rawQuery(
                        "SELECT Preset_Name FROM Preset_Apps WHERE Preset_Name = ? AND Preset_Type = ? AND Preset_Identifier = ? AND App_Identifier = ?",
                        new String[] { this.name, this.type.toString(), this.identifier, app.getIdentifier() });
        
        boolean result = (cursor.getCount() > 0);
        
        Log.v("PresetImpl#isAppAssigned(): The App " + app.getIdentifier() + " is " + (result ? "" : "not ")
                + " assigned to the Preset " + getName());
        
        cursor.close();
        return result;
    }
    
    
    @Override
    public void addApp(IApp app) {
        addApp(app, false);
    }
    
    
    @Override
    public void addApp(IApp app, boolean hidden) {
        ModelConditions.assertNotNull("app", app);
        
        Log.v("PresetImpl#addApp(): The App " + app.getIdentifier() + " is beeing added to the Preset " + getName());
        
        if (!isAppAssigned(app)) {
            SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper().getWritableDatabase();
            
            ContentValues cv = new ContentValues();
            cv.put("Preset_Name", this.name);
            cv.put("Preset_Type", this.type.toString());
            cv.put("Preset_Identifier", this.identifier);
            cv.put("App_Identifier", app.getIdentifier());
            
            db.insert("Preset_Apps", null, cv);
            
            if (!hidden) {
                app.verifyServiceLevel();
            }
        } else {
            Log.v("PresetImpl#addApp(): The App " + app.getIdentifier() + " was already assigned to the Preset "
                    + getName());
        }
    }
    
    
    @Override
    public void removeApp(IApp app) {
        removeApp(app, false);
    }
    
    
    @Override
    public void removeApp(IApp app, boolean hidden) {
        ModelConditions.assertNotNull("app", app);
        
        SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper().getWritableDatabase();
        
        db.delete("Preset_Apps",
                "Preset_Name = ? AND Preset_Type = ? AND Preset_Identifier = ? AND App_Identifier = ?", new String[] {
                        this.name, this.type.toString(), this.identifier, app.getIdentifier() });
        
        Log.v("PresetImpl#removeApp(): Removed the App " + app.getIdentifier() + " from the Preset " + getName());
        
        if (!hidden) {
            app.verifyServiceLevel();
        }
    }
    
    
    @Override
    public IPrivacyLevel[] getUsedPrivacyLevels() {
        SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper().getReadableDatabase();
        
        List<IPrivacyLevel> list = new ArrayList<IPrivacyLevel>();
        
        Cursor cursor = db
                .rawQuery(
                        "SELECT pl.ResourceGroup_Identifier, pl.Identifier, pl.Name_Cache, pl.Description_Cache, pp.Value FROM PrivacyLevel as pl, Preset_PrivacyLevels AS pp "
                                + "WHERE pp.Preset_Name = ? AND pp.Preset_Type = ? AND pp.Preset_Identifier = ? AND pp.ResourceGroup_Identifier = pl.ResourceGroup_Identifier AND "
                                + "pp.PrivacyLevel_Identifier = pl.Identifier",
                        new String[] { this.name, this.type.toString(), this.identifier });
        
        cursor.moveToNext();
        
        while (!cursor.isAfterLast()) {
            String resourceGroupIdentifier = cursor.getString(cursor.getColumnIndex("ResourceGroup_Identifier"));
            String identifier = cursor.getString(cursor.getColumnIndex("Identifier"));
            String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
            String description = cursor.getString(cursor.getColumnIndex("Description_Cache"));
            String value = cursor.getString(cursor.getColumnIndex("Value"));
            
            list.add(new PrivacyLevelImpl(resourceGroupIdentifier, identifier, name, description, value));
            
            cursor.moveToNext();
        }
        
        Log.v("PresetImpl#getUsedPrivacyLevels(): is returning " + list.size() + " datasets for Preset-Name "
                + getName());
        
        cursor.close();
        return list.toArray(new IPrivacyLevel[list.size()]);
    }
    
    
    @Override
    public void setPrivacyLevel(IPrivacyLevel privacyLevel) {
        setPrivacyLevel(privacyLevel, false);
    }
    
    
    @Override
    public void setPrivacyLevel(IPrivacyLevel privacyLevel, boolean hidden) {
        ModelConditions.assertNotNull("privacyLevel", privacyLevel);
        ModelConditions.assertNotNull("privacyLevel.getValue()", privacyLevel.getValue());
        
        SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper().getWritableDatabase();
        
        Log.v("PresetImpl#setPrivacyLevel(): First remove the " + privacyLevel.getIdentifier() + " from the Preset "
                + getName());
        
        /*
         * Remove the privacy level from preset (either it is inside or not),
         * will prevent that the privacy level is doubled inside.
         */
        removePrivacyLevel(privacyLevel, true);
        
        /* add the new (or changed) privacy level */
        ContentValues cv = new ContentValues();
        cv.put("Preset_Name", this.name);
        cv.put("Preset_Type", this.type.toString());
        cv.put("Preset_Identifier", this.identifier);
        cv.put("ResourceGroup_Identifier", privacyLevel.getResourceGroup().getIdentifier());
        cv.put("PrivacyLevel_Identifier", privacyLevel.getIdentifier());
        cv.put("Value", privacyLevel.getValue());
        
        db.insert("Preset_PrivacyLevels", null, cv);
        
        Log.v("PresetImpl#setPrivacyLevel(): Added the PrivacyLevel " + privacyLevel.getIdentifier()
                + " to the Preset " + getName());
        
        if (!hidden) {
            for (IApp app : getAssignedApps()) {
                app.verifyServiceLevel();
            }
        }
    }
    
    
    @Override
    public void removePrivacyLevel(IPrivacyLevel privacyLevel) {
        removePrivacyLevel(privacyLevel, false);
    }
    
    
    @Override
    public void removePrivacyLevel(IPrivacyLevel privacyLevel, boolean hidden) {
        ModelConditions.assertNotNull("privacyLevel", privacyLevel);
        ModelConditions.assertNotNull("resourceGroup of privacylevel", privacyLevel.getResourceGroup());
        
        SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper().getWritableDatabase();
        
        db.delete(
                "Preset_PrivacyLevels",
                "Preset_Name = ? AND Preset_Type = ? AND Preset_Identifier = ? AND ResourceGroup_Identifier = ? AND PrivacyLevel_Identifier = ?",
                new String[] { this.name, this.type.toString(), this.identifier,
                        privacyLevel.getResourceGroup().getIdentifier(), privacyLevel.getIdentifier() });
        
        Log.v("PresetImpl#removePrivacyLevel(): Removed the PrivacyLevel " + privacyLevel.getIdentifier()
                + " from the Preset " + getName());
        
        if (!hidden) {
            for (IApp app : getAssignedApps()) {
                app.verifyServiceLevel();
            }
        }
    }
}
