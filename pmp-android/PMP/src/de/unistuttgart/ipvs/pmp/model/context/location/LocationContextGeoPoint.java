/*
 * Copyright 2012 pmp-android development team
 * Project: PMP
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.model.context.location;

/**
 * A geo point for the {@link LocationContextCondition}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class LocationContextGeoPoint {
    
    private double latitude, longitude;
    
    
    public LocationContextGeoPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    
    public double getLatitude() {
        return this.latitude;
    }
    
    
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    
    public double getLongitude() {
        return this.longitude;
    }
    
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    
    @Override
    public String toString() {
        return String.format("%.6f %s, %.6f %s", Math.abs(this.latitude), this.latitude > 0 ? "N" : "S",
                Math.abs(this.longitude), this.longitude > 0 ? "E" : "W");
    }
    
    
    @Override
    public int hashCode() {
        return Double.valueOf(this.latitude).hashCode() ^ Double.valueOf(this.longitude).hashCode();
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof LocationContextGeoPoint)) {
            return false;
        }
        LocationContextGeoPoint lcgp = (LocationContextGeoPoint) o;
        return lcgp.latitude == this.latitude && lcgp.longitude == this.longitude;
    }
    
}
