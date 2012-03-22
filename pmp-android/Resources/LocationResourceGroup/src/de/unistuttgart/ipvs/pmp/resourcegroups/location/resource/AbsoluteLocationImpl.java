package de.unistuttgart.ipvs.pmp.resourcegroups.location.resource;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import android.location.Address;
import android.location.Geocoder;
import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.Distance;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.LocationResourceGroup;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.PermissionValidator;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.UseLocationDescriptionEnum;
import de.unistuttgart.ipvs.pmp.resourcegroups.location.aidl.IAbsoluteLocation;
import de.unistuttgart.ipvs.pmp.util.location.PMPGeoPoint;

/**
 * {@link AbsoluteLocationImpl} is the AIDL implementation for communication with the app.
 * 
 * @author Jakob Jarosch
 */
public class AbsoluteLocationImpl extends IAbsoluteLocation.Stub {
    
    /**
     * Reference to the {@link LocationResourceGroup}.
     */
    private LocationResourceGroup locationRG;
    
    /**
     * Resource which provides access to the GPS location.
     */
    private AbsoluteLocationResource absoluteLocationR;
    
    /**
     * Identifier of the app.
     */
    private String appIdentifier;
    
    /**
     * Instance of the {@link PermissionValidator}.
     */
    private PermissionValidator psv;
    
    /**
     * Used {@link UpdateRequest} to turn GPS on.
     */
    private UpdateRequest updateRequest = null;
    
    /**
     * Last used latitude to calculate the random inaccuracy.
     */
    private PMPGeoPoint lastRILocation = null;
    
    /**
     * Inaccuracy to force a inaccurate position.
     */
    private double randomInaccuracyLat = 0.0;
    
    /**
     * Inaccuracy to force a inaccurate position.
     */
    private double randomInaccuracyLong = 0.0;
    
    /**
     * Last returned latitude.
     */
    private double lastLatitude = 0.0;
    
    /**
     * Last returned longitude.
     */
    private double lastLongitude = 0.0;
    
    /**
     * Last time when an update was returned.
     */
    private long lastUpdate = 0;
    
