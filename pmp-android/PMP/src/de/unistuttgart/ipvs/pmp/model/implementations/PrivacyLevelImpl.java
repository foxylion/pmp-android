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

import java.util.Locale;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelConditions;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;
import de.unistuttgart.ipvs.pmp.service.utils.ResourceGroupServiceConnector;

/**
 * Implementation of the {@link PrivacyLevelImpl} interface.
 * 
 * @author Jakob Jarosch
 */
public class PrivacyLevelImpl implements IPrivacyLevel {
    
    private String resourceGroupIdentifier;
    private String identifier;
    private String name;
    private String description;
    private String value;
    
    
    public PrivacyLevelImpl(String resourceGroupIdentifier, String identifier, String name, String description) {
        this(resourceGroupIdentifier, identifier, name, description, null);
    }
    
    
    public PrivacyLevelImpl(String resourceGroupIdentifier, String identifier, String name, String description,
            String value) {
        this.resourceGroupIdentifier = resourceGroupIdentifier;
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.value = value;
    }
    
    
    private String getRessourceGroupIdentifier() {
        return this.resourceGroupIdentifier;
    }
    
    
    @Override
    public String getIdentifier() {
        return this.identifier;
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
    public String getValue() {
        return this.value;
    }
    
    
    @Override
    public IResourceGroup getResourceGroup() {
        SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper().getReadableDatabase();
        
        ResourceGroupImpl returnValue = null;
        
        Cursor cursor = db.rawQuery(
                "SELECT Name_Cache, Description_Cache FROM ResourceGroup WHERE Identifier = ? LIMIT 1",
                new String[] { this.resourceGroupIdentifier });
        
        if (cursor.getCount() == 1) {
            cursor.moveToNext();
            String name = cursor.getString(cursor.getColumnIndex("Name_Cache"));
            String description = cursor.getString(cursor.getColumnIndex("Description_Cache"));
            
            returnValue = new ResourceGroupImpl(this.resourceGroupIdentifier, name, description);
        } else {
            Log.e("PrivacyLevelImpl#getResourceGroup(): PrivacyLevel " + this.identifier
                    + " has no parent ResourceGroup.");
        }
        
        cursor.close();
        return returnValue;
    }
    
    
    @Override
    public String getHumanReadableValue(String value) throws RemoteException {
        ModelConditions.assertNotNull("value", value);
        
        ResourceGroupServiceConnector rgsc = new ResourceGroupServiceConnector(PMPApplication.getContext(),
                PMPApplication.getSignee(), getRessourceGroupIdentifier());
        
        rgsc.bind(true);
        
        if (!rgsc.isBound() || rgsc.getPMPService() == null) {
            Log.e("PrivacyLevelImpl#getResourceGroup(): Binding of ResourceGroupService "
                    + getRessourceGroupIdentifier() + " failed, can't do satisfies");
            rgsc.unbind();
            RemoteException re = new RemoteException();
            re.initCause(new Throwable("Binding of ResourceGroupService " + getRessourceGroupIdentifier()
                    + " failed, can't do satisfies"));
            throw re;
        } else {
            String result = rgsc.getPMPService().getHumanReadablePrivacyLevelValue(Locale.getDefault().getLanguage(),
                    this.identifier, value);
            rgsc.unbind();
            return result;
        }
    }
    
    
    @Override
    public boolean permits(String reference, String value) throws RemoteException {
        ModelConditions.assertNotNull("reference", reference);
        ModelConditions.assertNotNull("value", value);
        
        ResourceGroupServiceConnector rgsc = new ResourceGroupServiceConnector(PMPApplication.getContext(),
                PMPApplication.getSignee(), getRessourceGroupIdentifier());
        
        rgsc.bind(true);
        
        if (!rgsc.isBound() || rgsc.getPMPService() == null) {
            Log.e("PrivacyLevelImpl#getResourceGroup(): Binding of ResourceGroupService "
                    + getRessourceGroupIdentifier() + " failed, can't do satisfies");
            rgsc.unbind();
            RemoteException re = new RemoteException();
            re.initCause(new Throwable("Binding of ResourceGroupService " + getRessourceGroupIdentifier()
                    + " failed, can't do satisfies"));
            throw re;
        } else {
            boolean result = rgsc.getPMPService().permitsPrivacyLevel(this.identifier, reference, value);
            rgsc.unbind();
            return result;
        }
    }
    
}
