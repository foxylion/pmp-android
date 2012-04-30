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
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidEventIdException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidEventOrderException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidParameterException;

/**
 * Stores information about the device's cellular connection and allows to update or insert a new device information set
 * 
 * @author Patrick Strobel
 */
public class CellularConnectionProperties extends Properties {
    
    private String provider;
    private byte signal;
    
    
    /**
     * Creates a new battery property-set
     * 
     * @param service
     *            Helper class used for communication with the webservice
     * @param provider
     *            Name of the network provider
     * @param signal
     *            Signal strength in percent
     */
    public CellularConnectionProperties(Service service, String provider, byte signal) {
        super(service);
        this.provider = provider;
        this.signal = signal;
    }
    
    
    /**
     * Gets the network provider's name
     * 
     * @return Name of the provider
     */
    public String getProvider() {
        return this.provider;
    }
    
    
    /**
     * Gets the signal's strength
     * 
     * @return Gets the signal's strength
     */
    public byte getSignalStrength() {
        return this.signal;
    }
    
    
    @Override
    public void commit() throws InternalDatabaseException, InvalidParameterException, InvalidEventIdException,
            InvalidEventOrderException, IOException {
        try {
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("provider", this.provider));
            params.add(new BasicNameValuePair("signal", Byte.toString(this.signal)));
            super.service.requestPostService("update_connection_cellular.php", params);
        } catch (JSONException e) {
            throw new IOException("Server returned no valid JSON object: " + e);
        }
    }
    
}
