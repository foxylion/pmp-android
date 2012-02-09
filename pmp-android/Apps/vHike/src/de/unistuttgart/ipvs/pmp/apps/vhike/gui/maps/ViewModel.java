package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.SlidingDrawer;
import android.widget.Spinner;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.ProfileActivity;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.NotificationAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.QueryObject;

/**
 * MapModel grants access to all elements needed to work with the map view
 * 
 * @author andres
 * 
 */
public class ViewModel {
    
    private List<ViewObject> lqo;
    Controller ctrl = new Controller();
    private List<ViewObject> banned;
    MapView mapView;
    Context context;
    private float my_lat;
    private float my_lon;
    public List<ViewObject> getLQO(){
        return lqo;
    }
    
    private ViewModel() {
        lqo = new ArrayList<ViewObject>();
        banned = new ArrayList<ViewObject>();
    }
    
    
    public void addToBanned(ViewObject vObject) {
        banned.add(vObject);
        removeFromLQO(vObject);
    }
    
    private void removeFromLQO(ViewObject toRMVvObject){
        int i = 0;
        for (ViewObject vObject : lqo) {
            
            if(vObject.getProfile().getID()== toRMVvObject.getProfile().getID()){
                lqo.remove(i);
            }
            i++;
        }
    }
    
    public void updateView(){
        updateLQO(new ArrayList<QueryObject>());
        ViewModel.getInstance().getDriverAdapter(context, mapView).notifyDataSetChanged();
        mapView.invalidate();
    }
    
    
    public void setMyPosition(float my_lat, float my_lon){
        this.my_lat = my_lat;
        this.my_lon = my_lon;
        updateView();
    }
    public void clearViewModel(){
        clearLQO();
        clearBanList();
        my_lat = 0;
        my_lon = 0;
    }
    private void clearLQO(){
        lqo.clear();
    }
    public void updateLQO(List<QueryObject> queries) {
        
        
        ViewModel.getInstance().clearDriverOverlayList();
        ViewModel.getInstance().getHitchPassengers().clear();

        // Add me to the mapView
        int my_new_lat = (int) (my_lat * 1E6);
        int my_new_lon = (int) (my_lon * 1E6);
        GeoPoint my_point = new GeoPoint(my_new_lat, my_new_lon);
        ViewModel.getInstance().add2DriverOverlay(context, my_point, Model.getInstance().getOwnProfile(), mapView, 0,
                Model.getInstance().getOwnProfile().getID());
        
        
        try{
        for (QueryObject queryObject : queries) {
            float lat = queryObject.getCur_lat();
            float lon = queryObject.getCur_lon();
            if (!isInBanned(queryObject.getUserid())) {
                if (isInLVO(queryObject.getUserid())) {
                    updateViewObject(queryObject.getUserid(), lat, lon);
                } else {
                    Profile profile = ctrl.getProfile(Model.getInstance().getSid(), queryObject.getUserid());
                    ViewObject vObject = new ViewObject(lat, lon, profile);
                    vObject.setqObject(queryObject);
                    lqo.add(vObject);
                    // Popup slider if new found
                    ViewModel.getInstance().fireNotification(context, profile, profile.getID(), 1,
                            mapView);

                }
            }
        }
        }catch(Exception ex){
        }
        
        
        for(ViewObject vObject : lqo){
            int lat = (int) (vObject.getLat() * 1E6);
            int lng = (int) (vObject.getLon() * 1E6);
            GeoPoint gpsDriver = new GeoPoint(lat, lng);
            ViewModel.getInstance().add2DriverOverlay(context, gpsDriver, vObject.getProfile(), mapView, 1,
                    vObject.getProfile().getID());
            ViewModel.getInstance().getHitchPassengers().add(vObject.getProfile());
            ViewModel.getInstance().getDriverAdapter(context, mapView).notifyDataSetChanged();
            mapView.invalidate();
        }
    }
    
    
    /**
     * Updates the viewObject
     */
    private void updateViewObject(int userid, float lat, float lon) {
        for (ViewObject vObject : lqo) {
            if (vObject.getProfile().getID() == userid) {
                vObject.updatePos(lat, lon);
            }
        }
    }
    
    private void clearBanList(){
        banned.clear();
    }
    
