package de.unistuttgart.ipvs.pmp.model.context.location;

import android.location.Location;
import de.unistuttgart.ipvs.pmp.util.location.PMPGeoPoint;

public class LocationContextState extends PMPGeoPoint {
    
    /**
     * Last time of state
     */
    private long time;
    
    /**
     * Accuracy of state in meters
     */
    private float accuracy;
    
    /**
     * Whether this was successfully fully set or not
     */
    private int set;
    
    private static int LONGITUDE_UNSET = 1 << 0;
    private static int LATITUDE_UNSET = 1 << 1;
    private static int TIME_UNSET = 1 << 2;
    private static int ACCURACY_UNSET = 1 << 3;
    
    
    public LocationContextState() {
        super(0.0, 0.0);
        unset();
    }
    
    
    public boolean isSet() {
        return this.set == 0;
    }
    
    
    public void unset() {
        this.set = LONGITUDE_UNSET | LATITUDE_UNSET | TIME_UNSET | ACCURACY_UNSET;
    }
    
    
    public long getTime() {
        return this.time;
    }
    
    
    public void setTime(long time) {
        this.time = time;
        this.set &= ~TIME_UNSET;
    }
    
    
    public float getAccuracy() {
        return this.accuracy;
    }
    
    
    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
        this.set &= ~ACCURACY_UNSET;
    }
    
    
    @Override
    public void setLongitude(double longitude) {
        super.setLongitude(longitude);
        this.set &= ~LONGITUDE_UNSET;
    }
    
    
    @Override
    public void setLatitude(double latitude) {
        super.setLatitude(latitude);
        this.set &= ~LATITUDE_UNSET;
    }
    
    
    public void update(double latitude, double longitude, float accuracy, long time) {
        setLatitude(latitude);
        setLongitude(longitude);
        setAccuracy(accuracy);
        setTime(time);
    }
    
    
    public synchronized void update(Location l) {
        update(l.getLatitude(), l.getLongitude(), l.getAccuracy(), l.getTime());
    }
    
    
    @Override
    public String toString() {
        return super.toString() + ", accuracy " + this.accuracy + ", time " + this.time + ", set " + this.set;
    }
    
    
    @Override
    public int hashCode() {
        return super.hashCode() ^ Float.valueOf(this.accuracy).hashCode() ^ Long.valueOf(this.time).hashCode()
                ^ this.set;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof LocationContextState)) {
            return false;
        }
        
        LocationContextState lcs = (LocationContextState) o;
        return super.equals(lcs) && lcs.accuracy == this.accuracy && lcs.time == this.time && lcs.set == this.set;
    }
}
