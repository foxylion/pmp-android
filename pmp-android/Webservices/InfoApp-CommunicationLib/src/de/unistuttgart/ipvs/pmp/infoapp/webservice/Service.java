package de.unistuttgart.ipvs.pmp.infoapp.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InternalDatabaseException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidEventIdException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidEventOrderException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidParameterException;

public class Service {
    
    private String url;
    private String deviceId;
    
    
    public Service(String url, String deviceId) {
        this.url = url;
        this.deviceId = deviceId;
    }
    
    
    public String getDeviceId() {
        return this.deviceId;
    }
    
    
    /**
     * Requests a webservice using HTTP-GET
     * 
     * @param service
     *            The scripts name (filename, e. g. upload_connection_events.php)
     * @param params
     *            Get parameters that should be sent to the service
     * @return Result returned by the service
     * @throws IOException
     *             Thrown, if communication with server failed
     * @throws JSONException
     *             Thrown, if no JSON-string was returned by the server
     */
    public JSONObject requestGetService(String service, HttpParams params) throws IOException, JSONException {
        HttpGet httpGet = new HttpGet(url + "/" + service);
        httpGet.setParams(params);
        
        HttpClient client = new DefaultHttpClient();
        
        return getContent(client.execute(httpGet));
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
