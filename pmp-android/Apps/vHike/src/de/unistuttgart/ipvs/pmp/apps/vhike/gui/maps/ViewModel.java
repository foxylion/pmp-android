package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.NotificationAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.ContactDialog;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.OfferObject;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.QueryObject;
import de.unistuttgart.ipvs.pmp.resourcegroups.contact.aidl.IContact;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.aidl.IAbsoluteLocation;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.aidl.IvHikeWebservice;

/**
 * MapModel grants access to all elements needed to work with the map view
 * 
 * @author Andres Nguyen
 * 
 */
public class ViewModel {
    
    private List<ViewObject> lvo;
    Controller ctrl;
    private List<ViewObject> banned;
    MapView mapView;
    Context context;
    private float my_lat = 0;
    private float my_lon = 0;
    private boolean newFound = false;
    
    private boolean locationIsCanceled = false;
    private boolean queryIsCanceled = false;
    private IvHikeWebservice ws;
    private IContact iContact;
    
    
    public void setvHikeWSRGandCreateController(IvHikeWebservice ws) {
        this.ws = ws;
        ctrl = new Controller(ws);
    }
    
    
    public void setContactRG(IContact iContact) {
        this.iContact = iContact;
    }
    
    
    public void cancelLocation() {
        locationIsCanceled = true;
    }
    
    
    public void startLocation() {
        locationIsCanceled = false;
    }
    
    
    public void cancelQuery() {
        queryIsCanceled = true;
    }
    
    
    public void startQuery() {
        queryIsCanceled = false;
    }
    
    
    public boolean locationIsCanceled() {
        return locationIsCanceled;
    }
    
    
    public boolean queryIsCanceled() {
        return queryIsCanceled;
    }
    
    
    public void resetTimers() {
        locationIsCanceled = false;
        queryIsCanceled = false;
    }
    
    
    public List<ViewObject> getLVO() {
        return this.lvo;
    }
    
    
    private ViewModel() {
        this.lvo = new ArrayList<ViewObject>();
        this.banned = new ArrayList<ViewObject>();
    }
    
    
    public void addToBanned(ViewObject vObject) {
        this.banned.add(vObject);
        removeFromlvo(vObject);
    }
    
    
    private void removeFromlvo(ViewObject toRMVvObject) {
        int i = 0;
        for (ViewObject vObject : this.lvo) {
            
            if (vObject.getProfile().getID() == toRMVvObject.getProfile().getID()) {
                this.lvo.remove(i);
            }
            i++;
        }
    }
    
    
    public void updateView(int whichHitcher) {
        if (whichHitcher == 0) {
            updateLQO(new ArrayList<QueryObject>());
            ViewModel.getInstance().getDriverAdapter(this.context, this.mapView).notifyDataSetChanged();
        } else {
            updateLOO(new ArrayList<OfferObject>());
            ViewModel.getInstance().getPassengerAdapter(this.context, this.mapView).notifyDataSetChanged();
        }
        this.mapView.invalidate();
    }
    
    
    public void setMyPosition(float my_lat, float my_lon, int whichHitcher) {
        this.my_lat = my_lat;
        this.my_lon = my_lon;
        updateView(whichHitcher);
    }
    
    
    public float getMy_lat() {
        return this.my_lat;
    }
    
    
    public float getMy_lon() {
        return this.my_lon;
    }
    
    
    public void clearViewModel() {
        clearlvo();
        clearBanList();
        this.my_lat = 0;
        this.my_lon = 0;
    }
    
    
    private void clearlvo() {
        this.lvo.clear();
    }
    
    
    public void updateLQO(List<QueryObject> queries) {
        
        ViewModel.getInstance().clearDriverOverlayList();
        ViewModel.getInstance().getHitchPassengers().clear();
        this.newFound = false;
        
        // Add me to the mapView
        int my_new_lat = (int) (this.my_lat * 1E6);
        int my_new_lon = (int) (this.my_lon * 1E6);
        GeoPoint my_point = new GeoPoint(my_new_lat, my_new_lon);
        ViewModel.getInstance().add2DriverOverlay(this.context, my_point, Model.getInstance().getOwnProfile(),
                this.mapView, 0, my_point);
        
        try {
            for (QueryObject queryObject : queries) {
                float lat = queryObject.getCur_lat();
                float lon = queryObject.getCur_lon();
                if (!isInBanned(queryObject.getUserid())) {
                    if (isInLVO(queryObject.getUserid())) {
                        updateViewObject(queryObject.getUserid(), lat, lon);
                    } else {
                        Profile profile = this.ctrl.getProfile(Model.getInstance().getSid(), queryObject.getUserid());
                        ViewObject vObject = new ViewObject(ws, lat, lon, profile);
                        vObject.setqObject(queryObject);
                        this.lvo.add(vObject);
                        this.newFound = true;
                    }
                }
            }
        } catch (Exception ex) {
        }
        
        for (ViewObject vObject : this.lvo) {
            int lat = (int) (vObject.getLat() * 1E6);
            int lng = (int) (vObject.getLon() * 1E6);
            GeoPoint gpsPassenger = new GeoPoint(lat, lng);
            if (vObject.getStatus() != Constants.V_OBJ_SATUS_PICKED_UP) {
                ViewModel.getInstance().add2DriverOverlay(this.context, gpsPassenger, vObject.getProfile(),
                        this.mapView, 1, my_point);
                // if drawing route is enabled for user, draw route
                if (isRouteDrawn(vObject.getProfile().getUsername())) {
                    RouteOverlay routeOverlay = new RouteOverlay(context, my_point, gpsPassenger);
                    mapDriverOverlays.add(routeOverlay);
                    getRoutes.put(vObject.getProfile().getUsername(), true);
                    getRouteHM.put(vObject.getProfile().getUsername(), routeOverlay);
                } 
            }
            ViewModel.getInstance().getHitchPassengers().add(vObject.getProfile());
            
            // Popup slider if new found
            if (this.newFound) {
                ViewModel.getInstance().fireNotification(this.context, vObject.getProfile(),
                        vObject.getProfile().getID(), 1, this.mapView, 0);
            }
            getDriverAdapter(this.context, this.mapView).notifyDataSetChanged();
            this.mapView.invalidate();
            this.mapView.postInvalidate();
        }
    }
    
    
    public void updateLOO(List<OfferObject> loo) {
        
        clearPassengerOverlayList();
        getHitchDrivers().clear();
        this.newFound = false;
        
        // Add me to the mapView
        int my_new_lat = (int) (this.my_lat * 1E6);
        int my_new_lon = (int) (this.my_lon * 1E6);
        GeoPoint my_point = new GeoPoint(my_new_lat, my_new_lon);
        add2PassengerOverlay(this.context, my_point, Model.getInstance().getOwnProfile(), this.mapView, 0, Model
                .getInstance().getOwnProfile().getID());
        
        try {
            for (OfferObject offerObject : loo) {
                float lat = offerObject.getLat();
                float lng = offerObject.getLon();
                if (!isInBanned(offerObject.getUser_id())) {
                    if (isInLVO(offerObject.getUser_id())) {
                        updateViewObject(offerObject.getUser_id(), lat, lng);
                    } else {
                        Profile driver = this.ctrl.getProfile(Model.getInstance().getSid(), offerObject.getUser_id());
                        ViewObject vObject = new ViewObject(ws, lat, lng, driver);
                        vObject.setoObject(offerObject);
                        this.lvo.add(vObject);
                        this.newFound = true;
                    }
                }
            }
        } catch (Exception ex) {
        }
        
        for (ViewObject vObject : this.lvo) {
            int lat = (int) (vObject.getLat() * 1E6);
            int lng = (int) (vObject.getLon() * 1E6);
            GeoPoint gpsDriver = new GeoPoint(lat, lng);
            add2PassengerOverlay(this.context, gpsDriver, vObject.getProfile(), this.mapView, 1, vObject.getProfile()
                    .getID());
            getHitchDrivers().add(vObject.getProfile());
            
            // notify user on hitchhiker found
            if (this.newFound) {
                fireNotification(this.context, vObject.getProfile(), vObject.getProfile().getID(), 0, this.mapView, 1);
            }
            getPassengerAdapter(this.context, this.mapView).notifyDataSetChanged();
            this.mapView.invalidate();
            this.mapView.postInvalidate();
        }
    }
    
    
    /**
     * Updates the viewObject
     */
    private void updateViewObject(int userid, float lat, float lon) {
        for (ViewObject vObject : this.lvo) {
            if (vObject.getProfile().getID() == userid) {
                vObject.updatePos(lat, lon);
            }
        }
    }
    
    
    private void clearBanList() {
        this.banned.clear();
    }
    
    
    private boolean isInBanned(int userid) {
        boolean isInBanned = false;
        for (ViewObject vObject : this.banned) {
            if (vObject.getProfile().getID() == userid) {
                isInBanned = true;
            }
        }
        return isInBanned;
    }
    
    
    private boolean isInLVO(int userid) {
        boolean isInLVO = false;
        for (ViewObject vObject : this.lvo) {
            if (vObject.getProfile().getID() == userid) {
                isInLVO = true;
            }
        }
        return isInLVO;
    }
    