    private boolean isInBanned(int userid) {
        boolean isInBanned = false;
        for (ViewObject vObject : banned) {
            if (vObject.getProfile().getID() == userid) {
                isInBanned = true;
            }
        }
        return isInBanned;
    }
    
    
    private boolean isInLVO(int userid) {
        boolean isInLVO = false;
        for (ViewObject vObject : lqo) {
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
    private int numSeats = 0;
    private SlidingDrawer slider_Driver;
    private SlidingDrawer slider_Passenger;
    
    private List<Profile> hitchDrivers;
    private List<Profile> hitchPassengers;
    
    private NotificationAdapter driverAdapter;
    private NotificationAdapter passengerAdapter;
    
    
    public static ViewModel getInstance() {
        if (instance == null) {
            instance = new ViewModel();
        }
        return instance;
    }
    
    
    /**
     * Holds all overlays of the the drivers Mapview
     * 
     * @param mapView
     * @return
     */
    public List<Overlay> getDriverOverlayList(MapView mapView) {
        if (mapDriverOverlays == null) {
            mapDriverOverlays = mapView.getOverlays();
        }
        return mapDriverOverlays;
    }
    
    
    public void clearDriverOverlayList() {
        if (mapDriverOverlays != null) {
            mapDriverOverlays.clear();
            mapDriverOverlays = null;
        }
    }
    
    
    /**
     * Holds all overlays of the passengers MapView
     * 
     * @param mapView
     * @return
     */
    public List<Overlay> getPassengerOverlayList(MapView mapView) {
        if (mapPassengerOverlays == null) {
            mapPassengerOverlays = mapView.getOverlays();
        }
        return mapPassengerOverlays;
    }
    
    
    public void clearPassengerOverlayList() {
        if (mapPassengerOverlays != null) {
            mapPassengerOverlays.clear();
            mapPassengerOverlays = null;
        }
        
    }
    
    
    /**
     * set destination in RideActivity
     * 
     * @param spDestination
     */
    public void setDestination(Spinner spDestination) {
        destination = spDestination.getSelectedItem().toString();
    }
    
    
    /**
     * set number of seats available/needed depending on users wishes
     * 
     * @param spNumSeats
     */
    public void setNumSeats(Spinner spNumSeats) {
        numSeats = spNumSeats.getSelectedItemPosition() + 1;
        if (numSeats == 0) {
            numSeats = 1;
        }
    }
    
    
    /**
     * get destination set by user
     * 
     * @return
     */
    public String getDestination() {
        return destination;
    }
    
    
    /**
     * get number of seats available set by driver or number of seats needed by
     * a passenger
     * 
     * @return
     */
    public int getNumSeats() {
        return numSeats;
    }
    
    
    public void initDriversList() {
        hitchDrivers = new ArrayList<Profile>();
    }
    
    
    public void initPassengersList() {
        hitchPassengers = new ArrayList<Profile>();
    }
    
    
    /**
     * list of all drivers who sent an invitation to passengers
     * 
     * @return
     */
    public List<Profile> getHitchDrivers() {
        if (hitchDrivers == null) {
            hitchDrivers = new ArrayList<Profile>();
        }
        return hitchDrivers;
    }
    
    
    public void clearHitchDrivers() {
        if (hitchDrivers != null) {
            hitchDrivers.clear();
            hitchDrivers = null;
        }
    }
    
    
    /**
     * list of passengers within perimeter of a driver
     * 
     * @return
     */
    public List<Profile> getHitchPassengers() {
        if (hitchPassengers == null) {
            hitchPassengers = new ArrayList<Profile>();
        }
        return hitchPassengers;
    }
    
    
    public void clearHitchPassengers() {
        if (hitchPassengers != null) {
            hitchPassengers.clear();
            hitchPassengers = null;
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
        if (driverAdapter == null) {
            driverAdapter = new NotificationAdapter(context, getHitchPassengers(), 0, mapView);
        }
        return driverAdapter;
    }
    
    
    public void clearDriverNotificationAdapter() {
        if (driverAdapter != null) {
            driverAdapter = null;
            slider_Driver = null;
        }
    }
    
    
    /**
     * Adapter to show found passengers
     * 
     * @param context
     * @return
     */
    public NotificationAdapter getPassengerAdapter(Context context, MapView mapView) {
        if (passengerAdapter == null) {
            passengerAdapter = new NotificationAdapter(context, getHitchDrivers(), 1, mapView);
        }
        return passengerAdapter;
    }
    
    
    public void clearPassengerNotificationAdapter() {
        if (passengerAdapter != null) {
            passengerAdapter = null;
        }
    }
    
    
    /**
     * Simulating notifications per button click if button is pressed slider is
     * opened and user receives a notification via the status bar
     * 
     * @param context
     * @param profile
     */
    public void fireNotification(Context context, Profile profile, int profileID, int which1, MapView mapView) {
        
        // get reference to notificationManager
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
        
        int icon = 0;
        CharSequence contentTitle;
        CharSequence contentText;
        CharSequence tickerText;
        
        // instantiate the notification
        if (which1 == 0) {
            icon = R.drawable.passenger_logo;
            contentTitle = "vHike found a hitchhiker";
            contentText = "A Hitchhiker was found";
            tickerText = "vHike found a hitchhiker!";
        } else {
            contentTitle = "vHike found a hitchhiker";
            contentText = "A Hitchhiker was found";
            tickerText = "vHike found a hitchhiker!";
        }
        
        long when = System.currentTimeMillis();
        
        Notification notification = new Notification(icon, tickerText, when);
        notification.defaults |= Notification.DEFAULT_SOUND;
        
        // define the notification's message and PendingContent
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, null, 0);
        
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        
        // pass notification to notificationManager
        mNotificationManager.notify(0, notification);
        
        if (which1 == 0) {
            // getHitchPassengers().clear();
            // getHitchPassengers().add(profile);
            
            slider_Driver = (SlidingDrawer) ((Activity) context).findViewById(R.id.notiSlider);
            slider_Driver.open();
            Log.i(this, "Slider opened");
            getDriverAdapter(context, mapView).notifyDataSetChanged();
        } else {
            // getHitchDrivers().clear();
            // getHitchDrivers().add(profile);
            slider_Passenger = (SlidingDrawer) ((Activity) context).findViewById(R.id.slidingDrawer);
            slider_Passenger.open();
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
    public void add2DriverOverlay(Context context, GeoPoint gps, Profile passenger, MapView mapView, int which1,
            int userID) {
        Drawable drawable;
        if (which1 == 0) {
            drawable = context.getResources().getDrawable(R.drawable.icon_ride);
            DriverOverlay driverOverlay = new DriverOverlay(drawable, context, gps);
            //            OverlayItem opDriverItem = new OverlayItem(gps, "Hop in man", "User: " + passenger.getUsername()
            //                    + ", Rating: " + passenger.getRating_avg());
            OverlayItem opDriverItem = new OverlayItem(gps, "Hop in man", "User: " + passenger.getUsername()
                    + ", Rating: " + 2);
            driverOverlay.addOverlay(opDriverItem);
            
            ViewModel.getInstance().getDriverOverlayList(mapView).add(driverOverlay);
            mapView.invalidate();
        } else {
            drawable = context.getResources().getDrawable(R.drawable.passenger_logo);
            PassengerOverlay passengerOverlay = new PassengerOverlay(drawable, context, userID);
            OverlayItem opPassengerItem = new OverlayItem(gps, "I need a ride", "User: " + passenger.getUsername()
                    + ", Rating: " + passenger.getRating_avg());
            passengerOverlay.addOverlay(opPassengerItem);
            
            // add found passenger to overlay
            ViewModel.getInstance().getDriverOverlayList(mapView).add(passengerOverlay);
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
            drawable = context.getResources().getDrawable(R.drawable.passenger_logo);
            PassengerOverlay passengerOverlay = new PassengerOverlay(drawable, context, userID);
            OverlayItem opDriverItem = new OverlayItem(gps, "I need a ride", "User: " + profile.getUsername()
                    + ", Rating: " + profile.getRating_avg());
            passengerOverlay.addOverlay(opDriverItem);
            
            ViewModel.getInstance().getPassengerOverlayList(mapView).add(passengerOverlay);
            mapView.invalidate();
        } else {
            drawable = context.getResources().getDrawable(R.drawable.icon_ride);
            DriverOverlay driverOverlay = new DriverOverlay(drawable, context, gps);
            OverlayItem opPassengerItem = new OverlayItem(gps, "Hop in man", "User: " + profile.getUsername()
                    + ", Rating: " + profile.getRating_avg());
            driverOverlay.addOverlay(opPassengerItem);
            
            // add found passenger to overlay
            ViewModel.getInstance().getPassengerOverlayList(mapView).add(driverOverlay);
            mapView.invalidate();
        }
    }
    //==========================================================================
    //=================NEW LIFE ================================
    
}
