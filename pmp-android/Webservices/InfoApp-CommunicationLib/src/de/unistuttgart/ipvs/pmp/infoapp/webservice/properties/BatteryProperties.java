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
 * Stores information about the device's battery and allows to update or insert a new device information set
 * 
 * @author Patrick Strobel
 */
public class BatteryProperties extends Properties {
    
    private String technology;
    private byte health;
    
    
    /**
     * Creates a new battery property-set
     * 
     * @param service
     *            Helper class used for communication with the webservice
     * @param technology
     *            The technology that is being used for the device's battery
     * @param health
     *            The battery's health in percent
     */
    public BatteryProperties(Service service, String technology, byte health) {
        super(service);
        this.technology = technology;
        this.health = health;
    }
    
    
    /**
     * Gets the technology that is being used for the device's battery
     * 
     * @return Battery technology
     */
    public String getTechnology() {
        return this.technology;
    }
    
    
    /**
     * Gets the battery's health
     * 
     * @return Battery's health in percent
     */
    public byte getHealth() {
        return this.health;
    }
    
    
    @Override
    public void commit() throws InternalDatabaseException, InvalidParameterException, IOException {
        try {
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("technology", this.technology));
            params.add(new BasicNameValuePair("health", Byte.toString(this.health)));
            super.service.requestPostService("update_battery.php", params);
        } catch (JSONException e) {
            throw new IOException("Server returned no valid JSON object: " + e);
        }
    }
    
}
