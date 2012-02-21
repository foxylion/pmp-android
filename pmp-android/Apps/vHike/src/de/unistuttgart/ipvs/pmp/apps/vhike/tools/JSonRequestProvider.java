/*
 * Copyright 2012 pmp-android development team
 * Project: vHike
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
package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;

/**
 * Provides all the JSonRequests that are possible between vHike and webservice
 * outside. It connects to the webservice and getting the JsonObject. After the
 * JsonObject was get, return the JsonObject to the caller class.
 * 
 * 
 * @author Alexander Wassiljew
 * 
 */
public class JSonRequestProvider {
    
    private static final String TAG = "JSonRequestProvider";
    
    boolean debug = false;
    
    
    /**
     * private constructor cause of singleton
     */
    public JSonRequestProvider() {
        
    }
    
    
    /**
     * Sending a request to the WEBSERVICE_URL defined in {@link Constants}
     * 
     * @param listToParse
     *            contains all the parameters, which have to be parsed
     * @param url
     * @param debug
     *            mode true or false
     * @return JsonObject
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static JsonObject doRequest(List<ParamObject> listToParse, String url, boolean debug)
            throws ClientProtocolException, IOException {
        
        String getParam = "";
        // GET REQUESTS
        StringBuffer buf = new StringBuffer();
        buf.append(url);
        buf.append("?");
        
        for (ParamObject object : listToParse) {
            
            if (!(object.isPost())) {
                buf.append(object.getKey() + "=" + object.getValue());
                buf.append("&");
                // getParam = getParam + object.getKey() + "=" +
                // object.getValue();
                // getParam = getParam + "&";
            }
        }
        getParam = buf.toString();
        // Cut the last '&' out
        getParam = getParam.substring(0, getParam.length() - 1);
        
        Log.i(TAG, "Parameter :" + getParam);
        
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        
        HttpPost httppost = new HttpPost(Constants.WEBSERVICE_URL + getParam);
        
        List<NameValuePair> namelist = new ArrayList<NameValuePair>();
        
        // Iterate over objects, wich have to be post to the webservice
        // POST REQUESTS
        for (ParamObject object : listToParse) {
            if ((object.isPost())) {
                namelist.add(new BasicNameValuePair(object.getKey(), object.getValue()));
            }
        }
        
        httppost.setEntity(new UrlEncodedFormEntity(namelist, "UTF-8"));
        
        // Execute HTTP Post Request
        HttpResponse response;
        JsonObject jsonObject = null;
        response = httpclient.execute(httppost);
        // for JSON:
        if (response != null) {
            InputStream is = response.getEntity().getContent();
            
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            JsonParser parser = new JsonParser();
            new BufferedReader(r);
            
            if (debug) {
                Log.w(TAG, "=====JSonRequestProvider DEBUG MODE===");
                Log.w(TAG, "======DEBUG MODE============");
                Log.w(TAG, "Json: " + r.readLine());
                Log.w(TAG, "Json: " + r.readLine());
                Log.w(TAG, "Json: " + r.readLine());
                Log.w(TAG, "Json: " + r.readLine());
                Log.w(TAG, "Json: " + r.readLine());
                Log.w(TAG, "Json: " + r.readLine());
                Log.w(TAG, "Json: " + r.readLine());
                Log.w(TAG, "Json: " + r.readLine());
                Log.w(TAG, "Json: " + r.readLine());
                Log.w(TAG, "Json: " + r.readLine());
                Log.w(TAG, "Json: " + r.readLine());
                Log.w(TAG, "Json: " + r.readLine());
                Log.w(TAG, "Json: " + r.readLine());
                Log.w(TAG, "Json: " + r.readLine());
                Log.w(TAG, "Json: " + r.readLine());
                Log.w(TAG, "======DEBUG MODE END=====");
            }
            
            try {
                jsonObject = parser.parse(r).getAsJsonObject();
            } catch (Exception e) {
            }
            
        }
        
        return jsonObject;
    }
    
}
