package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.OfferObject;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.QueryObject;

public class ViewObject {
    
   private  float lat;
   private  float lon;
   private Profile profile;
   private QueryObject qObject;
   private  OfferObject oObject;
    
    int status;
    
    
    public ViewObject(float lat, float lon, Profile profile) {
        super();
        this.lat = lat;
        this.lon = lon;
        this.profile = profile;
    }
    
    
    public QueryObject getqObject() {
        return qObject;
    }
    
    
    public void setqObject(QueryObject qObject) {
        this.qObject = qObject;
    }
    
    
    public OfferObject getoObject() {
        return oObject;
    }
    
    
    public void setoObject(OfferObject oObject) {
        this.oObject = oObject;
    }
    
    
    public int getStatus() {
        return status;
    }
    
    /**
     * Status: FOUND, INVITED, AWAIT_ACCEPTION, ACCEPTED, PICKED_UP, BANNED
     */
    public void setStatus(int status) {
        this.status = status;
    }
    
}
