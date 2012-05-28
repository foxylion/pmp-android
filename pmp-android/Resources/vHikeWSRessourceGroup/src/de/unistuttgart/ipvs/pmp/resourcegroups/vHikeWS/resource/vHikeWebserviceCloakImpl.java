package de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.resource;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.vHikeWSResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.aidl.IvHikeWebservice;

public class vHikeWebserviceCloakImpl extends IvHikeWebservice.Stub {
    
    public vHikeWebserviceCloakImpl(vHikeWSResourceGroup vHikeRG, vHikeWebserviceResource vHikeWebserviceResource,
            String appIdentifier) {
        // TODO Auto-generated constructor stub
    }
    
    
    @Override
    public String register(String username, String password, String email, String firstname, String lastname,
            String tel, String description, boolean email_public, boolean firstname_public, boolean lastname_public,
            boolean tel_public) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String login(String username, String pw) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String logout(String session_id) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String getOwnProfile(String sid) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String getProfile(String session_id, int id) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String tripUpdatePos(String sid, int trip_id, float current_lat, float current_lon) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String userUpdatePos(String sid, float lat, float lon) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String getUserPosition(String sid, int user_id) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String tripUpdateData(String sid, int trip_id, int avail_seats) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String getTripPassengers() throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String endTrip(String sid, int trip_id) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String startQuery(String sid, String destination, float current_lat, float current_lon, int seats)
            throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String stopQuery(String sid, int id) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String searchQuery(String sid, float lat, float lon, int perimeter) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String searchRides(String sid, float lat, float lon, int perimeter) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String sendOffer(String sid, int trip_id, int query_id, String message) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String viewOffer(String sid) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String handleOffer(String sid, int offer_id, boolean accept) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String pick_up(String sid, int user_id) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String isPicked(String sid) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String offer_accepted(String sid, int offer_id) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String getHistory(String sid, String role) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String rateUser(String sid, int userid, int tripid, int rating) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String announceTrip(String session_id, String destination, float current_lat, float current_lon,
            int avail_seats, long date) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String getOpenTrip(String sessionID) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String enableObservation(String sessionID, String user_id) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String disableObservation(String sessionID, String user_id) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String setProfileVisibility(String sid, boolean lastname_public, boolean firstname_public,
            boolean email_public, boolean tel_public) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String isProfileAnonymous(String sid, int uid) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String enableAnonymity(String sid) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String disableAnonymity(String sid) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String isObservationEnabled(int uid) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String queryUpdateData(String sid, int query_id, int wanted_seats) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String getMyTrips(int uid) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String editProfile(String sid, String lastname, String firstname, String tel, String description)
            throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
