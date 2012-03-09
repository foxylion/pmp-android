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
        String getParam = buf.toString();
        // Cut the last '&' out
        getParam = getParam.substring(0, getParam.length() - 1);
        
        Log.d(TAG, "Param: " + getParam);
        
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        
        HttpPost httppost = new HttpPost(Constants.WEBSERVICE_URL + getParam);
        
        List<NameValuePair> namelist = new ArrayList<NameValuePair>(listToParse.size());
        
        // Iterate over objects, which have to be post to the webservice
        // POST REQUESTS
        for (ParamObject object : listToParse) {
            if ((object.isPost())) {
                namelist.add(new BasicNameValuePair(object.getKey(), object.getValue()));
                // TODO: Remove this line
                Log.d(TAG, "POST: " + object.getKey() + " - " + object.getValue());
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
            try {
                jsonObject = (new JsonParser()).parse(r).getAsJsonObject();
                Log.d(TAG, "=====JSonRequestProvider DEBUG MODE===");
                Log.d(TAG, "======DEBUG MODE============");
                Log.d(TAG, jsonObject.toString());
                Log.d(TAG, "======DEBUG MODE END=====");
            } catch (Exception e) {
            }
        }
        return jsonObject;
    }
}