    private static ViewModel instance;
    private List<Overlay> mapDriverOverlays;
    private List<Overlay> mapPassengerOverlays;
    private String destination;
    private String destinationPassenger;
    private int numSeats = 0;
    private SlidingDrawer slider_Driver;
    private SlidingDrawer slider_Passenger;
    
    private List<Profile> hitchDrivers;
    private List<Profile> hitchPassengers;
    private List<Spinner> spinnersDest = new ArrayList<Spinner>();
    
    private Spinner clickedSpinner;
    
    private NotificationAdapter driverAdapter;
    private NotificationAdapter passengerAdapter;
    
    
    public static ViewModel getInstance() {
        if (instance == null) {
            instance = new ViewModel();
        }
        return instance;
    }
    
    public HashMap<String, RouteOverlay> getRouteHM = new HashMap<String, RouteOverlay>();
    public HashMap<String, Boolean> getRoutes = new HashMap<String, Boolean>();
    
    
    public RouteOverlay getRouteOverlay(String userRoute) {
        return getRouteHM.get(userRoute);
    }
    
    
    public boolean isRouteDrawn(String name) {
        
        if (getRoutes.get(name) == null) {
            Log.i(this, name + "'s route not drawn VIEW");
            return false;
        } else if (getRoutes.get(name)) {
            Log.i(this, name + "'s route is drawn VIEW");
            return true;
        } else {
            Log.i(this, name + "'s route not drawn VIEW");
            return false;
        }
    }
    
    
    public void removeRoute(RouteOverlay routeOverlay) {
        this.mapDriverOverlays.remove(routeOverlay);
    }
    
