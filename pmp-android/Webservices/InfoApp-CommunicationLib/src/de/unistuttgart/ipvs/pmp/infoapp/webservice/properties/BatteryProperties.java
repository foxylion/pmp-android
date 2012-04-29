package de.unistuttgart.ipvs.pmp.infoapp.webservice.properties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;

/**
 * Stores information about the device's battery and allows to update or insert a new device information set
 * 
 * @author Patrick Strobel
 */
public class BatteryProperties extends Properties {
    
    private String technology;
    private short health;
    
    
    public BatteryProperties(Service service, String technology, short health) {
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
    public short getHealth() {
        return this.health;
    }
    
    
    @Override
    public void commit() throws IOException {
        try {
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("technology", this.technology));
            params.add(new BasicNameValuePair("health", Short.toString(this.health)));
            super.service.requestPostService("update_battery.php", params);
        } catch (JSONException e) {
            throw new IOException("Server returned no valid JSON object: " + e);
        }
    }
    
}
