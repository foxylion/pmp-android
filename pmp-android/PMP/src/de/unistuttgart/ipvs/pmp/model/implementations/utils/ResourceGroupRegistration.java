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
package de.unistuttgart.ipvs.pmp.model.implementations.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelConditions;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.resource.ResourceGroup;
import de.unistuttgart.ipvs.pmp.service.RegistrationState;
import de.unistuttgart.ipvs.pmp.service.resource.IResourceGroupServicePMP;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;
import de.unistuttgart.ipvs.pmp.service.utils.ResourceGroupServiceConnector;

public class ResourceGroupRegistration {
    
    private String identifier;
    
    private byte[] publicKey;
    
    private final ResourceGroupServiceConnector rgsc;
    
    
    /**
     * Executes an asynchronous ResourceGroup registration.
     * 
     * @param identifier
     *            Identifier of the ResourceGroup
     * @param publicKey
     *            Public key of the ResourceGroup
     */
    public ResourceGroupRegistration(String identifier, byte[] publicKey) {
        ModelConditions.assertStringNotNullOrEmpty("identifier", identifier);
        ModelConditions.assertPublicKeyNotNullOrEmpty(publicKey);
        
        this.identifier = identifier;
        this.publicKey = publicKey.clone();
        this.rgsc = new ResourceGroupServiceConnector(PMPApplication.getContext(), PMPApplication.getSignee(),
                identifier);
        
        Log.v("Registration (" + identifier + "): Trying to connect to the ResourceGroupService");
        
        connect();
    }
    
    
    /**
     * Connect to the Service.
     */
    private void connect() {
        if (!this.rgsc.bind(true)) {
            Log.e("Registration ("
                    + this.identifier
                    + "): FAILED - Connection to the ResourceGroupService failed. More details can be found in the log.");
        } else {
            Log.d("Registration (" + this.identifier + "): Successfully bound.");
            
            if (this.rgsc.getPMPService() == null) {
                Log.e("Registration (" + this.identifier
                        + "): FAILED - Binding to the ResourceGroupService failed, only got a NULL IBinder.");
            } else {
                Log.d("Registration (" + this.identifier + "): Successfully connected, got IResourceGroupServicePMP.");
                loadResourceGroupData(this.rgsc.getPMPService());
            }
        }
    }
    
    
    /**
     * Load the informations from the ResourceGroup-Service.
     * 
     * @param rgService
     *            service which provides the {@link ResourceGroup} informations
     */
    private void loadResourceGroupData(IResourceGroupServicePMP rgService) {
        Log.v("Registration (" + this.identifier + "): loading resource group data...");
        
        try {
            String name = rgService.getName(Locale.getDefault().getLanguage());
            String description = rgService.getDescription(Locale.getDefault().getLanguage());
            
            ResourceGroupDS rgDS = new ResourceGroupDS(name, description);
            
            @SuppressWarnings("unchecked")
            List<String> privacyLevels = rgService.getPrivacyLevelIdentifiers();
            
            for (String identifier : privacyLevels) {
                name = rgService.getPrivacyLevelName(Locale.getDefault().getLanguage(), identifier);
                description = rgService.getPrivacyLevelDescription(Locale.getDefault().getLanguage(), identifier);
                
                rgDS.getPrivacyLevels().add(new PrivacyLevelDS(identifier, name, description));
            }
            
            checkResourceGroup(rgDS);
        } catch (RemoteException e) {
            Log.e("Registration (" + this.identifier
                    + "): FAILED - while loading resource group data a RemoteException was produced.", e);
            informAboutRegistration(false,
                    "IResourceGroupServicePMP produced a RemoteException, during information collection.");
        }
    }
    
    
    /**
     * Check if the informations provided by the {@link ResourceGroup} and if the {@link ResourceGroup} is not already
     * registered.
     */
    private void checkResourceGroup(ResourceGroupDS rgDS) {
        if (!rgDS.validate()) {
            Log.e("Registration (" + this.identifier
                    + "): FAILED - ResourceGroup informations are missing, details above.");
            informAboutRegistration(false, "ResourceGroup informations are missing, details in LogCat.");
            return;
        }
        
        if (ModelSingleton.getInstance().getModel().getResourceGroup(this.identifier) != null) {
            Log.e("Registration (" + this.identifier
                    + "): FAILED - ResourceGroup already registred in PMP-Database, maybe lost your key?.");
            informAboutRegistration(false, "ResourceGroup already registred, maybe lost your key?");
            return;
        }
        
        registerResourceGroupInDatabase(rgDS);
    }
    
    
    /**
     * Write the {@link ResourceGroupDS} to the Database.
     */
    private void registerResourceGroupInDatabase(ResourceGroupDS rgDS) {
        Log.v("Registration (" + this.identifier + "): Adding the ResourceGroup to the PMP-Database");
        
        SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper().getWritableDatabase();
        
        ContentValues cv = new ContentValues();
        cv.put("Identifier", this.identifier);
        cv.put("Name_Cache", rgDS.getName());
        cv.put("Description_Cache", rgDS.getDescription());
        
        db.insert("ResourceGroup", null, cv);
        
        for (PrivacyLevelDS pl : rgDS.getPrivacyLevels()) {
            cv = new ContentValues();
            cv.put("ResourceGroup_Identifier", this.identifier);
            cv.put("Identifier", pl.getIdentifier());
            cv.put("Name_Cache", pl.getName());
            cv.put("Description_Cache", pl.getDescription());
            
            db.insert("PrivacyLevel", null, cv);
        }
        
        publishPublicKey();
    }
    
    
    /**
     * Publish the public key of the to the {@link PMPSignee} of {@link PMPApplication}.
     */
    private void publishPublicKey() {
        Log.v("Loading ResourceGroups public key into PMPSignee");
        
        PMPApplication.getSignee().setRemotePublicKey(PMPComponentType.RESOURCE_GROUP, this.identifier, this.publicKey);
        
        informAboutRegistration(true, null);
    }
    
    
    /**
     * Inform the ResourceGroupService about the registration state.
     * 
     * @param state
     *            true means successful, false means unsuccessful
     * @param message
     *            a message with optional information provided.
     */
    private void informAboutRegistration(final boolean state, final String message) {
        Log.d("Registration (" + this.identifier + "): Informing ResourceGroup about registrationState (" + state + ")");
        
        if (!this.rgsc.isBound()) {
            if (!this.rgsc.bind(true)) {
                Log.e("Registration ("
                        + this.identifier
                        + "): FAILED - Connection to the ResourceGroupService failed. More details can be found in the log.");
            } else {
                Log.d("Registration (" + this.identifier + "): Successfully bound.");
                
                if (this.rgsc.getPMPService() == null) {
                    Log.e("Registration (" + this.identifier
                            + "): FAILED - Binding to the ResourceGroupService failed, only got a NULL IBinder.");
                } else {
                    Log.d("Registration (" + this.identifier
                            + "): Successfully connected got IResourceGroupServicePMP.");
                    informAboutRegistration(state, message);
                }
            }
        } else {
            try {
                Log.v("Registration (" + this.identifier + "): Calling setRegistrationState");
                IResourceGroupServicePMP rgService = this.rgsc.getPMPService();
                rgService.setRegistrationState(new RegistrationState(state, message));
                this.rgsc.unbind();
                
                Log.d("Registration (" + this.identifier + "): Registration " + (state ? "succeed" : "FAILED"));
            } catch (RemoteException e) {
                Log.e("Registration (" + this.identifier + "): " + (state ? "succeed" : "FAILED")
                        + ", but setRegistrationState() produced an RemoteException.", e);
            }
        }
    }
}