    public void clearRoutes() {
        getRoutes = null;
        getRouteHM = null;
    }
    
    public void updatePosition(IAbsoluteLocation loc, int whichHitcher) throws RemoteException {
        
        setMyPosition((float) loc.getLatitude(), (float) loc.getLongitude(), whichHitcher);
        
        /**
         * send server updated latitude and longitude
         */
        switch (this.ctrl.userUpdatePos(Model.getInstance().getSid(), (float) loc.getLatitude(),
                (float) loc.getLongitude())) {
            case Constants.STATUS_UPDATED:
                Toast.makeText(this.context, "Status updated", Toast.LENGTH_SHORT).show();
                break;
            case Constants.STATUS_UPTODATE:
                Toast.makeText(this.context, "Status up to date", Toast.LENGTH_SHORT).show();
                break;
            case Constants.STATUS_ERROR:
                Toast.makeText(this.context, "Error Update position", Toast.LENGTH_SHORT).show();
                break;
        }
        
    }
    
    
    /**
     * List containing all spinners/stop overs
     * 
     * @return
     */
    public List<Spinner> getDestinationSpinners() {
        return this.spinnersDest;
    }
    
    
    public void setClickedSpinner(Spinner spinner) {
        this.clickedSpinner = spinner;
    }
    
    
    public Spinner getClickedSpinner() {
        return this.clickedSpinner;
    }
    
    
    /**
     * Holds all overlays of the the drivers Mapview
     * 
     * @param mapView
     * @return
     */
    public List<Overlay> getDriverOverlayList(MapView mapView) {
        if (this.mapDriverOverlays == null) {
            this.mapDriverOverlays = mapView.getOverlays();
        }
        return this.mapDriverOverlays;
    }
    
    
    public void clearDriverOverlayList() {
        if (this.mapDriverOverlays != null) {
            this.mapDriverOverlays.clear();
            this.mapDriverOverlays = null;
        }
    }
    
    
    /**
     * Holds all overlays of the passengers MapView
     * 
     * @param mapView
     * @return
     */
    public List<Overlay> getPassengerOverlayList(MapView mapView) {
        if (this.mapPassengerOverlays == null) {
            this.mapPassengerOverlays = mapView.getOverlays();
        }
        return this.mapPassengerOverlays;
    }
    
    
    public void clearPassengerOverlayList() {
        if (this.mapPassengerOverlays != null) {
            this.mapPassengerOverlays.clear();
            this.mapPassengerOverlays = null;
        }
        
    }
    
    
    /**
     * set destination in RideActivity
     * 
     * @param spDestination
     */
    public void setDestination(Spinner spDestination) {
        this.destination = "";
        for (int i = 0; i < this.spinnersDest.size(); i++) {
            this.destination += ";" + this.spinnersDest.get(i).getSelectedItem().toString();
        }
        this.destination = this.destination + ";";
    }
    
    
    public void setDestination4Passenger(Spinner spDestination) {
        destinationPassenger = "";
        this.destinationPassenger = spDestination.getSelectedItem().toString();
    }
    
    
    public String getDestination4Passenger() {
        return this.destinationPassenger;
    }
    
    
    public void clearDestinations() {
        spinnersDest.clear();
    }
    
    
    /**
     * set number of seats available/needed depending on users wishes
     * 
     * @param spNumSeats
     */
    public void setNumSeats(Spinner spNumSeats) {
        this.numSeats = spNumSeats.getSelectedItemPosition() + 1;
        if (this.numSeats == 0) {
            this.numSeats = 1;
        }
    }
    
    
    public void setNewNumSeats(int newSeatNumber) {
        this.numSeats = newSeatNumber;
    }
    
    
    /**
     * get destination set by user
     * 
     * @return
     */
    public String getDestination() {
        return this.destination;
    }
    
    
    /**
     * get number of seats available set by driver or number of seats needed by
     * a passenger
     * 
     * @return
     */
    public int getNumSeats() {
        return this.numSeats;
    }
    
    
    public void initDriversList() {
        this.hitchDrivers = new ArrayList<Profile>();
    }
    
    
    public void initPassengersList() {
        this.hitchPassengers = new ArrayList<Profile>();
    }
    
    
    /**
     * list of all drivers who sent an invitation to passengers
     * 
     * @return
     */
    public List<Profile> getHitchDrivers() {
        if (this.hitchDrivers == null) {
            this.hitchDrivers = new ArrayList<Profile>();
        }
        return this.hitchDrivers;
    }
    
    
    public void clearHitchDrivers() {
        if (this.hitchDrivers != null) {
            this.hitchDrivers.clear();
            this.hitchDrivers = null;
        }
    }
    
    
    /**
     * list of passengers within perimeter of a driver
     * 
     * @return
     */
    public List<Profile> getHitchPassengers() {
        if (this.hitchPassengers == null) {
            this.hitchPassengers = new ArrayList<Profile>();
        }
        return this.hitchPassengers;
    }
    
    
    public void clearHitchPassengers() {
        if (this.hitchPassengers != null) {
            this.hitchPassengers.clear();
            this.hitchPassengers = null;
        }
    }
    
    
    /**
     * Adapter to show found drivers
     * 
     * @param context
     * @return
     */
    public NotificationAdapter getDriverAdapter(Context context, MapView mapView) {
        this.context = context;
        this.mapView = mapView;
        if (this.driverAdapter == null) {
            this.driverAdapter = new NotificationAdapter(ws, context, getHitchPassengers(), 0);
        }
        return this.driverAdapter;
    }
    
    
    public void clearDriverNotificationAdapter() {
        if (this.driverAdapter != null) {
            this.driverAdapter = null;
            this.slider_Driver = null;
        }
    }
    
    
    /**
     * Adapter to show found passengers
     * 
     * @param context
     * @return
     */
    public NotificationAdapter getPassengerAdapter(Context context, MapView mapView) {
        this.context = context;
        this.mapView = mapView;
        if (this.passengerAdapter == null) {
            this.passengerAdapter = new NotificationAdapter(ws, context, getHitchDrivers(), 1);
        }
        return this.passengerAdapter;
    }
    
    
    public void clearPassengerNotificationAdapter() {
        if (this.passengerAdapter != null) {
            this.passengerAdapter = null;
        }
    }
    
    
    /**
     * Simulating notifications per button click if button is pressed slider is
     * opened and user receives a notification via the status bar
     * 
     * @param context
     * @param profile
     */
    public void fireNotification(Context context, Profile profile, int profileID, int which1, MapView mapView,
            int whichSlider) {
        
        // get reference to notificationManager
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
        
        int icon = 0;
        CharSequence contentTitle;
        CharSequence contentText;
        CharSequence tickerText;
        
        // instantiate the notification
        if (which1 == 0) {
            icon = R.drawable.icon_ride;
        } else {
            icon = R.drawable.passenger_logo;
        }
        contentTitle = "vHike found a hitchhiker";
        contentText = "A Hitchhiker was found";
        tickerText = "vHike found a hitchhiker!";
        
        long when = System.currentTimeMillis();
        
        Notification notification = new Notification(icon, tickerText, when);
        notification.defaults |= Notification.DEFAULT_SOUND;
        
        // define the notification's message and PendingContent
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, null, 0);
        
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        
        // pass notification to notificationManager
        mNotificationManager.notify(0, notification);
        