    /**
     * Current LocationAddress.
     */
    private Address lastAddress = null;
    
    
    /**
     * Creates a new instance of the {@link AbsoluteLocationImpl}.
     * 
     * @param locationRG
     *            The instance of the {@link LocationResourceGroup}.
     * @param absoluteLocationR
     *            The instance of the {@link AbsoluteLocationResource}.
     * @param appIdentifier
     *            The identifier of the app.
     */
    public AbsoluteLocationImpl(LocationResourceGroup locationRG, AbsoluteLocationResource absoluteLocationR,
            String appIdentifier) {
        this.locationRG = locationRG;
        this.absoluteLocationR = absoluteLocationR;
        this.appIdentifier = appIdentifier;
        this.psv = new PermissionValidator(this.locationRG, this.appIdentifier);
    }
    
    
    public void startLocationLookup(long minTime, float minDistance) throws RemoteException {
        this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
        
        this.updateRequest = new UpdateRequest(minTime, minDistance);
        this.absoluteLocationR.startLocationLookup(this.appIdentifier, this.updateRequest);
    }
    
    
    public void endLocationLookup() throws RemoteException {
        this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
        
        this.updateRequest = null;
        this.absoluteLocationR.endLocationLookup(this.appIdentifier);
    }
    
    
    public boolean isGpsEnabled() throws RemoteException {
        this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
        updateLastRequest();
        
        return this.absoluteLocationR.isGpsEnabled();
    }
    
    
    public boolean isActive() throws RemoteException {
        this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
        updateLastRequest();
        
        return this.absoluteLocationR.isActive();
    }
    
    
    public boolean isFixed() throws RemoteException {
        this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
        updateLastRequest();
        
        return this.absoluteLocationR.isFixed();
    }
    
    
    public boolean isUpdateAvailable() {
        this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
        updateLastRequest();
        
        double currentLatitude = this.absoluteLocationR.getLatitude();
        double currentLongitude = this.absoluteLocationR.getLongitude();
        
        double distance = Distance.calculateArc(this.lastLatitude, this.lastLongitude, currentLatitude,
                currentLongitude, Distance.KILOMETERS);
        
        boolean update = false;
        
        if (distance > this.updateRequest.getMinDistance()) {
            /* Distance to last coordinate is greater than minDistance. */
            update = true;
        } else if (System.currentTimeMillis() > this.lastUpdate + this.updateRequest.getMinTime()) {
            /* Last update is older than minTime. */
            update = true;
        }
        
        if (update) {
            this.lastLatitude = currentLatitude;
            this.lastLongitude = currentLongitude;
            this.lastUpdate = System.currentTimeMillis();
            this.lastAddress = null;
        }
        
        return update;
    }
    
    
    public double getLongitude() throws RemoteException {
        this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
        this.psv.validate(LocationResourceGroup.PS_USE_COORDINATES, "true");
        updateLastRequest();
        
        PMPGeoPoint point = calculateRandomInaccuracyGeoPoint();
        return point.getLongitude();
        
        //return this.absoluteLocationR.getLongitude();
    }
    
    
    public double getLatitude() throws RemoteException {
        this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
        this.psv.validate(LocationResourceGroup.PS_USE_COORDINATES, "true");
        updateLastRequest();
        
        PMPGeoPoint point = calculateRandomInaccuracyGeoPoint();
        return point.getLatitude();
        
        //return this.absoluteLocationR.getLatitude();
    }
    
    
    public float getAccuracy() throws RemoteException {
        this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
        this.psv.validate(LocationResourceGroup.PS_USE_ACCURACY, "true");
        updateLastRequest();
        
        if (this.psv.getIntValue(LocationResourceGroup.PS_LOCATION_PRECISION) > this.absoluteLocationR.getAccuracy()) {
            return this.psv.getIntValue(LocationResourceGroup.PS_LOCATION_PRECISION);
        } else {
            return this.absoluteLocationR.getAccuracy();
        }
    }
    
    
    public float getSpeed() throws RemoteException {
        this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
        this.psv.validate(LocationResourceGroup.PS_USE_SPEED, "true");
        updateLastRequest();
        
        return this.absoluteLocationR.getSpeed();
    }
    
    
    public String getCountryCode() {
        this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
        this.psv.validate(UseLocationDescriptionEnum.COUNTRY);
        
        fetchAddress();
        
        if (this.lastAddress == null) {
            return null;
        } else {
            Log.v(this, "getCountryCode() : " + this.lastAddress.getCountryCode());
            return this.lastAddress.getCountryCode();
        }
    }
    
    
    public String getCountryName() {
        this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
        this.psv.validate(UseLocationDescriptionEnum.COUNTRY);
        
        fetchAddress();
        
        if (this.lastAddress == null) {
            return null;
        } else {
            Log.v(this, "getCountryName() : " + this.lastAddress.getCountryName());
            return this.lastAddress.getCountryName();
        }
    }
    
    
    public String getLocality() {
        this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
        this.psv.validate(UseLocationDescriptionEnum.CITY);
        
        fetchAddress();
        
        if (this.lastAddress == null) {
            return null;
        } else {
            Log.v(this, "getLocality() : " + this.lastAddress.getLocality());
            return this.lastAddress.getLocality();
        }
    }
    
    
    public String getPostalCode() {
        this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
        this.psv.validate(UseLocationDescriptionEnum.CITY);
        
        fetchAddress();
        
        if (this.lastAddress == null) {
            return null;
        } else {
            Log.v(this, "getPostalCode() : " + this.lastAddress.getPostalCode());
            return this.lastAddress.getPostalCode();
        }
    }
    
    
    public String getAddress() {
        this.psv.validate(LocationResourceGroup.PS_USE_ABSOLUTE_LOCATION, "true");
        this.psv.validate(UseLocationDescriptionEnum.STREET);
        
        fetchAddress();
        
        if (this.lastAddress == null) {
            return null;
        } else {
            Log.v(this, "getAddress() : " + this.lastAddress.getAddressLine(0));
            return this.lastAddress.getAddressLine(0);
        }
    }
    
    
    /**
     * Updates the time of the request.
     */
    private void updateLastRequest() {
        if (this.updateRequest != null) {
            this.updateRequest.setLastRequest(System.currentTimeMillis());
        }
    }
    
    
    /**
     * Calculates a random inaccuracy which is used to generate a vague location.
     */
    private PMPGeoPoint calculateRandomInaccuracyGeoPoint() {
        int precision = this.psv.getIntValue(LocationResourceGroup.PS_LOCATION_PRECISION);
        
        PMPGeoPoint newLocation = new PMPGeoPoint(this.absoluteLocationR.getLatitude(),
                this.absoluteLocationR.getLongitude());
        
        /* Avoid smearing of startup location (0,0). */
        if (newLocation.getDistance(new PMPGeoPoint(0.0, 0.0)) < 10.0) {
            return newLocation;
        }
        
        /* Calculate distance to last position. */
        double distanceToLastPosition = 100000000000.0;
        if (lastRILocation != null) {
            distanceToLastPosition = newLocation.getDistance(lastRILocation);
        }
        
        /* 
         * Only change the inaccuracy when the distance to the last inaccuracy change
         * is greater than the precision Privacy Setting.
         */
        if (distanceToLastPosition > precision) {
            Random rand = new Random();
            this.randomInaccuracyLat = rand.nextDouble() * precision;
            this.randomInaccuracyLong = rand.nextDouble() * precision;
            this.lastRILocation = newLocation;
        }
        
        System.out.println("current location: " + newLocation);
        System.out.println("long-rand: " + this.randomInaccuracyLong);
        System.out.println("lat-rand: " + this.randomInaccuracyLat);
        
        PMPGeoPoint newPoint = newLocation;
        
        try {
            newPoint = newLocation.inDistance(this.randomInaccuracyLat, this.randomInaccuracyLong);
            System.out.println("smeared location: " + newPoint);
        } catch (IllegalArgumentException e) {
            Log.e(this, "Catched illegal argument exception during caluculation of smeared location", e);
        }
        
        return newPoint;
    }
    
    
    private void fetchAddress() {
        Geocoder gc = new Geocoder(this.locationRG.getContext());
        
        try {
            List<Address> addresses = gc.getFromLocation(this.absoluteLocationR.getLatitude(),
                    this.absoluteLocationR.getLongitude(), 1);
            if (addresses != null && addresses.size() > 0) {
                this.lastAddress = addresses.get(0);
            }
        } catch (IOException e) {
            Log.d("GPS Resource", "Failed to get an address for the curent location.", e);
        }
    }
}