class ResourceGroupDS {
    
    private String name;
    private String description;
    
    private List<PrivacyLevelDS> privacyLevels = new ArrayList<PrivacyLevelDS>();
    
    
    public ResourceGroupDS(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    
    public String getName() {
        return this.name;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public String getDescription() {
        return this.description;
    }
    
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    
    public List<PrivacyLevelDS> getPrivacyLevels() {
        return this.privacyLevels;
    }
    
    
    public boolean validate() {
        boolean state = true;
        
        if (this.name == null || this.description == null) {
            Log.e("ResourceGroup validation failed: missing name or description (Locale: "
                    + Locale.getDefault().getLanguage() + ")");
            state = false;
        }
        
        for (PrivacyLevelDS plDS : this.privacyLevels) {
            if (!plDS.validate()) {
                state = false;
            }
        }
        return state;
    }
}

class PrivacyLevelDS {
    
    private String identifier;
    private String name;
    private String description;
    
    
    public PrivacyLevelDS(String identifier, String name, String description) {
        this.identifier = identifier;
        this.name = name;
        this.description = description;
    }
    
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    
    public String getName() {
        return this.name;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public String getDescription() {
        return this.description;
    }
    
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    
    public boolean validate() {
        if (this.identifier == null || this.name == null || this.description == null) {
            Log.e("PrivacyLevel validation failed: missing identifier, name or description (PrivacyLevel: "
                    + this.identifier + ", Locale: " + Locale.getDefault().getLanguage() + ")");
            return false;
        }
        return true;
    }
}
