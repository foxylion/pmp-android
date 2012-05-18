package de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.resource;

import java.util.ArrayList;
import java.util.List;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.PermissionValidator;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.vHikeWSResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.aidl.IvHikeWebservice;

public class vHikeWebserviceImpl extends IvHikeWebservice.Stub {
    
    static List<ParamObject> listToParse = new ArrayList<ParamObject>();
    
    vHikeWSResourceGroup vHikeRG;
    vHikeWebserviceResource vHikeRes;
    PermissionValidator psv;
    String appIdentifier;
    
    
    public vHikeWebserviceImpl(vHikeWSResourceGroup vHikeRG, vHikeWebserviceResource vHikeRes, String appIdentifier) {
        this.vHikeRG = vHikeRG;
        this.vHikeRes = vHikeRes;
        this.appIdentifier = appIdentifier;
        this.psv = new PermissionValidator(vHikeRG, appIdentifier);
    }
    
    
    @Override
    public String register(String username, String password, String email, String firstname, String lastname,
            String tel, String description, boolean email_public, boolean firstname_public, boolean lastname_public,
            boolean tel_public) throws RemoteException {
        
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        
        return this.vHikeRes.register(username, password, email, firstname, lastname, tel, description, email_public,
                firstname_public, lastname_public, tel_public);
    }
    
    
    @Override
    public String login(String username, String pw) throws RemoteException {
        
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.login(username, pw);
    }
    
    
    @Override
    public String logout(String session_id) throws RemoteException {
        
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        
        return this.vHikeRes.logout(session_id);
    }
    
    
    @Override
    public String getOwnProfile(String sid) throws RemoteException {
        
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.getOwnProfile(sid);
    }
    
    
    @Override
    public String getProfile(String session_id, int id) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.getProfile(session_id, id);
    }
    
    
    @Override
    public String tripUpdatePos(String sid, int trip_id, float current_lat, float current_lon) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.tripUpdatePos(sid, trip_id, current_lat, current_lon);
    }
    
    
    @Override
    public String userUpdatePos(String sid, float lat, float lon) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.userUpdatePos(sid, lat, lon);
    }
    
    
    @Override
    public String getUserPosition(String sid, int user_id) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        
        return this.vHikeRes.getUserPosition(sid, user_id);
    }
    
    
    @Override
    public String tripUpdateData(String sid, int trip_id, int avail_seats) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.tripUpdateData(sid, trip_id, avail_seats);
    }
    
    
    @Override
    public String getTripPassengers() throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.getTripPassengers();
    }
    
    
    @Override
    public String endTrip(String sid, int trip_id) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.endTrip(sid, trip_id);
    }
    
    
    @Override
    public String startQuery(String sid, String destination, float current_lat, float current_lon, int seats)
            throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.startQuery(sid, destination, current_lat, current_lon, seats);
    }
    
    
    @Override
    public String stopQuery(String sid, int id) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.stopQuery(sid, id);
    }
    
    
    @Override
    public String searchQuery(String sid, float lat, float lon, int perimeter) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.searchQuery(sid, lat, lon, perimeter);
    }
    
    
    @Override
    public String searchRides(String sid, float lat, float lon, int perimeter) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.searchRides(sid, lat, lon, perimeter);
    }
    
    
    @Override
    public String sendOffer(String sid, int trip_id, int query_id, String message) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.sendOffer(sid, trip_id, query_id, message);
    }
    
    
    @Override
    public String viewOffer(String sid) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.viewOffer(sid);
    }
    
    
    @Override
    public String handleOffer(String sid, int offer_id, boolean accept) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.handleOffer(sid, offer_id, accept);
    }
    
    
    @Override
    public String pick_up(String sid, int user_id) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.pick_up(sid, user_id);
    }
    
    
    @Override
    public String isPicked(String sid) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.isPicked(sid);
    }
    
    
    @Override
    public String offer_accepted(String sid, int offer_id) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.offer_accepted(sid, offer_id);
    }
    
    
    @Override
    public String getHistory(String sid, String role) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.getHistory(sid, role);
    }
    
    
    @Override
    public String rateUser(String sid, int userid, int tripid, int rating) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.rateUser(sid, userid, tripid, rating);
    }
    
    
    @Override
    public String announceTrip(String session_id, String destination, float current_lat, float current_lon,
            int avail_seats, long date) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.announceTrip(session_id, destination, current_lat, current_lon, avail_seats, date);
    }
    
    
    @Override
    public String getOpenTrip(String sessionID) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        
        return this.vHikeRes.getOpenTrip(sessionID);
    }
    
    
    @Override
    public String enableObservation(String sessionID, String user_id) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.enableObservation(sessionID, user_id);
    }
    
    
    @Override
    public String disableObservation(String sessionID, String user_id) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.disableObservation(sessionID, user_id);
    }
    
    
    @Override
    public String setProfileVisibility(String sid, boolean lastname_public, boolean firstname_public,
            boolean email_public, boolean tel_public) throws RemoteException {
        this.psv.validate(vHikeWSResourceGroup.PS_USE_vHIKE_WEBSERVICE, "true");
        return this.vHikeRes.setProfileVisibility(sid, lastname_public, firstname_public, email_public, tel_public);
    }
    
}