        if (whichSlider == 0) {
            this.slider_Driver = (SlidingDrawer) ((Activity) context).findViewById(R.id.notiSlider);
            this.slider_Driver.open();
            Log.i(this, "Slider opened");
            getDriverAdapter(context, mapView).notifyDataSetChanged();
        } else {
            this.slider_Passenger = (SlidingDrawer) ((Activity) context).findViewById(R.id.slidingDrawer);
            this.slider_Passenger.open();
            getPassengerAdapter(context, mapView).notifyDataSetChanged();
        }
        
    }
    
    
    /**
     * add a passenger or driver to DriverOverlay
     * 
     * @param context
     * @param gpsPassenger
     * @param passenger
     * @param mapView
     */
    public void add2DriverOverlay(Context context, GeoPoint gps, Profile passenger, MapView mapView, int which1, GeoPoint mypoint) {
        Drawable drawable;
        if (which1 == 0) {
            Profile me = Model.getInstance().getOwnProfile();
            drawable = context.getResources().getDrawable(R.drawable.icon_ride);
            DriverOverlay driverOverlay = new DriverOverlay(drawable, context, gps, me.getUsername());
            OverlayItem opDriverItem = new OverlayItem(gps, "Hop in man", "User: " + passenger.getUsername()
                    + ", Rating: " + passenger.getRating_avg());
            driverOverlay.addOverlay(opDriverItem);
            
            ViewModel.getInstance().getDriverOverlayList(mapView).add(driverOverlay);
            Log.i(this, "OVERLAY D: DRIVER ADDED");
            mapView.invalidate();
        } else {
            drawable = context.getResources().getDrawable(R.drawable.passenger_logo);
            
            if (iContact == null) {
                Log.i(this, "Contact null");
            }
            
            PassengerOverlay passengerOverlay = new PassengerOverlay(drawable, context, mapView, ws, iContact,
                    passenger.getUsername(), gps, new ContactDialog(context, mapView, passenger.getUsername(), iContact, mypoint), 0);
            OverlayItem opPassengerItem = new OverlayItem(gps, String.valueOf(passenger.getID()),
                    passenger.getUsername());
            passengerOverlay.addOverlay(opPassengerItem);
            
            // add found passenger to overlay
            ViewModel.getInstance().getDriverOverlayList(mapView).add(passengerOverlay);
            Log.i(this, "OVERLAY D: PASSENGER ADDED");
            mapView.invalidate();
        }
    }
    
    
    /**
     * add a passenger or driver to PassengerOverlay
     * 
     * @param context
     * @param gpsDriver
     * @param driver
     * @param mapView
     */
    public void add2PassengerOverlay(Context context, GeoPoint gps, Profile profile, MapView mapView, int which1,
            int userID) {
        Drawable drawable;
        if (which1 == 0) {
            if (context == null) {
                Log.i(this, "Context null");
            }
            drawable = context.getResources().getDrawable(R.drawable.passenger_logo);
            Profile me = Model.getInstance().getOwnProfile();
            PassengerOverlay passengerOverlay = new PassengerOverlay(drawable, context, mapView, ws, iContact,
                    me.getUsername(), gps, null, 1);
            OverlayItem opDriverItem = new OverlayItem(gps, "I need a ride", "User: " + profile.getUsername()
                    + ", Rating: " + profile.getRating_avg());
            passengerOverlay.addOverlay(opDriverItem);
            
            ViewModel.getInstance().getPassengerOverlayList(mapView).add(passengerOverlay);
            Log.i(this, "OVERLAY P: PASSENGER ADDED");
            mapView.invalidate();
        } else {
            drawable = context.getResources().getDrawable(R.drawable.icon_ride);
            DriverOverlay driverOverlay = new DriverOverlay(drawable, context, gps, profile.getUsername());
            OverlayItem opPassengerItem = new OverlayItem(gps, "Hop in man", "User: " + profile.getUsername()
                    + ", Rating: " + profile.getRating_avg());
            driverOverlay.addOverlay(opPassengerItem);
            
            // add found passenger to overlay
            ViewModel.getInstance().getPassengerOverlayList(mapView).add(driverOverlay);
            Log.i(this, "OVERLAY P: DRIVER ADDED");
            mapView.invalidate();
        }
    }
    
}
