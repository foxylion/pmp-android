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
 * Stores information about the device's cellular connection and allows to update or insert a new device information set
 * 
 * @author Patrick Strobel
 */
public class CellularConnectionProperties extends Properties {
    
    private String provider;
    private boolean roaming;
    
    public enum NetworkTypes {
        UNKNOWN,
        GPRS,
        EDGE,
        UMTS,
        HSDPA,
        HSUPA,
        HSPA,
        CDMA,
        EVDO_0,
        EVDO_A,
        EVDO_B,
        RTT,
        IDEN,
        LTE,
        EHRPD,
        HSPAP
    };
    
    private NetworkTypes network;
    
    
    /**
     * Creates a new battery property-set
     * 
     * @param service
     *            Helper class used for communication with the webservice
     * @param provider
     *            Name of the network provider
     * @param roaming
     *            The roaming status
     * @param network
     *            The network type
     */
    public CellularConnectionProperties(Service service, String provider, boolean roaming, NetworkTypes network) {
        super(service);
        this.provider = provider;
        this.roaming = roaming;
        this.network = network;
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
     * Gets the roaming status
     * 
     * @return True, if roaming is active
     */
    public boolean isRoaming() {
        return this.roaming;
    }
    
    
    /**
     * Gets the network type
     * 
     * @return The network type
     */
    public NetworkTypes getNetworkType() {
        return this.network;
    }
    
    
    @Override
    public void commit() throws InternalDatabaseException, InvalidParameterException, IOException {
        try {
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("provider", this.provider));
            params.add(new BasicNameValuePair("roaming", Boolean.toString(this.roaming)));
            switch (this.network) {
                case CDMA:
                    params.add(new BasicNameValuePair("network", "cd"));
                    break;
                case EDGE:
                    params.add(new BasicNameValuePair("network", "ed"));
                    break;
                case EHRPD:
                    params.add(new BasicNameValuePair("network", "eh"));
                    break;
                case EVDO_0:
                    params.add(new BasicNameValuePair("network", "e0"));
                    break;
                case EVDO_A:
                    params.add(new BasicNameValuePair("network", "ea"));
                    break;
                case EVDO_B:
                    params.add(new BasicNameValuePair("network", "eb"));
                    break;
                case GPRS:
                    params.add(new BasicNameValuePair("network", "gp"));
                    break;
                case HSDPA:
                    params.add(new BasicNameValuePair("network", "hd"));
                    break;
                case HSPA:
                    params.add(new BasicNameValuePair("network", "hs"));
                    break;
                case HSPAP:
                    params.add(new BasicNameValuePair("network", "hp"));
                    break;
                case HSUPA:
                    params.add(new BasicNameValuePair("network", "hu"));
                    break;
                case IDEN:
                    params.add(new BasicNameValuePair("network", "id"));
                    break;
                case LTE:
                    params.add(new BasicNameValuePair("network", "lt"));
                    break;
                case RTT:
                    params.add(new BasicNameValuePair("network", "1r"));
                    break;
                case UMTS:
                    params.add(new BasicNameValuePair("network", "um"));
                    break;
                case UNKNOWN:
                    params.add(new BasicNameValuePair("network", "un"));
                    break;
            
            }
            super.service.requestPostService("update_connection_cellular.php", params);
        } catch (JSONException e) {
            throw new IOException("Server returned no valid JSON object: " + e);
        }
    }
    
}
