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
package de.unistuttgart.ipvs.pmp.infoapp.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InternalDatabaseException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidEventIdException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidEventOrderException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidParameterException;

public class Service {
    
    public static String DEFAUL_URL = "http://infoapp.no-ip.org/json";
    private String url;
    private String deviceId;
    
    
    /**
     * Creates a new service-instance used for communicating with the webservices
     * 
     * @param url
     *            URL of the webservices.
     * @param deviceId
     *            16-bit (32 characters) HEX-value used the uniquely identify the Android device
     */
    public Service(String url, String deviceId) {
        this.url = url;
        this.deviceId = deviceId;
    }
    
    
    public String getDeviceId() {
        return this.deviceId;
    }
    
    
    /**
     * Requests a webservice using HTTP-GET. The device ID is automatically added to the parameter list
     * 
     * @param service
     *            The scripts name (filename, e. g. upload_connection_events.php)
     * @return Result returned by the service
     * @throws IOException
     *             Thrown, if communication with server failed
     * @throws JSONException
     *             Thrown, if no JSON-string was returned by the server
     */
    public JSONObject requestGetService(String service) throws IOException, JSONException {
        return requestGetService(service, new ArrayList<BasicNameValuePair>(1));
    }
    
    
    /**
     * Requests a webservice using HTTP-GET. The device ID is automatically added to the parameter list
     * 
     * @param service
     *            The scripts name (filename, e. g. upload_connection_events.php)
     * @param params
     *            Additional GET parameters that should be sent to the service
     * @return Result returned by the service
     * @throws IOException
     *             Thrown, if communication with server failed. May be a subclass from the "exceptions" subpackage
     * @throws JSONException
     *             Thrown, if no JSON-string was returned by the server
     */
    public JSONObject requestGetService(String service, List<BasicNameValuePair> params) throws IOException,
            JSONException {
        params.add(new BasicNameValuePair("device", this.deviceId));
        String paramString = URLEncodedUtils.format(params, "UTF-8");
        HttpGet httpGet = new HttpGet(url + "/" + service + "?" + paramString);
        
        HttpClient client = new DefaultHttpClient();
        
        return getContent(client.execute(httpGet));
    }
    
    
    /**
     * Requests a webservice using HTTP-POST. The device ID is automatically added to the parameter list
     * 
     * @param service
     *            The scripts name (filename, e. g. upload_connection_events.php)
     * @param params
     *            Additional POST parameters that should be sent to the service
     * @return Result returned by the service
     * @throws IOException
     *             Thrown, if communication with server failed. May be a subclass from the "exceptions" subpackage
     * @throws JSONException
     *             Thrown, if no JSON-string was returned by the server
     */
    public JSONObject requestPostService(String service, List<BasicNameValuePair> params) throws IOException,
            JSONException {
        params.add(new BasicNameValuePair("device", this.deviceId));
        HttpPost httpPost = new HttpPost(url + "/" + service);
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        
        HttpClient client = new DefaultHttpClient();
        
        return getContent(client.execute(httpPost));
    }
    
    
    private JSONObject getContent(HttpResponse res) throws JSONException, IOException {
        HttpEntity entity = res.getEntity();
        
        if (entity == null) {
            return null;
        }
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
        
        StringBuilder sb = new StringBuilder();
        String line = null;
        
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        reader.close();
        JSONObject json = new JSONObject(sb.toString());
        
        checkResult(json);
        
        return json;
    }
    
    
    /**
     * Inspects a result and throws a exception is an error was returned
     * 
     * @param res
     * @throws JSONException
     * @throws InternalDatabaseException
     * @throws InvalidParameterException
     * @throws InvalidEventIdException
     * @throws InvalidEventOrderException
     */
    private void checkResult(JSONObject res) throws JSONException, InternalDatabaseException,
            InvalidParameterException, InvalidEventIdException, InvalidEventOrderException {
        if (!res.getBoolean("successful")) {
            String error = res.getString("error");
            String msg = res.getString("msg");
            
            if (error.equalsIgnoreCase("internal_database_error")) {
                throw new InternalDatabaseException(msg);
            }
            
            if (error.equalsIgnoreCase("invalid_parameter")) {
                throw new InvalidParameterException(msg);
            }
            
            if (error.equalsIgnoreCase("invalid_event_id")) {
                throw new InvalidEventIdException(msg);
            }
            
            if (error.equalsIgnoreCase("invalid_event_order")) {
                throw new InvalidEventOrderException(msg);
            }
        }
    }
}
