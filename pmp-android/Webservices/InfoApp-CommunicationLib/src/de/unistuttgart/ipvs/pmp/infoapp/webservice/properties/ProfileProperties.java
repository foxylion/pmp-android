/*
 * Copyright 2012 pmp-android development team
 * Project: InfoApp-CommunicationLib
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
package de.unistuttgart.ipvs.pmp.infoapp.webservice.properties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InternalDatabaseException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidParameterException;

/**
 * Stores information about the user's profile and allows to update or insert a new profile information set
 * 
 * @author Patrick Strobel
 */
public class ProfileProperties extends Properties {
    
    public enum Rings {
        BOTH,
        RING,
        SILENT,
        VIBRATION
    };
    
    private Rings ring;
    private short contacts;
    private short apps;
    
    
    /**
     * 
     * @param service
     *            Helper class used for communication with the webservice
     * @param ring
     *            Type of the ring notification
     * @param contacts
     *            Number of contacts the user has in his/her address book
     * @param apps
     *            Number of apps installed on the device
     */
    public ProfileProperties(Service service, Rings ring, short contacts, short apps) {
        super(service);
        this.ring = ring;
        this.contacts = contacts;
        this.apps = apps;
    }
    
    
    /**
     * Gets the ring notification type
     * 
     * @return Ring-type (see constants)
     */
    public Rings getRingType() {
        return this.ring;
    }
    
    
    /**
     * Gets the number of apps that have been installed onto the device
     * 
     * @return Number of apps
     */
    public short getAppsCount() {
        return this.apps;
    }
    
    
    /**
     * Gets the number of contacts the user has inserted in his/her address book
     * 
     * @return Number of contacts
     */
    public short getContactCount() {
        return this.contacts;
    }
    
    
    @Override
    public void commit() throws InternalDatabaseException, InvalidParameterException, IOException {
        try {
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            
            switch (this.ring) {
                case BOTH:
                    params.add(new BasicNameValuePair("ring", "b"));
                    break;
                case RING:
                    params.add(new BasicNameValuePair("ring", "r"));
                    break;
                case SILENT:
                    params.add(new BasicNameValuePair("ring", "s"));
                    break;
                case VIBRATION:
                    params.add(new BasicNameValuePair("ring", "v"));
                    break;
            }
            
            params.add(new BasicNameValuePair("apps", Short.toString(this.apps)));
            params.add(new BasicNameValuePair("contacts", Short.toString(this.contacts)));
            super.service.requestPostService("update_profile.php", params);
        } catch (JSONException e) {
            throw new IOException("Server returned no valid JSON object: " + e);
        }
    }
    
}
