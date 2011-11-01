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

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.app.xmlparser.AppInformationSet;
import de.unistuttgart.ipvs.pmp.app.xmlparser.RequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.app.xmlparser.ServiceLevel;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelConditions;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.service.RegistrationState;
import de.unistuttgart.ipvs.pmp.service.app.IAppServicePMP;
import de.unistuttgart.ipvs.pmp.service.utils.AppServiceConnector;
import de.unistuttgart.ipvs.pmp.service.utils.PMPSignee;

public class AppRegistration {
    
    private String identifier;
    
    private byte[] publicKey;
    
    private AppInformationSet ais = null;
    
    private final AppServiceConnector asp;
    
    
    /**
     * Executes an asynchronous App registration.
     * 
     * @param identifier
     *            Identifier of the App
     * @param publicKey
     *            Public key of the App
     */
    public AppRegistration(String identifier, byte[] publicKey) {
        ModelConditions.assertStringNotNullOrEmpty("identifier", identifier);
        ModelConditions.assertPublicKeyNotNullOrEmpty(publicKey);
        
        this.identifier = identifier;
        this.publicKey = publicKey.clone();
        this.asp = new AppServiceConnector(PMPApplication.getContext(), PMPApplication.getSignee(), identifier);
        
        Log.v("Registration (" + identifier + "): Trying to connect to the AppService");
        
        connect();
    }
    
    
    /**
     * Connect to the Service.
     */
    private void connect() {
        if (!this.asp.bind(true)) {
            Log.e("Registration (" + this.identifier
                    + "): FAILED - Connection to the AppService failed. More details can be found in the log.");
        } else {
            Log.d("Registration (" + this.identifier + "): Successfully bound.");
            
            if (this.asp.getAppService() == null) {
                Log.e("Registration (" + this.identifier
                        + "): FAILED - Binding to the AppService failed, only got a null IBinder.");
            } else {
                Log.d("Registration (" + this.identifier + "): Successfully connected, got IAppServicePMP.");
                loadAppInformationSet(this.asp.getAppService());
            }
        }
    }
    
    
    /**
     * Load the {@link AppInformationSet} from the Service.
     * 
     * @param appService
     *            service which provides the {@link AppInformationSet}
     */
    private void loadAppInformationSet(IAppServicePMP appService) {
        Log.v("Registration (" + this.identifier + "): loading AppInformationSet...");
        
        try {
            this.ais = appService.getAppInformationSet().getAppInformationSet();
            
            checkAppInformationSet();
        } catch (RemoteException e) {
            Log.e("Registration (" + this.identifier
                    + "): FAILED - getAppInformationSet() produced an RemoteException.", e);
            informAboutRegistration(false, "getAppInformationSet() produced an RemoteException.");
        }
        
    }
    
    
    /**
     * Check if the {@link AppInformationSet} is correct and the application is not already registered.
     */
    private void checkAppInformationSet() {
        if (this.ais == null) {
            Log.e("Registration (" + this.identifier + "): FAILED - AppInformationSet is null.");
            informAboutRegistration(false, "AppInformationSet is null.");
            return;
        }
        if (this.ais.getNames() == null || this.ais.getDescriptions() == null || this.ais.getServiceLevels() == null) {
            Log.e("Registration (" + this.identifier + "): FAILED - AppInformationSet has methods with null-Return.");
            informAboutRegistration(false, "AppInformationSet has methods with null-Return.");
            return;
        }
        
        /* Check if the app is already in the PMP-Database. */
        if (ModelSingleton.getInstance().getModel().getApp(this.identifier) != null) {
            Log.e("Registration (" + this.identifier
                    + "): FAILED - There is already an App registred with that identifier.");
            informAboutRegistration(
                    false,
                    "There is already an App registred with that identifier, maybe lost your key and want to reregister? that will not work, yet.");
            return;
        }
        
        registerAppInDatabase();
    }
    
    
    /**
     * Write the {@link AppInformationSet} to the Database.
     */
    private void registerAppInDatabase() {
        Log.v("Registration (" + this.identifier + "): Adding the App to the PMP-Database");
        
        SQLiteDatabase db = DatabaseSingleton.getInstance().getDatabaseHelper().getWritableDatabase();
        
        ContentValues cv = new ContentValues();
        cv.put("Identifier", this.identifier);
        cv.put("Name_Cache", getLocalized(this.ais.getNames()));
        cv.put("Description_Cache", getLocalized(this.ais.getDescriptions()));
        
        db.insert("App", null, cv);
        
        for (Entry<Integer, ServiceLevel> sl : this.ais.getServiceLevels().entrySet()) {
            
            cv = new ContentValues();
            cv.put("App_Identifier", this.identifier);
            cv.put("Level", sl.getKey());
            cv.put("Name_Cache", getLocalized(sl.getValue().getNames()));
            cv.put("Description_Cache", getLocalized(sl.getValue().getDescriptions()));
            
            db.insert("ServiceLevel", null, cv);
            
            for (RequiredResourceGroup rrg : sl.getValue().getRequiredResourceGroups()) {
                
                for (Entry<String, String> pl : rrg.getPrivacyLevelMap().entrySet()) {
                    
                    cv = new ContentValues();
                    cv.put("App_Identifier", this.identifier);
                    cv.put("ServiceLevel_Level", sl.getKey());
                    cv.put("ResourceGroup_Identifier", rrg.getRgIdentifier());
                    cv.put("PrivacyLevel_Identifier", pl.getKey());
                    cv.put("Value", pl.getValue());
                    
                    db.insert("ServiceLevel_PrivacyLevels", null, cv);
                }
            }
        }
        
        publishPublicKey();
    }
    
    
    /**
     * Publish the public key of the to the {@link PMPSignee} of {@link PMPApplication}.
     */
    private void publishPublicKey() {
        Log.v("Loading Apps public key into PMPSignee");
        
        PMPApplication.getSignee().setRemotePublicKey(PMPComponentType.APP, this.identifier, this.publicKey);
        
        informAboutRegistration(true, null);
    }
    
    
    /**
     * Inform the AppService about the registration state.
     * 
     * @param state
     *            true means successful, false means unsuccessful
     * @param message
     *            a message with optional information provided.
     */
    private void informAboutRegistration(final boolean state, final String message) {
        Log.d("Registration (" + this.identifier + "): Informing App about registrationState (" + state + ")");
        
        if (!this.asp.isBound()) {
            if (!this.asp.bind(true)) {
                Log.e("Registration (" + this.identifier
                        + "): FAILED - Connection to the AppService failed. More details can be found in the log.");
            } else {
                Log.d("Registration (" + this.identifier + "): Successfully bound.");
                
                if (this.asp.getAppService() == null) {
                    Log.e("Registration (" + this.identifier
                            + "): FAILED - Binding to the AppService failed, only got a null IBinder.");
                } else {
                    Log.d("Registration (" + this.identifier + "): Successfully connected got IAppServicePMP");
                    informAboutRegistration(state, message);
                }
            }
        } else {
            try {
                Log.v("Registration (" + this.identifier + "): Calling setRegistrationState");
                IAppServicePMP appService = this.asp.getAppService();
                appService.setRegistrationState(new RegistrationState(state, message));
                this.asp.unbind();
                
                Log.d("Registration (" + this.identifier + "): Registration " + (state ? "succeed" : "FAILED"));
            } catch (RemoteException e) {
                Log.e("Registration (" + this.identifier + "): " + (state ? "succeed" : "FAILED")
                        + ", but setRegistrationState() produced an RemoteException.", e);
            }
        }
    }
    
    
    private String getLocalized(Map<Locale, String> map) {
        if (map.containsKey(new Locale(Locale.getDefault().getLanguage()))) {
            // TODO check if that really works when the default is de_DE.utf-8
            // or something like that...
            return map.get(new Locale(Locale.getDefault().getLanguage()));
        } else {
            return map.get(new Locale("en"));
        }
    }
}
