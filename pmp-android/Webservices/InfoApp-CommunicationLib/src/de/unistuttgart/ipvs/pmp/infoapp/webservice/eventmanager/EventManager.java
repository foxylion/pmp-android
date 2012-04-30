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
package de.unistuttgart.ipvs.pmp.infoapp.webservice.eventmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.Event;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InternalDatabaseException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidEventIdException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidEventOrderException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidParameterException;

/**
 * Abstract base class for all event managers that might be used to upload data to a webservice.
 * 
 * @author Patrick Strobel
 * 
 */
public abstract class EventManager {
    
    protected Service service;
    
    
    /**
     * Creates a new manager
     * 
     * @param service
     *            Helper class used for communication with the webservice
     */
    public EventManager(Service service) {
        this.service = service;
    }
    
    
    /**
     * Uploads events to the webservice
     * 
     * @param events
     *            Events to upload
     * @throws InternalDatabaseException
     *             Thrown, if an internal database error occurred while the webservice was running
     * @throws InvalidParameterException
     *             Thrown, if one parameter set by the constructor or a set-methode was not accepted by the webservice
     * @throws InvalidEventIdException
     *             Thrown, if the given events were not accepted by the webservice because at least one ID is already in
     *             use by a database entry
     * @throws InvalidEventOrderException
     *             Thrown, if the events are not ordered properly. That is, the IDs and timestamps are not in ascending
     *             order. See webservice specification for details
     * @throws IOException
     *             Thrown, if another communication error occured while contacting the webservice
     */
    public abstract void commitEvents(List<? extends Event> events) throws InternalDatabaseException,
            InvalidParameterException, InvalidEventIdException, InvalidEventOrderException, IOException;
    
    
    protected void commitEvents(List<? extends Event> events, String service) throws IOException {
        JSONObject data = new JSONObject();
        
        JSONArray jsonEvents = new JSONArray();
        try {
            for (Event event : events) {
                jsonEvents.put(event.toJSONObject());
                
            }
            data.put("events", jsonEvents);
            
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("data", data.toString()));
            this.service.requestPostService(service, params);
        } catch (JSONException e) {
            throw new IOException("Server returned no valid JSON object or JSON request could not be build: " + e);
        }
    }
    
    
    /**
     * Gets the ID of the event that has been uploaded to the webservice the last time
     * 
     * @return The last event's ID
     * @throws InternalDatabaseException
     *             Thrown, if an internal database error occurred while the webservice was running
     * @throws InvalidParameterException
     *             Thrown, if one parameter set by the constructor or a set-methode was not accepted by the webservice
     * @throws IOException
     *             Thrown, if another communication error occured while contacting the webservice
     */
    public abstract int getLastId() throws InternalDatabaseException, InvalidParameterException, IOException;
    
    
    protected int getLastId(String service) throws IOException {
        try {
            JSONObject res = this.service.requestGetService(service);
            return res.getInt("last_id");
        } catch (JSONException e) {
            throw new IOException("Server returned no valid JSON object: " + e);
        }
    }
    
}
